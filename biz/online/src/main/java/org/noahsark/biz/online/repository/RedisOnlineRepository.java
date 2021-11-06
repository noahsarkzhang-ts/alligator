package org.noahsark.biz.online.repository;

import com.google.common.base.Preconditions;
import org.noahsark.common.dto.UserInfo;
import org.noahsark.common.event.ServiceEvent;
import org.noahsark.common.event.UserEvent;
import org.noahsark.redis.cmd.CmdEnum;
import org.noahsark.redis.cmd.OperationEnum;
import org.noahsark.redis.cmd.RedisCmd;
import org.noahsark.redis.cmd.Triple;
import org.noahsark.redis.executor.RedisCmdRunner;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolAbstract;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Redis 在线数据访问类
 * @author zhangxt
 * @date 2021/11/06 14:28
 **/
public class RedisOnlineRepository implements OnlineRepository {

    private static Logger logger = LoggerFactory.getLogger(RedisOnlineRepository.class);

    /**
     * 用户id --> UserInfo
     * type: string
     */
    private static final String ONLINE_USER_KEY_FORMAT = "olu:us:%s";

    /**
     * userId id --> service id
     * type: string
     */
    private static final String ONLINE_USER_SERVICE_KEY_FORMAT = "olu:us:s:%s";

    /**
     * service id --> User id set
     * type: set
     */
    private static final String ONLINE_SERVICE_USERS_KEY_FORMAT = "olu:ss:%s";

    /**
     * 当前在线的所有用户
     * type: set
     */
    private static final String ONLINE_CURRENT_USERS_KEY = "olu:all";

    private RedisCmdRunner redisCmdRunner;

    private JedisPoolAbstract jedisPool;

    @Override
    public void userLogin(UserEvent event) {

        List<RedisCmd> cmds = new ArrayList<>();
        RedisCmd cmd;
        Triple triple;
        Triple element;

        // TODO 1、判断用户是否已经在线
        if (getUserStatus(event.getUserId())) {
            return;
        }

        // TODO 2. 构造用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setName(event.getName());
        userInfo.setServiceId(event.getServiceId());
        userInfo.setUserId(event.getUserId());
        userInfo.setState((byte) 1);

        // TODO 3. 添加用户信息到 Redis
        cmd = new RedisCmd();
        cmd.setType(CmdEnum.STRING);
        cmd.setOperationType(OperationEnum.ADD);

        triple = new Triple();
        triple.setKey(String.format(ONLINE_USER_KEY_FORMAT, userInfo.getUserId()));
        triple.setValue(JsonUtils.toJson(userInfo).getBytes(StandardCharsets.UTF_8));
        cmd.setKey(triple);

        cmds.add(cmd);

        // TODO 4. 添加用户到用户在线列表中
        cmd = new RedisCmd();
        cmd.setType(CmdEnum.SET);
        cmd.setOperationType(OperationEnum.ADD);

        triple = new Triple();
        triple.setKey(ONLINE_CURRENT_USERS_KEY);
        cmd.setKey(triple);

        element = new Triple();
        element.setKey(userInfo.getUserId());
        cmd.addElement(triple);

        cmds.add(cmd);

        // TODO 5. 添加用户与所在服务的绑定关系
        cmd = new RedisCmd();
        cmd.setType(CmdEnum.STRING);
        cmd.setOperationType(OperationEnum.ADD);

        triple = new Triple();
        triple.setKey(String.format(ONLINE_USER_SERVICE_KEY_FORMAT, userInfo.getUserId()));
        triple.setValue(userInfo.getServiceId().getBytes(StandardCharsets.UTF_8));
        cmd.setKey(triple);

        cmds.add(cmd);

        // TODO 6. 添加用户id到服务用户列表中
        cmd = new RedisCmd();
        cmd.setType(CmdEnum.SET);
        cmd.setOperationType(OperationEnum.ADD);

        triple = new Triple();
        triple.setKey(String.format(ONLINE_SERVICE_USERS_KEY_FORMAT, userInfo.getServiceId()));
        cmd.setKey(triple);

        element = new Triple();
        element.setKey(userInfo.getUserId());
        cmd.addElement(triple);

        cmds.add(cmd);

        // 7. 批量执行命令
        redisCmdRunner.exeCmds(cmds);

    }

    @Override
    public void userLogout(UserEvent event) {
        List<RedisCmd> cmds = offlineUser(event.getUserId());

        // 执行命令
        redisCmdRunner.exeCmds(cmds);
    }

    @Override
    public void serviceRegistor(ServiceEvent event) {
        // TODO 服务器上线，暂时不处理
    }

    @Override
    public void serviceUnRegistor(ServiceEvent event) {
        // TODO 服务器下线，需要将该服务器下的所有用户下线
        List<RedisCmd> cmds = new ArrayList<>();

        String serviceId = event.getServiceId();
        String key = String.format(ONLINE_SERVICE_USERS_KEY_FORMAT,serviceId);

        // TODO 1、获取该服务器下的所有用户id
        List<String> userIds = redisCmdRunner.getElementsFromSet(key);

        // TODO 2、下线所有用户
        userIds.stream().forEach(userId -> cmds.addAll(offlineUser(userId)));

        // TODO 3、清除服务器信息
        RedisCmd cmd = new RedisCmd();
        cmd.setType(CmdEnum.SET);
        cmd.setOperationType(OperationEnum.DEL);

        Triple triple = new Triple();
        triple.setKey(key);

        cmd.setKey(triple);
        cmds.add(cmd);

        // TODO 4、执行命令

        redisCmdRunner.exeCmds(cmds);
    }

    @Override
    public Set<UserInfo> getAllUser() {

        Set<UserInfo> users = new HashSet<>();
        List<String> userIds = redisCmdRunner.getElementsFromSet(ONLINE_CURRENT_USERS_KEY);

        userIds.stream().forEach(userId -> users.add(getUser(userId)));

        return users;
    }

    @Override
    public String getResidedService(String userId) {
        Preconditions.checkNotNull(userId);

        String serviceId = null;
        try (Jedis jedis = jedisPool.getResource()) {
            String key = String.format(ONLINE_USER_SERVICE_KEY_FORMAT, userId);
            byte[] value = jedis.get(key.getBytes(StandardCharsets.UTF_8));

            if (value != null && value.length > 0) {
                serviceId = new String(value);
            }
        } catch (Exception ex) {
            logger.error("catch an exception when getUser.", ex);
        }

        return serviceId;
    }

    /**
     * 获取用户在线状态信息
     *
     * @param userId 用户id
     * @return true: 在线；false: 离线
     */
    public boolean getUserStatus(String userId) {

        return redisCmdRunner.isExistInSet(ONLINE_CURRENT_USERS_KEY, userId);

    }

    /**
     * 获取用户在线信息
     *
     * @param userId 用户id
     * @return org.noahsark.common.dto.UserInfo
     * @author zhangxt
     * @date 2021/11/06 16:46
     */
    public UserInfo getUser(String userId) {
        Preconditions.checkNotNull(userId);

        UserInfo userInfo = null;
        try (Jedis jedis = jedisPool.getResource()) {
            String key = String.format(ONLINE_USER_KEY_FORMAT, userId);
            byte[] value = jedis.get(key.getBytes(StandardCharsets.UTF_8));

            if (value != null && value.length > 0) {
                userInfo = JsonUtils.fromJson(new String(value), UserInfo.class);
            }
        } catch (Exception ex) {
            logger.error("catch an exception when getUser.", ex);
        }

        return userInfo;
    }

    private List<RedisCmd> offlineUser(String userId) {
        List<RedisCmd> cmds = new ArrayList<>();
        RedisCmd cmd;
        Triple triple;
        Triple element;

        // TODO 1、判断用户是否已经在线
        if (!getUserStatus(userId)) {
            return cmds;
        }

        UserInfo userInfo = getUser(userId);
        String serviceId = userInfo.getServiceId();

        // TODO 2、删除用户
        cmd = new RedisCmd();
        cmd.setType(CmdEnum.STRING);
        cmd.setOperationType(OperationEnum.DEL);

        triple = new Triple();
        triple.setKey(String.format(ONLINE_USER_KEY_FORMAT, userId));
        cmd.setKey(triple);

        cmds.add(cmd);

        // TODO 3、移除在线用户列表
        cmd = new RedisCmd();
        cmd.setType(CmdEnum.SET);
        cmd.setOperationType(OperationEnum.REMOVE);

        triple = new Triple();
        triple.setKey(ONLINE_CURRENT_USERS_KEY);
        cmd.setKey(triple);

        element = new Triple();
        element.setKey(userId);
        cmd.addElement(triple);

        cmds.add(cmd);

        // TODO 4、移除用户与服务器的绑定关系
        cmd = new RedisCmd();
        cmd.setType(CmdEnum.STRING);
        cmd.setOperationType(OperationEnum.DEL);

        triple = new Triple();
        triple.setKey(String.format(ONLINE_USER_SERVICE_KEY_FORMAT, userId));
        cmd.setKey(triple);

        cmds.add(cmd);

        // TODO 5、移除服务器在线用户
        cmd = new RedisCmd();
        cmd.setType(CmdEnum.SET);
        cmd.setOperationType(OperationEnum.REMOVE);

        triple = new Triple();
        triple.setKey(String.format(ONLINE_SERVICE_USERS_KEY_FORMAT, serviceId));
        cmd.setKey(triple);

        element = new Triple();
        element.setKey(userId);
        cmd.addElement(triple);

        cmds.add(cmd);

        return cmds;
    }
}

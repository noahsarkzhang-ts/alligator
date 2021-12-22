package org.noahsark.registration.repository.redis;

import com.google.common.base.Preconditions;
import org.noahsark.redis.cmd.CmdEnum;
import org.noahsark.redis.cmd.OperationEnum;
import org.noahsark.redis.cmd.RedisCmd;
import org.noahsark.redis.cmd.Triple;
import org.noahsark.redis.executor.RedisCmdRunner;
import org.noahsark.registration.domain.Service;
import org.noahsark.registration.repository.MemoryRepository;
import org.noahsark.registration.repository.Repository;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolAbstract;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 注册中心 Redis 数据访问类
 * @author zhangxt
 * @date 2021/4/10 20:36
 **/
public class RedisRepository implements Repository {

    private static Logger logger = LoggerFactory.getLogger(MemoryRepository.class);

    /**
     * 服务器 Key
     * type: string
     */
    private static final String REGS_SERVICE_KEY_FORMAT = "regs:ss:%s";

    /**
     * 某一类型服务的服务器列表，biz -> set(service)
     * type: set
     */
    private static final String REGS_BIZ_SERVICE_KEY_FORMAT = "regs:biz:%d";

    private RedisCmdRunner redisCmdRunner;

    private JedisPoolAbstract jedisPool;

    @Override
    public void registerService(Service service) {

        if (redisCmdRunner.isExistKey(String.format(REGS_SERVICE_KEY_FORMAT, service.getId()))) {
            return;
        }

        List<RedisCmd> cmds = new ArrayList<>();
        RedisCmd cmd;
        Triple triple;
        Triple element;

        // TODO 1、添加服务器信息
        cmd = new RedisCmd();
        cmd.setType(CmdEnum.STRING);
        cmd.setOperationType(OperationEnum.ADD);

        triple = new Triple();
        triple.setKey(String.format(REGS_SERVICE_KEY_FORMAT, service.getId()));
        triple.setValue(JsonUtils.toJson(service).getBytes(StandardCharsets.UTF_8));
        cmd.setKey(triple);

        cmds.add(cmd);

        // TODO 2、添加服务器到服务类型列表
        cmd = new RedisCmd();
        cmd.setType(CmdEnum.SET);
        cmd.setOperationType(OperationEnum.ADD);

        triple = new Triple();
        triple.setKey(String.format(REGS_BIZ_SERVICE_KEY_FORMAT, service.getBiz()));
        cmd.setKey(triple);

        element = new Triple();
        element.setKey(service.getId());
        cmd.addElement(triple);

        cmds.add(cmd);

        // TODO 3、执行命令
        redisCmdRunner.exeCmds(cmds);

    }

    @Override
    public void unRegisterService(String id) {

        Service service = getServiceById(id);
        if (service == null) {
            return;
        }

        List<RedisCmd> cmds = new ArrayList<>();
        RedisCmd cmd;
        Triple triple;
        Triple element;

        // TODO 1、从服务类型列表中移除服务器到
        cmd = new RedisCmd();
        cmd.setType(CmdEnum.SET);
        cmd.setOperationType(OperationEnum.REMOVE);

        triple = new Triple();
        triple.setKey(String.format(REGS_BIZ_SERVICE_KEY_FORMAT, service.getBiz()));
        cmd.setKey(triple);

        element = new Triple();
        element.setKey(id);
        cmd.addElement(triple);

        cmds.add(cmd);

        // TODO 2、删除服务器信息
        cmd = new RedisCmd();
        cmd.setType(CmdEnum.STRING);
        cmd.setOperationType(OperationEnum.DEL);

        triple = new Triple();
        triple.setKey(String.format(REGS_SERVICE_KEY_FORMAT, id));
        triple.setValue(JsonUtils.toJson(service).getBytes(StandardCharsets.UTF_8));
        cmd.setKey(triple);

        cmds.add(cmd);

        // TODO 3、删除服务器列表
        cmd = new RedisCmd();
        cmd.setType(CmdEnum.SET);
        cmd.setOperationType(OperationEnum.DEL);

        triple = new Triple();
        triple.setKey(String.format(REGS_BIZ_SERVICE_KEY_FORMAT, service.getBiz()));
        cmd.setKey(triple);

        cmds.add(cmd);

        // TODO 4、执行命令
        redisCmdRunner.exeCmds(cmds);

    }

    @Override
    public List<Service> getServicesByBiz(int biz) {

        List<Service> services = new ArrayList<>();

        String key = String.format(REGS_BIZ_SERVICE_KEY_FORMAT, biz);
        List<String> serviceIds = redisCmdRunner.getElementsFromSet(key);
        serviceIds.stream().forEach(id -> services.add(getServiceById(id)));

        return services;
    }

    @Override
    public Service getServiceById(String serviceId) {

        Preconditions.checkNotNull(serviceId);

        Service service = null;
        try (Jedis jedis = jedisPool.getResource()) {
            String key = String.format(REGS_SERVICE_KEY_FORMAT, serviceId);
            byte[] value = jedis.get(key.getBytes(StandardCharsets.UTF_8));

            if (value != null && value.length > 0) {
                service = JsonUtils.fromJson(new String(value), Service.class);
            }
        } catch (Exception ex) {
            logger.error("catch an exception when getUser.", ex);
        }

        return service;
    }

    @Override
    public List<String> getExpireServices(int timeoutMillis) {
        return null;
    }

    @Override
    public void updateService(Service service) {

        RedisCmd cmd = new RedisCmd();
        cmd.setType(CmdEnum.STRING);
        cmd.setOperationType(OperationEnum.UPDATE);

        Triple triple = new Triple();
        triple.setKey(String.format(REGS_SERVICE_KEY_FORMAT, service.getId()));
        triple.setValue(JsonUtils.toJson(service).getBytes(StandardCharsets.UTF_8));
        cmd.setKey(triple);

        redisCmdRunner.exeCmd(cmd);
    }

}

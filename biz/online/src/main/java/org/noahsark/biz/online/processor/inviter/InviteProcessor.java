package org.noahsark.biz.online.processor.inviter;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.noahsark.biz.online.config.CommonConfig;
import org.noahsark.biz.online.context.ServerContext;
import org.noahsark.biz.online.repository.OnlineRepository;
import org.noahsark.client.future.CommandCallback;
import org.noahsark.registration.domain.CandidateService;
import org.noahsark.registration.domain.ServiceQuery;
import org.noahsark.rocketmq.RocketmqProxy;
import org.noahsark.rocketmq.RocketmqTopic;
import org.noahsark.server.constant.BizServiceType;
import org.noahsark.server.processor.AbstractProcessor;
import org.noahsark.server.rpc.MultiRequest;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.Result;
import org.noahsark.server.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 邀请处理器
 *
 * @author zhangxt
 * @date 2021/5/11
 */
@Component
public class InviteProcessor extends AbstractProcessor<InviteInfo> {

    private static Logger log = LoggerFactory.getLogger(InviteProcessor.class);

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private OnlineRepository onlineRepository;

    @Override
    protected void execute(InviteInfo request, RpcContext context) {

        try {
            MultiRequest command = (MultiRequest) context.getCommand();
            log.info("receive a request: {}", command);

            Set<String> targetIds = new HashSet<>();
            targetIds.addAll(request.getUserIds());
            // TODO 业务操作
            // 向用户发起请求

            // 获取用户所在服务器,假定用户都在一台服务器上
            // 获取第一个用户所在服务器，只发送第一个用户的邀请信息，多个用户的邀请后期扩展
            String serviceId = onlineRepository.getResidedService(request.getUserIds().get(0));
            ServiceQuery serviceQuery = new ServiceQuery();
            serviceQuery.setId(serviceId);

            Result<CandidateService> result = ServerContext.regClient.serviceLookup(serviceQuery);
            CandidateService service = result.getData();

            MultiRequest multiRequest = new MultiRequest.Builder()
                    .biz(BizServiceType.BIZ_CLIENT)
                    .cmd(command.getCmd())
                    .serializer(command.getSerializer())
                    .type(command.getType())
                    .ver(command.getVer())
                    .payload(command.getPayload())
                    .topic(commonConfig.getMqProxy().getTopic())
                    .targetIds(targetIds)
                    .build();

            log.info("send a request: {}", multiRequest);

            RocketmqProxy proxy = ServerContext.mqProxy;

            RocketmqTopic topic = new RocketmqTopic();
            topic.setTopic(service.getTopic());
            proxy.sendAsync(topic, multiRequest, new CommandCallback() {
                @Override
                public void callback(Object result, int currentFanout, int fanout) {

                    log.info("currentFanout:{},fanout:{}", currentFanout, fanout);

                    List<Object> results = new ArrayList<>();
                    if (result instanceof List) {
                        results = (List<Object>) result;
                    } else {
                        results.add(result);
                    }

                    log.info("receive responses size: {}", results.size());

                    results.stream().forEach(rs -> {
                        String sPayload = new String((byte[]) rs);
                        JsonObject paylpad = new JsonParser().parse(sPayload).getAsJsonObject();
                        log.info("receive a response: {}", paylpad);
                    });

                    InviteResult inviteResult = new InviteResult();
                    inviteResult.setStatus((byte) 1);

                    RocketmqTopic topic = new RocketmqTopic();
                    topic.setTopic(command.getTopic());

                    Response response = Response.buildResponse(command, inviteResult, 0, "success");
                    response.setAttachment(topic);

                    context.sendResponse(response);
                }

                @Override
                public void failure(Throwable cause, int currentFanout, int fanout) {
                    log.warn("Invoke catch an exception!", cause);
                    log.info("currentFanout:{},fanout:{}", currentFanout, fanout);

                    context.sendResponse(Response.buildCommonResponse(context.getCommand(),
                            -1, "failed"));
                }
            }, 10000);

        } catch (Exception ex) {
            log.warn("Invoke catch an exception!", ex);

            context.sendResponse(Response.buildCommonResponse(context.getCommand(),
                    -1, "failed"));
        }

    }

    @Override
    protected Class<InviteInfo> getParamsClass() {
        return InviteInfo.class;
    }

    @Override
    protected int getBiz() {
        return 201;
    }

    @Override
    protected int getCmd() {
        return 1;
    }
}

package org.noahsark.server.processor;

import com.google.gson.JsonElement;
import org.noahsark.server.dispatcher.Dispatcher;
import org.noahsark.server.rpc.RpcContext;
import org.noahsark.server.rpc.RpcRequest;
import org.noahsark.server.serializer.Serializer;
import org.noahsark.server.serializer.SerializerManager;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/12/3
 */

public abstract class AbstractProcessor<T> implements Runnable {

    private static Logger log = LoggerFactory.getLogger(AbstractProcessor.class);

    private RpcRequest request;

    private Dispatcher dispatcher = Dispatcher.getInstance();

    protected String getProcessName() {
        return this.getBiz() + ":" + this.getCmd();
    }

    @PostConstruct
    public void register() {
        dispatcher.register(getProcessName(), this);
    }

    public void unregister() {
        dispatcher.unregister(getProcessName());
    }

    public void process(RpcRequest request) {
        this.request = request;
        run();
    }

    public void process(RpcRequest request, ExecutorService executorService) {
        this.request = request;
        executorService.execute(this);
    }

    protected RpcRequest getRequest() {
        return this.request;
    }

    @Override
    public void run() {
        RpcRequest rpcRequest = this.getRequest();
        T request = null;

        try {
            Object params = rpcRequest.getRequest().getPayload();

            if (!Void.class.equals(getParamsClass())) {

                Serializer serializer = SerializerManager.getInstance()
                    .getSerializer(rpcRequest.getRequest().getSerializer());

                if (params instanceof JsonElement) {
                    request = JsonUtils.fromJson((JsonElement) params, getParamsClass());
                } else if (params.getClass().isArray()) {
                    log.info("body: {}", new String((byte[]) params));
                    log.info("size: {}",((byte[]) params).length);
                    request = serializer.decode((byte[]) params, getParamsClass());
                }

                log.info("request: {}", JsonUtils.toJson(request));
            }

            execute(request, rpcRequest.getContext());

        } catch (Exception ex) {
            log.warn("catch an exception!", ex);
        }

    }

    protected abstract void execute(T request, RpcContext context);

    /**
     * 请求参数的类型，用于Json的反序列化
     *
     * @return 类型
     */
    protected abstract Class<T> getParamsClass();

    /**
     * 一个请求由两个部分组成：类名 + 方法名
     *
     * @return 请求对应的类的名称
     */
    protected abstract int getBiz();

    /**
     * 一个请求由两个部分组成：类名 + 方法名
     *
     * @return 请求对应的类的方法
     */
    protected abstract int getCmd();

}


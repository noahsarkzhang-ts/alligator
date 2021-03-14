package org.noahsark.server.processor;


import com.google.gson.JsonElement;
import java.util.concurrent.ExecutorService;
import javax.annotation.PostConstruct;
import org.noahsark.server.dispatcher.Dispatcher;
import org.noahsark.server.rpc.Response;
import org.noahsark.server.rpc.RpcRequest;
import org.noahsark.server.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/12/3
 */

public abstract class AbstractProcessor<T, R> implements Runnable {

    private static Logger log = LoggerFactory.getLogger(AbstractProcessor.class);

    private RpcRequest request;

    @Autowired
    private Dispatcher dispatcher;

    protected String getProcessName() {
        return this.getClassName() + ":" + this.getMethod();
    }

    @PostConstruct
    protected void register() {
        dispatcher.register(getProcessName(), this);
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

        JsonElement params = (JsonElement)rpcRequest.getRequest().getPayload();

        if (!Void.class.equals(getParamsClass())) {
            request = JsonUtils.fromJson(params, getParamsClass());

            log.info("request: {}", params.toString());
        }

        Response<R> result = execute(request);

        log.info("result : {}", JsonUtils.toJson(result));

        if (result != null) {

            rpcRequest.sendResponse(result);
        }
    }

    /**
     * 业务处理方法，每一个业务都要实现这个方法
     *
     * @param request 请求的参数类
     * @return 返回结果
     */
    protected abstract Response<R> execute(T request);

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
    protected abstract String getClassName();

    /**
     * 一个请求由两个部分组成：类名 + 方法名
     *
     * @return 请求对应的类的方法
     */
    protected abstract String getMethod();
}

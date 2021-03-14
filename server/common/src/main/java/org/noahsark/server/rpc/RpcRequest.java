package org.noahsark.server.rpc;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/12/3
 */
public class RpcRequest {

    private Request request;

    private RpcContext context;

    public RpcRequest() {
    }

    public RpcRequest(Request request, RpcContext context) {
        this.context = context;
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public RpcContext getContext() {
        return context;
    }

    public void setContext(RpcContext context) {
        this.context = context;
    }

    public void sendResponse(Response<?> response) {
        this.getContext().sendResponse(response);
    }
}

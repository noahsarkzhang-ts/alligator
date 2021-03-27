package org.noahsark.server.rpc;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/12/3
 */
public class RpcRequest {

    private RpcCommand request;

    private RpcContext context;

    public RpcRequest() {
    }

    public RpcRequest(Builder builder) {
        this.request = builder.request;
        this.context = builder.context;
    }

    public RpcRequest(Request request, RpcContext context) {
        this.context = context;
        this.request = request;
    }

    public RpcCommand getRequest() {
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

    public void sendResponse(Response response) {
        this.getContext().sendResponse(response);
    }

    public static class Builder {
        private RpcCommand request;
        private RpcContext context;

        public Builder request(RpcCommand request) {
            this.request = request;
            return this;
        }

        public Builder context(RpcContext context) {
            this.context = context;
            return this;
        }

        public RpcRequest build() {
            return new RpcRequest(this);
        }
    }
}

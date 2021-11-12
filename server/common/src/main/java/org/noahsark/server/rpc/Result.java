package org.noahsark.server.rpc;

import java.io.Serializable;

/**
 * 结果类
 * @author zhangxt
 * @date 2021/3/26
 */
public class Result<R> implements Serializable {

    private int code;

    private String message;

    private R data;

    public Result() {
    }

    public Result(Builder<R> builder) {
        this.code = builder.code;
        this.message = builder.message;
        this.data = builder.data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public R getData() {
        return data;
    }

    public void setData(R data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Builder<R> {
        private int code;
        private String message;
        private R data;

        public Builder<R> code(int code) {
            this.code = code;
            return this;
        }

        public Builder<R> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<R> data(R data) {
            this.data = data;
            return this;
        }

        public Result<R> build() {
            return new Result<>(this);
        }
    }
}

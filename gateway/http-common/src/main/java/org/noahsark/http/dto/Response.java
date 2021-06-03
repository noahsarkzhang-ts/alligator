package org.noahsark.http.dto;

import java.io.Serializable;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/27
 */
public class Response implements Serializable {

    private static final long serialVersionUID = 5383171949602087538L;

    /**
     * 默认成功码
     */
    private static final int SUCCESS_CODE = 0;

    /**
     * 默认错误码
     */
    private static final int FAILURE_CODE = 1;

    /**
     * 默认成功消息
     */
    private static final String SUCCESS_DEFAULT_MESSAGE = "OK";

    /**
     * 默认错误消息
     */
    private static final String FAILURE_DEFAULT_MESSAGE = "FAIL";
    /**
     * 响应编码
     */
    private int code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应结果,面向API编程,API的格式定义什么，这里面就是啥
     */
    private Object result;

    /**
     * @Description: 无参构造
     * @Author: yicai.liu
     * @Date 17:56 2017/7/4
     */
    public Response() {
        super();
    }

    /**
     * 带参构造
     *
     * @param code    响应编码
     * @param message 响应消息
     * @param result  响应结果
     */
    public Response(int code, String message, Object result) {
        this.code = code;
        this.message = message;
        this.result = result;
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean success() {
        return this.code == 0? true : false;
    }

    public static Response ok(String message, Object result) {
        return new Builder().ok(message, result).build();
    }

    public static Response ok(Object result) {
        return new Builder().ok(result).build();
    }

    public static Response ok() {
        return new Builder().ok().build();
    }

    public static Response fail(String message, Object result) {
        return new Builder().fail(message, result).build();
    }

    public static Response fail(Object result) {
        return new Builder().fail(result).build();
    }

    public static Response fail() {
        return new Builder().fail().build();
    }

    public static class Builder {

        private int code;

        private String message;

        private Object result;

        public Builder() {
        }

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder result(Object result) {
            this.result = result;
            return this;
        }

        /**
         * @Description: 默认成功返回, 自定义返回结果集&返回消息
         * @Author: yicai.liu
         * @Date 9:36 2017/7/6
         */
        public Builder ok(String message, Object result) {
            this.code = SUCCESS_CODE;
            this.message = message;
            this.result = result;
            return this;
        }

        /**
         * @Description: 默认成功返回, 自定义返回结果集
         * @Author: yicai.liu
         * @Date 9:36 2017/7/6
         */
        public Builder ok(Object result) {
            this.code = SUCCESS_CODE;
            this.message = SUCCESS_DEFAULT_MESSAGE;
            this.result = result;
            return this;
        }

        /**
         * @Description: 默认成功返回
         * @Author: yicai.liu
         * @Date 9:36 2017/7/6
         */
        public Builder ok() {
            this.code = SUCCESS_CODE;
            this.message = SUCCESS_DEFAULT_MESSAGE;
            return this;
        }

        /**
         * @Description: 默认错误返回, 自定义返回结果集&返回消息
         * @Author: yicai.liu
         * @Date 9:36 2017/7/6
         */
        public Builder fail(String message, Object result) {
            this.code = FAILURE_CODE;
            this.message = message;
            this.result = result;
            return this;
        }

        /**
         * @Description: 默认错误返回, 自定义返回结果集
         * @Author: yicai.liu
         * @Date 9:36 2017/7/6
         */
        public Builder fail(Object result) {
            this.code = FAILURE_CODE;
            this.message = FAILURE_DEFAULT_MESSAGE;
            this.result = result;
            return this;
        }

        /**
         * @Description: 默认错误返回
         * @Author: yicai.liu
         * @Date 9:36 2017/7/6
         */
        public Builder fail() {
            this.code = FAILURE_CODE;
            this.message = FAILURE_DEFAULT_MESSAGE;
            return this;
        }

        public Response build() {
            return new Response(code, message, result);
        }
    }

}


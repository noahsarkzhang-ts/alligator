package org.noahsark.http.common;

/**
 * @Description: 常量
 * @Author:
 * @Date: 9:20 2017/6/30
 */
public final class Constants {

    /**
     * @Description: 响应编码
     * @Author: yicai.liu
     * @Date 9:36 2017/6/30
     */
    public static final class ResponseCode {
        /**
         * 成功
         */
        public static final int OK = 0;

        /**
         * 失败
         */
        public static final int FAIL = -1;

        /**
         * 异常
         */
        public static final int EXCEPTION = -2;

        /**
         * 异常
         */
        public static final int TIMEOUT = -3;


    }

    /**
     * @Description: 响应消息
     * @Author: yicai.liu
     * @Date 9:22 2017/6/30
     */
    public static final class ResponseMsg {

        /**
         * 成功
         */
        public static final String SUCCESS = "success";

        /**
         * 失败
         */
        public static final String FAIL = "fail";

        /**
         * 失败
         */
        public static final String EXCEPTION = "exception";

        /**
         * 异常
         */
        public static final String TIMEOUT = "timeout";

    }

    /**
     * 接口超时(MS)
     */
    public static final long INTERFACE_TIMEOUT_MS = 5000L;
}

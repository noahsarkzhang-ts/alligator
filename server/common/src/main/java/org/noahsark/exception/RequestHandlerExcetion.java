package org.noahsark.exception;

/**
 * 请求操作异常
 * @author zhangxt
 * @date 2021/5/13
 */
@Deprecated
public class RequestHandlerExcetion extends RuntimeException {
    public RequestHandlerExcetion() {
        super();
    }

    public RequestHandlerExcetion(String message) {
        super(message);
    }

    public RequestHandlerExcetion(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestHandlerExcetion(Throwable cause) {
        super(cause);
    }

    protected RequestHandlerExcetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

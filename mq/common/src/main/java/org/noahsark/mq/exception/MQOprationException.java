package org.noahsark.mq.exception;

/**
 * Created by hadoop on 2021/5/3.
 */
public class MQOprationException extends RuntimeException {

    public MQOprationException() {
    }

    public MQOprationException(String message) {
        super(message);
    }

    public MQOprationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQOprationException(Throwable cause) {
        super(cause);
    }

    public MQOprationException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

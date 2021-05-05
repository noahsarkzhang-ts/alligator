package org.noahsark.mq.exception;

/**
 * Created by hadoop on 2021/5/3.
 */
public class MQClientException extends RuntimeException {

    public MQClientException() {
    }

    public MQClientException(String message) {
        super(message);
    }

    public MQClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public MQClientException(Throwable cause) {
        super(cause);
    }
}

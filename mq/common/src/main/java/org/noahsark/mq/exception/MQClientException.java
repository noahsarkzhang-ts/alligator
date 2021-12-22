package org.noahsark.mq.exception;

/**
 * MQ 客户端异常
 *
 * @author zhangxt
 * @date 2021/5/3
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

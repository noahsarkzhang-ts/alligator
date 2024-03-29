package org.noahsark.rabbitmq;

import org.noahsark.mq.Topic;

/**
 * RabbitMQ TOPIC
 *
 * @author zhangxt
 * @date 2021/9/29
 */
public class RabbitmqTopic implements Topic {

    /**
     * 交换机名称
     */
    private String exchangeName;

    /**
     * 队列名称
     */
    private String queueName;

    /**
     * 绑定 key
     */
    private String bindingKey;

    /**
     * 路由 key
     */
    private String routeKey;

    /**
     * 默认为 1
     */
    private int qos = 1;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getBindingKey() {
        return bindingKey;
    }

    public void setBindingKey(String bindingKey) {
        this.bindingKey = bindingKey;
    }

    public String getRouteKey() {
        return routeKey;
    }

    public void setRouteKey(String routeKey) {
        this.routeKey = routeKey;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    @Override
    public String toString() {
        return "RabbitmqTopic{" +
                "exchangeName='" + exchangeName + '\'' +
                ", queueName='" + queueName + '\'' +
                ", bindingKey='" + bindingKey + '\'' +
                ", routeKey='" + routeKey + '\'' +
                '}';
    }
}

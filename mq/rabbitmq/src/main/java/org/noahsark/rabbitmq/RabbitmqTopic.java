package org.noahsark.rabbitmq;

import org.noahsark.mq.Topic;

/**
 * @author: zhangxt
 * @version:
 * @date: 2021/9/29
 */
public class RabbitmqTopic implements Topic {

    /**
     *  交换机名称
     */
    private String exchangeName;

    /**
     *  队列名称
     */
    private String queueName;

    /**
     *  绑定 key
     */
    private String bindingKey;

    /**
     *  路由 key
     */
    private String routeKey;

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

package org.noahsark.rabbitmq;

import org.noahsark.mq.*;
import org.noahsark.rabbitmq.pool.RabbitmqChannelFactory;
import org.noahsark.rabbitmq.pool.RabbitmqChannelPool;
import org.noahsark.server.rpc.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * RabbitMQ 代理
 *
 * @author zhangxt
 * @date 2021/9/29
 */
public class RabbitmqProxy extends AbstractMqProxy {

    private static Logger logger = LoggerFactory.getLogger(RabbitmqProxy.class);

    private Properties config = new Properties();

    private RabbitmqConnection connection;

    private RabbitmqChannelFactory channelFactory;

    private RabbitmqChannelPool pool;

    private RabbitmqConsumer consumer;

    private RabbitmqProducer producer;

    private List<RabbitmqTopic> topics = new ArrayList<>();

    public RabbitmqProxy(Builder builder) {
        config.setProperty("username", builder.username);
        config.setProperty("password", builder.password);
        config.setProperty("vhost", builder.vhost);
        config.setProperty("urls", builder.urls);

        this.topics = builder.topics;

        init();
    }

    private void init() {
        try {
            /**
             * 初始化 Connection
             */
            connection = new RabbitmqConnection();
            connection.init(config);

            /**
             * 初始化 channelFactory
             */
            channelFactory = new RabbitmqChannelFactory(connection);

            /**
             * 初始化 ChannelPool
             */
            pool = new RabbitmqChannelPool(channelFactory);
            pool.init();

            /**
             * 初始化 consumer
             */
            consumer = new RabbitmqConsumer(connection, pool);
            consumer.subscribe(topics);
            consumer.registerMessageListener(new DefaultmqMessageListener(this));

            /**
             * 初始化 producer
             */
            producer = new RabbitmqProducer(connection, pool);

            initHolder();

        } catch (Exception ex) {
            logger.error("Catch an exception when init rabbitmq proxy.", ex);
        }

    }

    @Override
    protected void initHolder() {
        promiseHolder = new MqPromiseHolder(producer);
        channelHolder = new MqChannelHolder(producer, promiseHolder);
    }

    @Override
    public void sendOneway(Topic topic, Request request) {
        request.setRequestId(promiseHolder.nextId());
        request.setAttachment(topic);

        producer.sendOneway(producer.buildMessage(request));
    }

    @Override
    public void start() {
        producer.start();
        consumer.start();
    }

    @Override
    public void shutdown() {
        try {
            connection.close();
        } catch (Exception ex) {
            logger.error("Catch an exception when closing connection! ", ex);
        }
    }

    public static class Builder {
        /**
         * 用户名称
         */
        private String username;

        /**
         * 登录密码
         */
        private String password;

        /**
         * 虚拟主机，类似多租户的概念
         */
        private String vhost;

        /**
         * rabbitmq连接
         */
        private String urls;

        private List<RabbitmqTopic> topics = new ArrayList<>();

        public Builder username(String username) {
            this.username = username;

            return this;
        }

        public Builder password(String password) {
            this.password = password;

            return this;
        }

        public Builder vhost(String vhost) {
            this.vhost = vhost;

            return this;
        }

        public Builder urls(String urls) {
            this.urls = urls;

            return this;
        }

        public Builder topics(List<RabbitmqTopic> topics) {
            this.topics = topics;

            return this;
        }

        public Builder topic(RabbitmqTopic topic) {
            this.topics.add(topic);

            return this;
        }

        public RabbitmqProxy build() {
            return new RabbitmqProxy(this);
        }

    }
}

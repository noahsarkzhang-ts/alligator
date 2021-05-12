package org.noahsark.biz.online.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/11
 */
@Component
@ConfigurationProperties("common")
public class CommonConfig {

    private MqProxy mqProxy;

    private RegServer regServer;

    public static class RegServer {
        private String host;

        private int port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public  static class MqProxy {
        private String nameSrv;

        private String producerGroup;

        private String consumerGroup;

        private String topic;

        public String getNameSrv() {
            return nameSrv;
        }

        public void setNameSrv(String nameSrv) {
            this.nameSrv = nameSrv;
        }

        public String getProducerGroup() {
            return producerGroup;
        }

        public void setProducerGroup(String producerGroup) {
            this.producerGroup = producerGroup;
        }

        public String getConsumerGroup() {
            return consumerGroup;
        }

        public void setConsumerGroup(String consumerGroup) {
            this.consumerGroup = consumerGroup;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }

    public MqProxy getMqProxy() {
        return mqProxy;
    }

    public void setMqProxy(MqProxy mqProxy) {
        this.mqProxy = mqProxy;
    }

    public RegServer getRegServer() {
        return regServer;
    }

    public void setRegServer(RegServer regServer) {
        this.regServer = regServer;
    }
}

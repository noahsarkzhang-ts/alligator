package org.noahsark.gw.ws.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/3/22
 */
@Component
@ConfigurationProperties("common")
public class CommonConfig {

    private WorkQueueConfig workQueue;

    private ServerConfig serverConfig;

    private RegServer regServer;

    private MqProxy mqProxy;

    public static class WorkQueueConfig {

        private int maxQueueNum;
        private int maxThreadNum;

        public int getMaxQueueNum() {
            return maxQueueNum;
        }

        public void setMaxQueueNum(int maxQueueNum) {
            this.maxQueueNum = maxQueueNum;
        }

        public int getMaxThreadNum() {
            return maxThreadNum;
        }

        public void setMaxThreadNum(int maxThreadNum) {
            this.maxThreadNum = maxThreadNum;
        }
    }

    public static class ServerConfig {

        private String host;

        private int port;

        private int zone;

        private String id;

        private String name;

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

        public int getZone() {
            return zone;
        }

        public void setZone(int zone) {
            this.zone = zone;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

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

    public WorkQueueConfig getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(WorkQueueConfig workQueue) {
        this.workQueue = workQueue;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public RegServer getRegServer() {
        return regServer;
    }

    public void setRegServer(RegServer regServer) {
        this.regServer = regServer;
    }

    public MqProxy getMqProxy() {
        return mqProxy;
    }

    public void setMqProxy(MqProxy mqProxy) {
        this.mqProxy = mqProxy;
    }
}

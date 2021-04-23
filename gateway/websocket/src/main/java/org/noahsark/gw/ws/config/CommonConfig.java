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
}

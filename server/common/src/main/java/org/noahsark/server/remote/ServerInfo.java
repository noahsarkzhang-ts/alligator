package org.noahsark.server.remote;

import java.net.URI;

/**
 * Created by hadoop on 2021/4/4.
 */
public class ServerInfo {

    private String originUrl;

    private String host;

    private int port;

    private URI uri;

    public ServerInfo() {
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

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

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }
}

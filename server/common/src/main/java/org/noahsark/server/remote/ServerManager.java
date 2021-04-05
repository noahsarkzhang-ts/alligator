package org.noahsark.server.remote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hadoop on 2021/4/4.
 */
public class ServerManager {

    private ServerInfo current;

    private List<ServerInfo> all;

    private List<ServerInfo> avialable;

    public ServerManager() {
        all = new ArrayList<>();
        avialable = new ArrayList<>();
    }

    public ServerManager(List<ServerInfo> servers) {
        this();

        all.addAll(servers);
        avialable.addAll(servers);
    }

    public ServerInfo toggleServer() {

        ServerInfo serverInfo = null;

        if (!avialable.isEmpty()) {
            serverInfo = avialable.get(0);

            current = serverInfo;

            avialable.remove(serverInfo);

            return serverInfo;
        }

        return serverInfo;
    }

    public void reset() {
        avialable.addAll(all);
    }
}

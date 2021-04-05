package org.noahsark.server.remote;

import org.noahsark.client.future.CommandCallback;
import org.noahsark.client.future.RpcPromise;
import org.noahsark.client.manager.ConnectionManager;
import org.noahsark.server.rpc.Request;
import org.noahsark.server.rpc.RpcCommand;

/**
 * Created by hadoop on 2021/3/7.
 */
public interface RemotingClient {

  void connect();

  void shutdown();

  void ping();

  void toggleServer();

  ServerInfo getServerInfo();

  ConnectionManager getConnectionManager();

  void sendMessage(RpcCommand command);

  RpcPromise invoke(Request request, CommandCallback commandCallback, int timeoutMillis);

}

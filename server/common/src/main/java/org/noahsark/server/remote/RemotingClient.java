package org.noahsark.server.remote;

import org.noahsark.server.future.CommandCallback;
import org.noahsark.server.future.RpcPromise;
import org.noahsark.server.rpc.Request;
import org.noahsark.server.rpc.RpcCommand;

/**
 * Created by hadoop on 2021/3/7.
 */
public interface RemotingClient {

  void connect();

  void shutdown();

  RetryPolicy getRetryPolicy();

  void ping();

  void toggleServer();

  void sendMessage(RpcCommand command);

  RpcPromise invoke(Request reques, CommandCallback callback);

}

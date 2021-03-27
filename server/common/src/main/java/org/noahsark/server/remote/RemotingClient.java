package org.noahsark.server.remote;

import org.noahsark.server.future.CommandCallback;
import org.noahsark.server.future.RpcPromise;
import org.noahsark.server.rpc.Request;

/**
 * Created by hadoop on 2021/3/7.
 */
public interface RemotingClient {

  void connect();

  void shutdown();

  RetryPolicy getRetryPolicy();

  void ping();

  void toggleServer();

  void sendMessage(String text);

  RpcPromise invoke(Request reques, CommandCallback callback);

}

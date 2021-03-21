package org.noahsark.server.remote;


/**
 * Created by hadoop on 2021/3/7.
 */
public interface RemotingServer {

  /**
   * 启动远程服务
   */
  void start();

  /**
   *  关闭远程服务
   */
  void shutdown();

}

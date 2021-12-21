package org.noahsark.server.remote;

/**
 * 服务器接口
 * @author zhangxt
 * @date 2021/3/7
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

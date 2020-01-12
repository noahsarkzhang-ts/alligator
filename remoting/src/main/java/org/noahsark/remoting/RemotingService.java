package org.noahsark.remoting;

/**
 * Created by hadoop on 2020/1/12.
 */
public interface RemotingService {

  /**
   * 启动服务
   */
  void start();

  /**
   *  关闭服务
   */
  void shutdown();

}

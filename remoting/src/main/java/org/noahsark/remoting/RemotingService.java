package org.noahsark.remoting;

/**
 * @author: noahsark
 * @version:
 * @date: 2020/1/14
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

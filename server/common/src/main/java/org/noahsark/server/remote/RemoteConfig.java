package org.noahsark.server.remote;

/**
 * RPC 配置项
 * @author zhangxt
 * @date 2021/3/21
 */
public class RemoteConfig {

  public static final String THREAD_NUM_OF_QUEUE_KEY = "THREAD_NUM_OF_QUEUE_KEY";

  public static final String CAPACITY_OF_QUEUE_KEY = "CAPACITY_OF_QUEUE_KEY";

  public static final String SSL_ENABLE_KEY = "SSL_ENABLE_KEY";

  public static final int THREAD_NUM_OF_QUEUE_DEFAULT = 100;

  public static final int CAPACITY_OF_QUEUE_DEFAULT = 1000;

  public static final boolean SSL_ENABLE_DEFAULT = false;

}

package org.noahsark.server.remote;


import java.util.Objects;

/**
 * RCP 配置项
 * @author zhangxt
 * @date 2021/3/21
 */
public class RemoteOption<T> {

  private String name;

  private T defaultValue;

  public RemoteOption() {
  }

  public RemoteOption(String name, T value) {
    this.name = name;
    this.defaultValue = value;
  }

  public T getDefaultValue() {
    return this.defaultValue;
  }

  public static <T> RemoteOption<T> valueOf(String name, T value) {
    return new RemoteOption<>(name, value);
  }

  public static final RemoteOption<Integer> THREAD_NUM_OF_QUEUE = valueOf(
      RemoteConfig.THREAD_NUM_OF_QUEUE_KEY, RemoteConfig.THREAD_NUM_OF_QUEUE_DEFAULT);

  public static final RemoteOption<Integer> CAPACITY_OF_QUEUE = valueOf(
      RemoteConfig.CAPACITY_OF_QUEUE_KEY, RemoteConfig.CAPACITY_OF_QUEUE_DEFAULT);

  public static final RemoteOption<Boolean> SSL_ENABLE = valueOf(RemoteConfig.SSL_ENABLE_KEY,
      RemoteConfig.SSL_ENABLE_DEFAULT);

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RemoteOption<?> that = (RemoteOption<?>) o;

    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}

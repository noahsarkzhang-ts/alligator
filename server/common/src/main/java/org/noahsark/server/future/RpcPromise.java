package org.noahsark.server.future;

import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;

import java.time.Instant;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/25
 */
public class RpcPromise extends DefaultPromise<Object> implements Comparable<RpcPromise> {

  private static Logger log = LoggerFactory.getLogger(RpcPromise.class);

  private static final UnorderedThreadPoolEventExecutor EVENT_EXECUTOR = new UnorderedThreadPoolEventExecutor(
      5);

  private long timeStampMillis;

  private int requestId;

  public RpcPromise() {
    super(EVENT_EXECUTOR);

    Instant instant = Instant.now();
    timeStampMillis = instant.toEpochMilli();

  }

  public void addCallback(CommandCallback callback) {
    this.addListener(new GenericFutureListener<Future<? super Object>>() {
      @Override
      public void operationComplete(Future<? super Object> future) throws Exception {

        Object result = null;

        try {
          result = future.get();

          callback.callback(result);

        } catch (InterruptedException ex) {
          log.warn("catch an ception.", ex);
        } catch (ExecutionException ex) {
          log.warn("catch an ception.", ex);
        }
      }
    });
  }

  public long getTimeStampMillis() {
    return timeStampMillis;
  }

  public void setTimeStampMillis(long timeStampMillis) {
    this.timeStampMillis = timeStampMillis;
  }

  public int getRequestId() {
    return requestId;
  }

  public void setRequestId(int requestId) {
    this.requestId = requestId;
  }

  public static void main(String[] args) {
        /*System.out.println("class = " + TypeUtils.getFirstParameterizedType(new RpcPromise<UserInfo>(){}));*/

  }

  @Override
  public int compareTo(RpcPromise o) {
    return (int) (this.timeStampMillis - o.getTimeStampMillis());
  }
}

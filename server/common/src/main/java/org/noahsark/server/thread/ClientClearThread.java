package org.noahsark.server.thread;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.noahsark.client.future.FutureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 请求清理线程
 *
 * @author zhangxt
 * @date 2021/4/10
 */
public class ClientClearThread extends ServiceThread {

    private static Logger log = LoggerFactory.getLogger(ClientClearThread.class);

    private static final int TIMEOUT = 5 * 60 * 60;


    @Override
    public void run() {

        log.info("clean thread start in {}", LocalDateTime.now());

        while (!this.isStopped()) {
            try {

                // 1.清空promise
                FutureManager.getInstance().clear(TIMEOUT);

                TimeUnit.SECONDS.sleep(60);

            } catch (Exception ex) {
                log.warn("catch an exception!", ex);
            }
        }

        log.info(" {} stop in {}", this.getServiceName(), LocalDateTime.now());

    }

    @Override
    public String getServiceName() {
        return "client-clear-thread";
    }
}

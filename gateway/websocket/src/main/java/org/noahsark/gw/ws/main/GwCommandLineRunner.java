package org.noahsark.gw.ws.main;

import org.noahsark.gw.ws.config.CommonConfig;
import org.noahsark.server.queue.WorkQueue;
import org.noahsark.server.ws.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by hadoop on 2021/3/13.
 */
@Component
public class GwCommandLineRunner  implements CommandLineRunner {

  private static Logger log = LoggerFactory.getLogger(GwCommandLineRunner.class);

  @Autowired
  private CommonConfig config;

  @Autowired
  private WorkQueue workQueue;

  @Override
  public void run(String... strings) throws Exception {

    final WebSocketServer webSocketServer = new WebSocketServer(config.getServerConfig().getHost(),
        config.getServerConfig().getPort());

    webSocketServer.setWorkQueue(workQueue);
    webSocketServer.init();

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        webSocketServer.shutdown();
      }
    });

    webSocketServer.start();

    log.info("Stat server!!!");

  }
}

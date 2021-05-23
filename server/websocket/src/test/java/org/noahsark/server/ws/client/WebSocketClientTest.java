package org.noahsark.server.ws.client;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.noahsark.client.future.CommandCallback;
import org.noahsark.server.rpc.Request;
import org.noahsark.server.ws.server.WebSocketServerTest;

import java.util.concurrent.TimeUnit;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/3/22
 */
public class WebSocketClientTest {


    @Test
    public void clientTest() {
        WebSocketServerTest.UserInfo userInfo = new WebSocketServerTest.UserInfo();
        userInfo.setUserId("1002");
        userInfo.setUserName("allen");
        userInfo.setPassword("pwd");

        singalServer(userInfo);

        userInfo = new WebSocketServerTest.UserInfo();
        userInfo.setUserId("1003");
        userInfo.setUserName("allen");
        userInfo.setPassword("pwd");

        singalServer(userInfo);

    }

    @Test
    public void testClient() {
        WebSocketServerTest.UserInfo userInfo = new WebSocketServerTest.UserInfo();


        userInfo = new WebSocketServerTest.UserInfo();
        userInfo.setUserId("1003");
        userInfo.setUserName("allen");
        userInfo.setPassword("pwd");

        singalServer(userInfo);
    }

    private void singalServer(WebSocketServerTest.UserInfo userInfo) {
        String url = System.getProperty("url", "ws://192.168.1.102:9091/websocket");

        WebSocketClient client = new WebSocketClient(url);
        client.registerProcessor(new InviteUserProcessor());
        client.connect();

        try {

            TimeUnit.SECONDS.sleep(2);

            Request request = new Request.Builder()
                .biz(1)
                .cmd(1)
                .payload(userInfo)
                .build();

            client.invoke(request, new CommandCallback() {
                @Override
                public void callback(Object result, int currentFanout, int fanout) {
                    System.out.println("result = " + result);
                }

                @Override
                public void failure(Throwable cause, int currentFanout, int fanout) {
                    cause.printStackTrace();
                }
            }, 300000);

            TimeUnit.HOURS.sleep(1);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            client.shutdown();
        }
    }


    @Test
    public void multiServerTest() {

        List<String> urls = new ArrayList<>();

        urls.add("ws://192.168.9.103:9090/websocket");
        urls.add("ws://192.168.9.103:9091/websocket");

        WebSocketClient client = new WebSocketClient(urls);

        try {

            client.connect();

            TimeUnit.SECONDS.sleep(120);


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            client.shutdown();
        }
    }

}

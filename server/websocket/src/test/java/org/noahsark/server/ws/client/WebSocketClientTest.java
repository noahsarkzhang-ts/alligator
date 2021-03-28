package org.noahsark.server.ws.client;

import org.junit.Test;
import org.noahsark.server.future.CommandCallback;
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
        String url = System.getProperty("url", "ws://192.168.9.100:9090/websocket");

        WebSocketClient client = new WebSocketClient(url);
        client.connect();

        try {

            TimeUnit.SECONDS.sleep(2);

            String [] requests = {
                    "{\"biz\":1,\"cmd\":1000,\"requestId\":1,\"version\":\"V1.0\",\"payload\":{\"userName\":\"allan\",\"password\":\"test\"}}",
                    "Hello World!"
            };

            WebSocketServerTest.UserInfo userInfo = new WebSocketServerTest.UserInfo();
            userInfo.setUserName("allen");
            userInfo.setPassword("pwd");

            Request request = new Request.Builder()
                    .biz(1)
                    .cmd(1000)
                    .payload(userInfo)
                    .build();

            client.invoke(request, new CommandCallback() {
                @Override
                public void callback(Object result) {
                    System.out.println("result = " + result);
                }
            });

           /* for (String request: requests) {
                System.out.println("request = " + request);
                client.sendMessage(request);
            }*/

            TimeUnit.SECONDS.sleep(120);

            /*BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String msg = console.readLine();
                if (msg == null) {
                    break;
                } else if ("bye".equals(msg.toLowerCase())) {
                    client.sendMessage(new CloseWebSocketFrame());
                    client.shutdown();
                    break;
                } else if ("ping".equals(msg.toLowerCase())) {
                    WebSocketFrame frame = new PingWebSocketFrame(
                            Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
                    client.sendMessage(frame);
                } else {
                    client.sendMessage(msg);
                }
            }*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            client.shutdown();
        }
    }

}

package org.noahsark.rocketmq;

import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * Created by hadoop on 2021/5/2.
 */
public class ConsumerTest {

    @Test
    public void testConsumer() throws Exception{
        RocketmqConsumer consumer = new RocketmqConsumer("test-consumer",
            "120.79.235.83:9876");

        RocketmqTopic topic = new RocketmqTopic();
        topic.setTopic("t-alligator-reg");
        topic.setTag("*");

        consumer.subscribe(topic);

        consumer.registerMessageListener(new RocketmqMessageListener(){

        });

        consumer.start();

        TimeUnit.SECONDS.sleep(60);
    }

}

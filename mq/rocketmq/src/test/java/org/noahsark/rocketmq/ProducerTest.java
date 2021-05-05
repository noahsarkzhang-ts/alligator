package org.noahsark.rocketmq;

import org.junit.Test;

/**
 * Created by hadoop on 2021/5/2.
 */
public class ProducerTest {

    @Test
    public void testProducer() throws Exception {
        RocketmqProducer producer = new RocketmqProducer("Test-Group",
            "120.79.235.83:9876");

        RocketmqMessage message = new RocketmqMessage();
        message.setTopic("t-alligator-reg");
        message.setContent("hello rocketmq".getBytes());

        producer.send(message);



    }
}

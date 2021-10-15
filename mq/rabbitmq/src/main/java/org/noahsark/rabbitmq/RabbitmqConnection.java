package org.noahsark.rabbitmq;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * <p>代表了连接到 RabbitMQ broker 的 TCP Connection </p>
 * Connection 内部包含一个独立的线程，用于处理 TCP 事件循环
 * Connection(1) --> channel(N) --> queue(N)
 * 一个 Connection 可以包含多个 channel, 一个 channel 可以订阅多个 queue.
 */

public class RabbitmqConnection {

    /**
     * 用户名称
     */
    private String username;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 虚拟主机，类似多租户的概念
     */
    private String vhost;

    /**
     * rabbitmq连接
     */
    private String urls;

    /**
     * rabbitmq连接工厂
     */
    private ConnectionFactory factory;

    /**
     * rabbitmq连接
     */
    private Connection connection;

    public RabbitmqConnection() {
    }

    /**
     * Description: 初始化函数 <br>
     *
     * @throws Exception 异常
     * @see
     */
    public void init(Properties config) throws Exception {

        this.username = config.getProperty("username");
        this.password = config.getProperty("password");
        this.vhost = config.getProperty("vhost");
        this.urls = config.getProperty("urls");

        String[] lists = urls.split(",");
        List<Address> addressList = new ArrayList<>();

        for (String url : lists) {
            url = url.trim();
            String[] segments = url.split(":");

            Address address = new Address(segments[0], Integer.parseInt(segments[1]));
            addressList.add(address);
        }

        factory = new ConnectionFactory();
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(vhost);

        connection = factory.newConnection(addressList);
    }

    public Connection getConnection() {
        return connection;
    }

    public synchronized void close() throws IOException {

        if(connection != null) {
            this.connection.close();

            this.connection = null;
        }


    }

}

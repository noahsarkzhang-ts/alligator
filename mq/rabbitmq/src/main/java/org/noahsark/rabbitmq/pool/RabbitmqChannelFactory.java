package org.noahsark.rabbitmq.pool;

import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.noahsark.rabbitmq.RabbitmqConnection;

/**
 * Rabbitmq channel连接池
 * @author zhangxt
 * @version 2018年7月18日
 * @see RabbitmqChannelFactory
 * @since
 */

public class RabbitmqChannelFactory extends BasePooledObjectFactory<Channel> {

    private RabbitmqConnection connection;
     
    public RabbitmqChannelFactory() {
        
    }
    
    public RabbitmqChannelFactory(RabbitmqConnection connection) {
        this.connection = connection;
    }
    
    @Override
    public Channel create() throws Exception {
        
        return connection.getConnection().createChannel();
    }

    @Override
    public PooledObject<Channel> wrap(Channel obj) {
        
        return new DefaultPooledObject<Channel>(obj);
    }

    @Override
    public void destroyObject(PooledObject<Channel> p) throws Exception {
       p.getObject().close(); 
       
    }

    @Override
    public boolean validateObject(PooledObject<Channel> p) {
        
        return p.getObject().isOpen();
    }
    

}

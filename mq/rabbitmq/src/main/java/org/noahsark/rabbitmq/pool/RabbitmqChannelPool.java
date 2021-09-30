/*
 * 文件名：RabbitmqChannelPool.java
 * 版权：Copyright by www.fsmeeting.com
 * 描述：
 * 修改人：zhangxt
 * 修改时间：2018年7月18日
 * 修改内容：
 */

package org.noahsark.rabbitmq.pool;

import com.rabbitmq.client.Channel;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.NoSuchElementException;

/**
 * Rabbitmq通道池
 *
 * @author zhangxt
 * @version 2018年7月18日
 * @see RabbitmqChannelPool
 * @since
 */

public class RabbitmqChannelPool implements ObjectPool<Channel> {

    /*
     *  真正的通道对象池
     */
    private GenericObjectPool<Channel> pool;

    /*
     * 最大的通道数
     */
    private int maxTotal = 6;

    /*
     * 最小空闲通道数
     */
    private int minIdle = 0;

    /*
     * 最大空闲通道数
     */
    private int maxIdle = 6;

    /*
     * 最大等待毫秒数
     */
    private long maxWaitMillis = 120000;

    /*
     * 空闲回收器线程运行期间休眠的时间毫秒数,如果设置为非正数,则不运行空闲回收器线程
     */
    private int timeBetweenEvictionRunsMillis = 3000;

    /*
     * 空闲对象回收器回收前在池中保持空闲状态的最小时间毫秒数
     */
    private int minEvictableIdleTimeMillis = 1000 * 60 * 30;

    /*
     * 当池中对象用完时,是否阻塞请求
     */
    private boolean blockWhenExhausted = true;

    /*
     * 设定在进行后台对象清理时，每次检查对象数
     */
    private int numTestsPerEvictionRun = 3;

    /*
     * 指明是否在从池中取出对象前进行检验
     */
    private boolean testOnBorrow = true;

    /*
     * 指明是否在归还到池中前进行检验
     */
    private boolean testOnReturn = true;

    /*
     * 指明连接是否被空闲连接回收器
     */
    private boolean testWhileIdle = true;

    /*
     * 是否开启jmx
     */
    private boolean jmxEnabled = true;

    /*
     * channel对象工厂
     */
    private RabbitmqChannelFactory objectFactory;

    /**
     * 构造函数
     */
    public RabbitmqChannelPool() {
    }

    /**
     * 构造函数
     */
    public RabbitmqChannelPool(RabbitmqChannelFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    /**
     * Description: 初始化函数<br>
     *
     * @see
     */
    public void init() {
        if (pool == null) {
            GenericObjectPoolConfig poolConf = new GenericObjectPoolConfig();

            poolConf.setMaxTotal(maxTotal);
            poolConf.setMaxIdle(maxIdle);
            poolConf.setMinIdle(minIdle);
            poolConf.setMaxWaitMillis(maxWaitMillis);
            poolConf.setBlockWhenExhausted(blockWhenExhausted);
            poolConf.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            poolConf.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
            poolConf.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            poolConf.setJmxEnabled(jmxEnabled);

            /* 保证获取有效的池对象 */
            poolConf.setTestOnBorrow(testOnBorrow);
            poolConf.setTestOnReturn(testOnReturn);
            poolConf.setTestWhileIdle(testWhileIdle);

            pool = new GenericObjectPool<>(objectFactory, poolConf);
        }
    }

    @Override
    public Channel borrowObject() throws Exception, NoSuchElementException, IllegalStateException {

        return pool.borrowObject();
    }

    @Override
    public void returnObject(Channel obj) throws Exception {
        pool.returnObject(obj);
    }

    @Override
    public void invalidateObject(Channel obj) throws Exception {
        pool.invalidateObject(obj);
    }

    @Override
    public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {
        pool.addObject();
    }

    @Override
    public int getNumIdle() {

        return pool.getNumIdle();
    }

    @Override
    public int getNumActive() {
        return pool.getNumActive();
    }

    @Override
    public void clear() throws Exception, UnsupportedOperationException {
        pool.clear();

    }

    @Override
    public void close() {
        pool.clear();

    }

    public GenericObjectPool<Channel> getPool() {
        return pool;
    }

    public void setPool(GenericObjectPool<Channel> pool) {
        this.pool = pool;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public RabbitmqChannelFactory getObjectFactory() {
        return objectFactory;
    }

    public void setObjectFactory(RabbitmqChannelFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public boolean isBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    public void setBlockWhenExhausted(boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }

    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public int getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(int minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isJmxEnabled() {
        return jmxEnabled;
    }

    public void setJmxEnabled(boolean jmxEnabled) {
        this.jmxEnabled = jmxEnabled;
    }

}

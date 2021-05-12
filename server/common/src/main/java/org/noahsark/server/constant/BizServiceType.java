package org.noahsark.server.constant;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/12
 */
public class BizServiceType {

    /**
     *  0-99 系统服务
     */

    /**
     * 系统服务，如心跳
     */
    public static final int BIZ_SYS = 1;

    /**
     * 注册中心服务
     */
    public static final int BIZ_REGISTRAION = 2;

    /**
     *  100-200 网关类服务
     */

    /**
     * websockets网关
     */
    public static final int BIZ_GW_WS = 100;

    /**
     * TCP 网关
     */
    public static final int BIZ_GW_TCP = 101;

    /**
     * http 网关
     */
    public static final int BIZ_GW_HTTP = 102;

    /**
     * http2网关
     */
    public static final int BIZ_GW_HTTP2 = 103;

    /**
     *  在线服务
     */
    public static final int BIZ_ONLINE = 201;
}

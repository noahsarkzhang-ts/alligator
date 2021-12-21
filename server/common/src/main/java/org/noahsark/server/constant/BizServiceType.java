package org.noahsark.server.constant;

/**
 * 业务服务类型
 * @author zhangxt
 * @date 2021/4/12
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
     * 用户事件服务
     */
    public static final int BIZ_USER_EVENT = 3;

    /**
     * 服务事件服务
     */
    public static final int BIZ_SERVICE_EVENT = 4;

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
     *  200-300 业务类服务
     */

    /**
     * 在线服务
     */

    public static final int BIZ_ONLINE = 201;

    /**
     * 客户端 300-400
     */
    public static final int BIZ_CLIENT = 300;
}

package org.noahsark.server.rpc;

import java.util.List;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/4/22
 */
public class MqCommand extends RpcCommand {

    /**
     *  service id
     */
    private String serviceId;

    /**
     *  发起方 userId
     */
    private String sourceId;

    /**
     *  目标用户id
     */
    private List<String> targetIds;

    @Override
    public String toString() {
        return "MqCommand{" +
                "serviceId='" + serviceId + '\'' +
                ", sourceId='" + sourceId + '\'' +
                ", targetIds=" + targetIds +
                '}';
    }
}

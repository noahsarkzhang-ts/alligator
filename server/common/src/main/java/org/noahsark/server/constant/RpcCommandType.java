
package org.noahsark.server.constant;

public class RpcCommandType {
    /**
     * rpc response
     */
    public static final byte RESPONSE = (byte) 0x02;
    /**
     * rpc request
     */
    public static final byte REQUEST = (byte) 0x01;
    /**
     * rpc oneway request
     */
    public static final byte REQUEST_ONEWAY = (byte) 0x03;

    /**
     *  rpc stream
     */
    public static final byte STREAM = (byte) 0x04;
}

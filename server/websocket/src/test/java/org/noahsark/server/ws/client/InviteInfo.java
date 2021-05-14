package org.noahsark.server.ws.client;

/**
 * @author: noahsark
 * @version:
 * @date: 2021/5/11
 */
public class InviteInfo {

    private String userId;

    private byte type;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}

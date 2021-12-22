package org.noahsark.biz.online.processor.inviter;

import java.util.List;

/**
 * 邀请信息
 *
 * @author zhangxt
 * @date 2021/5/11
 */
public class InviteInfo {

    private List<String> userIds;

    private byte type;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}

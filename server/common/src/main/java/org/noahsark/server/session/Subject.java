package org.noahsark.server.session;

import java.io.Serializable;

/**
 * 会话绑定的用户
 *
 * @author zhangxt
 * @date 2021/4/10
 */
public interface Subject extends Serializable {

    /**
     * 用户id
     *
     * @return id
     */
    String getId();

}

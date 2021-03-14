package org.noahsark.server.session;

import io.netty.channel.Channel;
import java.util.Date;

/**
 * Created by hadoop on 2021/3/13.
 */
public class Session {

  private UserInfo user;

  private Channel channel;

  private Date lastAccessTime;

  public UserInfo getUser() {
    return user;
  }

  public void setUser(UserInfo user) {
    this.user = user;
  }

  public Channel getChannel() {
    return channel;
  }

  public void setChannel(Channel channel) {
    this.channel = channel;
  }

  public Date getLastAccessTime() {
    return lastAccessTime;
  }

  public void setLastAccessTime(Date lastAccessTime) {
    this.lastAccessTime = lastAccessTime;
  }
}

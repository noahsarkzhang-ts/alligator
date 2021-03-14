package org.noahsark.server.session;

/**
 * Created by hadoop on 2021/3/13.
 */
public class UserInfo {

  private String clientId;

  private String userName;

  private String password;

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "UserInfo{" +
        "clientId='" + clientId + '\'' +
        ", userName='" + userName + '\'' +
        ", password='" + password + '\'' +
        '}';
  }
}

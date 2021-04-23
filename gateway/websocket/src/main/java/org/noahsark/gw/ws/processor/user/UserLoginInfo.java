package org.noahsark.gw.ws.processor.user;

/**
 * Created by hadoop on 2021/3/13.
 */
public class UserLoginInfo {

    private String userName;

    private String password;

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
}

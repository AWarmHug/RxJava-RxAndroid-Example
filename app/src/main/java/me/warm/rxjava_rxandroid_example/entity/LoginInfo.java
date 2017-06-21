package me.warm.rxjava_rxandroid_example.entity;

/**
 * Created by warm on 17/6/20.
 */

public class LoginInfo {
    private boolean login;
    private User user;


    public LoginInfo(boolean login, User user) {
        this.login = login;
        this.user = user;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginInfo{" +
                "login=" + login +
                ", user=" + user +
                '}';
    }
}

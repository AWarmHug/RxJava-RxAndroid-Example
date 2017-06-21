package me.warm.rxjava_rxandroid_example.entity;

/**
 * Created by warm on 17/6/20.
 */

public class Family {
    private User user;
    private Parent parent;

    public Family(User user, Parent parent) {
        this.user = user;
        this.parent = parent;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Family{" +
                "user=" + user +
                ", parent=" + parent +
                '}';
    }
}

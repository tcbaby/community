package com.tcbaby.community.pojo;

/**
 * @author tcbaby
 * @date 20/05/07 11:08
 */
public class HostHolder {

    private final static ThreadLocal<User> local = new ThreadLocal<>();

    public static User getUser() {
        return local.get();
    }

    public static void setUser(User user) {
        local.set(user);
    }

    public static void clean() {
        local.remove();
    }
}

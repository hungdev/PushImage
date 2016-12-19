package net.downloadblog.manic.pushimage.object;

/**
 * Created by ManIc on 9/15/2016.
 */
public class User {
    public String username;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}

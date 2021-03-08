package server;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Users {

    private List<String> users = new ArrayList<>();
    private List<String> onlineUsers = new ArrayList<>();

    public Users() {
        users.add("Lars");
        users.add("Johnny");
        users.add("Bendt");
        users.add("Luke");
        users.add("Bob");
        users.add("Rey");
    }

    public List<String> getUsers() {
        return this.users;
    }

    public void addOnlineUsers(String user) {
        onlineUsers.add(user);
    }

    public List<String> getOnlineUsers() {
        return onlineUsers;
    }

}

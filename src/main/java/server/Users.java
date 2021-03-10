package server;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Users {

    private Map<String, Boolean> users = new ConcurrentHashMap();

    public Users() {
        users.put("Lars", false);
        users.put("Johnny", false);
        users.put("Bendt", false);
        users.put("Luke", false);
        users.put("Bob", false);
        users.put("Rey", false);
    }

    public boolean getOnlineUser(String user) {
        return users.get(user);
    }

    public void addOnlineUsers(String user) {
        users.replace(user, true);
    }

    public void removeOnlineUser(String user) {
       users.replace(user, false);
    }

    public Map<String, Boolean> getUsers() {
        return users;
    }
}

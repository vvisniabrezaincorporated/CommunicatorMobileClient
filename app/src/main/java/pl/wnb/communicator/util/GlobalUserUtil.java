package pl.wnb.communicator.util;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class GlobalUserUtil {

    private static GlobalUserUtil instance;
    private String name;
    private ArrayList<String> onlineUsers = new ArrayList<>();

    private GlobalUserUtil() {
    }

    public static GlobalUserUtil getInstance() {
        if (instance == null) {
            instance = new GlobalUserUtil();
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addOnlineUser(String name) {
        onlineUsers.add(name);
    }

    public void addAllOnlineUser(ArrayList<String> newOnlineUsers) {
        for (String user : newOnlineUsers) {
            if (user.equals(name))
                continue;
            onlineUsers.addAll(newOnlineUsers);
        }
    }

    public void removeOnlineUser(String name) {
        onlineUsers.remove(name);
    }

    public ArrayList<String> getOnlineUsers() {
        return onlineUsers;
    }
}

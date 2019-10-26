package pl.wnb.communicator.model.util;

import android.app.Activity;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

public class GlobalUserUtil {

    private static GlobalUserUtil instance;
    private String name;
    private boolean subscribed = false;
    private ArrayList<String> onlineUsers = new ArrayList<>();
    private Disposable myTopic;
    private Activity currentActivity;

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
        if(!onlineUsers.contains(name) && !name.equals(this.name)){
            onlineUsers.add(name);
        }
    }

    public void addAllOnlineUser(ArrayList<String> newOnlineUsers) {
        for (String user : newOnlineUsers) {
            addOnlineUser(user);
        }
    }

    public void removeOnlineUser(String name) {
        onlineUsers.remove(name);
    }

    public ArrayList<String> getOnlineUsers() {
        return onlineUsers;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public Disposable getMyTopic() {
        return myTopic;
    }

    public void setMyTopic(Disposable myTopic) {
        this.myTopic = myTopic;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }
}

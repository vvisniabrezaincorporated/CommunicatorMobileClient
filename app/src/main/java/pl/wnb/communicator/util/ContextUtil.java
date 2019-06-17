package pl.wnb.communicator.util;

import android.app.Application;
import android.content.Context;

public class ContextUtil extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        ContextUtil.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ContextUtil.context;
    }
}
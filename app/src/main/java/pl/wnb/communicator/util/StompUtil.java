package pl.wnb.communicator.util;

import android.annotation.SuppressLint;
import android.util.Log;

import ua.naiksoftware.stomp.StompClient;

public class StompUtil {
    @SuppressLint("CheckResult")
    public static void lifecycle(StompClient stompClient) {
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d("Stomp", "Stomp connection opened");
                    break;

                case ERROR:
                    Log.e("Stomp", "Error", lifecycleEvent.getException());
                    break;

                case CLOSED:
                    Log.d("Stomp", "Stomp connection closed");
                    break;
            }
        });
    }
}

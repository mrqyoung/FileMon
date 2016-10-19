package com.mrqyoung.filemon;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationClickedReceiver extends BroadcastReceiver {

    public static final String actionStopMe = "action_notification_to_stop_mon";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.debug("onAction: " + action);
        if (action == actionStopMe) {
            context.stopService(new Intent(context, MonService.class));
            ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE))
                    .cancel(41364262);
        }
    }
}

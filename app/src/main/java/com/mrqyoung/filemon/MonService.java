package com.mrqyoung.filemon;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class MonService extends Service {

    public static boolean isRunning = false;
    private DefaultFileObserver fileObserver = null;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        isRunning = true;
        return super.onStartCommand(intent, flags, startId);
    }

    private void init() {
        fileObserver = new DefaultFileObserver();
        fileObserver.addFilter(getResources().getStringArray(R.array.apps_filter));
        new FileEventHelper(new TopTaskHelper(getApplicationContext()));
        showNotification();
        try { Log.initLogWriter(getExternalFilesDir(null).getAbsolutePath() + "/");
        } catch (Exception e) {}
        fileObserver.startWatching();
    }

    private void showNotification() {
        Intent intentClickToStopMe = new Intent(this, NotificationClickedReceiver.class);
        intentClickToStopMe.setAction(NotificationClickedReceiver.actionStopMe);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intentClickToStopMe, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("FileMon is Running")
                        .setContentText("Touch to STOP")
                        .setAutoCancel(true)
                        .setContentIntent(pi);
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(41364262, mBuilder.build());
    }



    @Override
    public void onDestroy() {
        if (fileObserver != null) {
            fileObserver.stopWatching();
            try { Log.closeLogWriter(); } catch (Exception e) {}
        }
        MonService.isRunning = false;
        Log.debug("FileMonService stopped.");
        super.onDestroy();
    }
}

package com.mrqyoung.filemon;

import android.os.FileObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yorn on 2016/1/15.
 */
public class FileEventHelper {

    private TopTaskHelper topTaskHelper;
    private static Handler handler;

    public FileEventHelper(TopTaskHelper h) {
        HandlerThread handlerThread = new HandlerThread("FileMon");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper(), callback);
        topTaskHelper = h;
    }

    private static void logLog(String s) {
        handler.obtainMessage(0, s).sendToTarget();
    }

    private String getTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
        return timeFormat.format(new Date());
    }
    private Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getTime());
            stringBuilder.append("\t[").append(topTaskHelper.getTopTask()).append("] ");
            stringBuilder.append(message.obj);
            Log.log(stringBuilder.toString());
            return true;
        }
    };

    public static void onFileEvent(int event, String path) {
        if (event == 32768) return;
        switch (event) {
            case FileObserver.ACCESS:
                logLog("ACCESS: " + path);
                break;
            case FileObserver.ATTRIB:
                logLog("ATTRIB: " + path);
                break;
            case FileObserver.CLOSE_NOWRITE:
                logLog("CLOSE_NOWRITE: " + path);
                break;
            case FileObserver.CLOSE_WRITE:
                logLog("CLOSE_WRITE: " + path);
                break;
            case FileObserver.CREATE:
                logLog("CREATE: " + path);
                break;
            case FileObserver.DELETE:
                logLog("DELETE: " + path);
                break;
            case FileObserver.DELETE_SELF:
                logLog("DELETE_SELF: " + path);
                break;
            case FileObserver.MODIFY:
                logLog("MODIFY: " + path);
                break;
            case FileObserver.MOVE_SELF:
                logLog("MOVE_SELF: " + path);
                break;
            case FileObserver.MOVED_FROM:
                logLog("MOVED_FROM: " + path);
                break;
            case FileObserver.MOVED_TO:
                logLog("MOVED_TO: " + path);
                break;
            case FileObserver.OPEN:
                logLog("OPEN: " + path);
                break;
            default:
                logLog("<" + event + "> : " + path);
                break;
        }
    }
}

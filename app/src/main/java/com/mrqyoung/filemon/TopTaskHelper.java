package com.mrqyoung.filemon;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.HashMap;
import java.util.List;

/**
 * Created by yorn on 2016/1/18.
 */
public class TopTaskHelper {

    private ActivityManager manager;
    private PackageManager packageManager;
    private HashMap<String, String> apps;

    public TopTaskHelper(Context context) {
        this.manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        this.packageManager = context.getPackageManager();
        this.apps = new HashMap<>();
    }

    public String getTopTask() {
        //List<ActivityManager.RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
        List<ActivityManager.RunningAppProcessInfo> runningTasks = manager.getRunningAppProcesses();
        if (runningTasks == null) {
            Log.debug("Top task list is null");
            return "";
        }
        String topTask = runningTasks.get(0).processName;
        Log.debug(topTask);
        return getAppName(topTask);
    }

    private String getAppName(String packageName){
        if (this.apps.containsKey(packageName)) return this.apps.get(packageName);
        try {
            ApplicationInfo appInfo = this.packageManager.getApplicationInfo(packageName, 0);
            String appName = this.packageManager.getApplicationLabel(appInfo).toString();
            this.apps.put(packageName, appName);
            return appName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

}

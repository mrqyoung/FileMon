package com.mrqyoung.filemon;

import android.os.FileObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by yorn on 2016/1/18.
 */
public class DefaultFileObserver extends RecursiveFileObserver{

    private static String sdcardPath = "/sdcard";
    private List<String> passList = null;

    public DefaultFileObserver() {
        super(sdcardPath, FileObserver.ALL_EVENTS);
    }

    public void addFilter(String[] paths) {
        this.passList = Arrays.asList(paths);
    }

    @Override
    public void startWatching() {
        if (mObservers != null) return;
        mObservers = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        stack.push(mPath);

        while (!stack.empty()) {
            String parent = stack.pop();
            mObservers.add(new SingleFileObserver(parent, mMask));
            if (passList.contains(parent)) {
                Log.debug("filter contains: " + parent);
                continue;
            }
            File path = new File(parent);
            File[] files = path.listFiles();
            if (files == null) continue;
            for (File file : files) {
                if (file.isDirectory() && !file.getName().equals(".")
                        && !file.getName().equals("..")) {
                    stack.push(file.getPath());
                }
            }
        }
        for (int i = 0; i < mObservers.size(); i++)
            mObservers.get(i).startWatching();
    }

    @Override
    public void onEvent(int event, String path) {
        FileEventHelper.onFileEvent(event, path);
    }
}

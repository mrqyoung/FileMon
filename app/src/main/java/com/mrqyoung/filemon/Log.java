package com.mrqyoung.filemon;

import android.os.Handler;
import android.os.Message;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by yorn on 2016/1/18.
 */
public class Log {

    public static boolean DEBUG = false;
    public static String logFile = "file_mon.log";
    private static PrintWriter logWriter;

    public static void debug(String s) {
        if (DEBUG) System.out.println(s);
    }

    public static void initLogWriter(String path) throws FileNotFoundException, UnsupportedEncodingException {
        Log.debug("init log writer: " + path);
        logWriter = new PrintWriter(path + logFile, "UTF-8");
    }


    public static void log(String s) {
        logWriter.println(s);
    }

    public static void closeLogWriter() throws IOException {
        if (logWriter != null) logWriter.close();
        Log.debug("close log writer");
    }

}

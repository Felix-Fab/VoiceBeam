package tk.felixfab.voicebeam.etc;

import android.util.Log;

public class Logger {

    public static void writeInfoMessage(String message){
        Log.i("[Info]",message);
    }

    public static void writeWarningMessage(String message){
        Log.w("[Warning]",message);
    }

    public static void writeErrorMessage(String message){
        Log.e("[Error]",message);
    }

    public static void writeSuccessMessage(String message){
        Log.i("[Info]",message);
    }
}
package tk.felixfab.voicebeam.Permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import tk.felixfab.voicebeam.Activity.MainActivity;

public class PermissionsManager {

    public static boolean checkPermissions(Activity activity,Context context){

        boolean status = false;

        if(ContextCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            requestPermission(activity,new String[]{Manifest.permission.RECORD_AUDIO});
            status = true;
        }

        if(ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermission(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
            status = true;
        }

        if(ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
             requestPermission(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
            status = true;
        }

        return status;
    }

    public static void requestPermission(Activity activity, String[] permissions){
        ActivityCompat.requestPermissions(activity ,permissions,123);
    }
}

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

        List<String> PermissionList = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)  != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.INTERNET)){
                PermissionList.add(Manifest.permission.INTERNET);
            }
        }

        if(ContextCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.RECORD_AUDIO)){
                PermissionList.add(Manifest.permission.RECORD_AUDIO);
            }
        }

        if(ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                PermissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }

        if(ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.READ_EXTERNAL_STORAGE)){
                PermissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

        if(ContextCompat.checkSelfPermission(context,Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.MANAGE_EXTERNAL_STORAGE)){
                PermissionList.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE);
            }
        }

        if(PermissionList.size() <= 0){
            return true;
        }

        String[] PermissionArray = new String[PermissionList.size()];
        PermissionArray = PermissionList.toArray(PermissionArray);

        requestPermission(activity,PermissionArray);
        return false;
    }

    public static void requestPermission(Activity activity, String[] permissions){
        ActivityCompat.requestPermissions(activity ,permissions,123);
    }
}

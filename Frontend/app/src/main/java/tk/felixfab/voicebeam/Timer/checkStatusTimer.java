package tk.felixfab.voicebeam.Timer;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;

import tk.felixfab.voicebeam.API.HTTP;
import tk.felixfab.voicebeam.Activity.MainActivity;
import tk.felixfab.voicebeam.User.UserInfos;
import tk.felixfab.voicebeam.etc.Logger;

public class checkStatusTimer {

    private static Timer timer = new Timer();

    public static void startTimer(){

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                if(UserInfos.getEmail() != null){

                    boolean status = false;

                    AudioManager audioManager = MainActivity.getAudioManager();
                    if (audioManager != null) {
                        for(AudioDeviceInfo deviceInfo : audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)){
                            if(deviceInfo.getType()==AudioDeviceInfo.TYPE_WIRED_HEADPHONES || deviceInfo.getType() == AudioDeviceInfo.TYPE_WIRED_HEADSET || deviceInfo.getType() == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP){
                                status = true;
                            }
                        }
                    }

                    try {
                        HttpURLConnection con = HTTP.createDefaultConnection("http://" + MainActivity.Host + ":3000/manager/status","PATCH");

                        String json = "{ \"email\": \"" + UserInfos.getEmail() + "\", \"status\": " + status + " }";

                        con.setDoOutput(false);
                        try (OutputStream os = con.getOutputStream()) {
                            byte[] input = json.getBytes("utf-8");
                            os.write(input, 0, input.length);
                        }

                        JSONObject jsonObject = HTTP.getJSONBody(con);

                        if(con.getResponseCode() != 200){
                            Logger.writeErrorMessage("Status Change Request Error");
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        },0,10000);
    }

    public static void stopTimer(){
        timer.cancel();
    }
}

package tk.felixfab.voicebeam;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioDeviceInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.HandlerThread;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class MainActivity extends AppCompatActivity {
    WebSocket ws = null;
    Button button;

    public static HandlerThread mHandlerThread = new HandlerThread("yeye");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaRecorder myAudioRecorder = new MediaRecorder();
        MediaPlayer mediaPlayer = new MediaPlayer();

        File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "OutputCache.mp3");
        File inputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "InputCache.mp3");

        try {
            mediaPlayer.setDataSource(inputFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        button = findViewById(R.id.btn_login);

        WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);

        button.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                    AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                    if (audioManager != null) {
                        for(AudioDeviceInfo deviceInfo : audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)){
                            if(deviceInfo.getType()==AudioDeviceInfo.TYPE_WIRED_HEADSET){
                                return true;
                            }
                        }
                    }

                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                    myAudioRecorder.setOutputFile(outputFile);

                    try {
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){

                    try {
                        myAudioRecorder.stop();
                        myAudioRecorder.reset();

                        ws.sendBinary(Files.readAllBytes(outputFile.toPath()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                return false;
            }
        });

        try {
            ws = factory.createSocket("ws://5.181.151.118:81");

            ws.addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String message) throws Exception {
                    System.out.println("Server Nachricht erhalten");
                }

                @Override
                public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {

                    System.out.println("Nachricht erhalten");

                    try (FileOutputStream outputStream = new FileOutputStream(inputFile)) {
                        outputStream.write(binary);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
            });

            ws.connectAsynchronously();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package tk.felixfab.voicebeam;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {
    WebSocket ws = null;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MediaRecorder myAudioRecorder = new MediaRecorder();
        File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "AudioRecording.3gp");

        button = findViewById(R.id.button);

        WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);

        button.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
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
                        myAudioRecorder.release();
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

                }
            });

            ws.connectAsynchronously();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
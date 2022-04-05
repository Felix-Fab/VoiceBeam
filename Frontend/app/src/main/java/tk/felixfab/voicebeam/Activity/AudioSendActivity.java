package tk.felixfab.voicebeam.Activity;

import android.content.Intent;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tk.felixfab.voicebeam.API.HTTP;
import tk.felixfab.voicebeam.Adapter.Data.UserData;
import tk.felixfab.voicebeam.Adapter.Data.UsersData;
import tk.felixfab.voicebeam.Adapter.UserAdapter;
import tk.felixfab.voicebeam.Adapter.UsersAdapter;
import tk.felixfab.voicebeam.Message.Toast;
import tk.felixfab.voicebeam.R;
import tk.felixfab.voicebeam.User.UserInfos;
import tk.felixfab.voicebeam.WebSocket.WebSocketManager;

public class AudioSendActivity extends AppCompatActivity {

    String username;

    ListView lv_messages;
    TextView tv_username;
    ImageView iv_pb;
    Button btn_back;
    Button btn_send;

    UserAdapter userAdapter;

    MediaRecorder myAudioRecorder = new MediaRecorder();
    File outputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ outputAudio.mp3");

    ArrayList<UserData> arrayList = new ArrayList<UserData>();

    Intent intent;

    LoadUserInformation loadUserInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_send);

        intent = getIntent();

        username = intent.getStringExtra("username");

        lv_messages = findViewById(R.id.lv_messages);
        tv_username = findViewById(R.id.tv_username);
        iv_pb = findViewById(R.id.iv_pb);
        btn_back = findViewById(R.id.btn_back);
        btn_send = findViewById(R.id.btn_send);

        tv_username.setText(username);
        Picasso.with(AudioSendActivity.this)
                .load(android.R.mipmap.sym_def_app_icon)
                .into(iv_pb);

        loadUserInformation = new LoadUserInformation();
        loadUserInformation.execute();

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AudioSendActivity.this,UserMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == event.ACTION_DOWN){

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

                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    try {
                        myAudioRecorder.stop();
                        myAudioRecorder.reset();

                        String dataString = Base64.getEncoder().encodeToString(Files.readAllBytes(outputFile.toPath()));

                        String json = "{ \"key\": \"message\", \"from\": \"" + UserInfos.getUsername() + "\", \"to\": \"" + username + "\", \"data\": \"" + dataString + "\"}";

                        WebSocketManager.ws.sendText(json);

                        arrayList.clear();

                        loadUserInformation = new LoadUserInformation();
                        loadUserInformation.execute();


                        btn_send.setEnabled(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btn_send.setEnabled(true);
                            }
                        },500);

                        Toast.ShowToast(AudioSendActivity.this,"Audio gesendet", android.widget.Toast.LENGTH_LONG);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    public class LoadUserInformation extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection con = null;
            try {
                con = HTTP.createDefaultConnection("http://" + MainActivity.Host + ":3000/messages/getMessages", "PATCH");

                String json = "{ \"username1\": \"" + UserInfos.getUsername() + "\", \"username2\": \"" + username + "\" }";

                con.setDoOutput(false);
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = json.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                JSONObject jsonObject = HTTP.getJSONBody(con);

                if(con.getResponseCode() == 200){

                    for(int i = 0;i < jsonObject.getJSONArray("messages").length();i++){

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");

                        Date date = simpleDateFormat.parse(jsonObject.getJSONArray("messages").getJSONObject(i).getString("createdAt"));

                        String Detail = jsonObject.getJSONArray("messages").getJSONObject(i).getString("audioLength") + " Sekunden | " + format.format(date);

                        if(jsonObject.getJSONArray("messages").getJSONObject(i).getString("from").equalsIgnoreCase(UserInfos.getUsername())){
                            arrayList.add(new UserData("Gesendet",Detail,true));
                        }else{
                            arrayList.add(new UserData("Empfangen",Detail,false));
                        }
                    }
                    return "Success";
                }
                return "Error";

            } catch (IOException | JSONException | ParseException e) {
                e.printStackTrace();
            }
            return "Error";
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("Success")){
                userAdapter = new UserAdapter(AudioSendActivity.this,arrayList);
                lv_messages.setAdapter(userAdapter);
            }else{
                Toast.ShowToast(AudioSendActivity.this,s, android.widget.Toast.LENGTH_LONG);
            }
        }
    }
}
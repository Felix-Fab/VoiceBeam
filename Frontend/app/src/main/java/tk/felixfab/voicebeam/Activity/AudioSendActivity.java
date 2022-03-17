package tk.felixfab.voicebeam.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class AudioSendActivity extends AppCompatActivity {

    String username;

    ListView lv_messages;
    TextView tv_username;
    ImageView iv_pb;
    Button btn_back;
    Button btn_send;

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
    }

    public class LoadUserInformation extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection con = null;
            try {
                con = HTTP.createDefaultConnection("http://5.181.151.118:3000/messages/getMessages", "PATCH");

                String json = "{ \"username1\": \"" + UserInfos.getUsername() + "\", \"username2\": \"" + username + "\" }";

                con.setDoOutput(false);
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = json.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                JSONObject jsonObject = HTTP.getJSONBody(con);

                if(con.getResponseCode() == 200){

                    for(int i = 0;i < jsonObject.getJSONArray("messages").length();i++){

                        //Date date = new Date(jsonObject.getJSONArray("messages").getJSONObject(i).get("createdAt").toString());
                        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

                        String Detail = jsonObject.getJSONArray("messages").getJSONObject(i).getString("audioLength") + " Sekunden | ";

                        if(jsonObject.getJSONArray("messages").getJSONObject(i).getString("from").equalsIgnoreCase(UserInfos.getUsername())){
                            arrayList.add(new UserData("Gesendet",Detail,true));
                        }else{
                            arrayList.add(new UserData("Empfangen",Detail,false));
                        }
                    }
                    return "Success";
                }
                return "Error";

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return "Error";
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("Success")){
                UserAdapter userAdapter = new UserAdapter(AudioSendActivity.this,arrayList);
                lv_messages.setAdapter(userAdapter);
            }else{
                Toast.ShowToast(AudioSendActivity.this,s, android.widget.Toast.LENGTH_LONG);
            }
        }
    }
}
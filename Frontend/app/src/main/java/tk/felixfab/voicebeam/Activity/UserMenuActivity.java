package tk.felixfab.voicebeam.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Timer;

import tk.felixfab.voicebeam.API.HTTP;
import tk.felixfab.voicebeam.Adapter.UserAdapter;
import tk.felixfab.voicebeam.Adapter.UsersAdapter;
import tk.felixfab.voicebeam.Adapter.Data.UsersData;
import tk.felixfab.voicebeam.R;
import tk.felixfab.voicebeam.User.UserInfos;

public class UserMenuActivity extends AppCompatActivity {

    static ListView lv_users;
    Button btn_settings;

    Intent intent;

    updateUserInput updateUserInput;

    Timer timer = new Timer();

    ArrayList<UsersData> arrayList = new ArrayList<UsersData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_menu);

        intent = getIntent();

        lv_users = findViewById(R.id.lv_users);
        btn_settings = findViewById(R.id.btn_settings);

        updateUserInput = new updateUserInput();
        updateUserInput.execute();

        lv_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UsersData itemTitle = (UsersData) parent.getAdapter().getItem(position);

                Intent intent = new Intent(UserMenuActivity.this, AudioSendActivity.class);
                intent.putExtra("username",itemTitle.UserName);
                startActivity(intent);
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMenuActivity.this,SettingsMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    public class updateUserInput extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            while (isFinishing() != true && isDestroyed() != true){
                try {
                    HttpURLConnection con = HTTP.createDefaultConnection("http://5.181.151.118:3000/manager/getUsers", "PATCH");

                    String json = "{ \"email\": \"" + UserInfos.getEmail() + "\"}";

                    con.setDoOutput(false);

                    try (OutputStream os = con.getOutputStream()) {
                        byte[] input = json.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    JSONObject jsonObject = HTTP.getJSONBody(con);

                    arrayList.clear();
                    for(int i = 0;i < jsonObject.getJSONArray("users").length();i++){
                        arrayList.add(new UsersData(jsonObject.getJSONArray("users").getJSONObject(i).getString("username"),""));
                    }

                    if(con.getResponseCode() == 200){
                        publishProgress();
                    }else{
                        return jsonObject.getJSONArray("errors").getJSONObject(0).getString("msg");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    return e.getMessage();
                }
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            UsersAdapter usersAdapter = new UsersAdapter(UserMenuActivity.this, arrayList);
            lv_users.setAdapter(usersAdapter);
        }
    }
}

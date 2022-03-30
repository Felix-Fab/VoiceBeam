package tk.felixfab.voicebeam.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import tk.felixfab.voicebeam.API.HTTP;
import tk.felixfab.voicebeam.Adapter.UsersAdapter;
import tk.felixfab.voicebeam.Adapter.Data.UsersData;
import tk.felixfab.voicebeam.Message.Toast;
import tk.felixfab.voicebeam.R;
import tk.felixfab.voicebeam.User.UserInfos;

public class UserMenuActivity extends AppCompatActivity {

    ListView lv_users;
    Button btn_settings;

    Intent intent;

    UserInput userInput;

    ArrayList<UsersData> arrayList = new ArrayList<UsersData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_menu);

        intent = getIntent();

        lv_users = findViewById(R.id.lv_users);
        btn_settings = findViewById(R.id.btn_settings);

        userInput = new UserInput();
        userInput.execute();

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
                ActivityCompat.requestPermissions(UserMenuActivity.this,new String[] { Manifest.permission.CAMERA },123);
            }
        });
    }

    public class UserInput extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                HttpURLConnection con = HTTP.createDefaultConnection("http://5.181.151.118:3000/manager/getUsers", "PATCH");

                String json = "{ \"email\": \"" + UserInfos.getEmail() + "\"}";

                con.setDoOutput(false);

                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = json.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                JSONObject jsonObject = HTTP.getJSONBody(con);

                for(int i = 0;i < jsonObject.getJSONArray("users").length();i++){
                    arrayList.add(new UsersData(jsonObject.getJSONArray("users").getJSONObject(i).getString("username"),""));
                }

                if(con.getResponseCode() == 200){
                    return "Success";
                }else{
                    return jsonObject.getJSONArray("errors").getJSONObject(0).getString("msg");
                }
            } catch (Exception e){
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equals("Success")){
                UsersAdapter userAdapter = new UsersAdapter(UserMenuActivity.this,arrayList);
                lv_users.setAdapter(userAdapter);
            }else{
                Toast.ShowToast(UserMenuActivity.this,s, android.widget.Toast.LENGTH_LONG);
            }
        }
    }
}

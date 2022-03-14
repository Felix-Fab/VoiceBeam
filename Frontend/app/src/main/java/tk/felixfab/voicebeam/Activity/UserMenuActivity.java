package tk.felixfab.voicebeam.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import tk.felixfab.voicebeam.API.HTTP;
import tk.felixfab.voicebeam.Adapter.UserAdapter;
import tk.felixfab.voicebeam.Data.UserData;
import tk.felixfab.voicebeam.Message.AlertBox;
import tk.felixfab.voicebeam.Message.Toast;
import tk.felixfab.voicebeam.R;

public class UserMenuActivity extends AppCompatActivity {

    ListView lv_users;

    Intent intent;

    UserInput userInput;

    ArrayList<UserData> arrayList = new ArrayList<UserData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_menu);

        intent = getIntent();

        lv_users = findViewById(R.id.lv_users);

        userInput = new UserInput();
        userInput.execute();

        lv_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserData itemTitle = (UserData) parent.getAdapter().getItem(position);

                Toast.ShowToast(UserMenuActivity.this,itemTitle.UserName, android.widget.Toast.LENGTH_LONG);
            }
        });
    }

    public class UserInput extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                HttpURLConnection con = HTTP.createDefaultConnection("http://5.181.151.118:3000/manager/getUsers", "PATCH");

                String json = "{ \"email\": \"" + intent.getStringExtra("email") + "\"}";

                con.setDoOutput(false);

                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = json.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                JSONObject jsonObject = HTTP.getJSONBody(con);

                for(int i = 0;i < jsonObject.getJSONArray("users").length();i++){
                    arrayList.add(new UserData(jsonObject.getJSONArray("users").getJSONObject(i).getString("username"),""));
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
                UserAdapter userAdapter = new UserAdapter(UserMenuActivity.this,arrayList);
                lv_users.setAdapter(userAdapter);
            }else{
                Toast.ShowToast(UserMenuActivity.this,s, android.widget.Toast.LENGTH_LONG);
            }
        }
    }
}

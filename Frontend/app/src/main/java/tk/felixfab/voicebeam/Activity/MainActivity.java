package tk.felixfab.voicebeam.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.neovisionaries.ws.client.WebSocket;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import tk.felixfab.voicebeam.API.HTTP;
import tk.felixfab.voicebeam.Message.AlertBox;
import tk.felixfab.voicebeam.Message.Toast;
import tk.felixfab.voicebeam.R;
import tk.felixfab.voicebeam.User.UserInfos;

public class MainActivity extends AppCompatActivity {
    public static Context context;

    public static Context getContext(){
        return context;
    }

    WebSocket ws = null;
    Button btn_login;
    EditText tf_email;
    EditText tf_password;

    String username;
    String email;

    LoginTask loginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getContext();

        btn_login = findViewById(R.id.btn_login);
        tf_email = findViewById(R.id.tf_email);
        tf_password = findViewById(R.id.tf_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginTask = new LoginTask();
                loginTask.execute();
            }
        });
    }

    public class LoginTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {

                if (tf_email.getText().length() > 0 && tf_password.length() > 0) {

                    HttpURLConnection con = HTTP.createDefaultConnection("http://5.181.151.118:3000/manager/login", "PATCH");

                    String json = "{ \"email\": \"" + tf_email.getText() + "\", \"password\":\"" + tf_password.getText() + "\"}";

                    con.setDoOutput(false);

                    try (OutputStream os = con.getOutputStream()) {
                        byte[] input = json.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    JSONObject jsonObject = HTTP.getJSONBody(con);

                    UserInfos.setUsername(jsonObject.getString("username"));
                    UserInfos.setEmail(jsonObject.getString("email"));

                    if(con.getResponseCode() == 200){
                        return "Success";
                    }else{
                        return jsonObject.getJSONArray("errors").getJSONObject(0).getString("msg");
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
                return "Internal Server Error";
            }
            return "Invalid Credentials";
        }

        @Override
        protected void onPostExecute(String s) {

            if(s.equals("Success")){

                //TODO: Login Prozess

                Intent intent = new Intent(MainActivity.this, UserMenuActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("username",username);
                startActivity(intent);

                Toast.ShowToast(MainActivity.this,"Login Success", android.widget.Toast.LENGTH_LONG);

            }else{
                AlertBox.ShowDefaultAlertBox(MainActivity.this,"Error",s);
            }
        }
    }
}


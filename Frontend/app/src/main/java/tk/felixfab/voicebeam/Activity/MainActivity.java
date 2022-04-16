package tk.felixfab.voicebeam.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
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
import tk.felixfab.voicebeam.Permissions.PermissionsManager;
import tk.felixfab.voicebeam.R;
import tk.felixfab.voicebeam.Service.DefaultService;
import tk.felixfab.voicebeam.Timer.checkStatusTimer;
import tk.felixfab.voicebeam.User.UserInfos;
import tk.felixfab.voicebeam.etc.Var;

public class MainActivity extends AppCompatActivity {
    public static Context context;

    public static Context getContext(){
        return context;
    }

    public static AudioManager audioManager;

    public static AudioManager getAudioManager(){
        return audioManager;
    }

    WebSocket ws = null;
    Button btn_login;
    EditText tf_email;
    EditText tf_password;

    String username;
    String email;

    SharedPreferences login_pref;

    LoginTask loginTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getContext();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        login_pref = getApplicationContext().getSharedPreferences("LoginSave", 0);

        btn_login = findViewById(R.id.btn_login);
        tf_email = findViewById(R.id.tf_email);
        tf_password = findViewById(R.id.tf_password);

        requestPermissions(new String[] { Manifest.permission.INTERNET }, 123);

        if (ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.INTERNET) ==
                PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermissions(new String[] { Manifest.permission.INTERNET }, 123);
        }

        ActivityCompat.requestPermissions(MainActivity.this,new String[] { Manifest.permission.CAMERA },123);

        if(login_pref.getString("email",null) != null && login_pref.getString("password",null) != null){
            tf_email.setText(login_pref.getString("email",null));
            tf_password.setText(login_pref.getString("password",null));
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!PermissionsManager.checkPermissions(MainActivity.this,MainActivity.this)){
                    loginTask = new LoginTask();
                    loginTask.execute(tf_email.getText().toString(),tf_password.getText().toString());
                }
            }
        });
    }

    public class LoginTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {

                if (strings.length >= 2) {

                    HttpURLConnection con = HTTP.createDefaultConnection("http://" + Var.Host + ":3000/manager/login", "PATCH");

                    String json = "{ \"email\": \"" + strings[0] + "\", \"password\":\"" + strings[1] + "\"}";

                    con.setDoOutput(false);

                    try (OutputStream os = con.getOutputStream()) {
                        byte[] input = json.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    JSONObject jsonObject = HTTP.getJSONBody(con);

                    if(con.getResponseCode() == 200){
                        UserInfos.setUsername(jsonObject.getString("username"));
                        UserInfos.setEmail(jsonObject.getString("email"));
                        UserInfos.setAccessToken(jsonObject.getString("accessToken"));

                        SharedPreferences.Editor editor = login_pref.edit();
                        editor.putString("email",strings[0]);
                        editor.putString("password",strings[1]);
                        editor.commit();

                        return "Success";
                    }else{
                        SharedPreferences.Editor editor = login_pref.edit();
                        editor.remove("email");
                        editor.remove("password");
                        editor.commit();

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

                checkStatusTimer.startTimer();

                Intent UserMenuActivity = new Intent(MainActivity.this, UserMenuActivity.class);
                startActivity(UserMenuActivity);

                Intent DefaultService = new Intent(MainActivity.this, DefaultService.class);
                startService(DefaultService);

                finish();

            }else{
                AlertBox.ShowDefaultAlertBox(MainActivity.this,"Error",s);
                btn_login.setClickable(true);
            }
        }
    }
}


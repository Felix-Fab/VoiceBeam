package tk.felixfab.voicebeam.Activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import tk.felixfab.voicebeam.Adapter.UserAdapter;
import tk.felixfab.voicebeam.Data.UserData;
import tk.felixfab.voicebeam.R;

public class UserMenu extends AppCompatActivity {

    ListView lv_users;

    ArrayList<UserData> arrayList = new ArrayList<UserData>();

    private String[] users = { "Suresh Dasari", "Rohini Alavala", "Trishika Dasari", "Praveen Alavala", "Madav Sai", "Hamsika Yemineni"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_menu);


        lv_users = findViewById(R.id.lv_users);
        arrayList.add(new UserData("JAVA","https://www.tutorialspoint.com/java/images/java-mini-logo.jpg"));
        arrayList.add(new UserData("Python","https://www.tutorialspoint.com/python/images/python-mini.jpg"));
        arrayList.add(new UserData("Javascript", "https://www.tutorialspoint.com/javascript/images/javascript-mini-logo.jpg"));

        UserAdapter userAdapter = new UserAdapter(this,arrayList);
        lv_users.setAdapter(userAdapter);

    }
}

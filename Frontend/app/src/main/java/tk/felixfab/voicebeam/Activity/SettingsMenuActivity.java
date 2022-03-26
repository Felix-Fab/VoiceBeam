package tk.felixfab.voicebeam.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import tk.felixfab.voicebeam.Adapter.Data.SettingsData;
import tk.felixfab.voicebeam.Adapter.SettingsAdapter;
import tk.felixfab.voicebeam.Message.Toast;
import tk.felixfab.voicebeam.R;

public class SettingsMenuActivity extends AppCompatActivity {

    ListView lv_settings;

    ArrayList<SettingsData> arrayList = new ArrayList<SettingsData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_menu);

        lv_settings = findViewById(R.id.lv_settings);

        arrayList.add(new SettingsData("Logout","#f82c00"));

        SettingsAdapter settingsAdapter = new SettingsAdapter(SettingsMenuActivity.this,arrayList);
        lv_settings.setAdapter(settingsAdapter);


        lv_settings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SettingsData settingsData = (SettingsData) parent.getAdapter().getItem(position);

                Toast.ShowToast(SettingsMenuActivity.this,settingsData.Text, android.widget.Toast.LENGTH_LONG);
            }
        });
    }
}

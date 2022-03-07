package tk.felixfab.voicebeam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    WebSocket ws = null;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);

        WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ws.connectAsynchronously();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
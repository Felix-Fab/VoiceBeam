package tk.felixfab.voicebeam.WebSocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;

public class WebSocketManager {

    public static WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);
    public static WebSocket ws = null;

    public static void connectToSocket(String uri) throws IOException, WebSocketException {

        ws = factory.createSocket(uri);

        ws.addListener(new WebSocketListener());

        ws.connect();
    }

    public static void SocketDisconnect(){
        ws.disconnect();
    }
}

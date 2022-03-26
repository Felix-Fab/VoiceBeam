package tk.felixfab.voicebeam.WebSocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;

public class WebSocketManager {

    public static WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);
    public static WebSocket ws = null;
    private static boolean connected = false;

    public static void connect(String uri) throws IOException, WebSocketException {

        ws = factory.createSocket(uri);

        ws.addListener(new WebSocketListener());

        ws.connect();
    }

    public static void disconnect(){
        ws.disconnect();
        setConnection(true);
    }

    public static boolean isConnected(){
        return connected;
    }

    public static void setConnection(Boolean _connected){
        connected = _connected;
    }
}

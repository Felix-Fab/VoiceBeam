package tk.felixfab.voicebeam.WebSocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import tk.felixfab.voicebeam.Activity.MainActivity;
import tk.felixfab.voicebeam.User.UserInfos;

public class WebSocketListener extends WebSocketAdapter {

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {

        JSONObject object = new JSONObject(text);

        if(object.getString("from").equalsIgnoreCase(UserInfos.username)){
            File file = new File(MainActivity.getContext().getFilesDir().getParent() + "/Temp_File.mp3");

            byte[] bytes = Base64.getDecoder().decode(object.getString("data"));

            try(FileOutputStream fos = new FileOutputStream(MainActivity.getContext().getFilesDir().getParent() + "/TempFile.mp3")){
                fos.write(bytes);
            }

            //TODO: AudioPlayer
        }
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {

    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {

    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {

    }


}

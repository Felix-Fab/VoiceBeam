package tk.felixfab.voicebeam.WebSocket;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import tk.felixfab.voicebeam.Activity.MainActivity;
import tk.felixfab.voicebeam.User.UserInfos;
import tk.felixfab.voicebeam.etc.Logger;

public class WebSocketListener extends WebSocketAdapter {

    MediaPlayer mediaPlayer = new MediaPlayer();
    File inputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "InputCache.mp3");

    List<File>Playlist = new ArrayList<>();

    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        WebSocketManager.setConnection(true);

        JSONObject object = new JSONObject(text);

        if(object.getString("to").equalsIgnoreCase(UserInfos.username)){

            File inputFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + object.getString("from") + "_Cache.mp3");

            byte[] bytes = Base64.getDecoder().decode(object.getString("data"));

            try(FileOutputStream fos = new FileOutputStream(inputFile)){
                fos.write(bytes);
            }

            if(mediaPlayer.isPlaying()){
                Playlist.add(inputFile);
            }else{
                mediaPlayer.setDataSource(inputFile.getPath());

                mediaPlayer.prepare();
                mediaPlayer.start();

                inputFile.deleteOnExit();
            }
        }
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
        WebSocketManager.setConnection(true);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                try {
                    if(Playlist.size() > 0){
                        mediaPlayer.setDataSource(Playlist.get(0).getPath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        Playlist.get(0).deleteOnExit();
                        Playlist.remove(0);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Logger.writeSuccessMessage("Connected to Web Socket");
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
        WebSocketManager.setConnection(false);
        Logger.writeErrorMessage("Web Socket Connect Error");
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
        WebSocketManager.setConnection(false);
        Logger.writeWarningMessage("Disconnected from Web Socket");
    }


}

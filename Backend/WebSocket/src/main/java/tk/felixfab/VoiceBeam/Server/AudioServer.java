package tk.felixfab.VoiceBeam.Server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.InputFormatException;
import it.sauronsoftware.jave.MultimediaInfo;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import tk.felixfab.VoiceBeam.API.HTTP;
import tk.felixfab.VoiceBeam.Manager.FileManager;
import tk.felixfab.VoiceBeam.etc.Logger;
import top.jfunc.json.impl.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class AudioServer extends WebSocketServer {

    HashMap<String,WebSocket>Clients = new HashMap<>();
    List<Integer> hallo = new ArrayList<>();

    public AudioServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public AudioServer(InetSocketAddress address) {
        super(address);
    }

    public AudioServer(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

        System.out.println();

        clientHandshake.

        if(clientHandshake.getFieldValue("key").equals("q4t7w9z$C&F)J@NcRfUjXn2r5u8x/A%D")){

            try {
                HttpURLConnection con = HTTP.createDefaultConnection("http://37.114.34.153:3000/manager/checkAccessToken","GET");

                con.addRequestProperty("authorization","Bearer " + clientHandshake.getFieldValue("accessToken"));

                JSONObject jsonObject = HTTP.getJSONBody(con);

                switch (con.getResponseCode()){
                    case 200:
                        Clients.put(jsonObject.getString("username"),webSocket);

                        Logger.writeInfoMessage("[onOpen] Client connected: " + webSocket.getRemoteSocketAddress() + " | " + jsonObject.getString("username"));
                        break;

                    case 401:
                        Logger.writeWarningMessage("[onOpen] Not Authorized!");
                        webSocket.close();
                        break;

                    case 403:
                        Logger.writeWarningMessage("[onOpen] Access Token Invalid or Expired!");
                        webSocket.close();
                        break;

                    default:
                        Logger.writeErrorMessage("[onOpen] checkAccessToken Error | " + con.getResponseCode());
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            webSocket.close();
            Logger.writeWarningMessage("[onOpen] Client not authenticated");
        }
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

        for (Map.Entry<String, WebSocket> set : Clients.entrySet()) {
            if(set.getValue() == webSocket){
                Logger.writeInfoMessage("[onClose] " + set.getKey() + " disconnected!");
                Clients.remove(set.getKey());
                return;
            }
        }
        Logger.writeWarningMessage("[onClose] Unknown disconnected");
    }

    @Override
    protected boolean onConnect(SelectionKey key) {
        return true;
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {

        JSONObject object = new JSONObject(s);

        switch (object.getString("key")){
            case "message":
                File file = FileManager.saveTempAudio(object.getString("data"),object.getString("from") + ".mp3");

                String from = object.getString("from");
                String to = object.getString("to");
                String accessToken = object.getString("accessToken");
                int file_duration = 0;

                if(Clients.containsKey(object.getString("to"))){

                    try {
                        HttpURLConnection con = HTTP.createDefaultConnection("http://37.114.34.153:3000/messages/add","POST");
                        con.addRequestProperty("authorization","Bearer " + accessToken);

                        String json = "{ \"from\": \"" + from + "\", \"to\":\"" + to + "\", \"audioLength\": \"" + file_duration + "\"}";

                        con.setDoOutput(true);
                        try (OutputStream os = con.getOutputStream()) {
                            byte[] input = json.getBytes("utf-8");
                            os.write(input, 0, input.length);
                        }

                        switch (con.getResponseCode()){

                            case 401:
                                Logger.writeErrorMessage("[onMessage/Message] Message Add Request Error | Code 400");
                                break;

                            case 201:
                                Logger.writeSuccessMessage("[onMessage/Message] Message Add | Code 201");
                                break;

                            default:
                                Logger.writeErrorMessage("[onMessage/Message] Message Add Request Error | Code " + con.getResponseCode());
                                break;
                        }

                        Clients.get(object.getString("to")).send(s);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Logger.writeWarningMessage("[onMessage] Recipient User not registerd");
                }
                break;

            default:
                Logger.writeWarningMessage("[onMessage] " + object.getString("key") + "| Key not defined");
                break;
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

        for (Map.Entry<String, WebSocket> set : Clients.entrySet()) {
            if(set.getValue() == webSocket){
                Logger.writeErrorMessage("[onError] " + set.getKey() + " Error!");
                Clients.remove(set.getKey());
                return;
            }
        }
        Logger.writeErrorMessage("[onError] Unknown User Error\nError:" + e.getMessage());

    }

    @Override
    public void onStart() {
        Logger.writeSuccessMessage("[onStart] AudioServer started");
    }
}


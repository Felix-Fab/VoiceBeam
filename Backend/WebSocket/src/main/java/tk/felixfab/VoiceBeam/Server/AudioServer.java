package tk.felixfab.VoiceBeam.Server;

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
import java.util.Base64;
import java.util.Collections;

public class AudioServer extends WebSocketServer {

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
        Logger.writeInfoMessage("Client connected: " + webSocket.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        Logger.writeInfoMessage("Client disconnected");
    }

    @Override
    protected boolean onConnect(SelectionKey key) {
        return true;
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {

        JSONObject object = new JSONObject(s);

        File file = FileManager.saveTempAudio(object.getString("data"),object.getString("from") + ".mp3");

        String from = object.getString("from");
        String to = object.getString("to");
        int file_duration = 0;

        try {
            HttpURLConnection con = HTTP.createDefaultConnection("http://5.181.151.118:3000/messages/add","POST");

            String json = "{ \"from\": \"" + from + "\", \"to\":\"" + to + "\", \"audioLength\": \"" + file_duration + "\"}";

            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            switch (con.getResponseCode()){

                case 400:
                    Logger.writeErrorMessage("Message Add Request Error | Code 400");
                    break;

                case 201:
                    Logger.writeSuccessMessage("Message Add | Code 201");
                    break;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        Logger.writeErrorMessage(e.getMessage());
    }

    @Override
    public void onStart() {
        Logger.writeSuccessMessage("AudioServer started");
    }
}


package tk.felixfab.VoiceBeam.Server;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import tk.felixfab.VoiceBeam.Manager.FileManager;
import tk.felixfab.VoiceBeam.etc.Logger;
import top.jfunc.json.impl.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
        Logger.writeInfoMessage("Client disconnected: " + webSocket.getRemoteSocketAddress());
    }

    @Override
    protected boolean onConnect(SelectionKey key) {
        System.out.println("Client connected");
        return true;
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {

        JSONObject object = new JSONObject(s);

        FileManager.saveTempAudio(object.getString("data"),object.getString("from") + ".mp3");
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


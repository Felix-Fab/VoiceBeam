package tk.felixfab.VoiceBeam.Server;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.file.Files;
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

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    @Override
    protected boolean onConnect(SelectionKey key) {
        System.out.println("Client connected");
        return true;
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println("Nachricht erhalten");
        webSocket.send("Server hat Nachricht erhaten");
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        System.out.println("File erhalten");

        File outputFile = new File("Hallo.mp3");
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(message.array());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            conn.send(Files.readAllBytes(outputFile.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {
        System.out.println("AudioServer started...");
    }
}

package tk.felixfab.VoiceBeam.Server;

import java.net.UnknownHostException;

public class ServerManager {

    public static AudioServer audioServer;

    /*
        AudioServer Port 81
     */
    public static void startDefaultServers() throws UnknownHostException {

        audioServer = new AudioServer(81);
        audioServer.start();

    }
}

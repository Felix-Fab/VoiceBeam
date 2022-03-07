package tk.felixfab.VoiceBeam;

import tk.felixfab.VoiceBeam.Server.ServerManager;

import java.net.UnknownHostException;

public class VoiceBeam {

    public static void main(String[] args) throws UnknownHostException {

        ServerManager.startDefaultServers();

    }
}

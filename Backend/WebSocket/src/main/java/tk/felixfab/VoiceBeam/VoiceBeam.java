package tk.felixfab.VoiceBeam;

import tk.felixfab.VoiceBeam.Manager.FileManager;
import tk.felixfab.VoiceBeam.Server.ServerManager;

import java.io.IOException;

public class VoiceBeam {

    public static void main(String[] args) throws IOException{

        ServerManager.startDefaultServers();

        FileManager.createTempFolder();

    }
}
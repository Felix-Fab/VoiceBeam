package tk.felixfab.VoiceBeam;

import tk.felixfab.VoiceBeam.Server.ServerManager;

import java.net.UnknownHostException;
import java.sql.SQLException;

public class VoiceBeam {

    public static void main(String[] args) throws UnknownHostException, SQLException {

        ServerManager.startDefaultServers();

    }
}

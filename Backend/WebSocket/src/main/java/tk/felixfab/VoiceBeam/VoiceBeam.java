package tk.felixfab.VoiceBeam;

import tk.felixfab.VoiceBeam.SQL.MySQL;
import tk.felixfab.VoiceBeam.SQL.SQLManager;
import tk.felixfab.VoiceBeam.Server.ServerManager;

import java.net.UnknownHostException;
import java.sql.SQLException;

public class VoiceBeam {

    public static void main(String[] args) throws UnknownHostException, SQLException {

        MySQL.connect("127.0.0.1","voicebeam","root","");
        SQLManager.onCreate();

        ServerManager.startDefaultServers();

    }
}

package tk.felixfab.VoiceBeam.SQL;

import java.sql.SQLException;

public class SQLManager {

    public static void onCreate() throws SQLException {

        MySQL.onUpdate("CREATE TABLE IF NOT EXISTS Users(Id INT NOT NULL AUTO_INCREMENT, Username TEXT, Status INT, Email TEXT, PW TEXT)");

    }
}

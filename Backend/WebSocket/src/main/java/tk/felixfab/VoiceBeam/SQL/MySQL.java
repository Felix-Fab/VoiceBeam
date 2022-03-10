package tk.felixfab.VoiceBeam.SQL;

import java.sql.*;

public class MySQL {

    private static Connection conn;
    private static Statement stmt;

    public static void connect(String host,String database,String user,String psw) {
        conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://"+ host + "/" + database + "?useUnicode=true&JDBCCompliantTimezoneShift=true&useLegacyDatatimeCode=false&serverTimezone=Europe/Berlin";

            conn = DriverManager.getConnection(url,user,psw);

            System.out.println("Verbindung zur Datenbank hergestellt.");

            stmt = conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Verbindung zur Datenbank getrennt.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean isConnected(){
        try {
            if(conn != null){
                return true;
            }else {
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void onUpdate(String sql) throws SQLException, SQLException {

        stmt.execute(sql);
    }

    public static ResultSet onQuery(String sql) throws SQLException {
        try{
            return stmt.executeQuery(sql);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}


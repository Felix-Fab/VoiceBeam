package tk.felixfab.voicebeam.User;

public class UserInfos {

    public static String username;
    public static String email;
    public static String accessToken;

    public static void setUsername(String _username){
        username = _username;
    }

    public static String getUsername(){
        return username;
    }

    public static void setEmail(String _email){
        email = _email;
    }

    public static String getEmail(){
        return email;
    }

    public static void setAccessToken(String _accessToken){
        accessToken = _accessToken;
    }

    public static String getAccessToken(){
        return accessToken;
    }
}

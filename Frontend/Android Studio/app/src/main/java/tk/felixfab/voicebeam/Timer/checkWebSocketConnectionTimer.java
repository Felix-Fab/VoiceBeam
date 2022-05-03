package tk.felixfab.voicebeam.Timer;

import java.util.Timer;
import java.util.TimerTask;

import tk.felixfab.voicebeam.User.UserInfos;
import tk.felixfab.voicebeam.WebSocket.WebSocketManager;
import tk.felixfab.voicebeam.etc.Var;

public class checkWebSocketConnectionTimer {

    private static Timer timer = new Timer();

    public static void startTimer(){

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                try{
                    if(!WebSocketManager.isConnected()){
                        WebSocketManager.connect("ws://" + Var.Host + ":81");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },0,10000);
    }

    public static void stopTimer(){
        timer.cancel();
    }
}

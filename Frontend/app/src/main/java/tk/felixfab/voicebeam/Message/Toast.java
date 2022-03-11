package tk.felixfab.voicebeam.Message;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class Toast {

    public static void ShowToast(Context context,String message,int duration){

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                android.widget.Toast toast = android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }
}

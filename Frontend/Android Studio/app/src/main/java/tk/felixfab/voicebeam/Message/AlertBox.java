package tk.felixfab.voicebeam.Message;

import android.app.AlertDialog;
import android.content.Context;

public class AlertBox {

    public static void ShowDefaultAlertBox(Context context, String title, String message){

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

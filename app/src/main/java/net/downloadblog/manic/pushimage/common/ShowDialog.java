package net.downloadblog.manic.pushimage.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by ManIc on 12/17/2016.
 */
public class ShowDialog {
    Context context;

    public ShowDialog(Context context){this.context=context;}

    public void DialogPress(String message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Notice:");
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}

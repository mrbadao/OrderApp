package tk.order_sys.orderapp.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by HieuNguyen on 4/17/2015.
 */
public class CartDialog {
//    AlertDialog dialog;
//    Context context;
//    String Title;

    public CartDialog(){

    }

    public static void showDialog(Context context, String Title, String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(Title);
        alertDialogBuilder.setMessage(msg);

        alertDialogBuilder.setNegativeButton("Xác Nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}

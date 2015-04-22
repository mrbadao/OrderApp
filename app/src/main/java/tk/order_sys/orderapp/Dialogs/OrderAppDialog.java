package tk.order_sys.orderapp.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.text.InputType;
import android.widget.EditText;

/**
 * Created by HieuNguyen on 4/17/2015.
 */
public class OrderAppDialog {
//    AlertDialog dialog;
//    Context context;
//    String Title;

    public OrderAppDialog(){

    }

    public static void showAlertDialog(Context context, String Title, String msg){
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

    public static void showPromtInfoDialog(Context context, String Title, String resMsg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Title);

     // Set up the input
        final EditText phone = new EditText(context);
        phone.setHint("Số điện thoại");

        phone.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(phone);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                phone.getText().toString();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}

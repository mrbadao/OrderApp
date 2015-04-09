package tk.order_sys.mapi.httpRequest;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tk.order_sys.mapi.API;
import tk.order_sys.orderapp.R;
import tk.order_sys.orderapp.config.appConfig;

/**
 * Created by HieuNguyen on 4/9/2015.
 */
public class httpRequestAddCartItems extends AsyncTask<JSONObject, Void, Void> {
    private Context context;

    public httpRequestAddCartItems(Context context) {
        super();
        this.context = context;
    }

    @Override
    protected Void doInBackground(JSONObject... params) {
        if(appConfig.isNetworkAvailable(context)){
            API.addCartItem(params[0]);
        }
        else {
            Toast.makeText(context, R.string.error_no_connection, Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}

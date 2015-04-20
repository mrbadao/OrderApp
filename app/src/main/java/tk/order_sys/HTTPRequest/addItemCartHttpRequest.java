package tk.order_sys.HTTPRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tk.order_sys.Interface.AdapterResponse;
import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.config.appConfig;
import tk.order_sys.mapi.API;
import tk.order_sys.orderapp.R;

/**
 * Created by HieuNguyen on 4/20/2015.
 */
public class addItemCartHttpRequest extends AsyncTask<JSONObject, Void, JSONObject> {
    private Context context;
    private JSONArray jsonCookieStore;
    public HTTPAsyncResponse delegate;
    private ProgressDialog pdia;

    public addItemCartHttpRequest(Context context, JSONArray jsonCookieStore, HTTPAsyncResponse delegate) {
        super();
        this.context = context;
        this.jsonCookieStore = jsonCookieStore;
        this.delegate= delegate;
    }

    @Override
    protected void onPreExecute() {
        pdia = new ProgressDialog(context);
        pdia.setMessage("Loading...");
        pdia.show();
    }

    @Override
    protected JSONObject doInBackground(JSONObject... params) {
        if (appConfig.isNetworkAvailable(context)) {
            return API.addCartItem(params[0], jsonCookieStore);
        } else {
            Toast.makeText(context, R.string.error_no_connection, Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        delegate.onHTTPAsyncResponse(jsonObject);
        pdia.dismiss();
    }
}
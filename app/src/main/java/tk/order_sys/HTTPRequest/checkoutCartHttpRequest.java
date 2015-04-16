package tk.order_sys.HTTPRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import tk.order_sys.Interface.CartHttpAsyncResponse;
import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.mapi.API;

/**
 * Created by HieuNguyen on 4/16/2015.
 */
public class checkoutCartHttpRequest extends AsyncTask<String, String, JSONObject> {
    public CartHttpAsyncResponse delegate;
    private ProgressDialog pdia;
    private Context context;
    private JSONArray jsonCookieStore;

    public checkoutCartHttpRequest(Context context, JSONArray jsonCookieStore, CartHttpAsyncResponse delegate) {
        this.context = context;
        this.jsonCookieStore = jsonCookieStore;
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute() {
        pdia = new ProgressDialog(context);
        pdia.setMessage("Loading...");
        pdia.show();
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        return API.checkoutCart(null, jsonCookieStore);
    }

    protected void onPostExecute(JSONObject jsonObject) {
        delegate.onCartHttpAsyncResponse(jsonObject);
        pdia.dismiss();
    }
}

package tk.order_sys.HTTPRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tk.order_sys.Interface.CancelOrderHTTPAsyncResponse;
import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.mapi.API;

/**
 * Created by mrbadao on 13/04/2015.
 */
public class cancelOrderHttpRequest extends AsyncTask<String, String, JSONObject> {
    public CancelOrderHTTPAsyncResponse delegate;
    private ProgressDialog pdia;
    private Context context;
    private JSONArray jsonCookieStore;

    public cancelOrderHttpRequest(Context context, JSONArray jsonCookieStore, CancelOrderHTTPAsyncResponse delegate) {
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
        JSONObject post_params = null;
        try {
            post_params =new JSONObject();
            post_params.put("id", params[0]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return API.cancelOrder(post_params, jsonCookieStore);
    }

    protected void onPostExecute(JSONObject jsonObject) {
        delegate.onCancelOrderHTTPAsyncResponse(jsonObject);
        pdia.dismiss();
    }
}
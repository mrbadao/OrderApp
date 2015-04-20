package tk.order_sys.HTTPRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.mapi.API;

/**
 * Created by HieuNguyen on 4/20/2015.
 */
public class getHotProductHttpRequest extends AsyncTask<String, String, JSONObject> {
    public HTTPAsyncResponse delegate;
    private Context context;
    private ProgressDialog pdia;
    private JSONArray jsonCookieStore;
    private static final String PRODUCT_CATEGORY_ID_TAG = "category_id";

    public getHotProductHttpRequest(Context context, JSONArray jsonCookieStore, HTTPAsyncResponse delegate) {
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
        return API.getHotProducts(jsonCookieStore);
    }

    protected void onPostExecute(JSONObject jsonObject) {
        delegate.onHTTPAsyncResponse(jsonObject);
        pdia.dismiss();
    }
}

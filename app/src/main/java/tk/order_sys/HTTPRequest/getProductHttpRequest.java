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
 * Created by HieuNguyen on 4/15/2015.
 */

public class getProductHttpRequest extends AsyncTask<String, String, JSONObject> {
    public HTTPAsyncResponse delegate;
    private Context context;
    private ProgressDialog pdia;
    private String cat_id;
    private JSONArray jsonCookieStore;
    private static final String PRODUCT_CATEGORY_ID_TAG = "category_id";

    public getProductHttpRequest(Context context, String cat_id, JSONArray jsonCookieStore, HTTPAsyncResponse delegate) {
        this.context = context;
        this.cat_id = cat_id;
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
        if (cat_id != "") {
            try {
                post_params = new JSONObject();
                post_params.put(PRODUCT_CATEGORY_ID_TAG, cat_id);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return API.getProducts(post_params, jsonCookieStore);
    }

    protected void onPostExecute(JSONObject jsonObject) {
        delegate.onHTTPAsyncResponse(jsonObject);
        pdia.dismiss();
    }
}
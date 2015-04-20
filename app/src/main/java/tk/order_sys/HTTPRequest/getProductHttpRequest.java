package tk.order_sys.HTTPRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.mapi.API;
import tk.order_sys.orderapp.ProductActivity;

/**
 * Created by HieuNguyen on 4/15/2015.
 */

public class getProductHttpRequest extends AsyncTask<Integer, String, JSONObject> {
    public HTTPAsyncResponse delegate;
    private Context context;
    private ProgressDialog pdia;
    private String cat_id;
    private JSONArray jsonCookieStore;
    private static final String PRODUCT_CATEGORY_ID_TAG = "category_id";
    private int LOAD_MORE_ITEM;

    public getProductHttpRequest(Context context, String cat_id, int LOAD_MORE_ITEM, JSONArray jsonCookieStore, HTTPAsyncResponse delegate) {
        this.context = context;
        this.cat_id = cat_id;
        this.jsonCookieStore = jsonCookieStore;
        this.delegate = delegate;
        this.LOAD_MORE_ITEM = LOAD_MORE_ITEM;
    }

    @Override
    protected void onPreExecute() {
        pdia = new ProgressDialog(context);
        pdia.setMessage("Loading...");
        pdia.show();
    }

    @Override
    protected JSONObject doInBackground(Integer... params) {
        JSONObject post_params = null;
        if (cat_id != "") {
            try {
                post_params = new JSONObject();
                post_params.put(PRODUCT_CATEGORY_ID_TAG, cat_id);
                post_params.put("limit", LOAD_MORE_ITEM);
                post_params.put("offset", params[0]);
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
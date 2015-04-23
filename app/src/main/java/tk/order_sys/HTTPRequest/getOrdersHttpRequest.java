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
 * Created by HieuNguyen on 4/21/2015.
 */
public class getOrdersHttpRequest extends AsyncTask<Integer, String, JSONObject> {
    private ProgressDialog pdia;
    private Context context;
    private JSONArray jsonCookieStore;
    private HTTPAsyncResponse delegate;
    private int LOAD_MORE_ITEM;
    private String phone;
    private String email;

    public getOrdersHttpRequest(Context context, String phone, String email, int LOAD_MORE_ITEM, JSONArray jsonCookieStore, HTTPAsyncResponse delegate) {
        this.context = context;
        this.jsonCookieStore = jsonCookieStore;
        this.LOAD_MORE_ITEM= LOAD_MORE_ITEM;
        this.delegate = delegate;
        this.email= email;
        this.phone = phone;
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
        if (!phone.isEmpty() && ! email.isEmpty()) {
            try {
                post_params = new JSONObject();
                post_params.put("email", email);
                post_params.put("phone", phone);
                post_params.put("limit", LOAD_MORE_ITEM);
                post_params.put("offset", params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return API.getOrder(post_params, jsonCookieStore);
    }

    protected void onPostExecute(JSONObject jsonObject) {
        delegate.onHTTPAsyncResponse(jsonObject);
        pdia.dismiss();
    }
}
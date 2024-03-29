package tk.order_sys.HTTPRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.mapi.API;

/**
 * Created by mrbadao on 13/04/2015.
 */
public class getCategoriesHttpRequest extends AsyncTask<String, String, JSONObject> {
    private ProgressDialog pdia;
    private Context context;
    private JSONArray jsonCookieStore;
    private HTTPAsyncResponse delegate;

    public getCategoriesHttpRequest(Context context, JSONArray jsonCookieStore, HTTPAsyncResponse delegate){
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
        return API.getCategories(jsonCookieStore);
    }

    protected void onPostExecute(JSONObject jsonObject) {
        delegate.onHTTPAsyncResponse(jsonObject);
        pdia.dismiss();
    }
}

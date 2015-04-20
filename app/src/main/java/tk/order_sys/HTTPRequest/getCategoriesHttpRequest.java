package tk.order_sys.HTTPRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.mapi.API;
import tk.order_sys.mapi.models.ContentCategory;
import tk.order_sys.orderapp.Menu.Adapter.MenuCategoryAdapter;
import tk.order_sys.orderapp.ProductActivity;
import tk.order_sys.orderapp.R;

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

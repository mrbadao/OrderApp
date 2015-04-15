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
//        if (jsonObject != null) {
//            JSONArray jsonArrCategories = null;
//            try {
//                JSONArray jsonArray = new JSONArray(jsonObject.get("Cookies").toString());
//
//                jsonCookieStore = jsonArray;
//
//                Log.i("CURRCOOKIE", "Category " + jsonCookieStore.toString());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            try {
//                jsonArrCategories = jsonObject.getJSONArray("categories");
//                JSONObject jsonCategory = null;
//
//                for (int i = 0; i < jsonArrCategories.length(); i++) {
//                    jsonCategory = jsonArrCategories.getJSONObject(i);
//
//                    listCategory.add(new ContentCategory(
//                            jsonCategory.getString("id"),
//                            jsonCategory.getString("name"),
//                            jsonCategory.getString("abbr_cd"),
//                            jsonCategory.getString("created"),
//                            jsonCategory.getString("modified")
//                    ));
//                }
//
//                lvCategory = (ListView) rootView.findViewById(R.id.lvCategory);
//                lvCategory.setAdapter(new MenuCategoryAdapter(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, listCategory));
//
//
//
//                lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        try {
//
//                            Intent intent = new Intent(getActivity().getBaseContext(), ProductActivity.class);
//
//                            intent.putExtra("cat_id", listCategory.get(position).id);
//                            intent.putExtra("cat_name", listCategory.get(position).name);
//                            if (jsonCookieStore != null) {
//                                intent.putExtra("jsonCookieStore", jsonCookieStore.toString());
//                                Log.i("CURRCOOKIE", "Category-p " + jsonCookieStore.toString());
//                            } else intent.putExtra("jsonCookieStore", "");
//
//                            getActivity().startActivityForResult(intent, ACTIVITY_CODE);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//        }
        pdia.dismiss();
    }
}

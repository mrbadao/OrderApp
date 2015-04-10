package tk.order_sys.orderapp.leftmenu.menufragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tk.order_sys.mapi.API;
import tk.order_sys.mapi.models.ContentCart;
import tk.order_sys.mapi.models.ContentCategory;
import tk.order_sys.orderapp.ProductActivity;
import tk.order_sys.orderapp.R;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuOrderListFragment extends Fragment {
    View rootView;
    private JSONArray jsonCookieStore;
    Context context;
    ListView lvCart;
    ArrayList<ContentCart> listCartItem = new ArrayList<ContentCart>();

    public MenuOrderListFragment(JSONArray cookiestore){
        this.jsonCookieStore = cookiestore;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_order_list_fragment, container, false);
        if(jsonCookieStore != null)
            Toast.makeText(getActivity().getApplicationContext(), "cookie" + jsonCookieStore.toString(), Toast.LENGTH_SHORT).show();
        return rootView;
    }

    private class HTTPRequest extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Loading...");
            pdia.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            return API.getCategories(jsonCookieStore);
        }

        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                JSONArray jsonArrCategories = null;
                try {
                    JSONArray jsonArray = new JSONArray(jsonObject.get("Cookies").toString());

                    jsonCookieStore = jsonArray;

                    Log.i("CURRCOOKIE", "Category " + jsonCookieStore.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    jsonArrCategories = jsonObject.getJSONArray("categories");
                    JSONObject jsonCategory = null;

                    for (int i = 0; i < jsonArrCategories.length(); i++) {
                        jsonCategory = jsonArrCategories.getJSONObject(i);

                        listCategory.add(new ContentCategory(
                                jsonCategory.getString("id"),
                                jsonCategory.getString("name"),
                                jsonCategory.getString("abbr_cd"),
                                jsonCategory.getString("created"),
                                jsonCategory.getString("modified")
                        ));
                    }

                    lvCategory.setAdapter(new MenuCategoryAdapter(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, listCategory));

                    lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {

                                Intent intent = new Intent(getActivity().getBaseContext(), ProductActivity.class);

                                intent.putExtra("cat_id", listCategory.get(position).id);
                                intent.putExtra("cat_name", listCategory.get(position).name);
                                if (jsonCookieStore != null) {
                                    intent.putExtra("jsonCookieStore", jsonCookieStore.toString());
                                    Log.i("CURRCOOKIE", "Category-p " + jsonCookieStore.toString());
                                } else intent.putExtra("jsonCookieStore", "");

                                getActivity().startActivityForResult(intent, ACTIVITY_CODE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            pdia.dismiss();
        }
    }

}

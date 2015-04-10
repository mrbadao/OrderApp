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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tk.order_sys.mapi.API;
import tk.order_sys.mapi.models.ContentCategory;
import tk.order_sys.orderapp.ProductActivity;
import tk.order_sys.orderapp.R;
import tk.order_sys.orderapp.config.appConfig;

/**
 * Created by HieuNguyen on 4/6/2015.
 */

public class MenuCategoryFragment extends Fragment {
    private static final String CATEGORY_INSTANCE_TAG = "lisCategories";
    public static final int ACTIVITY_CODE = 101;
    private JSONArray jsonCookieStore;

    View rootView;
    Context context;
    ListView lvCategory;
    ArrayList<ContentCategory> listCategory = new ArrayList<ContentCategory>();

    public MenuCategoryFragment(JSONArray jsonCookieStore) {
        this.jsonCookieStore = jsonCookieStore;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity().getBaseContext();
        rootView = inflater.inflate(R.layout.menu_category_fragment, container, false);

        if (appConfig.isNetworkAvailable(context)) {
            try {
                new HTTPRequest().execute();
                lvCategory = (ListView) rootView.findViewById(R.id.lvCategory);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            Toast.makeText(context, R.string.error_no_connection, Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    private class MenuCategoryAdapter extends ArrayAdapter {
        Context context;
        int layoutRes;
        ArrayList<ContentCategory> Categories;

        public MenuCategoryAdapter(Context context, int resource, ArrayList<ContentCategory> objects) {
            super(context, resource, objects);
            this.context = context;
            this.layoutRes = resource;
            Categories = objects;
        }

        @Override
        public int getCount() {
            return Categories.size();
        }

        @Override
        public Object getItem(int position) {
            return Categories.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                view = mInflater.inflate(layoutRes, parent, false);
            } else {
                view = convertView;
            }

            TextView categoryListItem = (TextView) view;

            categoryListItem.setText((CharSequence) ((ContentCategory) getItem(position)).name);

            return view;
        }
    }


}

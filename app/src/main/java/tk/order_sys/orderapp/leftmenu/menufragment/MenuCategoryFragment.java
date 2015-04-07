package tk.order_sys.orderapp.leftmenu.menufragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import tk.order_sys.orderapp.R;
import tk.order_sys.orderapp.config.appConfig;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuCategoryFragment extends Fragment {
    View rootView;
    Context context;
    ListView lvCategory;
    ArrayList<ContentCategory> listCategory = new ArrayList<ContentCategory>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity().getBaseContext();
        rootView = inflater.inflate(R.layout.menu_category_fragment, container, false);

        if (appConfig.isNetworkAvailable(context)) {
            new HTTPRequest().execute();
            lvCategory = (ListView) rootView.findViewById(R.id.lvCategory);
            lvCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Toast.makeText(getActivity().getBaseContext(), listCategory.get(position).abbr_cd, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else {
            Toast.makeText(context, "Vui lòng kiểm tra kết nối Internet của bạn.", Toast.LENGTH_SHORT).show();
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
            JSONObject jsonObj = null;
            try {
                jsonObj = new JSONObject(API.getCategory());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObj;
        }

        protected void onPostExecute(JSONObject jsonObject) {
            JSONArray jsonArrCategories = null;
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

                lvCategory = (ListView) rootView.findViewById(R.id.lvCategory);
                lvCategory.setAdapter(new MenuCategoryAdapter(getActivity().getBaseContext(), android.R.layout.simple_list_item_1, listCategory));

            } catch (JSONException e) {
                e.printStackTrace();
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

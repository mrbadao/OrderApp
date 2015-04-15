package tk.order_sys.orderapp.Menu.Fragment;

import android.content.Context;
import android.content.Intent;
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

import tk.order_sys.HTTPRequest.HTTPRequestCategories;
import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.config.appConfig;
import tk.order_sys.mapi.models.ContentCategory;
import tk.order_sys.orderapp.Menu.Adapter.MenuCategoryAdapter;
import tk.order_sys.orderapp.ProductActivity;
import tk.order_sys.orderapp.R;

/**
 * Created by HieuNguyen on 4/6/2015.
 */

public class MenuCategoryFragment extends Fragment implements HTTPAsyncResponse {
    public static final int ACTIVITY_CODE = 101;
    View rootView;
    Context context;
    ListView lvCategory;
    ArrayList<ContentCategory> listCategory = new ArrayList<ContentCategory>();
    private JSONArray jsonCookieStore;

    public MenuCategoryFragment(JSONArray jsonCookieStore) {
        this.jsonCookieStore = jsonCookieStore;
    }

    public MenuCategoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity().getBaseContext();
        rootView = inflater.inflate(R.layout.menu_category_fragment, container, false);

        if (appConfig.isNetworkAvailable(context)) {
            try {

                new HTTPRequestCategories(getActivity(), jsonCookieStore, this).execute();
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

    @Override
    public void onHTTPAsyncResponse(JSONObject jsonObject) {
        if (jsonObject != null) {
            JSONArray jsonArrCategories = null;

            try {
                if(!jsonObject.isNull("Cookies"))
                {
                    JSONArray jsonArray = new JSONArray(jsonObject.get("Cookies").toString());
                    jsonCookieStore = jsonArray;
                }

                if(!jsonObject.isNull("categories")){
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

}

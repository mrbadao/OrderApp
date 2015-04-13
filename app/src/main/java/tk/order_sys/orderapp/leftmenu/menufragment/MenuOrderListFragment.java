package tk.order_sys.orderapp.leftmenu.menufragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tk.order_sys.mapi.API;
import tk.order_sys.mapi.models.ContentCart;
import tk.order_sys.orderapp.R;
import tk.order_sys.orderapp.config.appConfig;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuOrderListFragment extends Fragment {
    View rootView;
    private JSONArray jsonCookieStore;
    Context context;
    ListView lvCart;
    ArrayList<ContentCart> listCartItem = new ArrayList<ContentCart>();

    public MenuOrderListFragment(JSONArray cookiestore) {
        this.jsonCookieStore = cookiestore;
    }

    public MenuOrderListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_order_list_fragment, container, false);
        if (appConfig.isNetworkAvailable(getActivity().getBaseContext())) {
            try {

                new HTTPRequest().execute();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            Toast.makeText(getActivity().getBaseContext(), R.string.error_no_connection, Toast.LENGTH_SHORT).show();
        }
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
            return API.getCart(jsonCookieStore);
        }

        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                JSONArray jsonArrCart = null;

                Log.i("CURRCOOKIE", "getCart1 " + jsonObject.toString());

                try {
                    JSONArray jsonArray = new JSONArray(jsonObject.get("Cookies").toString());

                    jsonCookieStore = jsonArray;

                    Log.i("CURRCOOKIE", "getCart " + jsonCookieStore.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    jsonArrCart = jsonObject.getJSONArray("Cart");
                    JSONObject jsonArrCartItem = null;

                    for (int i = 0; i < jsonArrCart.length(); i++) {
                        jsonArrCartItem = jsonArrCart.getJSONObject(i);

                        listCartItem.add(new ContentCart(
                                jsonArrCartItem.getString("id"),
                                jsonArrCartItem.getString("name"),
                                jsonArrCartItem.getString("price"),
                                jsonArrCartItem.getString("qty")
                        ));
                    }

                    Log.i("Current", listCartItem.get(0).name);
                    lvCart = (ListView) rootView.findViewById(R.id.lvCart);
                    lvCart.setAdapter(new MenuCartAdapter(getActivity().getApplicationContext(), R.layout.cart_item_row, listCartItem));


                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
            pdia.dismiss();
        }
    }

    private class MenuCartAdapter extends ArrayAdapter {
        Context context;
        int layoutRes;
        ArrayList<ContentCart> cartItems;

        public MenuCartAdapter(Context context, int resource, ArrayList<ContentCart> objects) {
            super(context, resource, objects);
            this.context = context;
            this.layoutRes = resource;
            cartItems = objects;
        }

        @Override
        public int getCount() {
            return cartItems.size();
        }

        @Override
        public Object getItem(int position) {
            return cartItems.get(position);
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

            TextView itemTitle = (TextView) view.findViewById(R.id.txtView_productTitle);
            TextView itemPrice = (TextView) view.findViewById(R.id.txtView_productPrice);
            TextView itemTotal = (TextView) view.findViewById(R.id.txtView_product_total);
            EditText itemQuanty = (EditText) view.findViewById(R.id.txtEdit_productQuanty);

            ContentCart cartItem = (ContentCart) getItem(position);

            itemTitle.setText((CharSequence) cartItem.name);
            itemPrice.setText((CharSequence) String.format("%,d", Long.valueOf(cartItem.price)) + " đồng");
            itemTotal.setText((CharSequence) String.format("%,d", Long.valueOf(cartItem.price) * Long.valueOf(cartItem.qty)) + " đồng");
            itemQuanty.setText((CharSequence) cartItem.qty);


            return view;
        }
    }

}

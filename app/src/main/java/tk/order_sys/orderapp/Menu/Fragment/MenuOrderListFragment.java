package tk.order_sys.orderapp.Menu.Fragment;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tk.order_sys.gps.GpsTracer;
import tk.order_sys.mapi.API;
import tk.order_sys.mapi.models.ContentCart;
import tk.order_sys.orderapp.Menu.Adapter.MenuCartAdapter;
import tk.order_sys.orderapp.R;
import tk.order_sys.orderapp.config.appConfig;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuOrderListFragment extends Fragment {
    View rootView;
    ListView lvCart;
    Button btnCheckOut;
    TextView txtViewCartTotal;
    Long orderTotal;
    GpsTracer gpsTracer;
    Location location;
    ArrayList<ContentCart> listCartItem = new ArrayList<ContentCart>();
    private JSONArray jsonCookieStore;

    public MenuOrderListFragment(JSONArray cookiestore) {
        this.jsonCookieStore = cookiestore;
    }

    public MenuOrderListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_order_list_fragment, container, false);

        orderTotal = Long.valueOf(0);

        GpsTracer gpsTracer = new GpsTracer(getActivity());
        if (!gpsTracer.canGetGPS())
            gpsTracer.showSettingAlert();
        else if (appConfig.isNetworkAvailable(getActivity().getBaseContext())) {
            try {
                location = gpsTracer.getLocation();
                btnCheckOut = (Button) rootView.findViewById(R.id.btnCheckOut);
                btnCheckOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });

                txtViewCartTotal = (TextView) rootView.findViewById(R.id.txtView_cart_total);
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
                        ContentCart item = new ContentCart(
                                jsonArrCartItem.getString("id"),
                                jsonArrCartItem.getString("name"),
                                jsonArrCartItem.getString("price"),
                                jsonArrCartItem.getString("qty"));

                        listCartItem.add(item);
                        orderTotal += Long.valueOf(item.price);
                    }

                    txtViewCartTotal.setText((CharSequence) String.format("%,d", orderTotal) + " đồng");

                    Log.i("Current", listCartItem.get(0).name);
                    lvCart = (ListView) rootView.findViewById(R.id.lvCart);
                    lvCart.setAdapter(new MenuCartAdapter(getActivity().getBaseContext(), R.layout.cart_item_row, listCartItem));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            pdia.dismiss();
        }
    }

}

package tk.order_sys.orderapp.Menu.Fragment;

import android.location.Location;
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

import tk.order_sys.HTTPRequest.checkoutCartHttpRequest;
import tk.order_sys.HTTPRequest.getCartHttpRequest;
import tk.order_sys.Interface.CartHttpAsyncResponse;
import tk.order_sys.gps.GpsTracer;
import tk.order_sys.mapi.models.ContentCart;
import tk.order_sys.orderapp.MainActivity;
import tk.order_sys.orderapp.Menu.Adapter.MenuCartAdapter;
import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.orderapp.R;
import tk.order_sys.config.appConfig;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuOrderListFragment extends Fragment implements HTTPAsyncResponse, View.OnClickListener, CartHttpAsyncResponse {
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
                btnCheckOut.setOnClickListener(this);

                txtViewCartTotal = (TextView) rootView.findViewById(R.id.txtView_cart_total);
                new getCartHttpRequest(getActivity(), jsonCookieStore, this).execute();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            Toast.makeText(getActivity().getBaseContext(), R.string.error_no_connection, Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }


    @Override
    public void onHTTPAsyncResponse(JSONObject jsonObject) {
        if (jsonObject != null) {
            JSONArray jsonArrCart = null;
            try {
                if(!jsonObject.isNull("Cookies")){
                    jsonCookieStore = new JSONArray(jsonObject.get("Cookies").toString());
                    ((MainActivity)getActivity()).updateFromFragment(jsonCookieStore);
                }

                if(!jsonObject.isNull("Cart")){
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
                        orderTotal += Long.valueOf(item.price) * Long.valueOf(item.qty);
                    }

                    txtViewCartTotal.setText((CharSequence) String.format("%,d", orderTotal) + " đồng");

                    Log.i("Current", listCartItem.get(0).name);
                    lvCart = (ListView) rootView.findViewById(R.id.lvCart);
                    lvCart.setAdapter(new MenuCartAdapter(getActivity().getApplicationContext(), R.layout.cart_item_row, listCartItem));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btnCheckOut:
                new checkoutCartHttpRequest(getActivity(), jsonCookieStore, this).execute();
                break;
        }
    }

    @Override
    public void onCartHttpAsyncResponse(JSONObject jsonObject) {
       if(jsonObject!= null){
           Log.i("ERROR CHECKOUT", jsonObject.toString());
           try {
               if (!jsonObject.isNull("Cookies")) {
                   jsonCookieStore = new JSONArray(jsonObject.get("Cookies").toString());
                   ((MainActivity) getActivity()).updateFromFragment(jsonCookieStore);
               }

               if(!jsonObject.isNull("error")){
                   JSONObject jsonError = jsonObject.getJSONObject("error");
                   Log.i("ERROR CHECKOUT", jsonError.getString("error_code"));
               }

           }catch (JSONException e) {
               e.printStackTrace();
           }catch (NullPointerException e){
               e.printStackTrace();
           }
       }
    }
}

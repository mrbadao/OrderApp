package tk.order_sys.orderapp.Menu.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.config.Constants;
import tk.order_sys.config.appConfig;
import tk.order_sys.gps.GpsTracer;
import tk.order_sys.mapi.models.ContentCart;
import tk.order_sys.orderapp.Dialogs.OrderAppDialog;
import tk.order_sys.orderapp.MainActivity;
import tk.order_sys.orderapp.Menu.Adapters.MenuCartAdapter;
import tk.order_sys.orderapp.R;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuOrderListFragment extends Fragment implements HTTPAsyncResponse, View.OnClickListener, CartHttpAsyncResponse, LocationListener {
    View rootView;
    ListView lvCart;
    Button btnCheckOut;
    EditText editTxtAddress;
    TextView txtViewCartTotal;
    Long orderTotal;
    ArrayList<ContentCart> listCartItem = new ArrayList<ContentCart>();

    private LocationManager locationManager;
    private String mProvider;
    private Location mCurrentLocation;
    private ProgressDialog pdia;
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
        mCurrentLocation = null;
        mProvider = null;
        locationManager = null;
        pdia = null;

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (appConfig.isNetworkAvailable(getActivity().getBaseContext())) {
            try {

                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                mProvider = locationManager.getBestProvider(criteria, true);

                if (mProvider == null){
                    OrderAppDialog.showAlertDialog(getActivity(), "Dịch vụ định vị", "Vui lòng kiểm trac các dịch vụ định vị của bạn");
                }else {

                    btnCheckOut = (Button) rootView.findViewById(R.id.btnCheckOut);
                    btnCheckOut.setOnClickListener(this);

                    editTxtAddress = (EditText) rootView.findViewById(R.id.txtAddress);

                    txtViewCartTotal = (TextView) rootView.findViewById(R.id.txtView_cart_total);
                    new getCartHttpRequest(getActivity(), jsonCookieStore, this).execute();

                    if(!mProvider.equals(LocationManager.GPS_PROVIDER)){
                        OrderAppDialog.showAlertDialog(getActivity(),"Khuyến cáo", "Bạn nên sữ dụng GPS để tăng độ chính xác của việc xác định vị trí");
                    }

                    locationManager.requestLocationUpdates(mProvider, 0, 0, this);
                    mCurrentLocation = locationManager.getLastKnownLocation(mProvider);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            Toast.makeText(getActivity().getBaseContext(), R.string.error_no_connection, Toast.LENGTH_SHORT).show();
        }
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

                String Name = "";
                String Email = "";
                String Phone = "";
                String Address = String.valueOf(editTxtAddress.getText());

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

                if (sharedPreferences.contains(Constants.SETTING_CUSTOMER_NAME)) {
                    Name = sharedPreferences.getString(Constants.SETTING_CUSTOMER_NAME, "");
                }
                if (sharedPreferences.contains(Constants.SETTING_CUSTOMER_PHONE)) {
                    Phone = sharedPreferences.getString(Constants.SETTING_CUSTOMER_PHONE, "");
                }
                if (sharedPreferences.contains(Constants.SETTING_CUSTOMER_EMAIL)) {
                    Email = sharedPreferences.getString(Constants.SETTING_CUSTOMER_EMAIL, "");
                }

                if(mCurrentLocation == null){
                    pdia = new ProgressDialog(getActivity());
                    pdia.setMessage("Đang dò tìm vị trí của bạn");
                    pdia.show();
                }
                else if( !Name.isEmpty() && !Email.isEmpty() && !Phone.isEmpty() && !Address.isEmpty()){
                    JSONObject checkoutParams = new JSONObject();
                    try {
                        checkoutParams.put("name", Name);
                        checkoutParams.put("email", Email);
                        checkoutParams.put("phone", Phone);
                        checkoutParams.put("address", Address);
                        checkoutParams.put("coordinate_long",String.valueOf(mCurrentLocation.getLongitude()));
                        checkoutParams.put("coordinate_lat", String.valueOf(mCurrentLocation.getLatitude()));

                        Log.i("PARAMS", checkoutParams.toString());

                        new checkoutCartHttpRequest(getActivity(), jsonCookieStore, this).execute(checkoutParams);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else OrderAppDialog.showAlertDialog(getActivity(), "Lỗi đặt hàng", "Thông tin đặt hàng chưa chính xác.\n Hãy kiểm tra lại cài đặt và địa chỉ của bạn.");

                break;
        }
    }

    @Override
    public void onCheckoutCartHttpAsyncResponse(JSONObject jsonObject) {
       if(jsonObject!= null){
           Log.i("ERROR CHECKOUT", jsonObject.toString());
           try {
               if (!jsonObject.isNull("Cookies")) {
                   jsonCookieStore = new JSONArray(jsonObject.get("Cookies").toString());
                   ((MainActivity) getActivity()).updateFromFragment(jsonCookieStore);
               }

               if(!jsonObject.isNull("error")){
                   JSONObject jsonError = jsonObject.getJSONObject("error");
                   String error_code = jsonError.getString("error_code");

                   switch (error_code){
                       case "1006":
                           OrderAppDialog.showAlertDialog(getActivity(), "Lỗi đặt hàng", "Không có mặt hàng trong giỏ hàng của bạn.");
                           break;
                       case "1010":
                           OrderAppDialog.showAlertDialog(getActivity(), "Lỗi đặt hàng", "Mặt hàng không phù hợp.");
                           break;
                       case "1018":
                           OrderAppDialog.showAlertDialog(getActivity(), "Lỗi đặt hàng", "Vị trí của bạn quá xa chúng tôi không thể giao hàng.");
                           break;
                       default:
                           OrderAppDialog.showAlertDialog(getActivity(), "Lỗi đặt hàng", "Có lỗi xãy ra trong qua trình đặt hàng.");
                   }
               }

               if(!jsonObject.isNull("status")){
                   JSONObject jsonStatus = jsonObject.getJSONObject("status");
                   String status_code = jsonStatus.getString("status_code");

                   if(status_code.equals("1009")){
                       lvCart.setAdapter(null);
                       txtViewCartTotal.setText(null);
                       OrderAppDialog.showAlertDialog(getActivity(), "Thông báo", "Bạn đã đặt hàng thành công. \nMã đơn hàng của bạn là:\n " + jsonStatus.get("order_id"));
                   }
               }

           }catch (JSONException e) {
               e.printStackTrace();
           }catch (NullPointerException e){
               e.printStackTrace();
           }
       }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        try{
            if (pdia != null){
                pdia.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Criteria criteria = new Criteria();
        mProvider = locationManager.getBestProvider(criteria, true);
        if (mProvider.isEmpty() || mProvider == null)
            Toast.makeText(getActivity().getApplicationContext(), "Dịch vụ định vị không hoạt động", Toast.LENGTH_SHORT).show();
        locationManager.requestLocationUpdates(mProvider, 0, 0, this);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Criteria criteria = new Criteria();
        mProvider = locationManager.getBestProvider(criteria, true);
        if (mProvider.isEmpty() || mProvider == null)
            Toast.makeText(getActivity().getApplicationContext(), "Dịch vụ định vị không hoạt động", Toast.LENGTH_SHORT).show();
        locationManager.requestLocationUpdates(mProvider, 0, 0, this);
    }
}

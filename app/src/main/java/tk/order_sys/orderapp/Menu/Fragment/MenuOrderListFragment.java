package tk.order_sys.orderapp.Menu.Fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
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
import tk.order_sys.gps.GpsTracer;
import tk.order_sys.mapi.models.ContentCart;
import tk.order_sys.orderapp.Dialogs.CartDialog;
import tk.order_sys.orderapp.MainActivity;
import tk.order_sys.orderapp.Menu.Adapter.MenuCartAdapter;
import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.orderapp.Profile.MyInfo;
import tk.order_sys.orderapp.R;
import tk.order_sys.config.appConfig;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuOrderListFragment extends Fragment implements HTTPAsyncResponse, View.OnClickListener, CartHttpAsyncResponse {
    View rootView;
    ListView lvCart;
    Button btnCheckOut;
    EditText editTxtPhone;
    EditText editTxtName;
    EditText editTxtEmail;
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

        if (!gpsTracer.canGetGPS()) {
            gpsTracer.showSettingAlert();
        }
        else if (appConfig.isNetworkAvailable(getActivity().getBaseContext())) {
            try {
                location = gpsTracer.getLocation();

//                Toast.makeText(getActivity().getApplicationContext(), String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();

                btnCheckOut = (Button) rootView.findViewById(R.id.btnCheckOut);
                btnCheckOut.setOnClickListener(this);

                editTxtPhone = (EditText) rootView.findViewById(R.id.txtPhoneNumber);
                editTxtName = (EditText) rootView.findViewById(R.id.txtName);
                editTxtEmail = (EditText) rootView.findViewById(R.id.txtEmail);


                editTxtPhone.setText(MyInfo.getPhoneNumber(getActivity()));
                editTxtEmail.setText(MyInfo.getGoogleMail(getActivity(), "com.google"));

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
//                try{
//                    location = gpsTracer.getLocation();

//                    Toast.makeText(getActivity().getApplicationContext(), String.valueOf(location.getLatitude()), Toast.LENGTH_SHORT).show();
//                }catch (NullPointerException e){e.printStackTrace();}

                String Name = String.valueOf(editTxtName.getText());
                String Email = String.valueOf(editTxtEmail.getText());
                String Phone = String.valueOf(editTxtPhone.getText());
                if( !Name.isEmpty() && !Email.isEmpty() && !Phone.isEmpty()){
//                    location != null &&
                    JSONObject checkoutParams = new JSONObject();
                    try {
                        checkoutParams.put("name", Name);
                        checkoutParams.put("email", Email);
                        checkoutParams.put("phone", Phone);
//                        checkoutParams.put("coordinate_long",String.valueOf(location.getLongitude()));
//                        checkoutParams.put("coordinate_lat", String.valueOf(location.getLatitude()));

                        Log.i("PARAMS", checkoutParams.toString());

                        new checkoutCartHttpRequest(getActivity(), jsonCookieStore, this).execute(checkoutParams);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else CartDialog.showDialog(getActivity(), "Lỗi đặt hàng", "Thông tin đặt hàng chưa chính xác.");

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
                           CartDialog.showDialog(getActivity(), "Lỗi đặt hàng", "Không có mặt hàng trong giỏ hàng của bạn.");
                           break;
                       case "1010":
                           CartDialog.showDialog(getActivity(), "Lỗi đặt hàng", "Mặt hàng không phù hợp.");
                           break;
                       default:
                           CartDialog.showDialog(getActivity(), "Lỗi đặt hàng", "Có lỗi xãy ra trong qua trình đặt hàng.");
                   }
               }

               if(!jsonObject.isNull("status")){
                   JSONObject jsonStatus = jsonObject.getJSONObject("status");
                   String status_code = jsonStatus.getString("status_code");

                   if(status_code.equals("1009")){
                       lvCart.setAdapter(null);
                       txtViewCartTotal.setText(null);
                       CartDialog.showDialog(getActivity(), "Thông báo", "Bạn đã đặt hàng thành công. \nMã đơn hàng của bạn là:\n " + jsonStatus.get("order_id"));
                   }
               }

           }catch (JSONException e) {
               e.printStackTrace();
           }catch (NullPointerException e){
               e.printStackTrace();
           }
       }
    }
}

package tk.order_sys.orderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tk.order_sys.HTTPRequest.cancelOrderHttpRequest;
import tk.order_sys.HTTPRequest.getOrderDetailHttpRequest;
import tk.order_sys.Interface.CancelOrderHTTPAsyncResponse;
import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.mapi.models.ContentCart;
import tk.order_sys.orderapp.Menu.Adapters.MenuCartAdapter;
import tk.order_sys.orderapp.Menu.Adapters.OrderDetailAdapter;
import tk.order_sys.orderapp.XListView.view.XListView;


public class OrderDetailActivity extends ActionBarActivity implements HTTPAsyncResponse, CancelOrderHTTPAsyncResponse {
    private static final String CALL_BACK_FRAGMET_TAG = "mMenuFragmentSection";
    private static final String CALL_BACK_COOKIE_STORE_TAG = "mCookieStore";

    private String order_name = "";
    private String order_id = "";
    private String order_stt = "";

    private JSONArray jsonCookieStore = null;
    ListView lisViewOrderDetail;
    ArrayList<ContentCart> listOrderDetail = new ArrayList<ContentCart>();

    OrderDetailAdapter mAdapter;

    Long orderTotal = Long.valueOf(0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        Bundle orderInfo = getIntent().getExtras();
        order_id = (String) orderInfo.get("order_id");
        order_name = (String) orderInfo.get("order_name");
        order_stt = (String) orderInfo.get("order_stt");

        Log.i("VIEW", order_stt);

        if (!orderInfo.get("jsonCookieStore").toString().isEmpty()) {
            try {
                jsonCookieStore = new JSONArray(orderInfo.get("jsonCookieStore").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        lisViewOrderDetail = (ListView) findViewById(R.id.listViewOrderDetail);

        new getOrderDetailHttpRequest(OrderDetailActivity.this, jsonCookieStore,this).execute(order_id);

        setTitle(order_name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent callBackData = null;

        switch (id) {
            case R.id.action_delete_order:
                new cancelOrderHttpRequest(OrderDetailActivity.this, jsonCookieStore, this).execute(order_id);
                break;

            case android.R.id.home:
                callBackData = new Intent();
                callBackData.putExtra(CALL_BACK_FRAGMET_TAG, 3);
                if (jsonCookieStore != null)
                    callBackData.putExtra(CALL_BACK_COOKIE_STORE_TAG, jsonCookieStore.toString());
                setResult(Activity.RESULT_OK, callBackData);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHTTPAsyncResponse(JSONObject jsonObject) {
        if(jsonObject != null){
            JSONArray jsonOrderDetail = null;
            try {
                if(!jsonObject.isNull("Cookies")){
                    jsonCookieStore = new JSONArray(jsonObject.get("Cookies").toString());
                }

                if(!jsonObject.isNull("order")){
                    jsonOrderDetail = jsonObject.getJSONArray("order");
                    JSONObject jsonOrderDetailItem = null;

                    for (int i = 0; i < jsonOrderDetail.length(); i++) {
                        jsonOrderDetailItem = jsonOrderDetail.getJSONObject(i);
                        ContentCart item = new ContentCart(
                                jsonOrderDetailItem.getString("id"),
                                jsonOrderDetailItem.getString("name"),
                                jsonOrderDetailItem.getString("price"),
                                jsonOrderDetailItem.getString("qty"));

                        listOrderDetail.add(item);
                        orderTotal += Long.valueOf(item.price) * Long.valueOf(item.qty);
                    }
                    mAdapter = new OrderDetailAdapter(getApplicationContext(), R.layout.order_detail_row, listOrderDetail);
                    lisViewOrderDetail.setAdapter(mAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCancelOrderHTTPAsyncResponse(JSONObject jsonObject) {
        if(jsonObject != null){
            Toast.makeText(getApplicationContext(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}

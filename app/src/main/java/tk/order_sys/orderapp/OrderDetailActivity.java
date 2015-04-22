package tk.order_sys.orderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import tk.order_sys.orderapp.XListView.view.XListView;


public class OrderDetailActivity extends ActionBarActivity {
    private static final String CALL_BACK_FRAGMET_TAG = "mMenuFragmentSection";
    private static final String CALL_BACK_COOKIE_STORE_TAG = "mCookieStore";

    private String order_name = "";
    private String order_id = "";
    private String order_stt = "";

    private JSONArray jsonCookieStore = null;

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
                Toast.makeText(getApplicationContext(), order_stt, Toast.LENGTH_SHORT).show();
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
}

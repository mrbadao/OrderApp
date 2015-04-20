package tk.order_sys.orderapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tk.order_sys.HTTPRequest.getProductHttpRequest;
import tk.order_sys.Interface.AdapterResponse;
import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.mapi.models.ContentProduct;
import tk.order_sys.orderapp.Menu.Adapter.ProductsAdapter;
import tk.order_sys.orderapp.XListView.view.XListView;


public class ProductActivity extends ActionBarActivity implements HTTPAsyncResponse, XListView.IXListViewListener, AdapterResponse {
    private String cat_name = "";
    private String cat_id = "";

    private JSONArray jsonCookieStore;

    private XListView lvProducts;
    private Handler mHandler;
    private int page;
    private int pages;
    private boolean isFirstLoad = false;
    private static final int LOAD_MORE_ITEMS = 5;

    private ProductsAdapter mAdapter;

    private static final String CALL_BACK_FRAGMET_TAG = "mMenuFragmentSection";
    private static final String CALL_BACK_COOKIE_STORE_TAG = "mCookieStore";

    ArrayList<ContentProduct> listProducts = new ArrayList<ContentProduct>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonCookieStore = null;
        setContentView(R.layout.activity_product);

        page = 1;
        pages = 0;

        Bundle catInfo = getIntent().getExtras();

        cat_id = (String) catInfo.get("cat_id");
        cat_name = (String) catInfo.get("cat_name");

        if (!catInfo.get("jsonCookieStore").toString().isEmpty()) {
            try {
                jsonCookieStore = new JSONArray(catInfo.get("jsonCookieStore").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        setTitle(cat_name);
        isFirstLoad = true;
        lvProducts = (XListView) findViewById(R.id.lvProducts);
        getProducts();
        lvProducts.setPullLoadEnable(true);
        mHandler = new Handler();
    }

    private void getProducts() {
        if (cat_id != "")
            new getProductHttpRequest(ProductActivity.this, cat_id, LOAD_MORE_ITEMS, jsonCookieStore, this).execute((page - 1) * LOAD_MORE_ITEMS);
    }

    private void onLoad() {
        lvProducts.stopRefresh();
        lvProducts.stopLoadMore();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        lvProducts.setRefreshTime(dateFormat.format(date));
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                pages = 0;
                isFirstLoad = true;
                listProducts.clear();
                getProducts();
                lvProducts.setAdapter(mAdapter);
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                if (page <= pages && pages != 0)
                    getProducts();
                else {
                    lvProducts.stopLoadMore();
                    lvProducts.stopRefresh();
                }
            }
        }, 2000);
    }

    @Override
    public void onHTTPAsyncResponse(JSONObject jsonObject) {
        try {
            if (!jsonObject.isNull("count")) {
                pages = (int) Math.ceil(jsonObject.getDouble("count") / (double) LOAD_MORE_ITEMS);
                Log.i("paging", String.valueOf(pages));
            }
            if (!jsonObject.isNull("Cookies")) {
                jsonCookieStore = new JSONArray(jsonObject.get("Cookies").toString());
            }

            if (!jsonObject.isNull("products")) {
                JSONArray jsonArrProducts = jsonObject.getJSONArray("products");
                JSONObject jsonArrProduct = null;

                for (int i = 0; i < jsonArrProducts.length(); i++) {
                    jsonArrProduct = jsonArrProducts.getJSONObject(i);

                    listProducts.add(new ContentProduct(
                            jsonArrProduct.getString("id"),
                            jsonArrProduct.getString("name"),
                            jsonArrProduct.getString("thumbnail"),
                            jsonArrProduct.getString("description"),
                            jsonArrProduct.getString("price"),
                            jsonArrProduct.getString("category_id"),
                            jsonArrProduct.getString("created"),
                            jsonArrProduct.getString("modified")
                    ));
                }
                if (isFirstLoad) {
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                    Date date = new Date();
                    lvProducts.setRefreshTime(dateFormat.format(date));

                    mAdapter = new ProductsAdapter(ProductActivity.this, R.layout.product_row, listProducts, jsonCookieStore, this);

                    lvProducts.setAdapter(mAdapter);
                    lvProducts.setXListViewListener(this);
                    isFirstLoad = false;
                } else {
                    mAdapter.notifyDataSetChanged();
                    onLoad();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent callBackData = null;

        switch (id) {
            case R.id.action_add_cart:
                callBackData = new Intent();
                callBackData.putExtra(CALL_BACK_FRAGMET_TAG, 4);

                if (jsonCookieStore != null)
                    callBackData.putExtra(CALL_BACK_COOKIE_STORE_TAG, jsonCookieStore.toString());

                setResult(Activity.RESULT_OK, callBackData);
                finish();
                break;

            case android.R.id.home:
                callBackData = new Intent();
                callBackData.putExtra(CALL_BACK_FRAGMET_TAG, 1);
                if (jsonCookieStore != null)
                    callBackData.putExtra(CALL_BACK_COOKIE_STORE_TAG, jsonCookieStore.toString());

                setResult(Activity.RESULT_OK, callBackData);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAdapterResponse(JSONObject jsonObject) {
        if (jsonObject != null) {
            if (!jsonObject.isNull("Cookies")) {
                try {
                    jsonCookieStore = new JSONArray(jsonObject.get("Cookies").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


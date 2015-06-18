package tk.order_sys.orderapp.Menu.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tk.order_sys.HTTPRequest.getHotProductHttpRequest;
import tk.order_sys.Interface.AdapterResponse;
import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.config.appConfig;
import tk.order_sys.mapi.models.ContentProduct;
import tk.order_sys.orderapp.Dialogs.OrderAppDialog;
import tk.order_sys.orderapp.MainActivity;
import tk.order_sys.orderapp.Menu.Adapters.ProductsAdapter;
import tk.order_sys.orderapp.R;
import tk.order_sys.orderapp.XListView.view.XListView;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuFavoriteFragment extends Fragment implements XListView.IXListViewListener, HTTPAsyncResponse, AdapterResponse {
    private XListView mListViewFavorite;
    private ProductsAdapter mAdapter;

    private Handler mHandler;
    private int start = 0;
    private boolean isFirstLoad = false;
    private static int refreshCnt = 0;



    View rootView;
    ArrayList<ContentProduct> listProducts = new ArrayList<ContentProduct>();
    private JSONArray jsonCookieStore;

    public MenuFavoriteFragment(JSONArray jsonCookieStore) {
        this.jsonCookieStore = jsonCookieStore;
    }

    public MenuFavoriteFragment() {
        this.jsonCookieStore = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MainActivity)getActivity()).updateSelectedFragment(2);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_favorite_fragment, container, false);
        if(appConfig.isNetworkAvailable(getActivity().getApplicationContext())) {
            isFirstLoad = true;
            getProducts();
            mListViewFavorite = (XListView) rootView.findViewById(R.id.xListViewFavorite);
            mListViewFavorite.setPullLoadEnable(false);

            mHandler = new Handler();
        }else{
            OrderAppDialog.showNetworkAlertDialog(getActivity());
        }

        return rootView;
    }

    private void getProducts() {
        new getHotProductHttpRequest(getActivity(), jsonCookieStore, this).execute();
    }

    private void onLoad() {
        mListViewFavorite.stopRefresh();
        mListViewFavorite.stopLoadMore();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        mListViewFavorite.setRefreshTime(dateFormat.format(date));
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start = ++refreshCnt;
                isFirstLoad = true;
                listProducts.clear();
                getProducts();
                mListViewFavorite.setAdapter(mAdapter);
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getProducts();
            }
        }, 2000);
    }

    @Override
    public void onHTTPAsyncResponse(JSONObject jsonObject) {
        try {
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
                            jsonArrProduct.getString("saleoff_price"),
                            jsonArrProduct.getString("saleoff_percent"),
                            jsonArrProduct.getString("category_id"),
                            jsonArrProduct.getString("created"),
                            jsonArrProduct.getString("modified")
                    ));
                }
                if (listProducts.size()>0) {
                    if (isFirstLoad) {
                        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                        Date date = new Date();
                        mListViewFavorite.setRefreshTime(dateFormat.format(date));


                        mAdapter = new ProductsAdapter(getActivity(), R.layout.product_row, listProducts, jsonCookieStore, this);

                        mListViewFavorite.setAdapter(mAdapter);
                        mListViewFavorite.setXListViewListener(this);
                        isFirstLoad = false;
                    } else {
                        mAdapter.notifyDataSetChanged();
                        onLoad();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAdapterResponse(JSONObject jsonObject) {
        if (jsonObject != null) {
            if (!jsonObject.isNull("Cookies")) {
                try {
                    jsonCookieStore = new JSONArray(jsonObject.get("Cookies").toString());
                    ((MainActivity) getActivity()).updateFromFragment(jsonCookieStore);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

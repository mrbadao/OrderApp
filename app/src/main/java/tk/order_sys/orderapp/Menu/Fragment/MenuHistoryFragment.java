package tk.order_sys.orderapp.Menu.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tk.order_sys.HTTPRequest.getOrderHttpRequest;
import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.config.appConfig;
import tk.order_sys.mapi.models.ContentOrder;
import tk.order_sys.orderapp.Dialogs.OrderAppDialog;
import tk.order_sys.orderapp.MainActivity;
import tk.order_sys.orderapp.Menu.Adapter.OrdersAdapter;
import tk.order_sys.orderapp.OrderDetailActivity;
import tk.order_sys.orderapp.R;
import tk.order_sys.orderapp.XListView.view.XListView;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuHistoryFragment extends Fragment implements HTTPAsyncResponse, XListView.IXListViewListener, AdapterView.OnItemClickListener {
    public static final int ACTIVITY_CODE = 102;
    View rootView;

    private XListView listViewHistory;
    private Handler mHandler;
    private int page;
    private int pages;
    private boolean isFirstLoad = false;
    private static final int LOAD_MORE_ITEMS = 10;

    private JSONArray jsonCookieStore;
    private ArrayList<ContentOrder> listOrder = new ArrayList<ContentOrder>();
    OrdersAdapter mAdapter;


    public MenuHistoryFragment(JSONArray cookiestore) {
        this.jsonCookieStore = cookiestore;
    }

    public MenuHistoryFragment() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ((MainActivity)getActivity()).updateSelectedFragment(3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_history_fragment, container, false);
        if (appConfig.isNetworkAvailable(getActivity().getApplicationContext())) {

            page = 1;
            pages = 0;

            isFirstLoad = true;
            listViewHistory = (XListView) rootView.findViewById(R.id.xListViewHistory);
            getProducts();

            listViewHistory.setPullLoadEnable(true);
            listViewHistory.setOnItemClickListener(this);
            mHandler = new Handler();
        } else {
            OrderAppDialog.showNetworkAlertDialog(getActivity());
        }

        return rootView;
    }

    private void getProducts() {
        new getOrderHttpRequest(getActivity(), "0929028027", "hieunc18@gmail.com", LOAD_MORE_ITEMS, jsonCookieStore, this).execute((page - 1) * LOAD_MORE_ITEMS);
    }

    private void onLoad() {
        listViewHistory.stopRefresh();
        listViewHistory.stopLoadMore();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        listViewHistory.setRefreshTime(dateFormat.format(date));
    }

    @Override
    public void onHTTPAsyncResponse(JSONObject jsonObject) {
        if (jsonObject != null) {

            try {
                if (!jsonObject.isNull("count")) {
                    pages = (int) Math.ceil(jsonObject.getDouble("count") / (double) LOAD_MORE_ITEMS);
                    Log.i("paging", String.valueOf(pages));
                    if (pages == 1) {
                        listViewHistory.setPullLoadEnable(false);
                    }
                }

                if (!jsonObject.isNull("Cookies")) {
                    JSONArray jsonCookies = new JSONArray(jsonObject.get("Cookies").toString());
                    jsonCookieStore = jsonCookies;
                    ((MainActivity) getActivity()).updateFromFragment(jsonCookieStore);
                }

                if (!jsonObject.isNull("orders")) {
                    JSONArray jsonArrOrders = jsonObject.getJSONArray("orders");
                    JSONObject jsonArrOrder = null;

                    for (int i = 0; i < jsonArrOrders.length(); i++) {
                        jsonArrOrder = jsonArrOrders.getJSONObject(i);

                        listOrder.add(new ContentOrder(
                                jsonArrOrder.getString("id"),
                                jsonArrOrder.getString("name"),
                                jsonArrOrder.getString("status"),
                                jsonArrOrder.getString("created"),
                                jsonArrOrder.getString("completed")
                        ));
                    }
                    if(listOrder.size()>0){
                        if (isFirstLoad) {
                            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                            Date date = new Date();
                            listViewHistory.setRefreshTime(dateFormat.format(date));

                            mAdapter = new OrdersAdapter(getActivity(), R.layout.menu_history_row, listOrder, jsonCookieStore);

                            listViewHistory.setAdapter(mAdapter);
                            listViewHistory.setXListViewListener(this);
                            isFirstLoad = false;
                        } else {
                            mAdapter.notifyDataSetChanged();
                            onLoad();
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            }


            Log.i("HISTORY", jsonObject.toString());
        }
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page = 1;
                pages = 0;
                isFirstLoad = true;
                listOrder.clear();
                getProducts();
                listViewHistory.setAdapter(mAdapter);
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
                    listViewHistory.stopLoadMore();
                    listViewHistory.stopRefresh();
                    listViewHistory.setPullLoadEnable(false);
                }
            }
        }, 2000);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (listOrder.size() > 0) {
            try {
                Intent intent = new Intent(getActivity().getBaseContext(), OrderDetailActivity.class);

                intent.putExtra("order_id", listOrder.get(position - 1).id);
                intent.putExtra("order_name", listOrder.get(position - 1).name);
                intent.putExtra("order_stt", listOrder.get(position - 1).status);

                if (jsonCookieStore != null) {
                    intent.putExtra("jsonCookieStore", jsonCookieStore.toString());
                } else intent.putExtra("jsonCookieStore", "");

                getActivity().startActivityForResult(intent, ACTIVITY_CODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

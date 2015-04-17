package tk.order_sys.orderapp.Menu.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tk.order_sys.orderapp.R;
import tk.order_sys.orderapp.XListView.view.XListView;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuFavoriteFragment extends Fragment implements XListView.IXListViewListener {
    private XListView mListViewFavorite;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> items = new ArrayList<String>();
    private Handler mHandler;
    private int start = 0;
    private static int refreshCnt = 0;

    View rootView;
    private JSONArray jsonCookieStore;

    public MenuFavoriteFragment(JSONArray jsonCookieStore) {
        this.jsonCookieStore = jsonCookieStore;
    }

    public MenuFavoriteFragment() {
        this.jsonCookieStore = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_favorite_fragment, container, false);

        geneItems();
        mListViewFavorite = (XListView) rootView.findViewById(R.id.xListViewFavorite);
        mListViewFavorite.setPullLoadEnable(true);
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        mListViewFavorite.setRefreshTime(dateFormat.format(date));
        mAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item, items);
        mListViewFavorite.setAdapter(mAdapter);
        mListViewFavorite.setXListViewListener(this);
        mHandler = new Handler();



        return rootView;
    }

    private void geneItems() {
        for (int i = 0; i != 20; ++i) {
            items.add("refresh cnt " + (++start));
        }
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
                items.clear();
                geneItems();
                // mAdapter.notifyDataSetChanged();
                mAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item, items);
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
                geneItems();
                mAdapter.notifyDataSetChanged();
                onLoad();
            }
        }, 2000);
    }

}

package tk.order_sys.orderapp.leftmenu.menufragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

import tk.order_sys.mapi.models.ContentCart;
import tk.order_sys.mapi.models.ContentCategory;
import tk.order_sys.orderapp.R;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuOrderListFragment extends Fragment {
    View rootView;
    private JSONArray jsonCookieStore;
    Context context;
    ListView lvCart;
    ArrayList<ContentCart> listCartItem = new ArrayList<ContentCart>();

    public MenuOrderListFragment(JSONArray cookiestore){
        this.jsonCookieStore = cookiestore;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_order_list_fragment, container, false);
        if(jsonCookieStore != null)
            Toast.makeText(getActivity().getApplicationContext(), "cookie" + jsonCookieStore.toString(), Toast.LENGTH_SHORT).show();
        return rootView;
    }

}

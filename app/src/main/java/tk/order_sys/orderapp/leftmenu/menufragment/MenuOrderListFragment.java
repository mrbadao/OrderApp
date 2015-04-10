package tk.order_sys.orderapp.leftmenu.menufragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;

import tk.order_sys.orderapp.R;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuOrderListFragment extends Fragment {
    View rootView;
    private JSONArray cookiestore;

    public MenuOrderListFragment(JSONArray cookiestore){
        this.cookiestore = cookiestore;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_favorite_fragment, container, false);
        if(cookiestore != null)
            Toast.makeText(getActivity().getApplicationContext(), "cookie" + cookiestore.toString(), Toast.LENGTH_SHORT).show();
        return rootView;
    }

}

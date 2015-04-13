package tk.order_sys.orderapp.Menu.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.order_sys.orderapp.R;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuFavoriteFragment extends Fragment {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_favorite_fragment, container, false);
        return rootView;
    }

}

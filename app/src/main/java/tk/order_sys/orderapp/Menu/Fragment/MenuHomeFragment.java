package tk.order_sys.orderapp.Menu.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import tk.order_sys.config.appConfig;
import tk.order_sys.orderapp.R;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuHomeFragment extends Fragment {
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_home_fragment, container, false);
        ImageView imageViewHomeBanner = (ImageView)rootView.findViewById(R.id.imageViewHomeBanner);
        imageViewHomeBanner.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(appConfig.getRemoteWebUrl()));
                startActivity(intent);
            }
        });
        return rootView;
    }
}

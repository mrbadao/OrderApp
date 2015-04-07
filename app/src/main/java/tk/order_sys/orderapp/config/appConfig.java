package tk.order_sys.orderapp.config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by HieuNguyen on 4/3/2015.
 */

public class appConfig {
    private static final String _remoteApiUrl = "http://mapi.order-sys.tk/";
    private static final String _localApiUrl = "http://mapi.order-sys.tk/";

    public static String getApiUrl(boolean flag, String apiAction){
        return flag ? _remoteApiUrl + apiAction : _localApiUrl + apiAction;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

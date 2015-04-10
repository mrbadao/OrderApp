package tk.order_sys.mapi.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HieuNguyen on 4/7/2015.
 */
public class ContentCart implements Parcelable {
    public String id;
    public String name;
    public String price;
    public String qty;

    public ContentCart() {
        id = name = price = qty = null;
    }

    public ContentCart(String id, String name, String price, String qty) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.qty = qty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

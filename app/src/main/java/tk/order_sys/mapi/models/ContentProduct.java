package tk.order_sys.mapi.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HieuNguyen on 4/7/2015.
 */
public class ContentProduct implements Parcelable {
    public String id;
    public String name;
    public String thumbnail;
    public String description;
    public String price;
    public String saleoff_price;
    public String saleoff_percent;
    public String category_id;
    public String created;
    public String modified;

    public ContentProduct() {
        id = name = description = price = saleoff_price = saleoff_percent = category_id = created = modified = null;
    }

    public ContentProduct(String id, String name, String thumbnail, String description, String price, String saleoff_price, String saleoff_percent, String category_id, String created, String modified) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.description = description;
        this.price = price;
        this.saleoff_price = saleoff_price;
        this.saleoff_percent = saleoff_percent;
        this.category_id = category_id;
        this.created = created;
        this.modified = modified;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

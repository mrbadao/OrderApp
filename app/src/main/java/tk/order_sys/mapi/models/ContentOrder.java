package tk.order_sys.mapi.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HieuNguyen on 4/7/2015.
 */
public class ContentOrder implements Parcelable {
    public String id;
    public String name;
    public String status;
    public String created;
    public String completed;

    public ContentOrder() {
        id = name = status = completed = created = null;
    }

    public ContentOrder(String id, String name, String status, String created, String completed) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.completed = completed;
        this.created = created;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

package tk.order_sys.mapi.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HieuNguyen on 4/7/2015.
 */
public class ContentProduct implements Parcelable {
    public String id;
    public String name;
    public String description;
    public String price;
    public String category_id;
    public String created;
    public String modified;

   public ContentProduct(){
       id = name = description = price = category_id = created = modified = null;
   }

   public ContentProduct(String id, String name, String description, String price, String category_id, String created, String modified){
       this.id = id ;
       this.name = name;
       this.description = description;
       this.price = price;
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

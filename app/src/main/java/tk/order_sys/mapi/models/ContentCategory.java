package tk.order_sys.mapi.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HieuNguyen on 4/7/2015.
 */
public class ContentCategory implements Parcelable {
    public String id;
    public String name;
    public String abbr_cd;
    public String created;
    public String modified;

    public ContentCategory() {
        id = name = abbr_cd = created = modified = null;
    }

    public ContentCategory(String id, String name, String abbr_cd, String created, String modified) {
        this.id = id;
        this.name = name;
        this.abbr_cd = abbr_cd;
        this.created = created;
        this.modified = modified;
    }

    public ContentCategory(Parcel p){
        id = p.readString();
        name = p.readString();
        abbr_cd = p.readString();
        created = p.readString();
        modified = p.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(abbr_cd);
        dest.writeString(created);
        dest.writeString(modified);
    }

    public static final Parcelable.Creator<ContentCategory> CREATOR = new Creator<ContentCategory>() {

        public ContentCategory createFromParcel(Parcel source) {

            return new ContentCategory(source);
        }

        public ContentCategory[] newArray(int size) {

            return new ContentCategory[size];
        }

    };
}

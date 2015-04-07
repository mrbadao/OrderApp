package tk.order_sys.mapi.models;

/**
 * Created by HieuNguyen on 4/7/2015.
 */
public class ContentCategory {
    public String id;
    public String name;
    public String abbr_cd;
    public String created;
    public String modified;

   public ContentCategory(){
       id = name = abbr_cd = created = modified = null;
   }

   public ContentCategory(String id, String name, String abbr_cd, String created, String modified){
       this.id = id ;
       this.name = name;
       this.abbr_cd = abbr_cd;
       this.created = created;
       this.modified = modified;
   }

}

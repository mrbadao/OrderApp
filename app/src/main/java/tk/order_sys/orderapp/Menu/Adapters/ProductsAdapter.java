package tk.order_sys.orderapp.Menu.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tk.order_sys.HTTPRequest.addItemCartHttpRequest;
import tk.order_sys.Interface.AdapterResponse;
import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.mapi.models.ContentProduct;
import tk.order_sys.orderapp.R;

/**
 * Created by HieuNguyen on 4/17/2015.
 */
public class ProductsAdapter extends ArrayAdapter implements HTTPAsyncResponse {
    private Context context;
    private AdapterResponse delegate;
    private JSONArray jsonCookieStore;
    private int layoutRes;
    private ArrayList<ContentProduct> Products;

    public ProductsAdapter(Context context, int resource, ArrayList<ContentProduct> objects, JSONArray jsonCookieStore, AdapterResponse delegate) {
        super(context, resource, objects);
        this.context = context;
        this.layoutRes = resource;
        Products = objects;
        this.jsonCookieStore = jsonCookieStore;
        this.delegate = delegate;
    }

    @Override
    public int getCount() {
        return Products.size();
    }

    @Override
    public Object getItem(int position) {
        return Products.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = mInflater.inflate(R.layout.product_row, parent, false);
        } else {
            view = convertView;
        }

        TextView productName = (TextView) view.findViewById(R.id.txtView_productTitle);
        TextView productPrice = (TextView) view.findViewById(R.id.txtView_productPrice);
        ImageView productThumbnail = (ImageView) view.findViewById(R.id.productThumbnail);
        TextView productDescription = (TextView) view.findViewById(R.id.txtView_productDescription);
        Button btnAddtoCart = (Button) view.findViewById(R.id.btnAddCart);

        final ContentProduct item = (ContentProduct) getItem(position);

        Picasso.with(context.getApplicationContext())
                .load(item.thumbnail)
                .into(productThumbnail);

        productThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
        productName.setText((CharSequence) item.name);
        productPrice.setText((CharSequence) "Giá: " + String.format("%,d", Long.valueOf(item.price)) + " đồng");
        productDescription.setText((CharSequence) item.description);

        btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = v.getRootView();
                EditText editTxtQuanty = (EditText) rootView.findViewById(R.id.quanty);

                String quanty = String.valueOf(editTxtQuanty.getText());

                String CartItem = "{'cartItems':[{'id':'" + item.id + "', 'qty':'" + quanty + "'}]}";

                JSONObject post_data = null;

                try {
                    post_data = new JSONObject(CartItem);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new addItemCartHttpRequest(context, jsonCookieStore, ProductsAdapter.this).execute(post_data);
            }
        });

        return view;
    }

    @Override
    public void onHTTPAsyncResponse(JSONObject jsonObject) {
        if (jsonObject != null) {
            if (!jsonObject.isNull("Cookies")) {
                try {
                    jsonCookieStore = new JSONArray(jsonObject.get("Cookies").toString());
                    this.delegate.onAdapterResponse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

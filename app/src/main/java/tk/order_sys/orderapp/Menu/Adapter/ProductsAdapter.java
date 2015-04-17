package tk.order_sys.orderapp.Menu.Adapter;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tk.order_sys.mapi.models.ContentProduct;
import tk.order_sys.orderapp.R;

/**
 * Created by HieuNguyen on 4/17/2015.
 */
public class ProductsAdapter extends ArrayAdapter {
    Context context;
    int layoutRes;
    ArrayList<ContentProduct> Products;

    public ProductsAdapter(Context context, int resource, ArrayList<ContentProduct> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutRes = resource;
        Products = objects;
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

//                    new httpRequestAddCartItems(getApplicationContext()).execute(post_data);
            }
        });

        return view;
    }
}

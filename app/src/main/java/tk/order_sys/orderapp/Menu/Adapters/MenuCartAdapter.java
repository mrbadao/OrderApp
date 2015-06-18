package tk.order_sys.orderapp.Menu.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tk.order_sys.mapi.models.ContentCart;
import tk.order_sys.orderapp.R;

/**
 * Created by HieuNguyen on 4/13/2015.
 */
public class MenuCartAdapter extends ArrayAdapter {
    Context context;
    int layoutRes;
    ArrayList<ContentCart> cartItems;

    public MenuCartAdapter(Context context, int resource, ArrayList<ContentCart> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutRes = resource;
        cartItems = objects;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = mInflater.inflate(layoutRes, parent, false);
        } else {
            view = convertView;
        }

        TextView itemTitle = (TextView) view.findViewById(R.id.txtView_productTitle);
        TextView itemPrice = (TextView) view.findViewById(R.id.txtView_productPrice);
        TextView itemTotal = (TextView) view.findViewById(R.id.txtView_product_total);
        EditText itemQuanty = (EditText) view.findViewById(R.id.txtEdit_productQuanty);

        ContentCart cartItem = (ContentCart) getItem(position);

        itemTitle.setText((CharSequence) cartItem.name);
        itemPrice.setText((CharSequence) String.format("%,d", Long.valueOf(cartItem.price)) + " đồng");
        itemTotal.setText((CharSequence) String.format("%,d", Long.valueOf(cartItem.price) * Long.valueOf(cartItem.qty)) + " đồng");            itemQuanty.setText((CharSequence) cartItem.qty);

        Button btnEditItemCart = (Button) view.findViewById(R.id.btnEditItemCart);

        btnEditItemCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MENU","Order list btn click");
            }
        });

        return view;
    }
}
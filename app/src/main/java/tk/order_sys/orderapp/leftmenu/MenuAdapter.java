/**
 * Created by HieuNguyen on 4/2/2015.
 */

package tk.order_sys.orderapp.leftmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import tk.order_sys.orderapp.R;

class MenuAdapter extends ArrayAdapter {
    Context context;
    int layoutRes;

    String[] _menu;
    int[] _images = {R.drawable.ic_menu_home, R.drawable.ic_action_share, R.drawable.ic_action_favorite, R.drawable.ic_menu_recent_history, R.drawable.shopping_cart};


    public MenuAdapter(Context context, int resource, Object[] objects) {
        super(context, resource, objects);
        _menu = context.getResources().getStringArray(R.array.array_menu);
        this.context = context;
        this.layoutRes = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(layoutRes, parent, false);
        }


        TextView _rowTitle = (TextView) view.findViewById(R.id.textView1);
        ImageView _rowImage = (ImageView) view.findViewById(R.id.imageView1);

        _rowTitle.setText(_menu[position]);
        _rowImage.setImageResource(_images[position]);

        return view;
    }
}

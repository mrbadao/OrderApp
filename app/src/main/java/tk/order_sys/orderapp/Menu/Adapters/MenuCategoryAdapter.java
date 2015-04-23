package tk.order_sys.orderapp.Menu.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import tk.order_sys.mapi.models.ContentCategory;

/**
 * Created by mrbadao on 13/04/2015.
 */
public class MenuCategoryAdapter extends ArrayAdapter {
    Context context;
    int layoutRes;
    ArrayList<ContentCategory> Categories;

    public MenuCategoryAdapter(Context context, int resource, ArrayList<ContentCategory> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutRes = resource;
        Categories = objects;
    }

    @Override
    public int getCount() {
        return Categories.size();
    }

    @Override
    public Object getItem(int position) {
        return Categories.get(position);
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

        TextView categoryListItem = (TextView) view;

        categoryListItem.setText((CharSequence) ((ContentCategory) getItem(position)).name);

        return view;
    }
}

package tk.order_sys.orderapp.Menu.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;

import tk.order_sys.mapi.models.ContentOrder;
import tk.order_sys.orderapp.R;

/**
 * Created by HieuNguyen on 4/17/2015.
 */
public class OrdersAdapter extends ArrayAdapter {
    private Context context;
//    private AdapterResponse delegate;
    private JSONArray jsonCookieStore;
    private int layoutRes;
    private ArrayList<ContentOrder> Orders;

    public OrdersAdapter(Context context, int resource, ArrayList<ContentOrder> objects, JSONArray jsonCookieStore) {
        super(context, resource, objects);
        this.context = context;
        this.layoutRes = resource;
        Orders = objects;
        this.jsonCookieStore = jsonCookieStore;
//        this.delegate = delegate;
    }

    @Override
    public int getCount() {
        return Orders.size();
    }

    @Override
    public Object getItem(int position) {
        return Orders.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = mInflater.inflate(layoutRes, parent, false);
        } else {
            view = convertView;
        }

        ContentOrder item = (ContentOrder) getItem(position);
        TextView txtOrderName = (TextView) view.findViewById(R.id.txtHistoryRow);
        TextView txtOdrderCreated = (TextView) view.findViewById(R.id.txtCreated);
        TextView txtOrderCompleted = (TextView) view.findViewById(R.id.txtCompleted);
        ImageView imgVOrderStatus = (ImageView) view.findViewById(R.id.imgVHistoryStt);

        txtOrderName.setText((CharSequence) item.name);
        txtOdrderCreated.setText((CharSequence) "Đặt lúc: " + item.created);

        switch (item.status){
            case "2":
                txtOrderCompleted.setText((CharSequence) item.completed);
                imgVOrderStatus.setBackgroundResource(R.color.fragment_history_stt_complete);
                break;
            case "1":
                imgVOrderStatus.setBackgroundResource(R.color.fragment_history_stt_in_process);
                break;
            case "0":
                imgVOrderStatus.setBackgroundResource(R.color.fragment_history_stt_new);
                break;
        }


        return view;
    }
}

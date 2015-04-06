package tk.order_sys.orderapp.leftmenu.menufragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import tk.order_sys.orderapp.ProductActivity;
import tk.order_sys.orderapp.R;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuCategoryFragment extends Fragment {
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_category_fragment,container,false);

        String[] lsData = {"Gà gán", "Fasfood", "Nước giải khát", "Món Nhật", "Món Hàn", "Món Ý", "Đặt sản ba miền","Gà gán", "Fasfood", "Nước giải khát", "Món Nhật", "Món Hàn", "Món Ý", "Đặt sản ba miền","Gà gán", "Fasfood", "Nước giải khát", "Món Nhật", "Món Hàn", "Món Ý", "Đặt sản ba miền"};

        ListView lsCategory  = (ListView) rootView.findViewById(R.id.lvCategory);
        lsCategory.setAdapter(new ArrayAdapter<String>(rootView.getContext(),android.R.layout.simple_list_item_1,lsData));
        lsCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent = new Intent(getActivity().getApplicationContext(), ProductActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}

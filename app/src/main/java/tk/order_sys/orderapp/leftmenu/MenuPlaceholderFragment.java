package tk.order_sys.orderapp.leftmenu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.order_sys.orderapp.MainActivity;
import tk.order_sys.orderapp.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MenuPlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MenuPlaceholderFragment newInstance(int sectionNumber) {
        MenuPlaceholderFragment fragment = new MenuPlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MenuPlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 0:
                rootView = inflater.inflate(R.layout.menu_home_fragment, container, false);
                break;
            case 1:
                rootView = inflater.inflate(R.layout.menu_category_fragment, container, false);
                break;
            case 2:
                rootView = inflater.inflate(R.layout.menu_favorite_fragment, container, false);
                break;

            default:
                rootView = inflater.inflate(R.layout.menu_history_fragment, container, false);
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

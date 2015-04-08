package tk.order_sys.orderapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import tk.order_sys.orderapp.leftmenu.NavigationDrawerFragment;
import tk.order_sys.orderapp.leftmenu.menufragment.MenuCategoryFragment;
import tk.order_sys.orderapp.leftmenu.menufragment.MenuFavoriteFragment;
import tk.order_sys.orderapp.leftmenu.menufragment.MenuHistoryFragment;
import tk.order_sys.orderapp.leftmenu.menufragment.MenuHomeFragment;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    String[] MainMenu;
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    //Fragment managing the behaviors, interactions and presentation of the navigation drawer.
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle args = new Bundle();


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();
        // Get menu forom resources
        MainMenu = getResources().getStringArray(R.array.array_menu);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

//        HttpGetter get = new HttpGetter();
//        get.execute("http://mapi.order-sys.tk/category/searchs");
//


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment menuFragment = null;
        switch (position) {
            case 1:
                menuFragment = new MenuCategoryFragment();
                mTitle = this.MainMenu[1];
                break;
            case 2:
                menuFragment = new MenuFavoriteFragment();
                mTitle = this.MainMenu[2];
                break;
            case 3:
                menuFragment = new MenuHistoryFragment();
                mTitle = this.MainMenu[3];
                break;
            default:
                menuFragment = new MenuHomeFragment();
                break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, menuFragment)
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

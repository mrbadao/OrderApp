package tk.order_sys.orderapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;

import tk.order_sys.orderapp.leftmenu.NavigationDrawerFragment;
import tk.order_sys.orderapp.leftmenu.menufragment.MenuCategoryFragment;
import tk.order_sys.orderapp.leftmenu.menufragment.MenuFavoriteFragment;
import tk.order_sys.orderapp.leftmenu.menufragment.MenuHistoryFragment;
import tk.order_sys.orderapp.leftmenu.menufragment.MenuHomeFragment;
import tk.order_sys.orderapp.leftmenu.menufragment.MenuOrderListFragment;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    String[] MainMenu;
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private JSONArray jsonCookieStore;

    //Fragment managing the behaviors, interactions and presentation of the navigation drawer.
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private int mCurrentMenuFragmentSection = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentMenuFragmentSection = savedInstanceState.getInt("123");
        }


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();
        // Get menu forom resources
        MainMenu = getResources().getStringArray(R.array.array_menu);

        jsonCookieStore = null;

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mNavigationDrawerFragment.selectItem(mCurrentMenuFragmentSection);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == MenuCategoryFragment.ACTIVITY_CODE) {
            if (data.hasExtra("mMenuFragmentSection")) {
                mCurrentMenuFragmentSection = data.getIntExtra("mMenuFragmentSection", 0);
            }

            if (data.hasExtra("mCookieStore")) {
                try {
                    jsonCookieStore = new JSONArray(data.getStringExtra("mCookieStore"));
                    Log.i("CURRCOOKIE", "main" + jsonCookieStore.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    jsonCookieStore = null;
                }
            }
        }

    }

    @Override
    protected void onPostResume() {
        mNavigationDrawerFragment.selectItem(mCurrentMenuFragmentSection);
        mTitle = MainMenu[mCurrentMenuFragmentSection];
        restoreActionBar();
        super.onPostResume();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment menuFragment = null;
        switch (position) {
            case 1:
                menuFragment = new MenuCategoryFragment(jsonCookieStore);
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
            case 4:
                menuFragment = new MenuOrderListFragment(jsonCookieStore);
                mTitle = this.MainMenu[4];
                break;
            default:
                menuFragment = new MenuHomeFragment();
                mTitle = "Trang chủ";
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

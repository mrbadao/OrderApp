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

import tk.order_sys.orderapp.Menu.Fragment.MenuCategoryFragment;
import tk.order_sys.orderapp.Menu.Fragment.MenuFavoriteFragment;
import tk.order_sys.orderapp.Menu.Fragment.MenuHistoryFragment;
import tk.order_sys.orderapp.Menu.Fragment.MenuHomeFragment;
import tk.order_sys.orderapp.Menu.Fragment.MenuOrderListFragment;
import tk.order_sys.orderapp.Menu.NavigationDrawerFragment;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    String[] MainMenu;
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String STATE_COOKIE = "Cart_cookie_store";
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
        jsonCookieStore = null;

        if (savedInstanceState != null) {
            mCurrentMenuFragmentSection = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            try {
                String mSaveCookieStore = savedInstanceState.getString(STATE_COOKIE).toString();
                if (!mSaveCookieStore.isEmpty()) jsonCookieStore = new JSONArray(mSaveCookieStore);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();
        // Get menu forom resources
        MainMenu = getResources().getStringArray(R.array.array_menu);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mNavigationDrawerFragment.selectItem(mCurrentMenuFragmentSection);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentMenuFragmentSection);
        if (jsonCookieStore != null) {
            outState.putString(STATE_COOKIE, jsonCookieStore.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && (requestCode == MenuCategoryFragment.ACTIVITY_CODE || requestCode == MenuHistoryFragment.ACTIVITY_CODE)) {
            if (data.hasExtra("mMenuFragmentSection")) {
                mCurrentMenuFragmentSection = data.getIntExtra("mMenuFragmentSection", 0);
            }

            if (data.hasExtra("mCookieStore")) {
                String callBackCookieStore = data.getStringExtra("mCookieStore");
                if (!callBackCookieStore.isEmpty()) {
                    try {
                        jsonCookieStore = new JSONArray(callBackCookieStore);
                        Log.i("CURRCOOKIE", "main" + jsonCookieStore.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        jsonCookieStore = null;
                    }
                }
            }
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mNavigationDrawerFragment.selectItem(mCurrentMenuFragmentSection);
        mTitle = MainMenu[mCurrentMenuFragmentSection];
        restoreActionBar();
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
                menuFragment = new MenuFavoriteFragment(jsonCookieStore);
                mTitle = this.MainMenu[2];
                break;
            case 3:
                menuFragment = new MenuHistoryFragment(jsonCookieStore);
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
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateFromFragment(JSONArray jsonCookieStore){
        this.jsonCookieStore = jsonCookieStore;
    }
}

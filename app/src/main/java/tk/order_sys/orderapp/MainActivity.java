package tk.order_sys.orderapp;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import tk.order_sys.orderapp.leftmenu.MenuPlaceholderFragment;
import tk.order_sys.orderapp.leftmenu.NavigationDrawerFragment;

import static org.apache.http.protocol.HTTP.*;


public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    String[] MainMenu;

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

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Get menu forom resources
        MainMenu = getResources().getStringArray(R.array.array_menu);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        try {
            JSONObject jo = new JSONObject();
            jo.put("secret_key", "6dn9T3t2760yypWAhdhURmz7oZQrhdXjqRoTorybjWU=");

            URL url = new URL(appConfig.getApiUrl(true,"category/search"));

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url.toURI());

            // Prepare JSON to send by setting the entity
            httpPost.setEntity(new StringEntity(jo.toString(), "UTF-8"));

// Set up the header types needed to properly transfer JSON
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept-Encoding", "application/json");
            httpPost.setHeader("Accept-Language", "en-US");

// Execute POST
            HttpResponse response = httpClient.execute(httpPost);
            String temp = EntityUtils.toString(response.getEntity());
            Log.i("tag", temp);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.container, MenuPlaceholderFragment.newInstance(position))
                .commit();
    }

    public void onSectionAttached(int number) {
        for (int i = 0; i < MainMenu.length; i++){
            if(number == i) {
                mTitle = this.MainMenu[i];
                break;
            }
        }
       /* switch (number){
            case 0;
                this.MainMenu[0];
                break;
            case 1;
                this.MainMenu[1];
                break;
            case 2;
                this.MainMenu[2];
                break;
            case 3;
                this.MainMenu[3];
                break;
        }*/
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

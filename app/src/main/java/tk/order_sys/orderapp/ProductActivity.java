package tk.order_sys.orderapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ProductActivity extends ActionBarActivity {
    private String cat_name = "";
    private String cat_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Bundle catInfo = getIntent().getExtras();

        cat_id = (String) catInfo.get("cat_id");
        cat_name = (String) catInfo.get("cat_name");

        setTitle(cat_name);
    }
}

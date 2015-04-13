package tk.order_sys.orderapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import tk.order_sys.mapi.API;
import tk.order_sys.mapi.models.ContentProduct;
import tk.order_sys.config.appConfig;


public class ProductActivity extends ActionBarActivity {
    private String cat_name = "";
    private String cat_id = "";

    private JSONArray jsonCookieStore;

    ListView lvProducts;
    private static final String PRODUCT_CATEGORY_ID_TAG = "category_id";

    HashMap<String, String> hashCartItems = new HashMap<String, String>();

    ArrayList<ContentProduct> listProducts = new ArrayList<ContentProduct>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonCookieStore = null;
        setContentView(R.layout.activity_product);


        Bundle catInfo = getIntent().getExtras();

        cat_id = (String) catInfo.get("cat_id");
        cat_name = (String) catInfo.get("cat_name");


        if ((String) catInfo.get("jsonCookieStore") != null) {
            try {
                jsonCookieStore = new JSONArray(catInfo.get("jsonCookieStore").toString());
                Log.i("CURRCOOKIE", "product" + jsonCookieStore.toString());
            } catch (JSONException e) {
                Log.i("CURRCOOKIE", "product");
                e.printStackTrace();
            }
        }

        setTitle(cat_name);
        lvProducts = (ListView) findViewById(R.id.lvProducts);

        new HTTPRequest().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_cart:
                Intent data = new Intent();
                data.putExtra("mMenuFragmentSection", 4);

                if (jsonCookieStore != null)
                    data.putExtra("mCookieStore", jsonCookieStore.toString());

                setResult(Activity.RESULT_OK, data);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private class HTTPRequest extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            pdia = new ProgressDialog(ProductActivity.this);
            pdia.setMessage("Loading...");
            pdia.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject post_params = null;
            if (cat_id != "") {
                try {
                    post_params = new JSONObject();
                    post_params.put(PRODUCT_CATEGORY_ID_TAG, cat_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return API.getProducts(post_params, jsonCookieStore);
        }

        protected void onPostExecute(JSONObject jsonObject) {
            JSONArray jsonArrProducts = null;
            try {

                jsonArrProducts = jsonObject.getJSONArray("products");
                JSONObject jsonArrProduct = null;
                for (int i = 0; i < jsonArrProducts.length(); i++) {
                    jsonArrProduct = jsonArrProducts.getJSONObject(i);

                    listProducts.add(new ContentProduct(
                            jsonArrProduct.getString("id"),
                            jsonArrProduct.getString("name"),
                            jsonArrProduct.getString("thumbnail"),
                            jsonArrProduct.getString("description"),
                            jsonArrProduct.getString("price"),
                            jsonArrProduct.getString("category_id"),
                            jsonArrProduct.getString("created"),
                            jsonArrProduct.getString("modified")
                    ));
                }

                lvProducts.setAdapter(new ProductsAdapter(ProductActivity.this, R.layout.product_row, listProducts));
                lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getApplicationContext(), listProducts.get(position).name, Toast.LENGTH_SHORT).show();
                    }
                });

                Log.i("CURRCOOKIE", "product" + jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(jsonObject.get("Cookies").toString());

                jsonCookieStore = jsonArray;

                Log.i("CURRCOOKIE", "product httpres " + jsonCookieStore.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pdia.dismiss();
        }
    }

    private class ProductsAdapter extends ArrayAdapter {
        Context context;
        int layoutRes;
        ArrayList<ContentProduct> Products;

        public ProductsAdapter(Context context, int resource, ArrayList<ContentProduct> objects) {
            super(context, resource, objects);
            this.context = context;
            this.layoutRes = resource;
            Products = objects;
        }

        @Override
        public int getCount() {
            return Products.size();
        }

        @Override
        public Object getItem(int position) {
            return Products.get(position);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                view = mInflater.inflate(R.layout.product_row, parent, false);
            } else {
                view = convertView;
            }

            TextView productName = (TextView) view.findViewById(R.id.txtView_productTitle);
            TextView productPrice = (TextView) view.findViewById(R.id.txtView_productPrice);
            ImageView productThumbnail = (ImageView) view.findViewById(R.id.productThumbnail);
            TextView productDescription = (TextView) view.findViewById(R.id.txtView_productDescription);
            Button btnAddtoCart = (Button) view.findViewById(R.id.btnAddCart);

            final ContentProduct item = (ContentProduct) getItem(position);

            Picasso.with(getApplicationContext())
                    .load(item.thumbnail)
                    .into(productThumbnail);

            productThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
            productName.setText((CharSequence) item.name);
            productPrice.setText((CharSequence) "Giá: " + String.format("%,d", Long.valueOf(item.price)) + " đồng");
            productDescription.setText((CharSequence) item.description);

            btnAddtoCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View rootView = v.getRootView();
                    EditText editTxtQuanty = (EditText) rootView.findViewById(R.id.quanty);

                    String quanty = String.valueOf(editTxtQuanty.getText());

                    String CartItem = "{'cartItems':[{'id':'" + item.id + "', 'qty':'" + quanty + "'}]}";

                    JSONObject post_data = null;

                    try {
                        post_data = new JSONObject(CartItem);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new httpRequestAddCartItems(getApplicationContext()).execute(post_data);
                }
            });

            return view;
        }


    }

    private class httpRequestAddCartItems extends AsyncTask<JSONObject, Void, JSONObject> {
        private Context context;
        private ProgressDialog pdia;

        public httpRequestAddCartItems(Context context) {
            super();
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            pdia = new ProgressDialog(ProductActivity.this);
            pdia.setMessage("Loading...");
            pdia.show();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            if (appConfig.isNetworkAvailable(context)) {
                return API.addCartItem(params[0], jsonCookieStore);
            } else {
                Toast.makeText(context, R.string.error_no_connection, Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject != null) {
                try {
                    JSONArray jsonArray = new JSONArray(jsonObject.get("Cookies").toString());

                    jsonCookieStore = jsonArray;

                    Log.i("CURRCOOKIE", "httpRequestAddCartItems" + jsonCookieStore.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            pdia.dismiss();
        }
    }

}

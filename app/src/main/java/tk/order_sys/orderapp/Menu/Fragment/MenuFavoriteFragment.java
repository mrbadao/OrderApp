package tk.order_sys.orderapp.Menu.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tk.order_sys.HTTPRequest.getProductHttpRequest;
import tk.order_sys.Interface.HTTPAsyncResponse;
import tk.order_sys.mapi.models.ContentProduct;
import tk.order_sys.orderapp.Menu.Adapter.ProductsAdapter;
import tk.order_sys.orderapp.R;
import tk.order_sys.orderapp.XListView.view.XListView;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class MenuFavoriteFragment extends Fragment implements XListView.IXListViewListener, HTTPAsyncResponse {
    private XListView mListViewFavorite;
    private ProductsAdapter mAdapter;

//    private ArrayList<String> items = new ArrayList<String>();
    private Handler mHandler;
    private int start = 0;
    private boolean isFirstLoad = false;
    private static int refreshCnt = 0;

    View rootView;
    ArrayList<ContentProduct> listProducts = new ArrayList<ContentProduct>();
    private JSONArray jsonCookieStore;

    public MenuFavoriteFragment(JSONArray jsonCookieStore) {
        this.jsonCookieStore = jsonCookieStore;
    }

    public MenuFavoriteFragment() {
        this.jsonCookieStore = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.menu_favorite_fragment, container, false);
        isFirstLoad = true;
        getProducts();
        mListViewFavorite = (XListView) rootView.findViewById(R.id.xListViewFavorite);
        mListViewFavorite.setPullLoadEnable(true);


        mHandler = new Handler();



        return rootView;
    }

    private void getProducts() {
        new getProductHttpRequest(getActivity(), "12", jsonCookieStore, this).execute();
    }

    private void onLoad() {
        mListViewFavorite.stopRefresh();
        mListViewFavorite.stopLoadMore();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();
        mListViewFavorite.setRefreshTime(dateFormat.format(date));
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start = ++refreshCnt;
                isFirstLoad = true;
                listProducts.clear();
                getProducts();
                // mAdapter.notifyDataSetChanged();
                mAdapter = new ProductsAdapter(getActivity(), R.layout.product_row, listProducts);
                mListViewFavorite.setAdapter(mAdapter);
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getProducts();
            }
        }, 2000);
    }

    @Override
    public void onHTTPAsyncResponse(JSONObject jsonObject) {
        try {
            if (!jsonObject.isNull("Cookies")) {
                jsonCookieStore = new JSONArray(jsonObject.get("Cookies").toString());
            }

            if (!jsonObject.isNull("products")) {
                JSONArray jsonArrProducts = jsonObject.getJSONArray("products");
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
                if(isFirstLoad){
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                    Date date = new Date();
                    mListViewFavorite.setRefreshTime(dateFormat.format(date));

                    mAdapter = new ProductsAdapter(getActivity(), R.layout.product_row, listProducts);

                    mListViewFavorite.setAdapter(mAdapter);
                    mListViewFavorite.setXListViewListener(this);
                    isFirstLoad = false;
                }else {
                    mAdapter.notifyDataSetChanged();
                    onLoad();
                }

//                lvProducts.setAdapter(new ProductsAdapter(ProductActivity.this, R.layout.product_row, listProducts));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    private class ProductsAdapter extends ArrayAdapter {
//        Context context;
//        int layoutRes;
//        ArrayList<ContentProduct> Products;
//
//        public ProductsAdapter(Context context, int resource, ArrayList<ContentProduct> objects) {
//            super(context, resource, objects);
//            this.context = context;
//            this.layoutRes = resource;
//            Products = objects;
//        }
//
//        @Override
//        public int getCount() {
//            return Products.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return Products.get(position);
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            View view = null;
//            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            if (convertView == null) {
//                view = mInflater.inflate(R.layout.product_row, parent, false);
//            } else {
//                view = convertView;
//            }
//
//            TextView productName = (TextView) view.findViewById(R.id.txtView_productTitle);
//            TextView productPrice = (TextView) view.findViewById(R.id.txtView_productPrice);
//            ImageView productThumbnail = (ImageView) view.findViewById(R.id.productThumbnail);
//            TextView productDescription = (TextView) view.findViewById(R.id.txtView_productDescription);
//            Button btnAddtoCart = (Button) view.findViewById(R.id.btnAddCart);
//
//            final ContentProduct item = (ContentProduct) getItem(position);
//
//            Picasso.with(getActivity().getApplicationContext())
//                    .load(item.thumbnail)
//                    .into(productThumbnail);
//
//            productThumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            productName.setText((CharSequence) item.name);
//            productPrice.setText((CharSequence) "Giá: " + String.format("%,d", Long.valueOf(item.price)) + " đồng");
//            productDescription.setText((CharSequence) item.description);
//
//            btnAddtoCart.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    View rootView = v.getRootView();
//                    EditText editTxtQuanty = (EditText) rootView.findViewById(R.id.quanty);
//
//                    String quanty = String.valueOf(editTxtQuanty.getText());
//
//                    String CartItem = "{'cartItems':[{'id':'" + item.id + "', 'qty':'" + quanty + "'}]}";
//
//                    JSONObject post_data = null;
//
//                    try {
//                        post_data = new JSONObject(CartItem);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
////                    new httpRequestAddCartItems(getApplicationContext()).execute(post_data);
//                }
//            });
//
//            return view;
//        }
//    }
}

package tk.order_sys.mapi;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import tk.order_sys.orderapp.config.appConfig;

/**
 * Created by HieuNguyen on 4/6/2015.
 */
public class API {
    private static InputStream is = null;
    private static JSONObject jObj = null;
    private static String json = "";

    static final String SECRET_KEY = "6dn9T3t2760yypWAhdhURmz7oZQrhdXjqRoTorybjWU=";

    static final String API_CATEGORIES_SEARCH = "category/search";
    static final String API_PRODUCTS_SEARCH = "product/search";
    static final String API_CART_ADD_ITEM = "cart/add";
    // constructor

    public API() {
    }

    private static JSONObject getJSON(String address, JSONObject post_data) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(address);

        try {
            if (post_data == null){
                post_data = new JSONObject();
            }

            post_data.put("secret_key", SECRET_KEY);

            StringEntity se = new StringEntity(post_data.toString());
            httpPost.setEntity(se);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                return null;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = null;
        try{
            jsonObject = new JSONObject(builder.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject getCategories() {
        return getJSON(appConfig.getApiUrl(true) + API_CATEGORIES_SEARCH, null);
    }

    public static JSONObject getProducts(JSONObject params) {
        return getJSON(appConfig.getApiUrl(true) + API_PRODUCTS_SEARCH, params);
    }

    public static JSONObject addCartItem(JSONObject params) {
        return getJSON(appConfig.getApiUrl(true) + API_CART_ADD_ITEM, params);
    }

}



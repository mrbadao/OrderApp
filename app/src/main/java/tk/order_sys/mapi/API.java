package tk.order_sys.mapi;

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

    static final String CATEGORIES_API_SEARCH = "category/search";
    static final String PRODUCTS_API_SEARCH = "product/search";
    static final String SECRET_KEY = "6dn9T3t2760yypWAhdhURmz7oZQrhdXjqRoTorybjWU=";
    // constructor

    public API() {
    }

    private static String getJSON(String address, HashMap<String, String> post_data) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(address);

        try {
            JSONObject jsonPostData = new JSONObject();
            jsonPostData.put("secret_key", SECRET_KEY);

            if (post_data != null) {
                for (String s : post_data.keySet()) {
                    jsonPostData.put(s, (String) post_data.get(s));
                }
            }

            StringEntity se = new StringEntity(jsonPostData.toString());
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
        return builder.toString();
    }

    public static String getCategories() {
        return getJSON(appConfig.getApiUrl(true) + CATEGORIES_API_SEARCH, null);
    }

    public static String getProducts(HashMap<String, String> params) {
        return getJSON(appConfig.getApiUrl(true) + PRODUCTS_API_SEARCH, params);
    }

}



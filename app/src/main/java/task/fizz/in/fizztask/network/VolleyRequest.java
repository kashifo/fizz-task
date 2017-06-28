package task.fizz.in.fizztask.network;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

import task.fizz.in.fizztask.AppSingleton;

import static android.content.ContentValues.TAG;


/**
 * Created by Kashif on 12/12/2015.
 */
public class VolleyRequest {

    String tag = "Volley-";

    public VolleyRequest(String tag) {
        this.tag += tag;
    }

    public void postRequest(final NetworkCallback callback, String URL, final Map<String, String> params) {

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(tag, "onResponse: " + response.toString());
                callback.onAPIResponse(false, response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, "onErrorResponse : " + error.getMessage());
                callback.onAPIResponse(true, error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                return params;
            }

        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // Adding request to request queue
        AppSingleton.getInstance().addToRequestQueue(strReq, tag);

    }//postRequest


    public void getRequest(final NetworkCallback callback, String URL, final Map<String, String> params) {

        StringRequest strReq = new StringRequest(Request.Method.GET,
                createGetUrl(URL, params), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(tag, "onResponse: ");
                callback.onAPIResponse(false, response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, "onErrorResponse : " + error.getMessage());
                callback.onAPIResponse(true, error.getMessage());
            }
        });

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        // Adding request to request queue
        AppSingleton.getInstance().addToRequestQueue(strReq, tag);

    }//getRequest

    private String createGetUrl(String URL, Map<String, String> params){

        if( params!=null && !params.isEmpty() ){

            for( Map.Entry<String, String> entry : params.entrySet() ){

                URL += "?" + entry.getKey() +"="+ entry.getValue();

            }

            Log.i( TAG, "createGetUrl="+URL );
        }

        return URL;
    }

}

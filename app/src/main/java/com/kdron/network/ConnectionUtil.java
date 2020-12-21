package com.kdron.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConnectionUtil {

    public static final String PROTOCALL = "https://";
    public static String BASE_URL = PROTOCALL + "api.themoviedb.org/3";
    public static String API = "09de4b6e14ed091cb0472e5afaaf3caf";
    public static String MOVIE_LIST = BASE_URL+"/movie/now_playing";
    public static String IMG_URL = "https://image.tmdb.org/t/p/w500/";
    public static String MOVIE_DETAILS = BASE_URL+"/movie";


    public static StringRequest callVolleyGetRequest(final String urlString, Context context, final VollyResponse vollyResponse) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlString, new Response.Listener<String>() {
            @Override
            public void onResponse(String responce) {
                vollyResponse.onReceive(responce);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                vollyResponse.onError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return new HashMap<>();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                return hashMap;
            }
        };

        Volley.newRequestQueue(context).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000
                , 5
                , DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return stringRequest;
    }


}

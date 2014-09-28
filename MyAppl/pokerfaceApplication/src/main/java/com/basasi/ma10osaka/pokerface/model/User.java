package com.basasi.ma10osaka.pokerface.model;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.basasi.ma10osaka.pokerface.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User {
    private static final String TAG = User.class.getSimpleName();
    private final User self = this;

    private static String deviceId;
    private static String nickName;
    private Context mContext;

    public User(Context context){
        mContext = context;
    }

    public void setDeviceId() {
        User.deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void setNickName(String nickName) {
        User.nickName = nickName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getNickName() {
        return nickName;
    }

    public void login(){
        String url = mContext.getString(R.string.login_id_name_api);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, mLoginListener, mEListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                // Add Http Header
                Map<String, String> newHeaders = new HashMap<String, String>();
                newHeaders.putAll(headers);
                newHeaders.put("Content-Type","application/json");
                newHeaders.put("Accept", "application/json,text/html");
                newHeaders.put("device_id", deviceId);
                return newHeaders;
            }
        };

        requestQueue.add(request);
        requestQueue.start();
    }

    public void register(){
        String url = mContext.getString(R.string.login_id_name_api);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null, mLoginListener, mEListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                // Add Http Header
                Map<String, String> newHeaders = new HashMap<String, String>();
                newHeaders.putAll(headers);
                newHeaders.put("Content-Type","application/json");
                newHeaders.put("Accept", "application/json,text/html");
                newHeaders.put("device_id", deviceId);
                newHeaders.put("nickname", nickName);
                return newHeaders;
            }
        };

        requestQueue.add(request);
        requestQueue.start();
    }

    private Response.Listener<JSONObject> mLoginListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try{
                Log.d(TAG,JSONObject.numberToString(0));
            }catch(JSONException e){
                Log.d(TAG,e.getMessage());
            }
        }
    };

    private Response.Listener<JSONObject> mRegisterListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try{
                Log.d(TAG,JSONObject.numberToString(0));
            }catch(JSONException e){
                Log.d(TAG,e.getMessage());
            }
        }
    };

    private Response.ErrorListener mEListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.d(TAG,volleyError.toString());
        }
    };
}
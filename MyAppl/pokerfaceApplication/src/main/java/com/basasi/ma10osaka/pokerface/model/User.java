package com.basasi.ma10osaka.pokerface.model;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.Settings;
import android.util.Log;
import android.widget.EditText;

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
    private static int userId;
    private static String nickName;
    private Context mContext;

    private static ProgressDialog waitDialog;

    public User(Context context){
        mContext = context;
    }

    public void setDeviceId() {
        User.deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void setNickName(String nickName) {
        User.nickName = nickName;
    }

    public void setUserId(int id){
        User.userId = id;
    }

    public static String getDeviceId() {
        return deviceId;
    }

    public static String getNickName() {
        return nickName;
    }

    public static int getUserId() {
        return userId;
    }

    public void login(){
        waitDialog = new ProgressDialog(mContext);
        waitDialog.setMessage(mContext.getString(R.string.doing_login));
        waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitDialog.show();

        JSONObject user = new JSONObject();
        JSONObject dId = new JSONObject();
        try{
            dId.put("device_id", User.deviceId);
            user.put("user", dId);
            Log.d(TAG,user.toString());
        }catch(JSONException e){
            Log.d(TAG,e.toString());
        }

        String url = mContext.getString(R.string.login_id_name_api);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, user, mLoginListener, mEListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                // Add Http Header
                Map<String, String> newHeaders = new HashMap<String, String>();
                newHeaders.putAll(headers);
                newHeaders.put("Content-Type","application/json");
                newHeaders.put("Accept", "application/json,text/html");
                return newHeaders;
            }
        };

        requestQueue.add(request);
        requestQueue.start();
    }

    public void register(){
        waitDialog = new ProgressDialog(mContext);
        waitDialog.setMessage(mContext.getString(R.string.doing_login));
        waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitDialog.show();

        JSONObject user = new JSONObject();
        JSONObject info = new JSONObject();
        try{
            info.put("device_id", User.deviceId);
            info.put("nickname", User.nickName);
            user.put("user", info);
            Log.d(TAG,user.toString());
        }catch(JSONException e){
            Log.d(TAG,e.toString());
        }

        String url = mContext.getString(R.string.enter_name_api) + "/" + Integer.toString(userId) + ".json";
        Log.d(TAG, url);
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, user, mRegisterListener, mEListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                // Add Http Header
                Map<String, String> newHeaders = new HashMap<String, String>();
                newHeaders.putAll(headers);
                newHeaders.put("Content-Type","application/json");
                newHeaders.put("Accept", "application/json,text/html");
                //newHeaders.put("device_id", deviceId);
                //newHeaders.put("nickname", nickName);
                return newHeaders;
            }
        };

        requestQueue.add(request);
        requestQueue.start();
    }

    private void inputNickNameWithDialog(){
        final EditText editText = new EditText(mContext);
        new AlertDialog.Builder(mContext).setTitle("ニックネームを入力してください")
                .setView(editText).setPositiveButton("決定",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setNickName(editText.getText().toString());
                register();
            }
        }).show();
    }

    private Response.Listener<JSONObject> mLoginListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            Log.d(TAG,jsonObject.toString());
            waitDialog.dismiss();
            try{
                userId = jsonObject.getInt("id");
                Log.d(TAG,jsonObject.get("nickname").toString());
                if(jsonObject.get("nickname").equals(null)){
                    inputNickNameWithDialog();
                }else{
                    Log.d(TAG,"つらい");
                }
            }catch(JSONException e){
                Log.d(TAG, e.toString());
            }

        }
    };

    private Response.Listener<JSONObject> mRegisterListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            Log.d(TAG,jsonObject.toString());
            waitDialog.dismiss();
        }
    };

    private Response.ErrorListener mEListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {

            Log.d(TAG,volleyError.toString());
        }
    };
}
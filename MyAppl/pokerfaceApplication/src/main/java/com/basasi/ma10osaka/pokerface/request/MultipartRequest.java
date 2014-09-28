package com.basasi.ma10osaka.pokerface.request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MultipartRequest extends Request<String> {
    private final static String TAG = MultipartRequest.class.getSimpleName();

    private final Response.Listener mListener;
    private MultipartEntity mEntity;

    public MultipartRequest(String url, Response.Listener listener, Response.ErrorListener errorListener){
        super(Method.POST, url, errorListener);
        mListener = listener;
    }

    public void setParams(MultipartEntity entity){
        mEntity = entity;
    }

    @Override
    public String getBodyContentType() {
        Log.d(TAG, mEntity.getContentType().getValue());
        return mEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mEntity.writeTo(bos);
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage());
        }
        return bos.toByteArray();
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse networkResponse) {
        String resp = new String(networkResponse.data);
        Log.d(TAG, resp);
        // JSONObject型のレスポンス
        JSONObject resultJson;
        try {
            resultJson = new JSONObject(resp);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }
        return Response.success(resultJson, getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        Log.d(TAG,response);
    }


}
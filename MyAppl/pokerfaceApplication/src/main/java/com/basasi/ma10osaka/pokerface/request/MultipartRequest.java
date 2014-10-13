package com.basasi.ma10osaka.pokerface.request;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;

public class MultipartRequest extends Request<JSONObject> {
    private final static String TAG = MultipartRequest.class.getSimpleName();

    MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    HttpEntity httpEntitiy;
    private final Response.Listener<JSONObject> mListener;
    private final Map<String, String> mStringParts;
    private final Map<String, File> mFileParts;

    public MultipartRequest(String url, Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener,
                            Map<String, String> stringParts, Map<String, File> fileParts) {
        super(Method.POST, url, errorListener);

        mListener = listener;
        mStringParts = stringParts;
        mFileParts = fileParts;
        buildMultipartEntity();
    }

    private void buildMultipartEntity() {
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //送信するリクエストを設定する
        //StringData
        for (Map.Entry<String, String> entry : mStringParts.entrySet()) {
            entity.addTextBody(entry.getKey(), entry.getValue());

        }
        //File Data
        for (Map.Entry<String, File> entry : mFileParts.entrySet()) {
            entity.addPart(entry.getKey(), new FileBody(entry.getValue()));
        }
        httpEntitiy = entity.build();
    }

    @Override
    public String getBodyContentType() {
        return httpEntitiy.getContentType().getValue();
    }

    public HttpEntity getEntity() {
        return httpEntitiy;
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        String resp = new String(response.data);
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

    //リスナーにレスポンスを返す
    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }
}
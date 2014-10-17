package com.basasi.ma10osaka.pokerface.request;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Map;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.entity.mime.HttpMultipartMode;
import ch.boye.httpclientandroidlib.entity.mime.MultipartEntityBuilder;
import ch.boye.httpclientandroidlib.entity.mime.content.InputStreamBody;

public class MultipartRequest extends Request<JSONObject> {
    private final static String TAG = MultipartRequest.class.getSimpleName();

    private MultipartEntityBuilder mEntity = MultipartEntityBuilder.create();
    private HttpEntity mHttpEntity;
    private final Response.Listener<JSONObject> mListener;
    private final Map<String, String> mStringParts;
    private final Map<String, InputStream> mInputStreamParts;

    public MultipartRequest(String url, Response.Listener<JSONObject> listener,
                            Response.ErrorListener errorListener,
                            Map<String, String> stringParts, Map<String, InputStream> fileParts) {
        super(Method.POST, url, errorListener);

        mListener = listener;
        mStringParts = stringParts;
        mInputStreamParts = fileParts;
        buildMultipartEntity();
    }

    private void buildMultipartEntity() {
        mEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //送信するリクエストを設定する
        //StringData
        for (Map.Entry<String, String> entry : mStringParts.entrySet()) {
            mEntity.addTextBody(entry.getKey(), entry.getValue());

        }
        //File Data
        for (Map.Entry<String, InputStream> entry : mInputStreamParts.entrySet()) {
            mEntity.addPart(entry.getKey(), new InputStreamBody(entry.getValue(),"photo.jpg"));
        }
        mHttpEntity = mEntity.build();
    }

    @Override
    public String getBodyContentType() {
        return mHttpEntity.getContentType().getValue();
    }

    public HttpEntity getEntity() {
        return mHttpEntity;
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
package com.basasi.ma10osaka.pokerface.model;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.basasi.ma10osaka.pokerface.R;
import com.basasi.ma10osaka.pokerface.request.MultipartRequest;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CaptureImage {
    private static final String TAG = CaptureImage.class.getSimpleName();
    private final CaptureImage self = this;

    public static final int IMAGE_CAPTURE = 10001;
    public static final String KEY_IMAGE_URI = "KEY_IMAGE_URI";

    private Uri mImageUri;
    private Context mContext;
    private String requestUrl;
    private RequestQueue mRequestQueue;

    public CaptureImage(Context context){
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public void setImageUri(Uri imageUri) {
        mImageUri = imageUri;
    }

    public Uri getImageUri() {
        return mImageUri;
    }

    private String getDirPath() {
        String dirPath = "";
        File photoDir = null;
        File extStorageDir = Environment.getExternalStorageDirectory();
        if (extStorageDir.canWrite()) {
            photoDir = new File(extStorageDir.getPath() + "/" + mContext.getPackageName());
        }
        if (photoDir != null) {
            if (!photoDir.exists()) {
                photoDir.mkdirs();
            }
            if (photoDir.canWrite()) {
                dirPath = photoDir.getPath();
            }
        }
        return dirPath;
    }

    public void setPhotoUri() {
        long currentTimeMillis = System.currentTimeMillis();
        Date today = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String title = dateFormat.format(today);
        String dirPath = getDirPath();
        String fileName = "pokerface_" + title + ".jpg";
        String path = dirPath + "/" + fileName;
        File file = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATA, path);
        values.put(MediaStore.Images.Media.DATE_TAKEN, currentTimeMillis);
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length());
        }
        Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        mImageUri = uri;
    }



    public void postImage(){
        InputStream inputStream = null;
        try{
            inputStream = mContext.getContentResolver().openInputStream(mImageUri);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        MultipartEntity entity = new MultipartEntity();
        InputStreamBody streamBody = new InputStreamBody(inputStream, "photo.jpg");
        entity.addPart("inputFile", streamBody);

        //Log.d(TAG, inputStream.toString());

        requestUrl = mContext.getString(R.string.api_url);

        MultipartRequest request = new MultipartRequest(requestUrl, mResListener, mEListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = super.getHeaders();
                // Add Http Header
                Map<String, String> newHeaders = new HashMap<String, String>();
                newHeaders.putAll(headers);
                return newHeaders;
            }
        };
        request.setParams(entity);
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
        mRequestQueue.start();
    }

    private Response.Listener<String> mResListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            Log.d(TAG, s);
        }
    };

    private Response.ErrorListener mEListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.d(TAG,volleyError.toString());
        }
    };
}
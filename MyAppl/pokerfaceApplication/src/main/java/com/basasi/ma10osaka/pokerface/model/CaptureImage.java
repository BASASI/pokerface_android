package com.basasi.ma10osaka.pokerface.model;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.basasi.ma10osaka.pokerface.R;
import com.basasi.ma10osaka.pokerface.request.MultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String mRequestUrl;
    private RequestQueue mRequestQueue;

    private String mPath;

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
        mPath = dirPath + "/" + fileName;
        File file = new File(mPath);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATA, mPath);
        values.put(MediaStore.Images.Media.DATE_TAKEN, currentTimeMillis);
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length());
        }
        Uri uri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        mImageUri = uri;
    }



    public void postImage(){
        Map<String, String> stringMap = new HashMap<String, String>();
        Map<String, InputStream> fileMap = new HashMap<String, InputStream>();

        InputStream inputStream = null;
        try{
            inputStream = mContext.getContentResolver().openInputStream(mImageUri);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        stringMap.put("card[user_id]",Integer.toString(User.getUserId()));
        fileMap.put("card[image_url]",inputStream);
        mRequestUrl = mContext.getString(R.string.card_api);

        MultipartRequest request = new MultipartRequest(
                mRequestUrl,
                mResListener,
                mEListener,
                stringMap,
                fileMap
        );
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(request);
        mRequestQueue.start();
    }

    private Response.Listener<JSONObject> mResListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject s) {
            Log.d(TAG, s.toString());
            try {
                JSONObject my = s.getJSONObject("card");
                Card.my = new Card(my.getString("atk"),my.getString("def"),my.getString("image_url"),my.getString("nickname"));
                JSONObject supporter = s.getJSONObject("supporter");
                Card.suppoeter = new Card(supporter.getString("atk"),supporter.getString("def"),supporter.getString("image_url"),supporter.getString("nickname"));
                JSONObject e1 = s.getJSONArray("enemy").getJSONObject(0);
                Card.enemy1 = new Card(e1.getString("atk"),e1.getString("def"),e1.getString("image_url"),e1.getString("nickname"));
                JSONObject e2 = s.getJSONArray("enemy").getJSONObject(0);
                Card.enemy2 = new Card(e2.getString("atk"),e2.getString("def"),e2.getString("image_url"),e2.getString("nickname"));
            }catch(JSONException e){

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
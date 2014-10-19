package com.basasi.ma10osaka.pokerface.model;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.basasi.ma10osaka.pokerface.R;
import com.basasi.ma10osaka.pokerface.request.MultipartRequest;
import com.basasi.ma10osaka.pokerface.ui.main.MainActivity;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
    private MainActivity mActivity;
    private String requestUrl;
    private RequestQueue mRequestQueue;

    private static ProgressDialog waitDialog;


    public CaptureImage(MainActivity activity){
        mActivity = activity;
        mRequestQueue = Volley.newRequestQueue(activity);
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
            photoDir = new File(extStorageDir.getPath() + "/" + mActivity.getPackageName());
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
        Uri uri = mActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        mImageUri = uri;
    }



    public void postImage(){
        waitDialog = new ProgressDialog(mActivity);
        waitDialog.setMessage(mActivity.getString(R.string.doing_upload));
        waitDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitDialog.show();
        InputStream inputStream = null;
        try{
            inputStream = mActivity.getContentResolver().openInputStream(mImageUri);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        MultipartEntity entity = new MultipartEntity();
        InputStreamBody streamBody = new InputStreamBody(inputStream, "photo.jpg");
        entity.addPart("card[image_url]", streamBody);
        StringBody stringBody = null;

        try{
            stringBody = new StringBody(Integer.toString(User.getUserId()));
        }catch (UnsupportedEncodingException e){
           Log.d(TAG, e.toString());
        }

        entity.addPart("card[user_id]", stringBody);

        //Log.d(TAG, inputStream.toString());

        requestUrl = mActivity.getString(R.string.card_api);

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

    private Response.Listener<JSONObject> mResListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject s) {
            waitDialog.dismiss();
            Log.d(TAG, s.toString());
            try {
                JSONObject my = s.getJSONObject("card");
                Card.my = new Card(my.getString("offense"),my.getString("defense"),my.getString("image_url"),my.getString("nickname"));
                JSONObject supporter = s.getJSONObject("supporter");
                Card.supporter = new Card(supporter.getString("offense"),supporter.getString("defense"),supporter.getString("image_url"),supporter.getString("nickname"));
                JSONObject e1 = s.getJSONArray("enemys").getJSONObject(0);
                Card.enemy1 = new Card(e1.getString("offense"),e1.getString("defense"),e1.getString("image_url"),e1.getString("nickname"));
                JSONObject e2 = s.getJSONArray("enemys").getJSONObject(0);
                Card.enemy2 = new Card(e2.getString("offense"),e2.getString("defense"),e2.getString("image_url"),e2.getString("nickname"));
                Card.point = s.getInt("score");

                mActivity.transactionGetCardFragment();
            }catch(JSONException e){
                Log.d(TAG,e.toString());
            }
        }
    };

    private Response.ErrorListener mEListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.d(TAG,volleyError.toString());
            waitDialog.dismiss();
            Toast.makeText(mActivity,"画像送信に失敗しました",Toast.LENGTH_LONG).show();
        }
    };
}
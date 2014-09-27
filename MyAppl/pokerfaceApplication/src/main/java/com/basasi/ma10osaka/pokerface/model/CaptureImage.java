package com.basasi.ma10osaka.pokerface.model;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaptureImage {
    private static final String TAG = CaptureImage.class.getSimpleName();
    private final CaptureImage self = this;

    public static final int IMAGE_CAPTURE = 10001;
    public static final String KEY_IMAGE_URI = "KEY_IMAGE_URI";

    private Uri mImageUri;
    private Context mContext;
    private String requestUrl;

    public CaptureImage(Context context){
        mContext = context;
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
        entity.addPart("file", streamBody);

        Log.d(TAG, inputStream.toString());

        //MultipartRequest request = new MultipartRequest(requestUrl, );
    }
}
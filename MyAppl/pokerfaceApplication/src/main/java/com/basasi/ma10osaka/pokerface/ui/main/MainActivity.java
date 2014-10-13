package com.basasi.ma10osaka.pokerface.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;

import com.basasi.ma10osaka.pokerface.R;
import com.basasi.ma10osaka.pokerface.model.CaptureImage;
import com.basasi.ma10osaka.pokerface.model.User;


public class MainActivity extends Activity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private CaptureImage mCaptureImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = new User(this);
        user.setDeviceId();
        user.login();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }


    public void intentToCamera(){
        mCaptureImage = new CaptureImage(this);
        mCaptureImage.setPhotoUri();
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCaptureImage.getImageUri());
        startActivityForResult(intent, CaptureImage.IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CaptureImage.IMAGE_CAPTURE){
            if(resultCode == RESULT_OK){
                //TODO: 写真送信処理ぽよ
                mCaptureImage.postImage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

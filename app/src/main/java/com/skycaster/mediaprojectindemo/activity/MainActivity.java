package com.skycaster.mediaprojectindemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.skycaster.mediaprojectindemo.R;
import com.skycaster.mediaprojectindemo.base.BaseActivity;

import java.nio.ByteBuffer;

import static android.hardware.display.DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_SCREEN_SHOT = 19443;
    private static final int REQUEST_SCREEN_RECORDING = 1998;
    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    private int screenHeight;
    private int screenWidth;
    private int screenDpi;
    private ImageReader mImageReader;
    private ImageView iv_screenShot;
    private Button btn_captureScreen;
    private boolean isRecording;


    @Override
    protected int setRootView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        iv_screenShot= (ImageView) findViewById(R.id.main_iv_screen_shot);
        btn_captureScreen= (Button) findViewById(R.id.main_btn_capture_screen);
    }

    @Override
    protected void initData() {
        mMediaProjectionManager= (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth=displayMetrics.widthPixels;
        screenHeight=displayMetrics.heightPixels;
        screenDpi=displayMetrics.densityDpi;

    }

    @Override
    protected void initListener() {
        btn_captureScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureScreen();
            }
        });
    }

    private void captureScreen(){
        startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_SCREEN_SHOT);
    }

    private void startRecording(){
        startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_SCREEN_RECORDING);




        isRecording=true;
    }

    private void stopRecording(){


        isRecording=false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== REQUEST_SCREEN_SHOT){
            if(resultCode==RESULT_OK){
                mImageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 2);
                mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
                VirtualDisplay virtualDisplay = mMediaProjection.createVirtualDisplay(
                        "ScreenShort"+System.currentTimeMillis(),
                        screenWidth,
                        screenHeight,
                        screenDpi,
                        VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        mImageReader.getSurface(),
                        null,
                        null
                );
                SystemClock.sleep(1000);
                Image image = mImageReader.acquireLatestImage();
                if(image!=null){
                    Image.Plane plane = image.getPlanes()[0];
                    ByteBuffer buffer = plane.getBuffer();
                    Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);
                    iv_screenShot.setImageBitmap(bitmap);
                    image.close();
                }
                virtualDisplay.release();
                mMediaProjection.stop();
            }else {
                finish();
            }
        }else if(requestCode==REQUEST_SCREEN_RECORDING){
            if(resultCode==RESULT_OK){
                mMediaProjection=mMediaProjectionManager.getMediaProjection(resultCode,data);
                initCodec();

            }
        }
    }

    private void initCodec() {
//        MediaCodec.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_front_page,menu);
        MenuItem item = menu.findItem(R.id.menu_stat_recording);
        if(isRecording){
            item.setIcon(R.drawable.ic_pause_circle_fill_grey600_36dp);
        }else {
            item.setIcon(R.drawable.ic_play_circle_fill_grey600_36dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_to_surface_view_demo:
                startActivity(new Intent(MainActivity.this,SurfaceViewDemoActivity.class));
                break;
            case R.id.menu_stat_recording:
                if(isRecording){
                    stopRecording();
                }else {
                    startRecording();
                }
                supportInvalidateOptionsMenu();
                break;
            default:
                break;
        }
        return true;
    }
}

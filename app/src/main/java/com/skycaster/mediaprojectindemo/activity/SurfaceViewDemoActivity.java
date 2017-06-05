package com.skycaster.mediaprojectindemo.activity;

import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ToggleButton;

import com.skycaster.mediaprojectindemo.R;
import com.skycaster.mediaprojectindemo.base.BaseActivity;

public class SurfaceViewDemoActivity extends BaseActivity {
    private static final int REQUEST_SCREEN_MIRROR = 1997;
    private SurfaceView mSurfaceView;
    private ToggleButton mToggleButton;
    private MediaProjectionManager mProjectionManager;
    private MediaProjection mMediaProjection;
    private int screenHeight;
    private int screenWidth;
    private int screenDpi;
    private VirtualDisplay mVirtualDisplay;

    @Override
    protected int setRootView() {
        return R.layout.activity_surface_view_demo;
    }

    @Override
    protected void initView() {
        mSurfaceView= (SurfaceView) findViewById(R.id.sv_activity_surface_view);
        mToggleButton= (ToggleButton) findViewById(R.id.sv_activity_tgbtn);

    }

    @Override
    protected void initData() {
        mProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth=displayMetrics.widthPixels;
        screenHeight=displayMetrics.heightPixels;
        screenDpi=displayMetrics.densityDpi;

    }

    @Override
    protected void initListener() {
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLog("toggle button is check:"+mToggleButton.isChecked());
                if(mToggleButton.isChecked()){
                    startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_SCREEN_MIRROR);
                }else {
                    releaseProjection();
                }
            }
        });
    }

    private void releaseProjection() {
        if(mVirtualDisplay!=null){
            mVirtualDisplay.release();
        }
        if(mMediaProjection!=null){
            mMediaProjection.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseProjection();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_SCREEN_MIRROR){
            boolean result=false;
            if(resultCode==RESULT_OK){
                mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
                if(mMediaProjection!=null){
                    mVirtualDisplay = mMediaProjection.createVirtualDisplay(
                            "DemoView",
                            screenWidth,
                            screenHeight,
                            screenDpi,
                            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                            mSurfaceView.getHolder().getSurface(),
                            null,
                            null);
                    if(mVirtualDisplay!=null){
                        result=true;
                    }
                }
            }
            if(!result){
                mToggleButton.setChecked(false);
            }
        }
    }
}

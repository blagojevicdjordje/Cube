package com.DefaultCompany.Cube;

import com.unity3d.player.*;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

public class UnityPlayerActivity extends Activity implements View.OnClickListener, View.OnTouchListener{
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    private Button button;
    WindowManager windowManager;


    // Override this in your custom UnityPlayerActivity to tweak the command line arguments passed to the Unity Android Player
// The command line arguments are passed as a string, separated by spaces
// UnityPlayerActivity calls this from 'onCreate'
// Supported: -force-gles20, -force-gles30, -force-gles31, -force-gles31aep, -force-gles32, -force-gles, -force-vulkan
// See https://docs.unity3d.com/Manual/CommandLineArguments.html
// @param cmdLine the current command line arguments, may be null
// @return the modified command line string or null
    protected String updateUnityCommandLineArguments(String cmdLine)
    {
        return cmdLine;
    }

    // Setup activity layout
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mUnityPlayer=new UnityPlayer(this);
        super.onCreate(savedInstanceState);
        FrameLayout view = new FrameLayout(this);

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        //here is all the science of params
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                300,
                300,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        // String cmdLine=updateUnityCommandLineArguments(getIntent().getStringExtra("unity"));
        // getIntent().putExtra("unity",cmdLine);
        view.addView(mUnityPlayer.getView());
        windowManager.addView(view, params);
        mUnityPlayer.onWindowFocusChanged(true);
//        Intent intent = new Intent(UnityPlayerActivity.this, MainActivity.class);
//        startActivity(intent);

        //UnityPlayer.UnitySendMessage("Cube","Rotate","false");
        // finish(); // Just close the Activity after the toggle


    }

    private void toggleService() {
        // Try to stop the service if it is already running
        // Otherwise start the service
        Intent intent = new Intent(this,
                CubeOverlay.class);
        if (!stopService(intent)) {
            startService(intent);
        }

    }

    @Override protected void onNewIntent(Intent intent)
    {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
        mUnityPlayer.newIntent(intent);
    }

    // Quit Unity
    @Override protected void onDestroy()
    {
        //mUnityPlayer.destroy();
        System.out.println("destroyed");
        super.onDestroy();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();
        System.out.println("paused");
       // mUnityPlayer.pause();
        //toggleService();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();
        mUnityPlayer.resume();
    }

    @Override protected void onStart()
    {
        super.onStart();
        mUnityPlayer.start();
    }

    @Override protected void onStop()
    {
        super.onStop();
        System.out.println("stopped");
       // mUnityPlayer.stop();
    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if(level==TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
// Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if(event.getAction()==KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode,KeyEvent event){return mUnityPlayer.injectEvent(event);}
    @Override public boolean onKeyDown(int keyCode,KeyEvent event){return mUnityPlayer.injectEvent(event);}
    @Override public boolean onTouchEvent(MotionEvent event){
        System.out.println("touched the unity view");
        return mUnityPlayer.injectEvent(event);}
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event){return mUnityPlayer.injectEvent(event);}

    @Override
    public void onClick(View v){
        UnityPlayer.UnitySendMessage("Cube","Rotate","false");
       // mUnityPlayer.pause();
    }

    @Override
    public boolean onTouch(View v,MotionEvent event){
        mUnityPlayer.performClick();
        return false;
    }
}

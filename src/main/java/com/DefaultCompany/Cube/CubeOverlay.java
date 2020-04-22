package com.DefaultCompany.Cube;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;

public class CubeOverlay extends Service implements View.OnClickListener {

    Intent mIntent;

    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code

    // Override this in your custom UnityPlayerActivity to tweak the command line arguments passed to the Unity Android Player
// The command line arguments are passed as a string, separated by spaces
// UnityPlayerActivity calls this from 'onCreate'
// Supported: -force-gles20, -force-gles30, -force-gles31, -force-gles31aep, -force-gles32, -force-gles, -force-vulkan
// See https://docs.unity3d.com/Manual/CommandLineArguments.html
// @param cmdLine the current command line arguments, may be null
// @return the modified command line string or null
    protected String updateUnityCommandLineArguments(String cmdLine) {
        return cmdLine;
    }

    WindowManager windowManager;

    public CubeOverlay() {
    }

    @Override
    public IBinder onBind(Intent i) {
        mIntent = i;
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(),  "pocelo", Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        FrameLayout view = new FrameLayout(this);

        //view.setBackgroundColor(Color.argb(0, 0, 0, 0));
        mUnityPlayer = new UnityPlayer(this);
        mUnityPlayer.start();
        try {
            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            //here is all the science of params
            final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    300,
                    300,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
//            final WindowManager.LayoutParams unityParams = new WindowManager.LayoutParams(
//                    300,
//                    300,
//                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // TYPE_SYSTEM_ALERT is denied in apiLevel >=19
//                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    PixelFormat.TRANSLUCENT
//            );
            view.addView(mUnityPlayer.getView());
            windowManager.addView(view, params);
            mUnityPlayer.onWindowFocusChanged(true);
            //UnityPlayer.UnitySendMessage("Cube","Rotate","false")
            view.setOnClickListener(this);
        } catch (Exception e) {
            System.out.println("error");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnityPlayer.getView() != null) {
            windowManager.removeView(mUnityPlayer.getView());
        }
    }

    @Override
    public void onClick(View v) {
        //UnityPlayer.UnitySendMessage("Cube","Rotate","false");
        Toast.makeText(getApplicationContext(), "123",  Toast.LENGTH_SHORT).show();
        System.out.println("clicked");
    }
}

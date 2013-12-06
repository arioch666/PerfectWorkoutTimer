package com.perfectworkout588;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {
	MainActivity mainActivity;
    public static boolean wasScreenOn = true;
    
    public void setMainActivity(MainActivity mainActivity)
    {
    	this.mainActivity = mainActivity;
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            mainActivity.enableSensors();
            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            wasScreenOn = true;
        }
    }

}
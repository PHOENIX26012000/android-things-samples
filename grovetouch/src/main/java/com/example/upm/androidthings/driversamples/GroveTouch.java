package com.example.upm.androidthings.driversamples;

import android.app.Activity;
import android.os.Bundle;
import android.os.Build;
import android.util.Log;

import mraa.mraa;

public class GroveTouch extends Activity {

    private static final String TAG = "GroveTouchActivity";
    static {
        try {
            System.loadLibrary("javaupm_ttp223");
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, "Native code library failed to load." +  e);
            System.exit(1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grove_touch);

        BoardDefaults bd = new BoardDefaults(this.getApplicationContext());
        int gpioIndex = -1;

        switch (bd.getBoardVariant()) {
            case BoardDefaults.DEVICE_EDISON_ARDUINO:
                gpioIndex = mraa.getGpioLookup(getString(R.string.TOUCH_Edison_Arduino));
                break;
            case BoardDefaults.DEVICE_EDISON_SPARKFUN:
                gpioIndex = mraa.getGpioLookup(getString(R.string.TOUCH_Edison_Sparkfun));
                break;
            case BoardDefaults.DEVICE_JOULE_TUCHUCK:
                gpioIndex = mraa.getGpioLookup(getString(R.string.TOUCH_Joule_Tuchuck));
                break;
            default:
                throw new IllegalStateException("Unknown Board Variant: " + bd.getBoardVariant());
        }

        try {
            upm_ttp223.TTP223 touch = new upm_ttp223.TTP223(gpioIndex);

            // This should be in a worker thread.

            while (true) {
                if (touch.isPressed())
                    Log.i(TAG, touch.name() + " is pressed");
                else
                    Log.i(TAG, touch.name() + " is not pressed");

                Thread.sleep(1000);
            }

        // Shouldn't catch a universal exception and should exit in any case.
        } catch (Exception e) {
            Log.e(TAG, "Error in UPM APIs", e);
        }
    }
}
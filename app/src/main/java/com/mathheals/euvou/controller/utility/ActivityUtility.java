/*
 *File: ActivityUtility.java
 * Purpose: allows the Back Stack to be cleared and restart the activity when is needed.
 */

package com.mathheals.euvou.controller.utility;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class ActivityUtility{

    private static final String TAG = "ActivityLog";

    public static void restartActivity(Activity activity){

        Log.d(TAG, "restartActivity: beggining the restart of the activity");

        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);
    }

    public static void clearBackStack(FragmentActivity fragmentActivity){
        FragmentManager manager = fragmentActivity.getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0){
            Log.d(TAG, "clearBackStack: the back stack is ready to be cleared");
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else{
            //Nothing to do.
        }
    }
}
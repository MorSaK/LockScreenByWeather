package com.morsak.lockscreenbyweather.lockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.morsak.lockscreenbyweather.util.Constant;
import com.morsak.lockscreenbyweather.lockscreen.LockScreenActivity;

/**
 * Lock Screen Receiver
 *
 * @author MorSaK
 */
public class LockScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Intent lockIntent = new Intent(Constant.LOCK_SCREEN_ACTION);
            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

            context.startActivity(lockIntent);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // do other things if you need
        } else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            // do other things if you need
        }
    }


}

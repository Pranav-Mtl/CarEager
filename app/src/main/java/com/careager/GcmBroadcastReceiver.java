package com.careager;

/**
 * Created by Balram on 5/14/2015.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.careager.Constant.Constant;


public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMNotificationIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);



        try {
            Bundle extras = intent.getExtras();
            Intent i = new Intent("CHAT_MESSAGE_RECEIVED");
            System.out.println("BroadCast reciever" + extras.get(Constant.MSG_KEY));
            i.putExtra("message", extras.get(Constant.MSG_KEY).toString());
            context.sendBroadcast(i);
        }
        catch (NullPointerException NPE)
        {
            NPE.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
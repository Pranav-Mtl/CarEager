package com.careager;

/**
 * Created by Balram on 5/14/2015.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.careager.BE.UserChatBE;
import com.careager.Constant.Constant;
import com.careager.DB.DBOperation;
import com.careager.careager.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class GCMNotificationIntentService extends IntentService {
    // Sets an ID for the notification, so it can be updated

    //private NotificationManager mNotificationManager;
    DBOperation dbOperation;
    String dt="";
    String localTime;

    public static final int notifyID = 9001;
    public static final int NOTIFICATION_ID = 1;
    NotificationCompat.Builder builder;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        //Log.d("EXTRA GCM",extras.toString());
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {


            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                   /*// sendNotification("Deleted messages on server: "
                        + extras.toString());*/
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {

                try {

                    System.out.println("GCM MESSAGE" + "" + extras.get(Constant.MSG_KEY));

                    String msgArray = extras.get(Constant.MSG_KEY) + "";
                    try {
                        JSONObject jsonObj = new JSONObject(msgArray);
                        String notification_type = jsonObj.getString("notification_type");
                        if (notification_type.equalsIgnoreCase("Chat")) {
                            if(!Constant.flatGCM) {
                                dbOperation = new DBOperation(this);
                                dbOperation.createAndInitializeTables();
                                dt = getCurrentDateTime();
                                UserChatBE curChatObj = addToChat(jsonObj.getString("user_id"), jsonObj.getString("user_name"), jsonObj.getString("message"), "RECEIVED", dt);
                                addToDB(curChatObj);
                                sendNotificationChat("" + extras.get(Constant.MSG_KEY));
                            }
                        }

                        else if (notification_type.equalsIgnoreCase("forum_chat")) {
                            if(!Constant.flatFORUM) {
                                dbOperation = new DBOperation(this);
                                dbOperation.createAndInitializeTables();
                                dt = getCurrentDateTime();
                                UserChatBE curChatObj = addToChat(jsonObj.getString("user_id"), jsonObj.getString("user_name"), jsonObj.getString("message"), "RECEIVED", dt);
                                addToDBForum(curChatObj);
                                sendNotificationForum("" + extras.get(Constant.MSG_KEY));
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                }


            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    private void sendNotificationChat(String msg) {
        String id="";
        String messagees="";
        String name="";
        String image="";
        try {
            JSONObject jsonObj = new JSONObject(msg);

            messagees=jsonObj.getString("message");
            id=jsonObj.getString("user_id");
            name=jsonObj.getString("user_name");


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
         Intent resultIntent = new Intent(this, UserChat.class);
         resultIntent.putExtra("ID",id);
         resultIntent.putExtra("NAME",name);

       // resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //............................................
        System.out.println("Notification Intent Message" + messagees);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("CarEager")
                .setContentText(messagees)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messagees))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(contentIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(messagees);

        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification


        mNotificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());
    }

    /*----------------------------*/

    private void sendNotificationForum(String msg) {
        String id="";
        String messagees="";
        String name="";
        String image="";
        try {
            JSONObject jsonObj = new JSONObject(msg);

            messagees=jsonObj.getString("message");
            id=jsonObj.getString("user_id");
            name=jsonObj.getString("user_name");


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Intent resultIntent = new Intent(this, ForumChat.class);
        resultIntent.putExtra("ID",id);
        resultIntent.putExtra("NAME",name);

        // resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //............................................
        System.out.println("Notification Intent Message" + messagees);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("CarEager")
                .setContentText(messagees)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messagees))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher);
        // Set pending intent
        mNotifyBuilder.setContentIntent(contentIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(messagees);

        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification


        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }




    UserChatBE addToChat(String personID,String name, String chatMessage, String toOrFrom,String chatDate) {

        /*Log.i(TAG, "inserting : " + personName + ", " + chatMessage + ", "
                + toOrFrom + " , " + chattingToDeviceID);*/
        UserChatBE curChatObj = new UserChatBE();
        curChatObj.setPERSON_ID(personID);
        curChatObj.setPERSON_NAME(name);
        curChatObj.setPERSON_CHAT_MESSAGE(chatMessage);
        curChatObj.setPERSON_CHAT_TO_FROM(toOrFrom);
        curChatObj.setCHAT_DATE(chatDate);
        return curChatObj;

    }



    void addToDB(UserChatBE curChatObj) {

        UserChatBE people = new UserChatBE();
        ContentValues values = new ContentValues();
        values.put(people.getPERSON_ID(), curChatObj.getPERSON_ID());
        values.put(people.getPERSON_NAME(),curChatObj.getPERSON_NAME());
        values.put(people.getPERSON_CHAT_MESSAGE(),
                curChatObj.getPERSON_CHAT_MESSAGE());
        values.put(people.getPERSON_CHAT_TO_FROM(),
                curChatObj.getPERSON_CHAT_TO_FROM());
        values.put(people.getCHAT_DATE(), dt);
        dbOperation.open();
        long id = dbOperation.insertTableData(people.getTableName(), values);
        dbOperation.close();
        if (id != -1) {
            //  Log.i(TAG, "Succesfully Inserted");
        }

        //populateChatMessages("ADD to DB");
    }

    void addToDBForum(UserChatBE curChatObj) {

        UserChatBE people = new UserChatBE();
        ContentValues values = new ContentValues();
        values.put(people.getPERSON_ID(), curChatObj.getPERSON_ID());
        values.put(people.getPERSON_NAME(),curChatObj.getPERSON_NAME());
        values.put(people.getPERSON_CHAT_MESSAGE(),
                curChatObj.getPERSON_CHAT_MESSAGE());
        values.put(people.getPERSON_CHAT_TO_FROM(),
                curChatObj.getPERSON_CHAT_TO_FROM());
        values.put(people.getCHAT_DATE(), dt);
        dbOperation.open();
        long id = dbOperation.insertTableData(people.getTableNameForum(), values);
        dbOperation.close();
        if (id != -1) {
            //  Log.i(TAG, "Succesfully Inserted");
        }

        //populateChatMessages("ADD to DB");
    }

    public String getCurrentDateTime()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime = cal.getTime();
        SimpleDateFormat date = new SimpleDateFormat("dd:MM:yyyy hh:mm a");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        localTime = date.format(currentLocalTime);

        return localTime;
    }


}


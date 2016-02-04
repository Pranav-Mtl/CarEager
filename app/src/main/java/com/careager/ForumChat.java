package com.careager;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.careager.Adapter.ForumChatAdapter;
import com.careager.Adapter.ForumChatListAdapter;
import com.careager.Adapter.UserChatAdapter;
import com.careager.BE.UserChatBE;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.DB.DBOperation;
import com.careager.WS.RestFullWS;
import com.careager.careager.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ForumChat extends AppCompatActivity implements View.OnClickListener {

    String dealerID;
    String dealerName;
    String userID,userType;
    Intent intent;
    TextView tvName;
    EditText etMessage;
    LinearLayout llSend;

    String strMessage;
    String dt="";
    String localTime;

    DBOperation dbOperation;

    ArrayList<UserChatBE> ChatPeoples;

    ListView recList;

    ForumChatListAdapter objForumChatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_chat);
        initialize();

    }
    private void initialize(){
        tvName= (TextView) findViewById(R.id.chat_person_name);
        recList = (ListView) findViewById(R.id.chat_list);
        recList.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        recList.setStackFromBottom(true);

        etMessage= (EditText) findViewById(R.id.et_message);
        llSend= (LinearLayout) findViewById(R.id.ll_send);

        llSend.setOnClickListener(this);

        /*recList.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recList.setLayoutManager(llm);*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        intent=getIntent();
        dealerID=intent.getStringExtra("ID");
        dealerName=intent.getStringExtra("NAME");

        tvName.setText(dealerName);

        userID= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SP_LOGIN_ID);
        userType=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_TYPE);



        dbOperation = new DBOperation(this);
        dbOperation.createAndInitializeTables();

        ChatPeoples = new ArrayList<UserChatBE>();

        populateChatMessages();

        registerReceiver(broadcastReceiver, new IntentFilter(
                "CHAT_MESSAGE_RECEIVED"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void populateChatMessages() {

        getData();

        if (ChatPeoples.size() > 0) {
            objForumChatAdapter = new ForumChatListAdapter(this, ChatPeoples);
            recList.setAdapter(objForumChatAdapter);
        }

    }

    void getData() {

        ChatPeoples.clear();

        Cursor cursor = dbOperation.getDataFromTableForum(dealerID);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                UserChatBE people = addToChat(cursor.getString(0),
                        cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));
                ChatPeoples.add(people);
            } while (cursor.moveToNext());
        }
        cursor.close();

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


    /*    call broad cast reciever */

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(GCMNotificationIntentService.notifyID);


            Bundle b = intent.getExtras();

            String mm="";
            // JSONObject jsonObj = new JSONObject(msg);
            String message = b.getString("message");

            Log.d(" FORUM Broad cast call", message);
            try {
                dt=getCurrentDateTime();
                JSONObject jsonObj = new JSONObject(message);
                UserChatBE curChatObj = addToChat(jsonObj.getString("user_id"), jsonObj.getString("user_name"), jsonObj.getString("message"), "RECEIVED", dt);
                addToDB(curChatObj);
                populateChatMessages();
            }catch (Exception e){

            }



        }
    };

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_send:
                strMessage=etMessage.getText().toString().trim();
                dt=getCurrentDateTime();
                if(strMessage.length()>0){
                    UserChatBE curChatObj = addToChat(dealerID,dealerName,strMessage, "Sent", dt);
                    addToDB(curChatObj); // adding to db
                    populateChatMessages();

                    clearMessageTextBox();

                    new SendChatData().execute(userID,dealerID,strMessage);

                }
                break;
        }
    }

    void clearMessageTextBox() {

        etMessage.setText("");

        //hideKeyBoard(edtMessage);

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
        SimpleDateFormat date = new SimpleDateFormat("hh:mm a dd:MM:yyyy");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        localTime = date.format(currentLocalTime);

        return localTime;
    }

    private class SendChatData extends AsyncTask<String,String,String>
    {


        @Override
        protected String doInBackground(String... params) {

            String text="";
            String URL="sender_id="+params[0]+"&reciever_id="+params[1]+"&message="+params[2];
            text = RestFullWS.serverRequest(Constant.WS_PATH_USER, URL, Constant.WS_FORUM_CHAT);

            return text;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constant.flatFORUM=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constant.flatFORUM=false;
    }
}

package com.careager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.careager.Adapter.ForumUserListAdapter;
import com.careager.Adapter.ProfileSaleAdapter;
import com.careager.BE.DealerProfileBE;
import com.careager.BL.ForumUserListBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.UI.RecyclerItemClickListener;
import com.careager.careager.R;

public class ForumUserList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ForumUserListAdapter objForumUserListAdapter;

    RecyclerView recList;

    ProgressDialog progressDialog;

    Toolbar toolbar;

    String userID,userType;

    AutoCompleteTextView tvUserList;

    ForumUserListBL objForumUserListBL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_user_list);
        initialize();



        recList.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getApplicationContext(), ForumChat.class);
                        intent.putExtra("ID", Constant.forumUserID[position]);
                        intent.putExtra("NAME", Constant.forumUserName[position]);
                        startActivity(intent);


                    }

                }));

        tvUserList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

         /* GET DATA FOR SEARCHED KEYWORD*/

        tvUserList.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1 && s.length() <= 10) {
                    // Toast.makeText(getApplicationContext(),"char:"+s,Toast.LENGTH_LONG).show();
                        if(Util.isInternetConnection(ForumUserList.this))
                    new GetSearchUser().execute(userID,s.toString().trim(),userType);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initialize(){

        tvUserList= (AutoCompleteTextView) findViewById(R.id.user_search);
        recList = (RecyclerView) findViewById(R.id.forum_user_list);
        recList.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        objForumUserListBL=new ForumUserListBL();

        tvUserList.setOnItemClickListener(this);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressDialog=new ProgressDialog(ForumUserList.this);

        userID= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SP_LOGIN_ID);
        userType= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SP_LOGIN_TYPE);

        if(userID!=null){
            String userType=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_TYPE);
            if(userType!=null)
                if(userType.equalsIgnoreCase(Constant.strLoginUser)){
                    new GetForumList().execute(userID);
                }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), ForumChat.class);
        intent.putExtra("ID", Constant.forumUserIDSearch[position]);
        intent.putExtra("NAME", Constant.forumUserNameSearch[position]);
        startActivity(intent);
    }

    private class GetForumList extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            objForumUserListAdapter=new ForumUserListAdapter(getApplicationContext(),params[0]);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                recList.setAdapter(objForumUserListAdapter);
            }
            catch (Exception e){
                e.printStackTrace();
            }finally {
                progressDialog.dismiss();
            }

        }
    }

    private class GetSearchUser extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            objForumUserListBL.getSearchUserList(params[0],params[1],params[2]);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try{
            if(Constant.forumUserNameSearch!=null) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, Constant.forumUserNameSearch);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tvUserList.setAdapter(adapter);
            }}catch (NullPointerException e){
                e.printStackTrace();
            }
            catch (Exception e){

            }
        }
    }

}

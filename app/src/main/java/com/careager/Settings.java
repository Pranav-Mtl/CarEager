package com.careager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.careager.BE.ProfileInformationBE;
import com.careager.BL.ProfileInformationBL;
import com.careager.BL.SettingsBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.facebook.*;

public class Settings extends AppCompatActivity {

    ImageButton arroBtn;
    LinearLayout showPassword,btnClick,btnAppShare;
    boolean flag=true;
    boolean flag1=true;
    EditText userName,userMobileNum,oldPass,newPass,confirmPass,userEmail,userAddress;
    ImageView mobileBtn;
    String newPassword,confirmPassword,oldPassword;
    String name,address;
    ImageButton nameBtn,addressBtn;
    Button saveBtn;


    ProfileInformationBE objProfileInformationBE;
    ProfileInformationBL objProfileInformationBL;
    SettingsBL objSettingsBL;

    ProgressDialog progressDialog;

    String userID,userType;

    LinearLayout llAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initialize();

        arroBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBtn.setVisibility(View.VISIBLE);
                if (flag) {
                    showPassword.setVisibility(View.VISIBLE);
                    arroBtn.setBackgroundResource(R.drawable.arrow_below);
                    flag = false;
                } else if (!flag) {
                    showPassword.setVisibility(View.GONE);
                    arroBtn.setBackgroundResource(R.drawable.arrow_btn);
                    flag = true;
                }
            }
        });


        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveBtn.setVisibility(View.VISIBLE);

                if (flag1) {
                    showPassword.setVisibility(View.VISIBLE);
                    arroBtn.setBackgroundResource(R.drawable.arrow_below);
                    flag1 = false;
                } else if (!flag1) {
                    showPassword.setVisibility(View.GONE);
                    arroBtn.setBackgroundResource(R.drawable.arrow_btn);
                    flag1 = true;
                }
            }
        });

        nameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setEnabled(true);
                saveBtn.setVisibility(View.VISIBLE);
                userName.setSelection(userName.getText().length());
                userName.setFocusable(true);
                userName.requestFocus();



            }
        });

        addressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAddress.setEnabled(true);
                saveBtn.setVisibility(View.VISIBLE);
                userAddress.setSelection(userEmail.getText().length());
                userAddress.setFocusable(true);
                userAddress.requestFocus();


            }
        });

        if(Util.isInternetConnection(Settings.this)){
            new GetData().execute(userID,userType);
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=userName.getText().toString();
                address=userAddress.getText().toString();

                oldPassword=oldPass.getText().toString();

                if(userType.equalsIgnoreCase(Constant.strLoginUser)){
                    if(name.length()>0){

                            if (oldPassword.trim().length() > 0) {
                                newPassword = newPass.getText().toString();
                                confirmPassword = confirmPass.getText().toString();

                                if (newPassword.length() > 0) {
                                    if (newPassword.equalsIgnoreCase(confirmPassword)) {
                                        if(Util.isInternetConnection(Settings.this)) {
                                            new UpdateData().execute(userID, userType, name, "", oldPassword, newPassword);
                                        }
                                    }
                                    else
                                        confirmPass.setError("Password Mismatch");
                                }
                            } else {

                                if(Util.isInternetConnection(Settings.this))
                                    new UpdateData().execute(userID,userType,name,"","","");
                            }
                    }
                    else
                        userName.setError("Required");
                }
                else {
                    if(name.length()>0){
                        if(address.length()>0) {
                            if (oldPassword.trim().length() > 0) {
                                newPassword = newPass.getText().toString();
                                confirmPassword = confirmPass.getText().toString();

                                if (newPassword.length() > 0) {
                                    if (newPassword.equalsIgnoreCase(confirmPassword)) {
                                        if(Util.isInternetConnection(Settings.this)) {
                                            new UpdateData().execute(userID, userType, name, address, oldPassword, newPassword);
                                        }
                                    }
                                    else
                                        confirmPass.setError("Password Mismatch");
                                }
                            } else {
                                if(Util.isInternetConnection(Settings.this)) {
                                    new UpdateData().execute(userID, userType, name, address, "", "");
                                }


                            }
                        }else
                            userAddress.setError("Required");
                    }
                    else
                        userName.setError("Required");
                }


            }
        });

        btnAppShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "#GetAPP"+"\n"+"http://tinyurl.com/careager");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });



    }

    private void initialize(){

        userName=(EditText)findViewById(R.id.userName);
        userAddress=(EditText)findViewById(R.id.useraddress);
        userMobileNum=(EditText)findViewById(R.id.userMobile);
        oldPass=(EditText)findViewById(R.id.userOldPadd);
        newPass=(EditText)findViewById(R.id.userNewPass);
        confirmPass=(EditText)findViewById(R.id.userConfirmPass);
        nameBtn=(ImageButton)findViewById(R.id.nameBtn);
        mobileBtn=(ImageView)findViewById(R.id.numberBtn);
        addressBtn=(ImageButton)findViewById(R.id.addressBtn);
        showPassword=(LinearLayout)findViewById(R.id.pass_layout);
        arroBtn=(ImageButton)findViewById(R.id.arrow_btn);
        saveBtn=(Button)findViewById(R.id.saveBtn);
        userEmail=(EditText)findViewById(R.id.userEmail);
        btnClick=(LinearLayout)findViewById(R.id.arrow_btn1);
        llAddress=(LinearLayout)findViewById(R.id.ll_address);
        btnAppShare= (LinearLayout) findViewById(R.id.setting_appshare);



        objProfileInformationBE=new ProfileInformationBE();
        objProfileInformationBL=new ProfileInformationBL();
        objSettingsBL=new SettingsBL();

        progressDialog=new ProgressDialog(Settings.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        userID=Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SP_LOGIN_ID);
        userType=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_TYPE);

        if(userType.equalsIgnoreCase(Constant.strLoginUser))
            llAddress.setVisibility(View.GONE);

        userName.setEnabled(false);
        userEmail.setEnabled(false);
        userMobileNum.setEnabled(false);
        userAddress.setEnabled(false);
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

    private class GetData extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            objProfileInformationBL.getData(params[0],params[1],objProfileInformationBE);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                userName.setText(objProfileInformationBE.getName());
                userEmail.setText(objProfileInformationBE.getEmail());
                userMobileNum.setText(objProfileInformationBE.getPhone());
                userAddress.setText(objProfileInformationBE.getAddress());
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }

    private class UpdateData extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objSettingsBL.updateDetails(params[0], params[1], params[2], params[3], params[4], params[5]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    userName.setEnabled(false);

                    userAddress.setEnabled(flag);
                    saveBtn.setVisibility(View.INVISIBLE);
                    oldPass.setText("");
                    newPass.setText("");
                    confirmPass.setText("");
                    Constant.NAME=userName.getText().toString();
                    Toast.makeText(getApplicationContext(),"Old password didn't matched",Toast.LENGTH_SHORT).show();
                }
                else if("Fail".equalsIgnoreCase(s)){
                    Toast.makeText(getApplicationContext(),"Old password didn't matched",Toast.LENGTH_SHORT).show();
                    System.out.print("Update res"+s);
                }
                else
                    Toast.makeText(getApplicationContext(),"Something went wrong. please try again",Toast.LENGTH_SHORT).show();

            }catch (NullPointerException e){

            }
            catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }

}

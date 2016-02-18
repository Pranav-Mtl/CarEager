package com.careager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.careager.BE.DealerCategoryBE;
import com.careager.BE.DealerSignUpBE;
import com.careager.BL.DealerSignUpBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DealerSignup extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    Button btnDone;
    EditText etName,etEmail,etContact,etPassword,etShop,etArea,etCity,etState,etZip;
    String strName,strEmail,strContact,strPassword,strCompany,strArea,strState;

    Spinner spnCompanyName,spnStates;

    DealerSignUpBE objDealerSignUpBE;
    DealerCategoryBE objDealerCategoryBE;
    DealerSignUpBL objDealerSignUpBL;
    ProgressDialog mProgressDialog;

    String selectedCategory;

    List listCompany=new ArrayList<>();
    List listStates=new ArrayList();

    String deviceId;
    GoogleCloudMessaging gcmObj;

    Context applicationContext;
    String gcmID;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_signup);

        initialize();
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

    private void initialize(){

        btnDone= (Button) findViewById(R.id.btnDealerDone);

        etName= (EditText) findViewById(R.id.signup_name);
        etEmail= (EditText) findViewById(R.id.signup_email);
        etContact= (EditText) findViewById(R.id.signup_mobile);
        etPassword= (EditText) findViewById(R.id.signup_password);
        spnCompanyName= (Spinner) findViewById(R.id.signup_company_name);
        //etArea= (EditText) findViewById(R.id.signup_area_house);
        spnStates= (Spinner) findViewById(R.id.signup_states);


        objDealerSignUpBE= (DealerSignUpBE) getIntent().getSerializableExtra("DealerSignupBE");
        objDealerCategoryBE= (DealerCategoryBE) getIntent().getSerializableExtra("DealerCategoryBE");
        objDealerSignUpBL=new DealerSignUpBL();
        mProgressDialog=new ProgressDialog(DealerSignup.this);

        applicationContext=getApplicationContext();
        gcmID=Util.getSharedPrefrenceValue(DealerSignup.this, Constant.SP_GCM_ID);
        deviceId=Util.getSharedPrefrenceValue(DealerSignup.this,Constant.SP_DEVICE_ID);
        if(gcmID==null){
            if (checkPlayServices()) {
                registerInBackground();
            }
        }

        btnDone.setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        selectedCategory=objDealerSignUpBE.getDealerCategory().trim();
        if(selectedCategory.equalsIgnoreCase("Car Dealer (Authorised)") || selectedCategory.equalsIgnoreCase("Service Station (Authorised)")){
            spnCompanyName.setVisibility(View.VISIBLE);
        }
        else {
            spnCompanyName.setVisibility(View.GONE);
        }

        parseCompany(objDealerCategoryBE.getCompanyJSON());
        parseStates(objDealerCategoryBE.getStatesJSON());

        ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,listCompany);
        adapterBrand.setDropDownViewResource(R.layout.spinner_item);
        spnCompanyName.setAdapter(adapterBrand);

        ArrayAdapter<String> adapterStates = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,listStates);
        adapterStates.setDropDownViewResource(R.layout.spinner_item);
        spnStates.setAdapter(adapterStates);
    }

    private boolean validateDetails(){
        boolean flag=true;

        strName=etName.getText().toString();
        strEmail=etEmail.getText().toString();
        strContact=etContact.getText().toString();
        strPassword=etPassword.getText().toString();
        //strArea=etArea.getText().toString();
        strState=spnStates.getSelectedItem().toString();
        strCompany=spnCompanyName.getSelectedItem().toString();


        if(strName.trim().length()==0){
            etName.setError("required");
            flag=false;
        }

        if(strEmail.trim().length()==0){
            etEmail.setError("required");
            flag=false;
        }
        else if(!Util.isEmailValid(strEmail)){
            etEmail.setError("Invalid Email-id");
            flag=false;
        }

        if(strContact.trim().length()==0){
            etContact.setError("required");
            flag=false;
        }

        if(strPassword.trim().length()==0){
            etPassword.setError("required");
            flag=false;
        }

       /* if(strArea.trim().length()==0){
            etArea.setError("required");
            flag=false;
        }*/


        if(strState.trim().equalsIgnoreCase("Select state")){
            flag=false;
        }


        return flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDealerDone:
                if(validateDetails()){
                    objDealerSignUpBE.setEmail(strEmail);
                    objDealerSignUpBE.setName(strName);
                    objDealerSignUpBE.setContact(strContact);
                    objDealerSignUpBE.setPassword(strPassword);
                    objDealerSignUpBE.setState(strState);
                    objDealerSignUpBE.setDealerCompany(strCompany);
                    objDealerSignUpBE.setGcmId(gcmID);
                    objDealerSignUpBE.setDeviceId(deviceId);



                    Util.hideSoftKeyboard(DealerSignup.this);
                    new InsertSignup().execute();
                }
                break;
        }
    }

    private class InsertSignup extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objDealerSignUpBL.insertSignUpDetails(objDealerSignUpBE,getApplicationContext());

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                String ss[]=s.split(",");
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(ss[0])){
                    Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.SP_LOGIN_TYPE,Constant.strLoginBusiness);
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                }
                else {
                    Snackbar snack = Snackbar.make(findViewById(R.id.login_root),
                            ss[1],
                            Snackbar.LENGTH_LONG).setText(ss[1]).setActionTextColor(getResources().getColor(R.color.redColor));
                    ViewGroup group = (ViewGroup) snack.getView();
                    TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    group.setBackgroundColor(getResources().getColor(R.color.redColor));
                    snack.show();

                }
            }
            catch (NullPointerException e){

            }
            finally {
                mProgressDialog.dismiss();
            }
        }
    }

    private void parseCompany(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            listCompany.add("Select company");
            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                listCompany.add(jsonObject.get("maker_name"));
            }


        }
        catch (Exception e){

        }
    }

    private void parseStates(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            listStates.add("Select state");
            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                listStates.add(jsonObject.get("state"));
            }


        }
        catch (Exception e){

        }
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

                finish();
            }
            return false;
        } else {

        }
        return true;
    }

    /*--------------------------GCM KEY ----------------------------------------*/
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(applicationContext);
                    }
                    gcmID = gcmObj
                            .register(Constant.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + gcmID;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(gcmID)) {
                    //Toast.makeText(getApplicationContext(),"GSM"+gcmID,Toast.LENGTH_SHORT).show();
                    Util.setSharedPrefrenceValue(applicationContext,Constant.PREFS_NAME,Constant.SP_GCM_ID,gcmID);
                } else {
                    /*Toast.makeText(
                       applicationContext,
                            "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();*/
                }
            }
        }.execute(null, null, null);
    }
}

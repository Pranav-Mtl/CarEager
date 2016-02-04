package com.careager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.careager.BE.ClaimBusinessBE;
import com.careager.BL.ClaimBusinessBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.careager.R;

public class ClaimBusiness extends AppCompatActivity implements View.OnClickListener{

    EditText etBusinessName,etBusinessContact,etBusinessEmail,etBusinessLocation,etBusinessYear;
    EditText etClaimName,etClaimContact,etClaimEmail,etClaimLocation,etClaimDesignation;

    String businessID,businessName;

    ClaimBusinessBE objClaimBusinessBE;
    ClaimBusinessBL objClaimBusinessBL;

    String strBusinessName,strBusinessContact,strBusinessEmail,strBusinessLocation,strBusinessYear;

    String strClaimName,strClaimContact,strClaimEmail,strClaimLocation,strClaimDesignation;

    Button btnDone;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_business);

        initialize();
    }

    private void initialize(){
        etBusinessName= (EditText) findViewById(R.id.business_name);
        etBusinessContact= (EditText) findViewById(R.id.business_mobile);
        etBusinessEmail= (EditText) findViewById(R.id.business_email);
        etBusinessLocation= (EditText) findViewById(R.id.business_location);
        etBusinessYear= (EditText) findViewById(R.id.business_year);

        etClaimName= (EditText) findViewById(R.id.claim_name);
        etClaimContact= (EditText) findViewById(R.id.claim_mobile);
        etClaimLocation= (EditText) findViewById(R.id.claim_location);
        etClaimEmail= (EditText) findViewById(R.id.claim_email);
        etClaimDesignation= (EditText) findViewById(R.id.claim_designation);
        btnDone= (Button) findViewById(R.id.claim_business_done);

        btnDone.setOnClickListener(this);

        objClaimBusinessBE=new ClaimBusinessBE();
        objClaimBusinessBL=new ClaimBusinessBL();

        progressDialog=new ProgressDialog(ClaimBusiness.this);

        businessID=getIntent().getStringExtra("Id");
        businessName=getIntent().getStringExtra("Name");

        etBusinessName.setText(businessName);
        etBusinessName.setEnabled(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
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

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    private boolean validate(){
        boolean flag=true;
        strBusinessContact=etBusinessContact.getText().toString();
        strBusinessEmail=etBusinessEmail.getText().toString();
        strBusinessLocation=etBusinessLocation.getText().toString();
        strBusinessYear=etBusinessYear.getText().toString();

        strClaimName=etClaimName.getText().toString();
        strClaimContact=etClaimContact.getText().toString();
        strClaimDesignation=etClaimDesignation.getText().toString();
        strClaimEmail=etClaimEmail.getText().toString();
        strClaimLocation=etClaimLocation.getText().toString();

        if(strBusinessContact.length()==0){
            flag=false;
            etBusinessContact.setError("Required");
        }

        if(strBusinessEmail.length()==0){
            flag=false;
            etBusinessEmail.setError("Required");
        }

        if(strBusinessLocation.length()==0){
            flag=false;
            etBusinessLocation.setError("Required");
        }

        if(strBusinessYear.length()==0){
            flag=false;
            etBusinessYear.setError("Required");
        }

        if(strClaimName.length()==0){
            flag=false;
            etClaimName.setError("Required");
        }

        if(strClaimEmail.length()==0){
            flag=false;
            etClaimEmail.setError("Required");
        }

        if(strClaimContact.length()==0){
            flag=false;
            etClaimContact.setError("Required");
        }

        if(strClaimLocation.length()==0){
            flag=false;
            etClaimLocation.setError("Required");
        }

        if(strClaimDesignation.length()==0){
            flag=false;
            etClaimDesignation.setError("Required");
        }

        return flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.claim_business_done:
                if(validate()){
                    objClaimBusinessBE.setBusinessID(businessID);
                    objClaimBusinessBE.setBusinessEmail(strBusinessEmail);
                    objClaimBusinessBE.setBusinessMobile(strBusinessContact);
                    objClaimBusinessBE.setBusinessLocation(strBusinessLocation);
                    objClaimBusinessBE.setBusinessYear(strBusinessYear);

                    objClaimBusinessBE.setPersonalName(strClaimName);
                    objClaimBusinessBE.setPersonalDesignation(strClaimDesignation);
                    objClaimBusinessBE.setPersonalEmail(strClaimEmail);
                    objClaimBusinessBE.setPersonalLocation(strClaimLocation);
                    objClaimBusinessBE.setPersonalMobile(strClaimContact);

                    if(Util.isInternetConnection(getApplicationContext()))
                        new SendClaim().execute();
                }
                break;
        }
    }

    private class SendClaim extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objClaimBusinessBL.sendClaimBusiness(objClaimBusinessBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    Toast.makeText(getApplicationContext(),"Business Claimed",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"Something went wrong. Please try again.",Toast.LENGTH_SHORT).show();
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }
}

package com.careager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.careager.BL.DealerContactBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.careager.R;

public class DealerContact extends AppCompatActivity implements View.OnClickListener {

    EditText etName,etEmail,etPhone,etMessage,etRegistration,etPolicy;
    Button btnDone;
    TextView tvCategory;

    DealerContactBL objDealerContactBL;

    String categoryName,profileID;
    Intent intent;

    String name,email,mobile,message,policy,registration;

    ProgressDialog progressDialog;

    int xx,yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_contact);
        initialize();
    }

    private void initialize(){
        etName= (EditText) findViewById(R.id.contact_name);
        etEmail= (EditText) findViewById(R.id.contact_email);
        etPhone= (EditText) findViewById(R.id.contact_mobile);
        etMessage= (EditText) findViewById(R.id.contact_message);
        etRegistration= (EditText) findViewById(R.id.contact_registration);
        etPolicy= (EditText) findViewById(R.id.contact_policy);

        tvCategory= (TextView) findViewById(R.id.contact_category);
        btnDone= (Button) findViewById(R.id.contact_done);

        objDealerContactBL=new DealerContactBL();

        intent=getIntent();
        categoryName=intent.getStringExtra("CategoryName");
        profileID=intent.getStringExtra("ID");

        tvCategory.setText("Category: "+categoryName);

        progressDialog=new ProgressDialog(DealerContact.this);

        popupSize();

        btnDone.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

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


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.contact_done:
                name=etName.getText().toString();
                email=etEmail.getText().toString();
                mobile=etPhone.getText().toString();
                message=etMessage.getText().toString();
                policy=etPolicy.getText().toString();
                registration=etRegistration.getText().toString();

                if(Util.isInternetConnection(DealerContact.this))
                    if(validateDetails())
                    new SendMessage().execute(name,mobile,email,message,policy,registration,profileID);
                else
                    showDialogInternet(DealerContact.this);
                break;
        }
    }

    private class SendMessage extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objDealerContactBL.sendMessage(params[0],params[1],params[2],params[3],params[4],params[5],params[6]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    Toast.makeText(getApplicationContext(),"Your query shared with owner.",Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Something went wrong.",Toast.LENGTH_SHORT).show();
                }

            }catch (NullPointerException e){
                showDialogResponse(DealerContact.this);
            }catch (Exception e){

            }
            finally {
                progressDialog.dismiss();
            }
        }
    }

    /*--------------------------------------------------------------------------------*/

     /*-----------------------------------------------------------*/

    private void popupSize(){
        Display display = getWindowManager().getDefaultDisplay();

        int width = display.getWidth();
        int height = display.getHeight();

        // System.out.println("width" + width + "height" + height);

        if(width>=1000 && height>=1500){
            xx=700;
            yy=650;
        }
        else if(width>=700 && height>=1000)
        {
            xx=550;
            yy=500;
        }
        else
        {
            xx=450;
            yy=400;
        }

    }

    /* popup for no internet */
    private void showDialogInternet(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText(getResources().getString(R.string.no_internet_title));
        tvMsg.setText(getResources().getString(R.string.no_internet_message));
        btnClosePopup.setText(getResources().getString(R.string._no_internet_cancel));
        btnsave.setText(getResources().getString(R.string._no_internet_ok));

        btnClosePopup.setVisibility(View.GONE);


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {


                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

    /* popup for no Response From Server*/
    private void showDialogResponse(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText(getResources().getString(R.string._no_response_title));
        tvMsg.setText(getResources().getString(R.string._no_response_message));
        btnClosePopup.setText(getResources().getString(R.string._no_response_cancel));
        btnsave.setText(getResources().getString(R.string._no_response_save));

        //btnClosePopup.setVisibility(View.GONE);


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           new SendMessage().execute(categoryName,name,mobile,email,message,profileID);
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

    private boolean validateDetails(){
         boolean flag=true;

        if(name.length()==0){
            flag=false;
            etName.setError("Required");
        }

        if(policy.length()==0){
            flag=false;
            etPolicy.setError("Required");
        }

        if(registration.length()==0){
            flag=false;
            etRegistration.setError("Required");
        }

        return flag;
    }

}

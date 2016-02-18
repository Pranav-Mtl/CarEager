package com.careager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.careager.BE.ProfileServicesRequestBE;
import com.careager.BL.ProfileServiceRequestBL;
import com.careager.Constant.Constant;
import com.careager.careager.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfileServiceRequest extends AppCompatActivity implements View.OnClickListener {

    EditText etName,etMobile,etEmail,etDescription,etRegistration;

    Button btnDate,btnDone;

    String requestType;
    String values,profileID;

    ProfileServicesRequestBE objProfileServicesRequestBE;
    ProfileServiceRequestBL objProfileServiceRequestBL;

    private TextView datePickerShowDialogButton;

    private int hour;

    private int minute;

    String strName,strEmail,strPhone,strDescription,strVehicle,strDate="";

    ProgressDialog progressDialog;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_service_request);
        initialize();
    }

    private void initialize(){
        etName= (EditText) findViewById(R.id.request_name);
        etEmail= (EditText) findViewById(R.id.request_email);
        etMobile= (EditText) findViewById(R.id.request_mobile);
        etDescription= (EditText) findViewById(R.id.request_message);
        etRegistration= (EditText) findViewById(R.id.request_registration);

        btnDate= (Button) findViewById(R.id.request_date);
        btnDone= (Button) findViewById(R.id.request_done);

        btnDone.setOnClickListener(this);
        btnDate.setOnClickListener(this);

        objProfileServiceRequestBL=new ProfileServiceRequestBL();
        objProfileServicesRequestBE=new ProfileServicesRequestBE();
        progressDialog=new ProgressDialog(ProfileServiceRequest.this);

        intent=getIntent();

        requestType=getIntent().getStringExtra("TYPE");
        values=getIntent().getStringExtra("Values");
        profileID=getIntent().getStringExtra("ID");

        objProfileServicesRequestBE.setType(requestType);
        objProfileServicesRequestBE.setServices(values);
        objProfileServicesRequestBE.setShowroomID(profileID);

        if(requestType.equalsIgnoreCase("Schedule")){
            btnDate.setVisibility(View.VISIBLE);
        }

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
            case R.id.request_date:
                showDatePicker();
                break;
            case R.id.request_done:
                if(validate()){
                    try {
                        objProfileServicesRequestBE.setContact(strPhone);
                        objProfileServicesRequestBE.setName(strName);
                        objProfileServicesRequestBE.setDescription(strDescription);
                        objProfileServicesRequestBE.setEmail(strEmail);
                        objProfileServicesRequestBE.setDate(strDate);
                        objProfileServicesRequestBE.setVehicle(strVehicle);

                        new SendRequest().execute();
                    }catch (Exception e){

                    }
                }
                break;
        }
    }

    public void showDatePicker() {
        // Initializiation
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        final AlertDialog.Builder dialogBuilder =
                new AlertDialog.Builder(this);
        View customView = inflater.inflate(R.layout.custom_datepicker, null);
        dialogBuilder.setView(customView);
        final Calendar now = Calendar.getInstance();
        final DatePicker datePicker =
                (DatePicker) customView.findViewById(R.id.dialog_datepicker);
        final TextView dateTextView =
                (TextView) customView.findViewById(R.id.dialog_dateview);
        final SimpleDateFormat dateViewFormatter =
                new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        final SimpleDateFormat formatter =
                new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        // Minimum date
        Calendar minDate = Calendar.getInstance();
        try {
            minDate.setTime(formatter.parse("12-12-2015"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePicker.setMinDate(minDate.getTimeInMillis());
        // View settings
        dialogBuilder.setTitle("Choose a date");
        dialogBuilder.setIcon(R.drawable.ic_logo_header);
        Calendar choosenDate = Calendar.getInstance();
        int year = choosenDate.get(Calendar.YEAR);
        int month = choosenDate.get(Calendar.MONTH);
        int day = choosenDate.get(Calendar.DAY_OF_MONTH);
        try {
            Date choosenDateFromUI = formatter.parse(
                    datePickerShowDialogButton.getText().toString()
            );
            choosenDate.setTime(choosenDateFromUI);
            year = choosenDate.get(Calendar.YEAR);
            month = choosenDate.get(Calendar.MONTH);
            day = choosenDate.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar dateToDisplay = Calendar.getInstance();
        dateToDisplay.set(year, month, day);
        dateTextView.setText(
                dateViewFormatter.format(dateToDisplay.getTime())
        );


        dialogBuilder.setPositiveButton(
                "Choose",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar choosen = Calendar.getInstance();
                        choosen.set(
                                datePicker.getYear(),
                                datePicker.getMonth(),
                                datePicker.getDayOfMonth()
                        );
                        btnDate.setText(
                                dateViewFormatter.format(choosen.getTime())
                        );
                        dialog.dismiss();

                    }
                }
        );
        final AlertDialog dialog = dialogBuilder.create();
        // Initialize datepicker in dialog atepicker
        datePicker.init(
                year,
                month,
                day,
                new DatePicker.OnDateChangedListener() {
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                        Calendar choosenDate = Calendar.getInstance();
                        choosenDate.set(year, monthOfYear, dayOfMonth);
                        dateTextView.setText(
                                dateViewFormatter.format(choosenDate.getTime())
                        );
                        if (choosenDate.get(Calendar.DAY_OF_WEEK) ==
                                Calendar.SUNDAY ||
                                now.compareTo(choosenDate) < 0) {
                            dateTextView.setTextColor(
                                    Color.parseColor("#000000")
                            );
                            ((Button) dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE))
                                    .setEnabled(true);
                        } else {
                            dateTextView.setTextColor(
                                    Color.parseColor("#000000")
                            );
                            ((Button) dialog.getButton(
                                    AlertDialog.BUTTON_POSITIVE))
                                    .setEnabled(true);
                        }
                    }
                }
        );
        // Finish
        dialog.show();
    }

    private boolean validate(){

        boolean flag=true;

        strName=etName.getText().toString();
        strEmail=etEmail.getText().toString();
        strPhone=etMobile.getText().toString();
        strVehicle=etRegistration.getText().toString();
        strDescription=etDescription.getText().toString();
        strDate=btnDate.getText().toString();

        if(strName.length()==0){
            etName.setError("required");
            flag=false;
        }

        if(strEmail.length()==0){
            etEmail.setError("required");
             flag=false;
        }

        if(strPhone.length()==0){
            etMobile.setError("required");
            flag=false;
        }

        if(strVehicle.length()==0){
            etRegistration.setError("required");
            flag=false;
        }

        if(strDescription.length()==0){
            etDescription.setError("required");
            flag=false;
        }

        if(requestType.equalsIgnoreCase("Schedule")) {
            if (strDate.length() == 0) {
                btnDate.setError("Required");
                flag = false;
            }
        }


        return flag;
    }

    private class SendRequest extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String result=objProfileServiceRequestBL.getServices(objProfileServicesRequestBE);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{

                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    Toast.makeText(getApplicationContext(),"Request Send Successfully",Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"Something went wrong? please try again.",Toast.LENGTH_SHORT).show();
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }

}

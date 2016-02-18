package com.careager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.careager.BL.RoadsideAssistanceBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.careager.gps.GPSTracker;
import com.google.android.gms.maps.GoogleMap;

public class RoadsideAssistance extends AppCompatActivity implements View.OnClickListener {

    EditText tvName,tvMessage;
    Button btnDone;

    static Double currentlongtitude;
    static Double currentlatitude;
    Location objLocation;
    RoadsideAssistanceBL objRoadsideAssistanceBL;
    ProgressDialog progressDialog;

    String name,description;

    int xx,yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_assistance);
        initialize();
    }



    private void initialize(){
        tvName= (EditText) findViewById(R.id.roadside_name);
        tvMessage= (EditText) findViewById(R.id.roadside_message);
        btnDone= (Button) findViewById(R.id.roadside_button);

        progressDialog=new ProgressDialog(RoadsideAssistance.this);

        btnDone.setOnClickListener(this);

        objRoadsideAssistanceBL=new RoadsideAssistanceBL();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        popupSize();

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            showDialogInternet(RoadsideAssistance.this);
            //Toast.makeText(getApplicationContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
        }else {

            getCurrentLocation();
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

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.roadside_button:

                final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    Toast.makeText(getApplicationContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        getCurrentLocation();

                        Log.d("LAT LONG", currentlatitude + "/" + currentlongtitude);
                        if (currentlatitude != null && currentlongtitude != null) {
                            // getCurrentAddress(currentlatitude,currentlongtitude);


                        } else{
                            getCurrentLocation();
                        }



                    }catch (NullPointerException e){

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

                name=tvName.getText().toString();
                description=tvMessage.getText().toString();

                if(name.length()==0){
                    tvName.setError("Required");
                }
                else if(description.length()==0){
                    tvMessage.setError("Required");
                }
                else {
                    if(Util.isInternetConnection(RoadsideAssistance.this)){
                        if (currentlatitude != null && currentlongtitude != null) {
                            new SendAssistance().execute(name, description, currentlatitude + "", currentlongtitude + "");
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Unable to fetch current location. Try again",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if (currentlatitude != null && currentlongtitude != null) {
                            Uri uri = Uri.parse("smsto: 09220592205");
                            Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                            it.putExtra("sms_body","77YYA EMERGENCY Name: "+name+",\nMessage: "+description+",\n"+"Location: "+currentlatitude+" , "+currentlongtitude);
                            startActivity(it);
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Unable to fetch current location. Try again",Toast.LENGTH_SHORT).show();

                    }
                }


                break;
        }
    }

    private class SendAssistance extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objRoadsideAssistanceBL.sendRoadSide(params[0],params[1],params[2],params[3]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    Uri uri = Uri.parse("smsto: 09220592205");
                    Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                    it.putExtra("sms_body","77YYA EMERGENCY Name: "+name+",\nMessage: "+description+",\n"+"Location: "+currentlatitude+" , "+currentlongtitude);
                    startActivity(it);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Something went wrong. Try again",Toast.LENGTH_SHORT).show();
                }
            }catch (NullPointerException e){

            }catch(Exception e){

            }
            finally {
                progressDialog.dismiss();
            }
        }
    }


    private void getCurrentLocation() {
        // TODO Auto-generated method stub
        objLocation = GPSTracker.getInstance(getApplicationContext()).getLocation();
        currentlatitude=objLocation.getLatitude();
        currentlongtitude=objLocation.getLongitude();
    }

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

        tvTitle.setText(getResources().getString(R.string.no_gps_title));
        tvMsg.setText(getResources().getString(R.string.no_gps_message));
        btnClosePopup.setText(getResources().getString(R.string._no_internet_cancel));
        btnsave.setText("OK");

        btnClosePopup.setVisibility(View.GONE);


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                //finish();
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
}

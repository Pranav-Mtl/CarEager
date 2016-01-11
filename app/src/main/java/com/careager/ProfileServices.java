package com.careager;

import android.app.Activity;
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
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.careager.Adapter.ProfileServicesAdapter;
import com.careager.BL.ProfileServicesBL;
import com.careager.Configuration.Util;
import com.careager.careager.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProfileServices extends AppCompatActivity implements View.OnClickListener {

    ProfileServicesBL objProfileServicesBL;

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    String servicesArray[];
    String subServices;
    String subServicesArray[];

    ProfileServicesAdapter profileServicesAdapter;
    ExpandableListView expView;

    String profileID;

    ProgressDialog progressDialog;

    Button btnQuote, btnSchedule;

    int xx, yy;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_services);
        initialize();

        if (Util.isInternetConnection(ProfileServices.this))
            new GetServices().execute(profileID);
        else
            showDialogInternet(ProfileServices.this);
    }

    private void initialize() {
        expView = (ExpandableListView) findViewById(R.id.expandable_list);
        btnQuote = (Button) findViewById(R.id.profile_request_quote);
        btnSchedule = (Button) findViewById(R.id.profile_schedule_service);

        objProfileServicesBL = new ProfileServicesBL();

        progressDialog = new ProgressDialog(ProfileServices.this);

        profileID = getIntent().getStringExtra("ID");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        popupSize();

        btnQuote.setOnClickListener(this);
        btnSchedule.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.profile_request_quote:
                if (profileServicesAdapter.hashServices.isEmpty()) {

                } else {
                    startActivityForResult(new Intent(getApplicationContext(), ProfileServiceRequest.class).putExtra("TYPE", "Quote").putExtra("Values", profileServicesAdapter.hashServices.values().toString().replace("[", "").replace("]", "")).putExtra("ID", profileID), 1);
                    //finish();
                    Log.d("values", profileServicesAdapter.hashServices.values().toString());
                }

                break;
            case R.id.profile_schedule_service:
                if (profileServicesAdapter.hashServices.isEmpty()) {

                } else {
                    Log.d("values", profileServicesAdapter.hashServices.values().toString());
                    startActivityForResult(new Intent(getApplicationContext(), ProfileServiceRequest.class).putExtra("TYPE", "Schedule").putExtra("Values", profileServicesAdapter.hashServices.values().toString().replace("[", "").replace("]", "")).putExtra("ID", profileID), 1);
                    //finish();
                }


                break;
        }
    }


    private class GetServices extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result = objProfileServicesBL.getServices(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                parseJson(s);
                prepareList();
                profileServicesAdapter = new ProfileServicesAdapter(getApplicationContext(), listDataHeader, listDataChild);
                expView.setAdapter(profileServicesAdapter);
            } catch (NullPointerException e) {

            } catch (Exception e) {
                showDialogResponse(ProfileServices.this);
            } finally {
                progressDialog.dismiss();
            }
        }
    }


    private void prepareList() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //String strSubRoute[]=new String[Constant.routeId.length];


        // Adding child data
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(subServices);
            JSONArray jsonArrayObject = (JSONArray) obj;
            subServicesArray = new String[servicesArray.length];
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            for (int i = 0; i < servicesArray.length; i++) {
                listDataHeader.add(servicesArray[i]);
                String ssParse = servicesArray[i];
                Log.d("SUB ARRAY", servicesArray[i]);
                subServicesArray[i] = String.valueOf(jsonObject.get(ssParse.trim()));

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        List<String> TrainerTutor;
        for (int i = 0; i < servicesArray.length; i++) {
            TrainerTutor = new ArrayList<String>();
            if (subServicesArray[i].equals("[]")) {
                listDataChild.put(listDataHeader.get(i), TrainerTutor);
            } else {
                String str;
                JSONParser jsonPp = new JSONParser();
                try {
                    Object obj = jsonPp.parse(subServicesArray[i]);
                    JSONArray jsonArrayObject = (JSONArray) obj;

                    for (int j = 0; j < jsonArrayObject.size(); j++) {
                        JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(j).toString());
                        str = jsonObject.get("service").toString();
                        //str = str + "," + jsonObject.get("price").toString();
                        TrainerTutor.add(str);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                listDataChild.put(listDataHeader.get(i), TrainerTutor);

            }
        }

    }

    private void parseJson(String result) {
        String services = "";
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            services = jsonObject.get("services").toString();
            servicesArray = services.replace("[", "").replace("]", "").replaceAll("\"", "").split(",");
            subServices = jsonObject.get("serviceslist").toString();

        } catch (Exception e) {
            e.getLocalizedMessage();
        }


    }

/*-----------------------------------------------------------*/

    private void popupSize() {
        Display display = getWindowManager().getDefaultDisplay();

        int width = display.getWidth();
        int height = display.getHeight();

        // System.out.println("width" + width + "height" + height);

        if (width >= 1000 && height >= 1500) {
            xx = 700;
            yy = 650;
        } else if (width >= 700 && height >= 1000) {
            xx = 550;
            yy = 500;
        } else {
            xx = 450;
            yy = 400;
        }

    }

    /* popup for no internet */
    private void showDialogInternet(Context context) {
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg, tvTitle;
        Button btnClosePopup, btnsave;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width = xx;
        wmlp.height = yy;


        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave = (Button) dialog.findViewById(R.id.popup_add);
        tvMsg = (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle = (TextView) dialog.findViewById(R.id.popup_title);

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
                                           finish();
                                       }
                                   }

        );


        dialog.show();
    }

    /* popup for no Response From Server*/
    private void showDialogResponse(Context context) {
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg, tvTitle;
        Button btnClosePopup, btnsave;

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width = xx;
        wmlp.height = yy;


        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave = (Button) dialog.findViewById(R.id.popup_add);
        tvMsg = (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle = (TextView) dialog.findViewById(R.id.popup_title);

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

                                           new GetServices().execute(profileID);
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            finish();
        }
    }
}
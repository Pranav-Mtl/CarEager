package com.careager;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.careager.BL.AddBusinessBL;
import com.careager.BL.SearchServiceBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;
import com.careager.careager.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddBusiness extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    Uri imageUri = null;
    ProgressDialog prgDialog;
    String encodedString;
    RequestParams params = new RequestParams();
    String imgPath, fileName;

    Bitmap bitmap;
    private static int RESULT_LOAD_IMG_DP= 1;
    private static int RESULT_LOAD_IMG_COVER= 2;

    private static int RESULT_camera_image_dp = 100;
    private static int RESULT_camera_image_cover = 101;


    private static int PIC_CROP_DP=10;
    private static int PIC_CROP_COVER=11;


    ArrayList addCategory,addCompany,addStates;

    EditText etName, etEmail, etPhone;


    Spinner spnCategory,spnCompany,spnStates;

    LinearLayout llDP;
    RelativeLayout rlCover;


    String userID;

    Button btnDone;

    AddBusinessBL objAddBusinessBL;

    ProgressDialog progressDialog;

    String lat,longt,address;

    Intent intent;

    int xx,yy;

    private String selectDP="Business DP";
    private String selectCover="Business Cover";

    ImageView ivDP,ivCOVER;
    TextView tvMessage;

    AutoCompleteTextView tvAdddress;

    private static final String LOG_TAG = "Careager";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyBugME8AtB66ogVSb0kZShmnlkSLwGusC4";

    String textBefore="This section is for adding any local car business on CarEager.\n" +
            "But if you are owner of a business, please visit  ";
    String textLinked=" “ BUSINESS SIGNUP ” ";
    String textAfter="  page to add your business on CarEager for free.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);

        initialize();

        String text = "<font color=#ff0000> <u> "+textLinked+" </u></font>";

        tvMessage.setText(Html.fromHtml(textBefore + text+ textAfter));

        tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userID==null){
                    startActivity(new Intent(getApplicationContext(), HowItWork.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Sorry you are already Logged In,Please logout first.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (Util.isInternetConnection(AddBusiness.this))
            new GetCategory().execute();

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem=parent.getItemAtPosition(position).toString();

                if(selectedItem.equalsIgnoreCase("Car Dealer (Authorised)") || selectedItem.equalsIgnoreCase("Service Station (Authorised)") ){
                    spnCompany.setVisibility(View.VISIBLE);
                }
                else
                    spnCompany.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

       /* spnCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });*/

            }

    private void initialize() {
        tvAdddress = (AutoCompleteTextView) findViewById(R.id.business_address);
        etName = (EditText) findViewById(R.id.business_name);
        etEmail = (EditText) findViewById(R.id.business_email);
        etPhone = (EditText) findViewById(R.id.business_mobile);
        btnDone = (Button) findViewById(R.id.business_done);
        llDP= (LinearLayout) findViewById(R.id.ll_business_dp);
        rlCover= (RelativeLayout) findViewById(R.id.rl_business_cover);
        ivDP= (ImageView) findViewById(R.id.business_dp);
        ivCOVER= (ImageView) findViewById(R.id.business_cover);
        tvMessage= (TextView) findViewById(R.id.tv_top_message);


        spnCategory = (Spinner) findViewById(R.id.business_category_list);
        spnCompany= (Spinner) findViewById(R.id.business_company_list);
        spnStates= (Spinner) findViewById(R.id.business_state_list);

        tvAdddress.setAdapter(new GooglePlacesAutocompleteAdapter(getApplicationContext(), R.layout.gender_spinner_item));
        tvAdddress.setOnItemClickListener(this);

        intent=getIntent();
        lat=intent.getStringExtra("Lat");
        longt=intent.getStringExtra("Long");


        prgDialog=new ProgressDialog(AddBusiness.this);

        userID=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        objAddBusinessBL = new AddBusinessBL();

        progressDialog = new ProgressDialog(AddBusiness.this);

        btnDone.setOnClickListener(this);
        llDP.setOnClickListener(this);
        rlCover.setOnClickListener(this);

        addCategory = new ArrayList();
        addCompany=new ArrayList();
        addStates=new ArrayList();
        addCategory.add("Select Category");
        addCompany.add("Select Company");
        addStates.add("Select State");

        popupSize();

        if (Build.VERSION.SDK_INT >= 23){
// Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }



    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String add=parent.getItemAtPosition(position).toString();
        //tvAdddress.setText(add);
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    android.widget.Filter.FilterResults filterResults = new Filter.FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        System.out.println(resultList);

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, android.widget.Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=geocode");
            sb.append("&sensor=false");
            sb.append("&language=en");
            sb.append("components=country:IN");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);

            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } catch (Exception e) {
            Log.e(LOG_TAG, "mmmmmmmm", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            org.json.JSONObject jsonObj = new org.json.JSONObject(jsonResults.toString());
            org.json.JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_done:
                String company="";
                String name = etName.getText().toString();
                String spn = spnCategory.getSelectedItem().toString();
                String states=spnStates.getSelectedItem().toString();
                address=tvAdddress.getText().toString();
                 company=spnCompany.getSelectedItem().toString();
                //String address=tvAdddress.getText().toString();
                if (name.length() == 0)
                    etName.setError("Required");
                else if (spn.equalsIgnoreCase("Select Category"))
                    Toast.makeText(getApplicationContext(), "Select Category", Toast.LENGTH_SHORT).show();
                else if(states.equalsIgnoreCase("Select State")){
                    Toast.makeText(getApplicationContext(), "Select state", Toast.LENGTH_SHORT).show();
                }
                else if(address.length()==0){
                    Toast.makeText(getApplicationContext(), "Enter address", Toast.LENGTH_SHORT).show();
                }
                else if(etPhone.getText().toString().length()==0){
                    etPhone.setError("Required");
                }
                else
                    if(Util.isInternetConnection(AddBusiness.this)){

                        params.put("name",name);
                        params.put("email",etEmail.getText().toString());
                        params.put("category",spn);
                        params.put("company",company);
                        params.put("state",states);
                        params.put("contact_no",etPhone.getText().toString());
                        params.put("lat",lat);
                        params.put("lng",longt);
                        params.put("location", address);

                        new VerifiedBusiness().execute(name,etEmail.getText().toString(),etPhone.getText().toString());

                    }

                    //new AddBuss().execute(name, etEmail.getText().toString(), spn, etPhone.getText().toString(), lat, longt,address);
                break;
            case R.id.ll_business_dp:
               //showDialogOptions(AddBusiness.this,selectDP);
                break;
            case R.id.rl_business_cover:
                showDialogOptions(AddBusiness.this,selectCover);
                break;
        }

    }



    private class GetCategory extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String text = "";
            String URL = "";

            text = RestFullWS.serverRequest(Constant.WS_PATH, URL, Constant.WS_CATEGORY);

            return text;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                String status = "";
                JSONParser jsonP = new JSONParser();
                try {
                    Object obj = jsonP.parse(s);
                    JSONArray jsonArrayObject = (JSONArray) obj;
                    JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
                    parseCategory(jsonObject.get("category").toString());
                    parseState(jsonObject.get("state").toString());
                    parseCompany(jsonObject.get("makerlist").toString());


                } catch (Exception e) {
                    e.getLocalizedMessage();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, addCategory);
                adapter.setDropDownViewResource(R.layout.spinner_item);
                spnCategory.setAdapter(adapter);

                ArrayAdapter<String> adapterState = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, addStates);
                adapterState.setDropDownViewResource(R.layout.spinner_item);
                spnStates.setAdapter(adapterState);

                ArrayAdapter<String> adapterCompany = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, addCompany);
                adapterCompany.setDropDownViewResource(R.layout.spinner_item);
                spnCompany.setAdapter(adapterCompany);

            } catch (NullPointerException e) {

            } catch (Exception e) {

            } finally {
                progressDialog.dismiss();
            }
        }


    }

    private void parseCategory(String result) {
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;


            for (int i = 0; i < jsonArrayObject.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                addCategory.add(jsonObject.get("category"));
            }


        } catch (Exception e) {

        }
    }

    private void parseCompany(String result) {
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;


            for (int i = 0; i < jsonArrayObject.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                addCompany.add(jsonObject.get("maker_name"));
            }


        } catch (Exception e) {

        }
    }

    private void parseState(String result) {
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;


            for (int i = 0; i < jsonArrayObject.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                addStates.add(jsonObject.get("state"));
            }


        } catch (Exception e) {

        }
    }



    private class AddBuss extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result = objAddBusinessBL.addBusiness(params[0], params[1], params[2], params[3], params[4], params[5],params[6]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if (Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)) {
                    Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK,intent);
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Something went wrong? please try again", Toast.LENGTH_SHORT).show();
            } catch (NullPointerException e) {

            } catch (Exception e) {

            } finally {
                progressDialog.dismiss();
            }
        }
    }



    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction ", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current loction", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction", "Canont get Address!");
        }
        return strAdd;
    }

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


    /* popup for no Response From Server*/
    private void showDialogOptions(Context context, final String title){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.upload_photo_option);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);

        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText(title);



        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    if (getApplicationContext().getPackageManager().hasSystemFeature(
                            PackageManager.FEATURE_CAMERA)) {

                        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                        File file = getOutputMediaFile(1);
                        imageUri = Uri.fromFile(file); // create
                        i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // set the image file

                        if (title.equalsIgnoreCase(selectDP))
                            startActivityForResult(i, RESULT_camera_image_dp);
                        else if (title.equalsIgnoreCase(selectCover))
                            startActivityForResult(i, RESULT_camera_image_cover);
                    }
                }catch (Exception e){

                }

                dialog.dismiss();

            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                                   android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                           // Start the Intent
                                           if(title.equalsIgnoreCase(selectDP))
                                           startActivityForResult(galleryIntent, RESULT_LOAD_IMG_DP);
                                           else if(title.equalsIgnoreCase(selectCover))
                                               startActivityForResult(galleryIntent, RESULT_LOAD_IMG_COVER);
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }


    private class VerifiedBusiness extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String URL="name="+params[0]+"&email="+params[1]+"&contact_no="+params[2];
            String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_VERIFIED_BUSINESS);
            return txtJson;

        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONParser jsonP=new JSONParser();

                    Object obj =jsonP.parse(s);
                    JSONArray jsonArrayObject = (JSONArray) obj;
                    JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
                    String status=jsonObject.get("status").toString();

                    if(status.equalsIgnoreCase(Constant.WS_RESPONSE_SUCCESS)){
                        if (imgPath != null && !imgPath.isEmpty()) {
                            uploadImage();
                            // When Image is not selected from Gallery
                        } else {
                            params.put("cover_filename", "");
                            params.put("cover_image", "");
                            triggerImageUpload();

                        }

                    }
                    else {
                        String message=jsonObject.get("message").toString();
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    }

                } catch (Exception ex) {
                    ex.getLocalizedMessage();
                }
            finally {
                progressDialog.dismiss();
            }

        }
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos

    }

    // When Image is selected from Gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("REQUEST CODE" + requestCode + "RESULT CODE" + resultCode);
        try {
            // When an Image is picked

            if (resultCode == RESULT_OK  && requestCode==RESULT_LOAD_IMG_COVER) {
                // Get the Image from data

                try {


                    System.out.println("Data" + data);


                    Uri selectedImage = data.getData();

                    Log.d("URL---->", selectedImage + "");

                    System.out.println("GALLERY SELECTED" + selectedImage);
                    System.out.println("CROPPED URI" + selectedImage);

                    imgPath = getPath(AddBusiness.this, selectedImage);
                /*String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();
*/
                    System.out.println("IMAGE PATH" + imgPath);

                    // Set the Image in ImageView
                    ivCOVER.setImageBitmap(BitmapFactory
                            .decodeFile(imgPath));


                    // Get the Image's file name
                    String fileNameSegments[] = imgPath.split("/");
                    fileName = fileNameSegments[fileNameSegments.length - 1];
                    // Put file name in Async Http Post Param which will used in Php web app
                    params.put("cover_filename", fileName);
                    //params.put("cover_filename", "");

                    //performCropCover(selectedImage);
                }catch (NullPointerException e){

                }catch (Exception e){

                }

            }

            else if(requestCode==RESULT_camera_image_cover && resultCode == RESULT_OK){
              /*  Toast.makeText(this, "You  picked Image from camera",
                        Toast.LENGTH_LONG).show();*/

                try {

                    System.out.println(imageUri);
                    Uri selectedImage = imageUri;
                    Log.d("URI-->", selectCover + "");
               /* String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(imageUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgPath = cursor.getString(columnIndex);
                cursor.close();*/
                    imgPath = getPath(AddBusiness.this, imageUri);
                    //imgPath=imgPath.replace(imgPath.substring(0, 7), "");

                    System.out.println("IMAGE PATH" + imgPath);

                    // Set the Image in ImageView
                    ivCOVER.setImageBitmap(BitmapFactory
                            .decodeFile(imgPath));
                    // Get the Image's file name
                    String fileNameSegments[] = imgPath.split("/");
                    fileName = fileNameSegments[fileNameSegments.length - 1];
                    // Put file name in Async Http Post Param which will used in Php web app
                    params.put("cover_filename", fileName);
                    //params.put("cover_filename", "");
                    // performCropCover(selectedImage);

                }catch (NullPointerException e){

                }catch (Exception e){

                }


            }
                else if (requestCode == PIC_CROP_COVER) {
                    if (data != null) {
                        // get the returned data
                        //Uri output=data.getData();

                        Bundle extras = data.getExtras();
                        // get the cropped bitmap
                        Bitmap selectedBitmap = extras.getParcelable("data");

                        //imgView.setImageBitmap(selectedBitmap);
                        Uri uriSelected = getImageUri(AddBusiness.this, selectedBitmap);
                        System.out.println("CROPPED URI" + uriSelected);

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        // Get the cursor
                        Cursor cursor = getContentResolver().query(uriSelected,
                                filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        imgPath = cursor.getString(columnIndex);
                        cursor.close();

                        System.out.println("IMAGE PATH" + imgPath);

                        // Set the Image in ImageView
                   /* imgView.setImageBitmap(BitmapFactory
                            .decodeFile(imgPath));
*/

                        // Get the Image's file name
                        ivCOVER.setImageBitmap(BitmapFactory
                                .decodeFile(imgPath));


                        // Get the Image's file name
                        String fileNameSegments[] = imgPath.split("/");
                        fileName = fileNameSegments[fileNameSegments.length - 1];
                        // Put file name in Async Http Post Param which will used in Php web app
                        params.put("cover_filename", fileName);
                        //params.put("cover_filename", "");

                    }
                    }

            else {
               /* Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();*/
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("cameraImageUri")) {
            imageUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
        }
    }

    /** Create a File for saving an image */
    private File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CarEager");

        /**Create the storage directory if it does not exist*/
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    private void performCrop(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 150);
            cropIntent.putExtra("outputY", 150);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP_DP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void performCropCover(Uri picUri) {
        try {

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 250);
            cropIntent.putExtra("outputY", 150);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP_COVER);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
        catch (Exception anfe) {
            // display an error message
            String errorMessage = "Something went wrong";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    public void uploadImage() {
        // When Image is selected from Gallery


        if (imgPath != null && !imgPath.isEmpty()) {
            prgDialog.setMessage("Loading...");
            prgDialog.show();
            // Convert image to String using Base64
            encodeImagetoString();
            // When Image is not selected from Gallery
        } else {

            Toast.makeText(
                    getApplicationContext(),
                    "You must select cover image",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void encodeImagetoString() {
        new AsyncTask<Void, Void, String>() {

            protected void onPreExecute() {

            }

            ;

            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imgPath,
                        options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                encodedString = Base64.encodeToString(byte_arr, 0);
                /*bitmapDP = BitmapFactory.decodeFile(imgPathProfile,
                        options);
                ByteArrayOutputStream streamDP = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                bitmapDP.compress(Bitmap.CompressFormat.PNG, 50, streamDP);
                byte[] byte_arrDP = streamDP.toByteArray();
                // Encode Image to String
                encodedStringDP = Base64.encodeToString(byte_arrDP, 0);*/
                return "";
            }

            @Override
            protected void onPostExecute(String msg) {

                prgDialog.setMessage("loading...");
                // Put converted Image string into Async Http Post param
                params.put("cover_image", encodedString);
                //params.put("cover_image", "");
                //params.put("profile_image",encodedStringDP);
                // Trigger Image upload
                System.out.println("tttttttttttttttttt" + encodedString);
                triggerImageUpload();
            }
        }.execute(null, null, null);
    }


    public void triggerImageUpload() {
        makeHTTPCall();
    }

    // http://192.168.2.4:9000/imgupload/upload_image.php
    // http://192.168.2.4:9999/ImageUploadWebApp/uploadimg.jsp
    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        prgDialog.setMessage("Loading...");
        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to your LAN address. Port no as well.
        Log.d("URL-->",params+"");
        client.post("http://www.careager.com/careager_webservices/add_business?",
                params, new AsyncHttpResponseHandler() {

                    // When the response returned by REST has Http
                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        Log.d("Response-->",response+"");
                        startActivity(new Intent(getApplicationContext(),HomeScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                        Toast.makeText(getApplicationContext(),"Details Uploaded Successfully. Business will be live after verification",
                                Toast.LENGTH_LONG).show();
                    }

                    // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured \n Most Common Error: \n1. Device not connected to Internet\n2. Web App is not deployed in App server\n3. App server is not running\n HTTP Status code : "
                                            + statusCode, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // Dismiss the progress bar when application is closed
        if (prgDialog != null) {
            prgDialog.dismiss();
        }

    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

}

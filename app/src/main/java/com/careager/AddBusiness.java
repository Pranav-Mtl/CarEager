package com.careager;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
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

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddBusiness extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    GoogleMap googleMap;
    MarkerOptions marker;

    ArrayList addCategory;

    EditText etName, etEmail, etPhone;
    AutoCompleteTextView tvAdddress;

    Spinner spnCategory;

    Double lat = 28.6100, longt = 77.2300;

    Marker markers;

    Button btnDone;

    AddBusinessBL objAddBusinessBL;

    ProgressDialog progressDialog;

    boolean mark=false;

    private static final String LOG_TAG = "Careager";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyBugME8AtB66ogVSb0kZShmnlkSLwGusC4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);

        initialize();

        if (Util.isInternetConnection(AddBusiness.this))
            new GetCategory().execute();


        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragEnd..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
                lat = arg0.getPosition().latitude;
                longt = arg0.getPosition().longitude;

                String add = getCurrentAddress(lat, longt);
                Log.d("Address",add);
                tvAdddress.setText(add);

                googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }
        });
    }

    private void initialize() {

        etName = (EditText) findViewById(R.id.business_name);
        etEmail = (EditText) findViewById(R.id.business_email);
        etPhone = (EditText) findViewById(R.id.business_mobile);
        btnDone = (Button) findViewById(R.id.business_done);
        tvAdddress = (AutoCompleteTextView) findViewById(R.id.business_address);

        spnCategory = (Spinner) findViewById(R.id.business_category_list);

        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        objAddBusinessBL = new AddBusinessBL();

        progressDialog = new ProgressDialog(AddBusiness.this);

        btnDone.setOnClickListener(this);

        addCategory = new ArrayList();
        addCategory.add("Select Category");


        tvAdddress.setAdapter(new GooglePlacesAutocompleteAdapter(getApplicationContext(), R.layout.gender_spinner_item));
        tvAdddress.setOnItemClickListener(this);
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

    private void initializeMap(Double lat, Double lon) {
        // TODO Auto-generated method stub


        LatLng latLng = new LatLng(lat, lon);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

        //LatLng latlng=new LatLng(Double.valueOf(CreateGameRecord.latitude[0]),Double.valueOf(CreateGameRecord.longitude[0]));

    }

    public void showMarker(Double Latitude, Double Longitude) {

        double latitude = Latitude;
        double longitude = Longitude;

        // create marker
        marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title("");
        try {

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
            marker.draggable(true);
            // adding marker
            markers = googleMap.addMarker(marker);
            mark=true;
            LatLng latLng = new LatLng(Latitude, Longitude);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_done:
                String name = etName.getText().toString();
                String spn = spnCategory.getSelectedItem().toString();
                String address=tvAdddress.getText().toString();
                if (name.length() == 0)
                    etName.setError("Required");
                else if (spn.equalsIgnoreCase("Select Category"))
                    Toast.makeText(getApplicationContext(), "Select Category", Toast.LENGTH_SHORT).show();
                else if (address.length() == 0)
                    tvAdddress.setError("Required");
                else
                    if(Util.isInternetConnection(AddBusiness.this))
                    new AddBuss().execute(name, etEmail.getText().toString(), spn, etPhone.getText().toString(), lat + "", longt + "",address);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String add=parent.getItemAtPosition(position).toString();
        latlong(add);
        if(mark)
        markers.remove();
        showMarker(lat,longt);

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
                initializeMap(lat, longt);
                showMarker(lat, longt);

                String status = "";
                JSONParser jsonP = new JSONParser();
                try {
                    Object obj = jsonP.parse(s);
                    JSONArray jsonArrayObject = (JSONArray) obj;
                    JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
                    parseCategory(jsonObject.get("category").toString());


                } catch (Exception e) {
                    e.getLocalizedMessage();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, addCategory);
                adapter.setDropDownViewResource(R.layout.spinner_item);
                spnCategory.setAdapter(adapter);

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


    private String getCurrentAddress(Double latitude, Double longitude) {
        String addressess="";
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            Log.d("Addresss", addresses + "");
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();


          addressess=address;

        } catch (Exception e) {

        }

        return  addressess;

}

    private void latlong(String strAddress){

            Geocoder coder = new Geocoder(this);
            List<Address> address;


            try {
                address = coder.getFromLocationName(strAddress,5);
                if (address==null) {

                }
                Address location=address.get(0);
                lat=location.getLatitude();
                longt=location.getLongitude();


            }
            catch (Exception e){

            }
        }



}

package com.careager;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.careager.careager.R;
import com.careager.gps.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddBusinessMap extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {

    GoogleMap googleMap;
    MarkerOptions marker;
    AutoCompleteTextView tvAdddress;

    Location objLocation;

    Button btnCurrentLocation,btnNext;

    Double lat = 28.6100, longt = 77.2300;

    Marker markers;

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
        setContentView(R.layout.activity_add_business_map);

        initialize();

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
                Log.d("Address", add);
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

    private void initialize(){
        tvAdddress = (AutoCompleteTextView) findViewById(R.id.business_address);
        btnCurrentLocation= (Button) findViewById(R.id.business_currentlocation);
        btnNext= (Button) findViewById(R.id.business_next);
        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();

        tvAdddress.setAdapter(new GooglePlacesAutocompleteAdapter(getApplicationContext(), R.layout.gender_spinner_item));
        tvAdddress.setOnItemClickListener(this);

        btnNext.setOnClickListener(this);
        btnCurrentLocation.setOnClickListener(this);

        initializeMap(lat, longt);
        showMarker(lat, longt);

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.business_currentlocation:

                final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    Toast.makeText(getApplicationContext(), "Please enable GPS", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        getCurrentLocation();

                        if (lat != null && longt != null) {
                            if(mark)
                                markers.remove();
                            showMarker(lat, longt);
                            String add = getCurrentAddress(lat, longt);
                            Log.d("Address", add);
                            tvAdddress.setText(add);

                        } else
                            Toast.makeText(getApplicationContext(), "Problem with location detection. Try again.", Toast.LENGTH_SHORT).show();
                    }catch (NullPointerException e){

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


                break;
            case R.id.business_next:
                if(tvAdddress.getText().toString().trim().length()>0) {
                    Intent intent = new Intent(getApplicationContext(), AddBusiness.class);
                    intent.putExtra("Address", tvAdddress.getText().toString());
                    intent.putExtra("Lat", lat + "");
                    intent.putExtra("Long", longt + "");
                    startActivityForResult(intent, 1);
                }
                else
                  tvAdddress.setError("Select Location");
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String add=parent.getItemAtPosition(position).toString();
        latlong(add);
        if(mark)
            markers.remove();
        showMarker(lat, longt);

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

    private String getCurrentAddress(Double latitude, Double longitude) {
        String addressess="";
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            Log.d("Addresss", addresses + "");
            String address = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();


            addressess=address+","+city;

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

    private void getCurrentLocation() {
        // TODO Auto-generated method stub
        objLocation = GPSTracker.getInstance(getApplicationContext()).getLocation();
        lat=objLocation.getLatitude();
        longt=objLocation.getLongitude();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            finish();
        }
    }
}

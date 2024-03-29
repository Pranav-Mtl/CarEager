package com.careager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.careager.BL.SearchServiceBL;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.careager.gps.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearbyMap extends AppCompatActivity  {

    GoogleMap googleMap;
    String location,category;
    SearchServiceBL objSearchServiceBL;

    ProgressDialog progressDialog;

    MarkerOptions marker,marker1;

    Location objLocation;
    Double lat = 28.6100, longt = 77.2300;

    boolean mark=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_map);
        initialize();

        new GetServices().execute(location, category);

    /*    googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
                // TODO Auto-generated method stub
                //Her            public void onMarkerDragStart(Marker marker) {
e your code
                String id[] = marker.getTitle().split(":");
                startActivity(new Intent(getApplicationContext(), DealerProfile.class).putExtra("ID", id[0]));
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // TODO Auto-generated method stub

            }
        });
*/


        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                try {
                    String id[] = marker.getTitle().split(":");
                    if(id!=null && id[0].length()>0)
                    startActivity(new Intent(getApplicationContext(), DealerProfile.class).putExtra("ID", id[0]));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

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

    private void initialize() {
        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();
        objSearchServiceBL=new SearchServiceBL();
        location=getIntent().getStringExtra("Location");
        category = getIntent().getStringExtra("Category");

        progressDialog=new ProgressDialog(NearbyMap.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initializeMap(Double lat,Double lon) {
        // TODO Auto-generated method stub


        LatLng latLng = new LatLng(lat, lon);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(false);


        //googleMap.setOnMarkerClickListener(this);


        //LatLng latlng=new LatLng(Double.valueOf(CreateGameRecord.latitude[0]),Double.valueOf(CreateGameRecord.longitude[0]));

    }

   /* @Override
    public boolean onMarkerClick(Marker marker) {

        //Toast.makeText(getApplicationContext(),marker.getTitle(),Toast.LENGTH_SHORT).show();
        marker.hideInfoWindow();
        String id[]=marker.getTitle().split(":");
        startActivity(new Intent(getApplicationContext(),DealerProfile.class).putExtra("ID",id[0]));
        return false;
    }
*/

    private class GetServices extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            objSearchServiceBL.getSearchServiceList(params[0], params[1]);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {

                    for (int i = 0; i < Constant.serviceLatitude.length; i++) {
                        if(i==0)
                            initializeMap(Constant.serviceLatitude[i],Constant.serviceLongitude[i]);

                        showMarker(Constant.serviceLatitude[i], Constant.serviceLongitude[i],Constant.serviceID[i]+": "+Constant.serviceName[i]+" ("+Constant.serviceCategory[i]+")",Constant.serviceAddress[i]+"\n Mob. "+Constant.serviceContact[i]);

                }
            }catch (NullPointerException e){

            }catch(Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }

    public void showMarker(Double Latitude, Double Longitude,String title,String snippet) {

        double latitude = Latitude;
        double longitude = Longitude;

        // create marker
         marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title(title).snippet(snippet);
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(final Marker marker) {

                // Getttrying view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.info_window_layout, null);
                Button btnnote;
                TextView noteSnippet;
                TextView note = (TextView) v.findViewById(R.id.note);
                btnnote = (Button) v.findViewById(R.id.note_button);
                noteSnippet = (TextView) v.findViewById(R.id.note_snippet);
                try {


                    // Getting reference to the TextView to set title


                    String ttl[] = marker.getTitle().split(":");
                    note.setText(ttl[1]);
                    noteSnippet.setText(marker.getSnippet());
                }catch (Exception e){
                    e.printStackTrace();
                    btnnote.setVisibility(View.GONE);
                    noteSnippet.setText("Current Location");

                }

               /* btnnote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String id[]=marker.getTitle().split(":");
                        startActivity(new Intent(getApplicationContext(),DealerProfile.class).putExtra("ID",id[0]));
                    }
                });*/

                // Returning the view containing InfoWindow contents
                return v;

            }

        });


        try{

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
            // adding marker
            googleMap.addMarker(marker);

        }catch(Exception e){}
    }

    @Override
    protected void onResume() {
        super.onResume();
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(getApplicationContext(), "Please enable GPS to know your current location", Toast.LENGTH_LONG).show();
        }else {
            try {
                getCurrentLocation();

                if (lat != null && longt != null) {

                    marker1 = new MarkerOptions().position(
                            new LatLng(lat, longt)).title("");
                    try {

                        marker1.icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longt),11));
                        // adding marker
                        if(!mark)
                        googleMap.addMarker(marker1);
                        mark=true;


                    } catch (Exception e) {
                    }


                } else
                    Toast.makeText(getApplicationContext(), "Problem with location detection. Try again.", Toast.LENGTH_SHORT).show();
            }catch (NullPointerException e){

            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void getCurrentLocation() {
        // TODO Auto-generated method stub
        objLocation = GPSTracker.getInstance(getApplicationContext()).getLocation();
        lat=objLocation.getLatitude();
        longt=objLocation.getLongitude();
    }
}

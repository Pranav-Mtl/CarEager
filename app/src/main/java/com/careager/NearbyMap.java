package com.careager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.careager.BL.SearchServiceBL;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class NearbyMap extends AppCompatActivity implements GoogleMap.OnMarkerClickListener {

    GoogleMap googleMap;
    String location,category;
    SearchServiceBL objSearchServiceBL;

    ProgressDialog progressDialog;

    MarkerOptions marker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_map);
        initialize();

        new GetServices().execute(location,category);



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

    private void initialize(){
        googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();
        objSearchServiceBL=new SearchServiceBL();
        location=getIntent().getStringExtra("Location");
        category=getIntent().getStringExtra("Category");

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
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

        googleMap.setOnMarkerClickListener(this);


        //LatLng latlng=new LatLng(Double.valueOf(CreateGameRecord.latitude[0]),Double.valueOf(CreateGameRecord.longitude[0]));

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        //Toast.makeText(getApplicationContext(),marker.getTitle(),Toast.LENGTH_SHORT).show();
        marker.hideInfoWindow();
        startActivity(new Intent(getApplicationContext(),Profile.class).putExtra("ID",marker.getTitle()));
        return false;
    }

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

                        showMarker(Constant.serviceLatitude[i], Constant.serviceLongitude[i],Constant.serviceID[i]);

                }
            }catch (NullPointerException e){

            }catch(Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }

    public void showMarker(Double Latitude, Double Longitude,String title) {

        double latitude = Latitude;
        double longitude = Longitude;

        // create marker
         marker = new MarkerOptions().position(
                new LatLng(latitude, longitude)).title(title);
        try{

            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
            // adding marker
            googleMap.addMarker(marker);

        }catch(Exception e){}
    }
}

package com.careager;


import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.careager.careager.R;
import com.careager.gps.GPSTracker;

import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements View.OnClickListener {

    EditText etLocation;
    Button btnCurrentLOcation,btnSearch;

    static Double currentlongtitude;
    static Double currentlatitude;
    Location objLocation;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_dealer_search, container, false);
        initialize(view);

        return view;
    }

    private void initialize(View view){
        etLocation= (EditText) view.findViewById(R.id.nearby_location);
        btnCurrentLOcation= (Button) view.findViewById(R.id.nearby_current_location);
        btnSearch= (Button) view.findViewById(R.id.nearby_search);

        btnCurrentLOcation.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nearby_current_location:
                final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    Toast.makeText(getActivity(), "Please enable GPS", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        getCurrentLocation();

                        Log.d("LAT LONG",currentlatitude+"/"+currentlongtitude);
                        if (currentlatitude != null && currentlongtitude != null) {
                            // getCurrentAddress(currentlatitude,currentlongtitude);
                            getCurrentAddress(currentlatitude, currentlongtitude);

                        } else
                            Toast.makeText(getActivity(), "Problem with location detection. Try again.", Toast.LENGTH_SHORT).show();
                    }catch (NullPointerException e){

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    }

                break;
            case R.id.nearby_search:
                if(etLocation.getText().toString().length()>0) {
                    Intent intent = new Intent(getActivity(), NearbyMap.class);
                    intent.putExtra("Location", etLocation.getText().toString());
                    intent.putExtra("Category", "");

                    startActivity(intent);
                }
                break;
        }

    }

    private void getCurrentLocation() {
        // TODO Auto-generated method stub
        objLocation = GPSTracker.getInstance(
                getActivity()).getLocation();
        currentlatitude=objLocation.getLatitude();
        currentlongtitude=objLocation.getLongitude();
    }

    private void getCurrentAddress(Double latitude,Double longitude){
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            Log.d("Addresss",addresses+"");
            String address = addresses.get(0).getAddressLine(1); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();

            etLocation.setText(address+","+city);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
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


}

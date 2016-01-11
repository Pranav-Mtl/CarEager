package com.careager;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.careager.BE.DealerProfileBE;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

public class DealerMyProfile extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvEmail,tvName,tvLocation;
    EditText etMobile,etOverview;
    ImageView ivProfile;

    DealerProfileBE objDealerProfileBE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_my_profile);

        initialize();
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
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvName= (TextView) findViewById(R.id.dealer_profile_name);
        tvEmail= (TextView) findViewById(R.id.dealer_profile_email);
        tvLocation= (TextView) findViewById(R.id.dealer_profile_location);

        etMobile= (EditText) findViewById(R.id.dealer_profile_mobile);
        etOverview= (EditText) findViewById(R.id.dealer_profile_overview);

        ivProfile= (ImageView) findViewById(R.id.dealer_profile_image);

        objDealerProfileBE= (DealerProfileBE) getIntent().getSerializableExtra("DealerProfileBE");

        tvName.setText(objDealerProfileBE.getName());
        tvEmail.setText(objDealerProfileBE.getEmail());
        tvLocation.setText(objDealerProfileBE.getLocation());

        etMobile.setText(objDealerProfileBE.getPhone());
        etOverview.setText(objDealerProfileBE.getOverview());

        Picasso.with(getApplicationContext())
                .load(objDealerProfileBE.getProfileBaseURL()+ objDealerProfileBE.getImage())
                .resize(80,80)
                .placeholder(R.drawable.ic_default_logo)
                .error(R.drawable.ic_default_logo)
                .into(ivProfile);
    }
}

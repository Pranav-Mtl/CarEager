package com.careager;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.careager.careager.BuildConfig;
import com.careager.careager.R;

public class AboutUs extends AppCompatActivity {

    TextView tvVersion;
    ImageView ivImage;

    TextView tvTC,tvPrivacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initialize();

        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AboutusWebView.class).putExtra("URL","http://www.careager.com/careager/privacy-policy"));
            }
        });

        tvTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AboutusWebView.class).putExtra("URL","http://www.careager.com/careager/terms-conditions"));
            }
        });
    }

    private void initialize(){
        tvVersion= (TextView) findViewById(R.id.about_version);
        ivImage= (ImageView) findViewById(R.id.about_image);
        tvTC= (TextView) findViewById(R.id.aboutus_tandc);
        tvPrivacy= (TextView) findViewById(R.id.aboutus_privacy);

        tvTC.setPaintFlags(tvTC.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvPrivacy.setPaintFlags(tvPrivacy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tvVersion.setText("Application version: "+ BuildConfig.VERSION_NAME);
        ivImage.setBackgroundResource(R.mipmap.ic_launcher);
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

}

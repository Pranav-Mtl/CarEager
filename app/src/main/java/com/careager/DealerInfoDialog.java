package com.careager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.careager.BE.DealerProfileSaleDetailBE;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

import static com.careager.careager.R.id.dialog_view_profile;

public class DealerInfoDialog extends AppCompatActivity implements View.OnClickListener {
    int pop_height,pop_width;

    TextView tvName,tvAddres,tvOverview;
    ImageView imageView;
    RatingBar ratingBar;

    Button btnViewProfile;

    DealerProfileSaleDetailBE objDealerProfileSaleDetailBE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_info_dialog);
        overridePendingTransition(R.animator.anim_in, R.animator.anim_out);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;



        if(width> 1000)
        {
            pop_height = 1500;
            pop_width = width;
        }
        else if (width > 700) {

            pop_height = 1000;
            pop_width = width;

        } else if (width > 500) {
            pop_height = 1000;
            pop_width = width;
        } else {
            pop_height = 1000;
            pop_width = width;
        }

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = pop_height;
        params.width = pop_width;
        this.getWindow().setAttributes(params);
        this.getWindow().setGravity(Gravity.BOTTOM);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(0));


        initialize();
    }

    private void initialize(){
        tvName= (TextView) findViewById(R.id.dialog_name);
        tvAddres= (TextView) findViewById(R.id.dialog_address);
        tvOverview= (TextView) findViewById(R.id.dialog_overview);
        imageView= (ImageView) findViewById(R.id.dialog_image);
        ratingBar= (RatingBar) findViewById(R.id.dialog_rating);
        btnViewProfile= (Button) findViewById(dialog_view_profile);

        btnViewProfile.setOnClickListener(this);

        objDealerProfileSaleDetailBE= (DealerProfileSaleDetailBE) getIntent().getSerializableExtra("DealerProfileSaleDetailBE");

        tvName.setText(objDealerProfileSaleDetailBE.getDealerName());
        tvOverview.setText(objDealerProfileSaleDetailBE.getDealerOverview());
        tvAddres.setText(objDealerProfileSaleDetailBE.getDealerLocation());

        Picasso.with(getApplicationContext())
                .load(objDealerProfileSaleDetailBE.getDealerAvatar()+objDealerProfileSaleDetailBE.getDealerImage())
                .resize(100,100)
                .placeholder(R.drawable.ic_default_logo)
                .error(R.drawable.ic_default_logo)
                .into(imageView);

        try {
            ratingBar.setRating(Integer.valueOf(objDealerProfileSaleDetailBE.getDealerRating()));
            ratingBar.setIsIndicator(true);
        }catch (Exception e){

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialog_view_profile:
                startActivity(new Intent(getApplicationContext(),Profile.class).putExtra("ID",objDealerProfileSaleDetailBE.getShowRoomID()));
                break;
        }
    }
}

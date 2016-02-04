package com.careager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.careager.BE.DealerProfileSaleDetailBE;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

import static com.careager.careager.R.id.dialog_view_profile;
import static com.careager.careager.R.id.ll_exteriour_feature;

public class DealerInfoDialog extends AppCompatActivity implements View.OnClickListener {
    int pop_height,pop_width;

    TextView tvName,tvAddres,tvOverview;
    ImageView imageView;
    RatingBar ratingBar;

    Button btnViewProfile;

    LinearLayout llCall,llEnquiry;

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

        llCall= (LinearLayout) findViewById(R.id.dealer_dialog_call);
        llEnquiry= (LinearLayout) findViewById(R.id.dealer_dialog_enquiry);

        btnViewProfile.setOnClickListener(this);
        llCall.setOnClickListener(this);
        llEnquiry.setOnClickListener(this);

        objDealerProfileSaleDetailBE= (DealerProfileSaleDetailBE) getIntent().getSerializableExtra("DealerProfileSaleDetailBE");

        tvName.setText(objDealerProfileSaleDetailBE.getDealerName());
        tvOverview.setText(objDealerProfileSaleDetailBE.getDealerOverview());
        tvAddres.setText(objDealerProfileSaleDetailBE.getDealerLocation());

        Picasso.with(getApplicationContext())
                .load(objDealerProfileSaleDetailBE.getDealerAvatar()+objDealerProfileSaleDetailBE.getDealerImage())
                .resize(100,100)
                .placeholder(R.drawable.ic_default_dp)
                .error(R.drawable.ic_default_dp)
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
                startActivity(new Intent(getApplicationContext(),DealerProfile.class).putExtra("ID",objDealerProfileSaleDetailBE.getShowRoomID()));
                break;
            case R.id.dealer_dialog_call:
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" +objDealerProfileSaleDetailBE.getDealerPhone()));
                    startActivity(callIntent);
                }catch (SecurityException e){

                }
                break;
            case R.id.dealer_dialog_enquiry:

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { objDealerProfileSaleDetailBE.getDealerEmail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
                break;
        }
    }
}

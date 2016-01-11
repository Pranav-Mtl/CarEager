package com.careager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careager.BE.DealerProfileSaleDetailBE;
import com.careager.BL.DealerProfileSaleDetailBL;
import com.careager.Configuration.Util;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

public class ProfileSaleDetail extends AppCompatActivity implements View.OnClickListener {

    TextView tvTitle,tvPrice,tvMaker,tvModel,tvVehicleType,tvYear,tvColor,tvStatus,tvMileage,tvGearType,tvFuelType,tvInStock;
    TextView tvInteriourFeature,tvExteriourFeature,tvSafetyFeature,tvExtraFeature,tvDescription;
    ImageView ivImage;

    DealerProfileSaleDetailBE objDealerProfileSaleDetailBE;
    DealerProfileSaleDetailBL objDealerProfileSaleDetailBL;

    ProgressDialog progressDialog;

    LinearLayout llInteriour,llExteriour,llSafety,llExtra,llDescription;

    boolean flagInteriour,flagExteriour,flagSafety,flagExtra,flagDescription;

    Intent intent;

    String saleID;

    Toolbar toolbar;

    Button btnDealerDetail;

    int xx,yy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_profile_sale_detail);
        initialize();

        if(Util.isInternetConnection(ProfileSaleDetail.this))
        new GetVehicleDetail().execute(saleID);
        else
            showDialogInternet(ProfileSaleDetail.this);

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
        tvTitle= (TextView) findViewById(R.id.detail_title);
        tvPrice= (TextView) findViewById(R.id.detail_price);
        tvMaker= (TextView) findViewById(R.id.detail_maker);
        tvModel= (TextView) findViewById(R.id.detail_model);
        tvVehicleType= (TextView) findViewById(R.id.detail_vehicle_type);
        tvYear= (TextView) findViewById(R.id.detail_year);
        tvColor= (TextView) findViewById(R.id.detail_color);
        tvStatus= (TextView) findViewById(R.id.detail_status);
        tvMileage= (TextView) findViewById(R.id.detail_mileage);
        tvGearType= (TextView) findViewById(R.id.detail_gear_type);
        tvFuelType= (TextView) findViewById(R.id.detail_fuel_type);
        tvInStock= (TextView) findViewById(R.id.detail_in_stock);
        tvInteriourFeature= (TextView) findViewById(R.id.sub_interiour_feature);
        tvExteriourFeature= (TextView) findViewById(R.id.sub_exteriour_feature);
        tvSafetyFeature= (TextView) findViewById(R.id.sub_safety_feature);
        tvExtraFeature= (TextView) findViewById(R.id.sub_extra_feature);
        tvDescription= (TextView) findViewById(R.id.sub_description);
        btnDealerDetail= (Button) findViewById(R.id.btn_dealer_details);

        btnDealerDetail.setOnClickListener(this);

        ivImage= (ImageView) findViewById(R.id.detail_image);

        llInteriour= (LinearLayout) findViewById(R.id.ll_interiour_feature);
        llExteriour= (LinearLayout) findViewById(R.id.ll_exteriour_feature);
        llSafety= (LinearLayout) findViewById(R.id.ll_safety_feature);
        llExtra= (LinearLayout) findViewById(R.id.ll_extra_feature);
        llDescription= (LinearLayout) findViewById(R.id.ll_description);

        popupSize();

        objDealerProfileSaleDetailBE=new DealerProfileSaleDetailBE();
        objDealerProfileSaleDetailBL=new DealerProfileSaleDetailBL();

        progressDialog=new ProgressDialog(ProfileSaleDetail.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        intent=getIntent();
        saleID=intent.getStringExtra("SaleID");

        llInteriour.setOnClickListener(this);
        llExteriour.setOnClickListener(this);
        llSafety.setOnClickListener(this);
        llExtra.setOnClickListener(this);
        llDescription.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_interiour_feature:
                if(!flagInteriour){
                    tvInteriourFeature.setVisibility(View.VISIBLE);
                    flagInteriour=true;
                }
                else {
                    tvInteriourFeature.setVisibility(View.GONE);
                    flagInteriour=false;
                }
                break;
            case R.id.ll_exteriour_feature:
                if(!flagExteriour){
                    tvExteriourFeature.setVisibility(View.VISIBLE);
                    flagExteriour=true;
                }
                else {
                    tvExteriourFeature.setVisibility(View.GONE);
                    flagExteriour=false;
                }
                break;
            case R.id.ll_safety_feature:
                if(!flagSafety){
                    tvSafetyFeature.setVisibility(View.VISIBLE);
                    flagSafety=true;
                }
                else {
                    tvSafetyFeature.setVisibility(View.GONE);
                    flagSafety=false;
                }
                break;
            case R.id.ll_extra_feature:
                if(!flagExtra){
                    tvExtraFeature.setVisibility(View.VISIBLE);
                    flagExtra=true;
                }
                else {
                    tvExtraFeature.setVisibility(View.GONE);
                    flagExtra=false;
                }
                break;
            case R.id.ll_description:
                if(!flagDescription){
                    tvDescription.setVisibility(View.VISIBLE);
                    flagDescription=true;
                }
                else {
                    tvDescription.setVisibility(View.GONE);
                    flagDescription=false;
                }
                break;
            case R.id.btn_dealer_details:
                    startActivity(new Intent(getApplicationContext(),DealerInfoDialog.class).putExtra("DealerProfileSaleDetailBE",objDealerProfileSaleDetailBE));
                break;
        }
    }

    private class GetVehicleDetail extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            objDealerProfileSaleDetailBL.getVehicleDetail(objDealerProfileSaleDetailBE,params[0]);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                tvTitle.setText(objDealerProfileSaleDetailBE.getTitle());
                tvPrice.setText("\u20B9 "+objDealerProfileSaleDetailBE.getPrice());
                tvMaker.setText("Maker: "+objDealerProfileSaleDetailBE.getMaker());
                tvModel.setText("Model: "+objDealerProfileSaleDetailBE.getModel());
                tvVehicleType.setText("Vehicle Type: "+objDealerProfileSaleDetailBE.getVehicleType());
                tvYear.setText("Year: "+objDealerProfileSaleDetailBE.getYear());
                tvColor.setText("Color: "+objDealerProfileSaleDetailBE.getColor());
                tvStatus.setText("Status: "+objDealerProfileSaleDetailBE.getStatus());
                tvMileage.setText("Mileage: "+objDealerProfileSaleDetailBE.getMileage());
                tvGearType.setText("Gear Type: "+objDealerProfileSaleDetailBE.getGearType());
                tvFuelType.setText("Fuel Type: "+objDealerProfileSaleDetailBE.getFuelType());
                tvInStock.setText("In Stock: "+objDealerProfileSaleDetailBE.getInStock());

                tvInteriourFeature.setText(objDealerProfileSaleDetailBE.getInteriour());
                tvExteriourFeature.setText(objDealerProfileSaleDetailBE.getExteriour());
                tvSafetyFeature.setText(objDealerProfileSaleDetailBE.getSafety());
                tvExtraFeature.setText(objDealerProfileSaleDetailBE.getExtra());
                tvDescription.setText(objDealerProfileSaleDetailBE.getDescription());

                Picasso.with(getApplicationContext())
                        .load(objDealerProfileSaleDetailBE.getBaseUrl()+objDealerProfileSaleDetailBE.getImage())
                        .placeholder(R.drawable.ic_default_loading)
                        .error(R.drawable.ic_default_loading)
                        .into(ivImage);

            }
            catch (NullPointerException e){
                showDialogResponse(ProfileSaleDetail.this);
            }
            catch (Exception e){

            }
            finally {
                progressDialog.dismiss();
            }
        }
    }


     /*-----------------------------------------------------------*/

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

    /* popup for no internet */
    private void showDialogInternet(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText(getResources().getString(R.string.no_internet_title));
        tvMsg.setText(getResources().getString(R.string.no_internet_message));
        btnClosePopup.setText(getResources().getString(R.string._no_internet_cancel));
        btnsave.setText(getResources().getString(R.string._no_internet_ok));

        btnClosePopup.setVisibility(View.GONE);


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {


                                           dialog.dismiss();
                                           finish();
                                       }
                                   }

        );


        dialog.show();
    }

    /* popup for no Response From Server*/
    private void showDialogResponse(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.common_popup);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);

        tvTitle.setText(getResources().getString(R.string._no_response_title));
        tvMsg.setText(getResources().getString(R.string._no_response_message));
        btnClosePopup.setText(getResources().getString(R.string._no_response_cancel));
        btnsave.setText(getResources().getString(R.string._no_response_save));

        //btnClosePopup.setVisibility(View.GONE);


        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(SellerQuestionExpandable.this,edittext.getText().toString(),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           new GetVehicleDetail().execute(saleID);
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

}

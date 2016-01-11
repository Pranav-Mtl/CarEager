package com.careager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.careager.Adapter.ProfileCategoryAdapter;
import com.careager.BE.DealerProfileBE;
import com.careager.BL.DealerProfileBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Profile extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    DealerProfileBL objDealerProfileBL;
    DealerProfileBE objDealerProfileBE;

    RecyclerView recList;

    ProgressDialog progressDialog;

    LinearLayout btnMenu;
    Button btnChat;

    TextView tvName,tvLocation,tvRating,tvOverView,tvRatingCareager;
    RatingBar rbUserRating,rbCareagerRating;

    TextView tvReviewNameOne,tvReviewNameTwo,tvReviewDateOne,tvReviewDateTwo,tvReviewCommentOne,tvReviewCommentTwo;
    RatingBar rbReviewOne,rbReviewTwo;
    ImageView ivReviewOne,ivReviewTwo;

    ImageView ivProfile;

    LinearLayout llReviewOne,llReviewTwo;

    LinearLayout llSubLayout;

    RelativeLayout rlMain;

    String categoryName[];
    int categoryID[];

    String profileID,userID,userType;

    ProfileCategoryAdapter objProfileCategoryAdapter;

    int xx,yy;

    boolean flag;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_profile);
        initialize();

        if(Util.isInternetConnection(getApplicationContext())){
            Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.SP_DEALER_CATEGORY,null);
            new GetProfileData().execute(profileID);  // call AsyncTask class to call WS in background and set data in post execute method
        }
        else
            showDialogInternet(Profile.this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        return true;
    }

    private void clearArray(){

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

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    private void initialize(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressDialog=new ProgressDialog(Profile.this);

        objDealerProfileBE=new DealerProfileBE();
        objDealerProfileBL=new DealerProfileBL();

        userID=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_ID);
        userType=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_TYPE);

        popupSize();

        profileID=getIntent().getStringExtra("ID");

        recList = (RecyclerView) findViewById(R.id.category_list);
        btnChat= (Button) findViewById(R.id.profile_chat);

        recList.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


        tvName= (TextView) findViewById(R.id.dealer_profile_name);
        tvLocation= (TextView) findViewById(R.id.dealer_profile_location);
        tvRating= (TextView) findViewById(R.id.dealer_profile_rating_text);
        tvOverView= (TextView) findViewById(R.id.dealer_profile_overview);
        tvRatingCareager= (TextView) findViewById(R.id.dealer_profile_rating_text_careager);
        rbUserRating= (RatingBar) findViewById(R.id.dealer_profile_rating);
        rbCareagerRating= (RatingBar) findViewById(R.id.dealer_profile_rating_careager);
        ivProfile= (ImageView) findViewById(R.id.profile_user_image);

        rlMain= (RelativeLayout) findViewById(R.id.profile_main);
        llSubLayout= (LinearLayout) findViewById(R.id.profile_ll);


        tvReviewNameOne= (TextView) findViewById(R.id.dealer_profile_review_name_one);
        tvReviewDateOne= (TextView) findViewById(R.id.dealer_profile_review_date_one);
        tvReviewCommentOne= (TextView) findViewById(R.id.dealer_profile_review_comment_one);
        rbReviewOne= (RatingBar) findViewById(R.id.dealer_profile_review_rating_one);
        ivReviewOne= (ImageView) findViewById(R.id.dealer_profile_review_image_one);

        tvReviewNameTwo= (TextView) findViewById(R.id.dealer_profile_review_name_two);
        tvReviewDateTwo= (TextView) findViewById(R.id.dealer_profile_review_date_two);
        tvReviewCommentTwo= (TextView) findViewById(R.id.dealer_profile_review_comment_two);
        rbReviewTwo= (RatingBar) findViewById(R.id.dealer_profile_review_rating_two);
        ivReviewTwo= (ImageView) findViewById(R.id.dealer_profile_review_image_two);

        llReviewOne= (LinearLayout) findViewById(R.id.profile_ll_review_one);
        llReviewTwo= (LinearLayout) findViewById(R.id.profile_ll_review_two);

        btnMenu= (LinearLayout) findViewById(R.id.profile_menu);

        btnMenu.setOnClickListener(this);
        rlMain.setOnClickListener(this);
        btnChat.setOnClickListener(this);
        llSubLayout.setOnClickListener(this);

        if(userID!=null)
            if(userType!=null)
                if(userType.equalsIgnoreCase(Constant.strLoginUser)){
                    if(!userID.equalsIgnoreCase(profileID))
                        btnChat.setVisibility(View.VISIBLE);
                }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_menu:
                if(!flag){
                recList.setVisibility(View.VISIBLE);
                flag=true;
                }
                else {
                    recList.setVisibility(View.GONE);
                    flag=false;
                }
                break;
            case R.id.profile_main:
                recList.setVisibility(View.GONE);
                break;
            case R.id.profile_chat:
                Intent intent=new Intent(getApplicationContext(),UserChat.class);
                intent.putExtra("ID",profileID);
                intent.putExtra("NAME",objDealerProfileBE.getName());
                startActivity(intent);

                break;
            case R.id.profile_ll:
                recList.setVisibility(View.GONE);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        recList.setVisibility(View.GONE);
        flag=false;
    }

    private class GetProfileData extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            objDealerProfileBL.getProfileData(params[0],objDealerProfileBE,getApplicationContext());
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                /* set basic info*/
                tvName.setText(objDealerProfileBE.getName());
                tvLocation.setText(objDealerProfileBE.getLocation());
                tvRating.setText(objDealerProfileBE.getRating() + " by " + objDealerProfileBE.getTotalRating() + " users");
                tvOverView.setText(objDealerProfileBE.getOverview());
                tvRatingCareager.setText("Rated " + objDealerProfileBE.getCareagerRating() + " by CarEager ");

                rbUserRating.setRating(Float.valueOf(objDealerProfileBE.getRating()));
                rbUserRating.setIsIndicator(true);

                Picasso.with(getApplicationContext())
                        .load(objDealerProfileBE.getProfileBaseURL() + objDealerProfileBE.getImage())
                        .resize(100,100)
                        .placeholder(R.drawable.ic_default_logo)
                        .error(R.drawable.ic_default_logo)
                        .into(ivProfile);

                if(!objDealerProfileBE.getCareagerRating().isEmpty()){
                rbCareagerRating.setRating(Float.valueOf(objDealerProfileBE.getCareagerRating()));
                rbCareagerRating.setIsIndicator(true);
                }

                objProfileCategoryAdapter=new ProfileCategoryAdapter(getApplicationContext(),profileID);
                recList.setAdapter(objProfileCategoryAdapter);

                try {

                    if (!(Constant.reviewName[0] == null)) { /* start of if statement*/
                        for (int i = 0; i < Constant.reviewName.length; i++) {

                            if (i == 0) {    /* set first review info*/
                                llReviewOne.setVisibility(View.VISIBLE);
                                tvReviewNameOne.setText(Constant.reviewName[i]);
                                tvReviewDateOne.setText(Constant.reviewDate[i]);
                                tvReviewCommentOne.setText(Constant.reviewComment[i]);
                                rbReviewOne.setRating(Constant.reviewRating[i]);
                                rbReviewTwo.setIsIndicator(true);
                                Picasso.with(getApplicationContext())
                                        .load(objDealerProfileBE.getProfileBaseURL() + Constant.reviewImage[i])
                                        .resize(70, 70)
                                        .placeholder(R.drawable.ic_default_logo)
                                        .error(R.drawable.ic_default_logo)
                                        .into(ivReviewOne);
                            } else if (i == 1) {    /* set second review info*/
                                llReviewTwo.setVisibility(View.VISIBLE);
                                tvReviewNameTwo.setText(Constant.reviewName[i]);
                                tvReviewDateTwo.setText(Constant.reviewDate[i]);
                                tvReviewCommentTwo.setText(Constant.reviewComment[i]);
                                rbReviewTwo.setRating(Constant.reviewRating[i]);
                                rbReviewTwo.setIsIndicator(true);
                                Picasso.with(getApplicationContext())
                                        .load(objDealerProfileBE.getProfileBaseURL() + Constant.reviewImage[i])
                                        .resize(70, 70)
                                        .placeholder(R.drawable.ic_default_logo)
                                        .error(R.drawable.ic_default_logo)
                                        .into(ivReviewTwo);
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }/* end of if statement*/


                //setMenu();




            }
            catch (NullPointerException e){
                e.printStackTrace();
                showDialogResponse(Profile.this);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {

                progressDialog.dismiss();
            }
        }
    }

    private void setMenu(Menu menu){


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

                                           new GetProfileData().execute(profileID);  // call AsyncTask class to call WS in background and set data in post execute method
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }


}

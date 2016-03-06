package com.careager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.careager.Adapter.DealerRatingAdapter;
import com.careager.Adapter.ProfileCategoryAdapter;
import com.careager.BE.DealerProfileBE;
import com.careager.BL.DealerProfileBL;
import com.careager.BL.FilterBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.Container.GalleryPagerContainer;
import com.careager.Container.PagerContainer;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DealerProfile extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btnChat;
    RecyclerView recList;

    ImageButton btnShare,btnBusinessLogo;
    ImageView ivVerified;

    ImageButton btnRateDealer;

    DealerProfileBL objDealerProfileBL;
    DealerProfileBE objDealerProfileBE;



    ProgressDialog progressDialog;

    TextView tvName,tvLocation,tvRating,tvOverView,tvRatingCareager,tvCategory;
    RatingBar rbUserRating,rbCareagerRating;

    ImageView llTopMain;

    ImageView ivProfile;

    String profileID,userID,userType;

    DealerRatingAdapter objDealerRatingAdapter;

    int xx,yy;

    ViewPager pager;

    GalleryPagerContainer mContainer;

    LinearLayout llSale,llService,llProduct,llInsurance,llCall,llEnquiry,llPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_profile2);
        initialize();

        if(Util.isInternetConnection(getApplicationContext())){
            new GetProfileData().execute(profileID);  // call AsyncTask class to call WS in background and set data in post execute method
        }
        else
            showDialogInternet(DealerProfile.this);
    }

    private void initialize() {
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvName= (TextView) findViewById(R.id.dealer_profile_name);
        tvLocation= (TextView) findViewById(R.id.dealer_profile_location);
        tvRating= (TextView) findViewById(R.id.dealer_profile_rating_text);
        tvOverView= (TextView) findViewById(R.id.dealer_profile_overview);
        tvRatingCareager= (TextView) findViewById(R.id.dealer_profile_rating_text_careager);
        rbUserRating= (RatingBar) findViewById(R.id.dealer_profile_rating);
        rbCareagerRating= (RatingBar) findViewById(R.id.dealer_profile_rating_careager);
        ivProfile= (ImageView) findViewById(R.id.profile_user_image);
        llTopMain= (ImageView) findViewById(R.id.ll_top_main);
        btnShare= (ImageButton) findViewById(R.id.profile_btn_share);
        btnBusinessLogo= (ImageButton) findViewById(R.id.profile_btn_business_logo);
        btnRateDealer= (ImageButton) findViewById(R.id.profile_review_btn);
        ivVerified= (ImageView) findViewById(R.id.dealer_verified);
        tvCategory= (TextView) findViewById(R.id.dealer_profile_category);

        mContainer = (GalleryPagerContainer) findViewById(R.id.pager_container_gallery);

        recList = (RecyclerView) findViewById(R.id.profile_review);
        btnChat= (LinearLayout) findViewById(R.id.profile_chat);

        llService= (LinearLayout) findViewById(R.id.profile_service);
        llSale= (LinearLayout) findViewById(R.id.profile_sale);
        llProduct= (LinearLayout) findViewById(R.id.profile_product);
        llInsurance= (LinearLayout) findViewById(R.id.profile_insurance);
        llCall= (LinearLayout) findViewById(R.id.profile_call);
        llPhotos= (LinearLayout) findViewById(R.id.ll_photos);
        llEnquiry= (LinearLayout) findViewById(R.id.profile_enquiry);

        llService.setOnClickListener(this);
        llSale.setOnClickListener(this);
        llProduct.setOnClickListener(this);
        llInsurance.setOnClickListener(this);
        llCall.setOnClickListener(this);
        llEnquiry.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnBusinessLogo.setOnClickListener(this);
        mContainer.setOnClickListener(this);
        btnRateDealer.setOnClickListener(this);

        recList.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        progressDialog=new ProgressDialog(DealerProfile.this);

        objDealerProfileBE=new DealerProfileBE();
        objDealerProfileBL=new DealerProfileBL();

        userID= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SP_LOGIN_ID);
        userType=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_TYPE);
        profileID=getIntent().getStringExtra("ID");


        if(userID!=null)
            if(userType!=null)
                if(userType.equalsIgnoreCase(Constant.strLoginUser)){
                    if(!userID.equalsIgnoreCase(profileID))
                        btnChat.setVisibility(View.VISIBLE);
                        btnRateDealer.setVisibility(View.VISIBLE);
                }

        btnChat.setOnClickListener(this);


        popupSize();

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

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.profile_sale:
                startActivity(new Intent(getApplicationContext(), ProfileSaleList.class).putExtra("ID", profileID));
                break;
            case R.id.profile_service:
                startActivity(new Intent(getApplicationContext(), ProfileServices.class).putExtra("ID", profileID));
                break;
            case R.id.profile_product:
                startActivity(new Intent(getApplicationContext(), ProfileProduct.class).putExtra("ID", profileID));
                break;
            case R.id.profile_insurance:
                startActivity(new Intent(getApplicationContext(), DealerContact.class).putExtra("CategoryName", "Insurance").putExtra("ID", profileID));
                break;
            case R.id.profile_call:
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + objDealerProfileBE.getPhone()));
                    startActivity(callIntent);
                }catch (SecurityException e){

                }
                break;
            case R.id.profile_enquiry:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { objDealerProfileBE.getEmail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, ""));
                break;
            case R.id.profile_btn_share:
                shareImage();
                break;
            case R.id.profile_btn_business_logo:
                Intent claimIntent=new Intent(getApplicationContext(),ClaimBusinessPre.class);
                claimIntent.putExtra("Id",profileID);
                claimIntent.putExtra("Name",objDealerProfileBE.getName());
                startActivity(claimIntent);
                break;
            case R.id.profile_chat:
                Intent intentChat=new Intent(getApplicationContext(),UserChat.class);
                intentChat.putExtra("ID",profileID);
                intentChat.putExtra("NAME",objDealerProfileBE.getName());
                startActivity(intentChat);

                break;
            case R.id.profile_review_btn:
                Intent intentReview=new Intent(getApplicationContext(),DealerReview.class);
                intentReview.putExtra("ID",profileID);
                startActivityForResult(intentReview, 2);
                break;

        }
    }


    private class GetProfileData extends AsyncTask<String,String,String> {

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
                //tvRating.setText(objDealerProfileBE.getRating() + " by " + objDealerProfileBE.getTotalRating() + " users");
                tvRating.setText("User rating");
                tvOverView.setText(objDealerProfileBE.getOverview());
                tvRatingCareager.setText("CarEager rating");

                tvCategory.setText("("+objDealerProfileBE.getCategory()+")");

                rbUserRating.setRating(Float.valueOf(objDealerProfileBE.getRating()));
                rbUserRating.setIsIndicator(true);

                Picasso.with(getApplicationContext())
                        .load(objDealerProfileBE.getProfileBaseURL() + objDealerProfileBE.getImage())
                        .resize(100, 100)
                        .placeholder(R.drawable.ic_default_dp)
                        .error(R.drawable.ic_default_dp)
                        .into(ivProfile);

                Picasso.with(getApplicationContext())
                        .load(objDealerProfileBE.getProfileBaseURL() + objDealerProfileBE.getCover())
                        .placeholder(R.drawable.ic_default_cover)
                        .error(R.drawable.ic_default_cover)
                        .into(llTopMain);

                if(!objDealerProfileBE.getCareagerRating().isEmpty()){
                    rbCareagerRating.setRating(Float.valueOf(objDealerProfileBE.getCareagerRating()));
                    rbCareagerRating.setIsIndicator(true);
                }

                objDealerRatingAdapter=new DealerRatingAdapter(getApplicationContext());
                recList.setAdapter(objDealerRatingAdapter);

                if(objDealerProfileBE.getApproved().equalsIgnoreCase("0")){
                    ivVerified.setVisibility(View.GONE);
                }
                else
                {
                    ivVerified.setVisibility(View.VISIBLE);
                }


                for(int i=0;i<Constant.categoryTab.length;i++){
                    if(Constant.categoryTab[i].equalsIgnoreCase(Constant.tagSales))
                        llSale.setVisibility(View.VISIBLE);
                    else if(Constant.categoryTab[i].equalsIgnoreCase(Constant.tagService))
                        llService.setVisibility(View.VISIBLE);
                    else if(Constant.categoryTab[i].equalsIgnoreCase(Constant.tagProduct))
                        llProduct.setVisibility(View.VISIBLE);
                    else if(Constant.categoryTab[i].equalsIgnoreCase(Constant.tagInsurance))
                        llInsurance.setVisibility(View.VISIBLE);
                }


                ViewPager pager = mContainer.getViewPager();
                //ViewPager pager=(ViewPager) view.findViewById(R.id.pager);
                mContainer=new GalleryPagerContainer(getApplicationContext());

                try {
                    GalleryPageAdapter adapter = new GalleryPageAdapter(getApplicationContext());
                    pager.setAdapter(adapter);
                    pager.setOffscreenPageLimit(adapter.getCount());
                    //A little space between pages
                    pager.setPageMargin(5);
                    //If hardware acceleration is enabled, you should also remove
                    // clipping on the pager for its children.
                    pager.setClipChildren(false);
                    pager.setPadding(5, 0, 0, 0);

                    int size = adapter.getCount();
                    if (size == 0) {
                        llPhotos.setVisibility(View.GONE);
                    }
                }catch (NullPointerException e){

                }catch (Exception e){

                }




                //setMenu();




            }
            catch (NullPointerException e){
                e.printStackTrace();
                //showDialogResponse(DealerProfile.this);
            }
            catch (Exception e){
                e.printStackTrace();
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

                                           new GetProfileData().execute(profileID);  // call AsyncTask class to call WS in background and set data in post execute method
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }


    private class GalleryPageAdapter extends PagerAdapter {
        Context context;
        LayoutInflater inflater;
        ImageView imgPager;

        GalleryPageAdapter(Context contex) {
            this.context = contex;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.gallery_pager_raw, container,
                    false);

            imgPager= (ImageView) itemView.findViewById(R.id.gallery_images);

            Picasso.with(getApplicationContext())
                    .load(Constant.galleryBaseURL+Constant.galleryImages[position])
                    .resize(50,50)
                    .placeholder(R.drawable.ic_default_logo)
                    .error(R.drawable.ic_default_logo)
                    .into(imgPager);

            imgPager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),ProfileGallery.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });


            ((ViewPager) container).addView(itemView);

            return itemView;
        }

        @Override
        public int getCount() {
            if(Constant.galleryImages==null)
                return 0;
            return Constant.galleryImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        public float getPageWidth(int position)
        {
            return .2f;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    public void shareImage(){
        String path= Environment.getExternalStorageDirectory()+ File.separator+"Screenshots.jpeg";
        File imageFile=new File(path);
        // create bitmap screen capture
        DisplayMetrics dm = getResources().getDisplayMetrics();
        View v = getWindow().getDecorView().findViewById(R.id.coordinatorLayouts).getRootView();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);
        // v1.setDrawingCacheEnabled( false);
        OutputStream fout = null ;
        try {
            fout = new FileOutputStream(imageFile);
            returnedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();
            //Toast.makeText(activity, "Image saved!", Toast.LENGTH_SHORT).show();
        } catch ( FileNotFoundException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), "File not found!", Toast.LENGTH_SHORT).show();
            // e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(getApplicationContext(), "IO Exception!", Toast.LENGTH_SHORT).show();
            // e.printStackTrace();
        }

        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(path)));
        i.putExtra(Intent.EXTRA_TEXT, "#GetAPP"+"\n"+"https://play.google.com/store/apps/details?id=com.car.careager");
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            new GetProfileData().execute(profileID);
        }
    }
}

package com.careager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.careager.BL.LatestOffersBL;
import com.careager.Constant.Constant;
import com.careager.Container.PagerContainer;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

public class LatestOffers extends AppCompatActivity {

    private static final float MIN_SCALE = 0.85f;
    LatestOffersBL objLatestOffersBL;
    PagerContainer mContainer,showRoomContainer,carEagerContainer;
    ViewPager pager,showRoomPager,carEagerPager;

    ProgressDialog progressDialog;

    int xx,yy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_offers);
        initialize();

        new GetLatestOffer().execute();
    }

    private void initialize() {

        objLatestOffersBL = new LatestOffersBL();
        progressDialog=new ProgressDialog(LatestOffers.this);

        mContainer = (PagerContainer) findViewById(R.id.cars_container);
        showRoomContainer= (PagerContainer) findViewById(R.id.showroom_container);
        carEagerContainer= (PagerContainer) findViewById(R.id.careager_container);

        showRoomPager=showRoomContainer.getViewPager();
        carEagerPager=carEagerContainer.getViewPager();
        pager = mContainer.getViewPager();

        //ViewPager pager=(ViewPager) view.findViewById(R.id.pager);
        //pager.setPageTransformer(true, new BigImage());

        mContainer = new PagerContainer(getApplicationContext());
        showRoomContainer=new PagerContainer(getApplicationContext());
        carEagerContainer=new PagerContainer(getApplicationContext());

        //PagerContainer.setLayout(getActivity(),llCareager,llOldNewCar,llServices);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        popupSize();


    }

     /*-----------------------------------------------------------*/

    private void popupSize(){
        Display display = getWindowManager().getDefaultDisplay();

        int width = display.getWidth();
        int height = display.getHeight();

        // System.out.println("width" + width + "height" + height);

        if(width>=1000 && height>=1500){
            xx=width;
            yy=650;
        }
        else if(width>=700 && height>=1000)
        {
            xx=width;
            yy=500;
        }
        else
        {
            xx=width;
            yy=400;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            //Toast.makeText(getApplicationContext(),"BAck Clicked",Toast.LENGTH_SHORT).show();
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class CarPageAdapter extends PagerAdapter {
        Context context;
        LayoutInflater inflater;
        ImageView imgPager;
        TextView tvYear,tvTitle,tvOffer,tvPosted;

        CarPageAdapter(Context contex) {
            this.context = contex;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {


            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.car_offer_raw, container,
                    false);

            imgPager= (ImageView) itemView.findViewById(R.id.car_offer_image);
            tvYear= (TextView) itemView.findViewById(R.id.car_offer_year);
            tvTitle= (TextView) itemView.findViewById(R.id.car_offer_name);
            tvOffer= (TextView) itemView.findViewById(R.id.car_offer);
            tvPosted= (TextView) itemView.findViewById(R.id.car_offer_posted);

            Picasso.with(context)
                    .load(Constant.carOfferBaseURL + Constant.carOfferImage[position])
                    .placeholder(R.drawable.ic_default_loading)
                    .error(R.drawable.ic_default_loading)
                    .into(imgPager);
            tvTitle.setText(Constant.carOfferTitle[position]);
            tvOffer.setText(Constant.carOffer[position]);
            tvPosted.setText("Posted by "+Constant.carOfferPosted[position]);

            imgPager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), ProfileSaleDetail.class).putExtra("SaleID", Constant.carOfferID[position]));
                }
            });

            tvPosted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), DealerProfile.class).putExtra("ID", Constant.carOfferShowroomID[position]));
                }
            });

            ((ViewPager) container).addView(itemView);

            return itemView;
        }

        @Override
        public int getCount() {
            return Constant.carOfferID.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        public float getPageWidth(int position)
        {
            return .9f;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    private class ShowRoomPageAdapter extends PagerAdapter {
        Context context;
        LayoutInflater inflater;
        ImageView imgPager;
        TextView tvYear,tvTitle,tvOffer,tvPosted;

        ShowRoomPageAdapter(Context contex) {
            this.context = contex;
        }

        @Override
        public Object instantiateItem(ViewGroup container,final int position) {


            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.showroom_offer, container,
                    false);

            imgPager= (ImageView) itemView.findViewById(R.id.showroom_offer_image);

            tvOffer= (TextView) itemView.findViewById(R.id.showroom_offer);

            RelativeLayout rlMain= (RelativeLayout) itemView.findViewById(R.id.ll_showroomoffer);


            Picasso.with(context)
                    .load(Constant.showroomOfferBaseURL + Constant.showroomOfferImage[position])
                    .placeholder(R.drawable.ic_default_loading)
                    .error(R.drawable.ic_default_loading)
                    .into(imgPager);

            tvOffer.setText(Constant.showroomOffer[position]);

            rlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), DealerProfile.class).putExtra("ID", Constant.showroomOfferID[position]));
                }
            });



            ((ViewPager) container).addView(itemView);

            return itemView;
        }

        @Override
        public int getCount() {
            return Constant.showroomOfferID.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        public float getPageWidth(int position)
        {
            return .9f;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /*----------------------------------*/


    private class CarEagerPageAdapter extends PagerAdapter {
        Context context;
        LayoutInflater inflater;
        ImageView imgPager;
        TextView tvYear,tvTitle,tvOffer,tvPosted;

        CarEagerPageAdapter(Context contex) {
            this.context = contex;
        }

        @Override
        public Object instantiateItem(ViewGroup container,final int position) {


            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.careager_offer_raw, container,
                    false);

            imgPager= (ImageView) itemView.findViewById(R.id.careager_offer_image);

            tvOffer= (TextView) itemView.findViewById(R.id.careager_offer);


            imgPager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogImage(LatestOffers.this,Constant.carEagerOfferBaseURL + Constant.carEagerOfferImage[position]);
                }
            });


            Picasso.with(context)
                    .load(Constant.carEagerOfferBaseURL + Constant.carEagerOfferImage[position])
                    .placeholder(R.drawable.ic_default_loading)
                    .error(R.drawable.ic_default_loading)
                    .into(imgPager);

            tvOffer.setText(Constant.carEagerOffer[position]);



            ((ViewPager) container).addView(itemView);

            return itemView;
        }

        @Override
        public int getCount() {
            return Constant.carEagerOfferID.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        public float getPageWidth(int position)
        {
            return .9f;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    private class BigImage implements ViewPager.PageTransformer{

        @Override
        public void transformPage(View page, float position) {
            int pageWidth = page.getWidth();
            int pageHeight = page.getHeight();
            if (position < -1) { // [-Infinity,-1)

                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);


            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    page.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    page.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                // page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                //page.setBackground(getR.drawable.ball2);

                page.clearAnimation();
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
//            view.setAlpha(MIN_ALPHA);
                page.setScaleX(MIN_SCALE);
                page.setScaleY(MIN_SCALE);

            }
        }
    }

    private class GetLatestOffer extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            objLatestOffersBL.getOffers();
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {

                try {
                    CarPageAdapter adapter = new CarPageAdapter(getApplicationContext());
                    pager.setAdapter(adapter);
                    pager.setOffscreenPageLimit(adapter.getCount());
                    //A little space between pages
                    pager.setPageMargin(10);
                    //If hardware acceleration is enabled, you should also remove
                    // clipping on the pager for its children.
                    pager.setClipChildren(false);
                    pager.setPadding(10, 0, 10, 0);
                }catch (Exception e){

                }
            /*----------------------------------*/

                try {
                    ShowRoomPageAdapter adapterShow = new ShowRoomPageAdapter(getApplicationContext());
                    showRoomPager.setAdapter(adapterShow);
                    showRoomPager.setOffscreenPageLimit(adapterShow.getCount());
                    showRoomPager.setPageMargin(10);
                    //A little space between pages
                    //pager.setPageMargin(15);
                    //If hardware acceleration is enabled, you should also remove
                    // clipping on the pager for its children.
                    showRoomPager.setClipChildren(false);
                    showRoomPager.setPadding(10, 0, 10, 0);
                }catch (Exception e){

                }


             /*----------------------------------*/
                try {
                    CarEagerPageAdapter adapterCareager = new CarEagerPageAdapter(getApplicationContext());
                    carEagerPager.setAdapter(adapterCareager);
                    carEagerPager.setOffscreenPageLimit(adapterCareager.getCount());
                    carEagerPager.setPageMargin(10);
                    //A little space between pages
                    //pager.setPageMargin(15);
                    //If hardware acceleration is enabled, you should also remove
                    // clipping on the pager for its children.
                    carEagerPager.setClipChildren(false);
                    carEagerPager.setPadding(10, 0, 10, 0);
                }catch (Exception e){

                }

            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }


    private void showDialogImage(Context context,String url){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        ImageView inImage;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_big_image);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;


        inImage= (ImageView) dialog.findViewById(R.id.image_big);

        Picasso.with(getApplicationContext())
                .load(url)
                .placeholder(R.drawable.ic_default_loading)
                .error(R.drawable.ic_default_loading)
                .into(inImage);


        dialog.show();
    }

}
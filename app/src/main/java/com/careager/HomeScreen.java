package com.careager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.careager.Adapter.DrawerAdapter;
import com.careager.BL.HomeScreenBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.UI.RecyclerItemClickListener;
import com.careager.careager.R;

import java.util.Locale;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener {

    ViewPager mViewPager;
    PagerSlidingTabStrip tabs;
    public static int width = 0;

    CustomPagerAdapter mCustomPagerAdapter;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout
    ActionBarDrawerToggle mDrawerToggle;

    DrawerAdapter drawerAdapter;

    String userType,userID;

    HomeScreenBL objHomeScreenBL;

    ProgressDialog progressDialog;

    int xx,yy;
    View _itemColoured;

    FloatingActionButton btnChat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_home_screen);
        initialize();



        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (position != 0) {
                            if (_itemColoured != null) {
                                _itemColoured.setBackgroundColor(Color.TRANSPARENT);
                                _itemColoured.invalidate();
                            }
                            _itemColoured = view;
                            view.setBackgroundColor(getResources().getColor(R.color.redColor));
                        }

                        if (position == 0) {
                            if (userID != null) {
                                if (userType != null)
                                    if (!userType.equalsIgnoreCase(Constant.strLoginUser))
                                        startActivity(new Intent(getApplicationContext(), DealerProfile.class).putExtra("ID", userID));

                            }
                            else
                                startActivity(new Intent(getApplicationContext(), HowItWork.class));

                        } else if (position == 1) {
                            startActivity(new Intent(getApplicationContext(), LatestOffers.class));
                        }else if (position == 2) {
                            startActivity(new Intent(getApplicationContext(), TipsAndAdvice.class));
                        }
                        else if (position == 3) {
                            startActivity(new Intent(getApplicationContext(), AddBusinessMap.class));
                        }
                        else if (position == 4) {
                            startActivity(new Intent(getApplicationContext(), RoadsideAssistance.class));
                        }
                        else if (position == 5) {
                            startActivity(new Intent(getApplicationContext(), AboutUs.class));
                        }
                        else if (position == 6) {
                            startActivity(new Intent(getApplicationContext(), Settings.class));
                        }
                        else if (position == 7) {
                            if(Util.isInternetConnection(HomeScreen.this))
                           new Logout().execute(userID,userType);
                        }

                    }

                }));

        if(Util.isInternetConnection(HomeScreen.this))
        new GetAllLogo().execute(userID,userType);
        else
            showDialogInternet(HomeScreen.this);
    }


        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initialize(){
        btnChat= (FloatingActionButton) findViewById(R.id.chatFabButton);
        mViewPager= (ViewPager) findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(4);
        tabs= (PagerSlidingTabStrip) findViewById(R.id.pager_tab_strip);



        tabs.setTextColor(getResources().getColor(R.color.tabTextColor));
        tabs.setIndicatorColor(getResources().getColor(R.color.tabSlidingIndicator));
        tabs.setTabBackground(Color.parseColor("#000000"));

        tabs.setDividerColor(getResources().getColor(R.color.tabDivider));

        //tabs.setShouldExpand(true);
      /*  tabs.setUnderlineColor(getResources().getColor(R.color.tabUnderLineColor));
        tabs.setUnderlineHeight(5);*/
        tabs.setIndicatorHeight(8);

        popupSize();

        progressDialog=new ProgressDialog(HomeScreen.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View
        mRecyclerView.setHasFixedSize(true);

        userID=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_ID);
        if(userID==null)
            drawerAdapter = new DrawerAdapter(Constant.TITLES_LOGOUT, Constant.ICONS_LOGOUT,"Sign in", getApplicationContext());       // Creating the Adapter of com.example.balram.sampleactionbar.MyAdapter class(which we are going to see in a bit)
        else
        drawerAdapter = new DrawerAdapter(Constant.TITLES_LOGIN, Constant.ICONS,Constant.NAME, getApplicationContext());       // Creating the Adapter of com.example.balram.sampleactionbar.MyAdapter class(which we are going to see in a bit)

        // And passing the titles,icons,header view name, header view email,
        // and header  view profile picture
        mRecyclerView.setAdapter(drawerAdapter);

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        mRecyclerView.setLayoutManager(mLayoutManager);

        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }


        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();

        userID=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_ID);
        userType=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_TYPE);
        //Log.d("LOGIN TYPE-->",userType);

        objHomeScreenBL=new HomeScreenBL();

        btnChat.setOnClickListener(this);

        if(userID!=null)
            if(userType!=null)
                    btnChat.setVisibility(View.VISIBLE);




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chatFabButton:
                if(userType!=null){
                    if(userType.equalsIgnoreCase(Constant.strLoginUser))
                        startActivity(new Intent(getApplicationContext(),ForumUserList.class));
                    else if(userType.equalsIgnoreCase(Constant.strLoginBusiness))
                        startActivity(new Intent(getApplicationContext(),DealerChatList.class));

                }

                break;
        }
    }

    class CustomPagerAdapter extends FragmentPagerAdapter {

        Context mContext;

        public CustomPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {

            // Create fragment object
            switch (position) {
                case 0:
                    // Top Rated fragment activity
                    return new HomeFragment();
                case 1:
                    // Movies fragment activity
                    return new SearchFragment();
                case 2:
                    // Top Rated fragment activity
                    return new MagazineFragment();
                case 3:
                    // Top Rated fragment activity
                    return new UpdatesFragment();
                case 4:
                    // Top Rated fragment activity
                    return new ForumFragment();



            }
            return null;
        }


        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.dealer_tab_one_title);
                case 1:
                    return getString(R.string.dealer_tab_two_title);
                case 2:
                    return getString(R.string.dealer_tab_three_title);
                case 3:
                    return getString(R.string.dealer_tab_four_title);
                case 4:
                    return getString(R.string.dealer_tab_five_title);


            }

            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //drawerAdapter.notifyDataSetChanged();
    }

    private class GetAllLogo extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            objHomeScreenBL.getAllData(params[0],params[1]);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                drawerAdapter.notifyDataSetChanged();
                mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager(), getApplicationContext());

                mViewPager.setAdapter(mCustomPagerAdapter);
                tabs.setViewPager(mViewPager);
            }
            catch (NullPointerException e){
                showDialogResponse(HomeScreen.this);
            }catch (Exception e){

            }
            finally {
                progressDialog.dismiss();
            }
        }
    }


    /*--------------------------------------------------------------------------------*/

     /*-----------------------------------------------------------*/

    private void popupSize(){
        Display display = getWindowManager().getDefaultDisplay();

        int width = display.getWidth();
        int height = display.getHeight();

        // System.out.println("width" + width + "height" + height);

        if(width>=1000 && height>=1500){
            xx=700;
            yy=750;
        }
        else if(width>=700 && height>=1000)
        {
            xx=550;
            yy=600;
        }
        else
        {
            xx=450;
            yy=500;
        }

    }

    /* popup for no internet */
    private void showDialogInternet(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle,tvBottomMsg,tvBottomSmall;
        Button btnClosePopup,btnsave;

        final Dialog dialog  = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.popup_home_screen);
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.width=xx;
        wmlp.height=yy;




        btnClosePopup = (Button) dialog.findViewById(R.id.popup_cancel);
        btnsave= (Button) dialog.findViewById(R.id.popup_add);
        tvMsg= (TextView) dialog.findViewById(R.id.popup_message);
        tvTitle= (TextView) dialog.findViewById(R.id.popup_title);
        tvBottomMsg= (TextView) dialog.findViewById(R.id.popup_msg);
        tvBottomSmall= (TextView) dialog.findViewById(R.id.popup_msg_small);

        tvTitle.setText(getResources().getString(R.string.no_internet_title));
        tvMsg.setText(getResources().getString(R.string.no_internet_message_home));
        btnClosePopup.setText(getResources().getString(R.string._no_internet_cancel));
        btnsave.setText(getResources().getString(R.string._no_internet_ok));


        tvBottomMsg.setText("If you are in emergency, click");
        tvBottomSmall.setText("*Misuse of this service can permanently blacklist the device/account");

        btnClosePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), RoadsideAssistance.class));
                dialog.dismiss();
                //finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           Intent intent = new Intent(Intent.ACTION_MAIN);
                                           intent.addCategory(Intent.CATEGORY_HOME);
                                           intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
                                           startActivity(intent);
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

    /* popup for no Response From Server*/
    private void showDialogResponse(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle,tvBottomMsg;
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

                                           new GetAllLogo().execute(userID,userType);
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }


    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );//***Change Here***
            startActivity(intent);

            // System.exit(0);
            return;
        }

        Drawer.closeDrawers();
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    private class Logout extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objHomeScreenBL.getLogoutData(params[0], params[1]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
               if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                   Constant.NAME="Sign in";
                   Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SP_LOGIN_ID, null);
                   Util.setSharedPrefrenceValue(getApplicationContext(), Constant.PREFS_NAME, Constant.SP_LOGIN_TYPE, null);
                   Intent intent = new Intent(getApplicationContext(), HowItWork.class);
                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(intent);
               }
                else
                   Toast.makeText(getApplicationContext(),"Something went wrong.",Toast.LENGTH_SHORT).show();

            }
            catch (NullPointerException e){
                //showDialogResponse(HomeScreen.this);
            }catch (Exception e){

            }
            finally {
                progressDialog.dismiss();
            }
        }
    }
}

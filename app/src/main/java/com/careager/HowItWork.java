package com.careager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.careager.Constant.Constant;
import com.careager.careager.R;

public class HowItWork extends AppCompatActivity implements View.OnClickListener {

    RadioGroup groupRadio;
    Button btnSignup,btnSignin,btnSkip;
    RadioButton Gender;

    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_it_work);

        initialize();

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_how_it_work, menu);
        return true;
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
        groupRadio= (RadioGroup) findViewById(R.id.hiw_radio_group);
        btnSignup= (Button) findViewById(R.id.hiw_signup);
        btnSignin= (Button) findViewById(R.id.hiw_signin);
        btnSkip= (Button) findViewById(R.id.hiw_skip);
        pager= (ViewPager) findViewById(R.id.pager);

        btnSignin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
        btnSkip.setOnClickListener(this);

        DealerPageAdapter adapter=new DealerPageAdapter(getApplicationContext());
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(adapter.getCount());
    }

    @Override
    public void onClick(View v) {
        int selectedGender=groupRadio.getCheckedRadioButtonId();
        Gender= (RadioButton) findViewById(selectedGender);
        switch (v.getId()){
            case R.id.hiw_signup:
                if(Gender.getText().toString().equalsIgnoreCase(Constant.strLoginBusiness)){
                    startActivity(new Intent(getApplicationContext(),DealerSignupMap.class).putExtra("UserType",Gender.getText().toString()));
                }
                else {
                    startActivity(new Intent(getApplicationContext(),UserSignup.class).putExtra("UserType",Gender.getText().toString()));
                }
                break;
            case R.id.hiw_signin:
                startActivity(new Intent(getApplicationContext(),UserLogin.class).putExtra("UserType",Gender.getText().toString()));
                break;
            case R.id.hiw_skip:
                startActivity(new Intent(getApplicationContext(),HomeScreen.class));
                break;
        }
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


        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    private class DealerPageAdapter extends PagerAdapter {
        Context context;
        LayoutInflater inflater;
        ImageView imgPager;

        int images[]={R.drawable.hiw_one,R.drawable.hiw_two,R.drawable.hiw_three,R.drawable.hiw_four,R.drawable.hiw_five,R.drawable.hiw_six,R.drawable.hiw_seven};

        DealerPageAdapter(Context contex) {
            this.context = contex;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.fragment_hiwfragment_one, container,
                    false);

            imgPager= (ImageView) itemView.findViewById(R.id.hiw_image_pager);

            imgPager.setBackgroundResource(images[position]);



            ((ViewPager) container).addView(itemView);

            return itemView;
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        /*public float getPageWidth(int position)
        {
            return .9f;
        }*/

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}

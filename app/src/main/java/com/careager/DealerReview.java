package com.careager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.careager.BL.DealerRatingBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.careager.R;

public class DealerReview extends AppCompatActivity implements View.OnClickListener {

    RatingBar rbDealer;
    EditText etReview;
    Button btnDone;

    int rating;
    String review;

    DealerRatingBL objDealerRatingBL;
    ProgressDialog progressDialog;
    Intent intent;
    String userID;
    String showroomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_review);
        initialize();
    }

    private void initialize(){
        rbDealer= (RatingBar) findViewById(R.id.dealer_rating);
        etReview= (EditText) findViewById(R.id.dealer_review);
        btnDone= (Button) findViewById(R.id.dealer_review_button);

        objDealerRatingBL=new DealerRatingBL();
        progressDialog=new ProgressDialog(DealerReview.this);

        userID= Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_ID);

        intent=getIntent();
        showroomID=intent.getStringExtra("ID");

        btnDone.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dealer_review_button:
                rating=Math.round(rbDealer.getRating());
                Log.d("RAteing-->",rating+"");
                review=etReview.getText().toString();
                if(rating!=0){
                    if(review.length()!=0){
                        if(Util.isInternetConnection(DealerReview.this)){
                            new CallWS().execute(userID,showroomID,review,rating+"");

                        }
                    }
                    else {
                        etReview.setError("Required");
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Please Give Rating.",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private class CallWS extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objDealerRatingBL.sendRating(params[0],params[1],params[2],params[3]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Something went wrong. Please try again",Toast.LENGTH_LONG).show();
                }
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }
}

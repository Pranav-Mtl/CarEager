package com.careager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.careager.Adapter.DealerCategoryListAdapter;
import com.careager.BE.DealerCategoryBE;
import com.careager.BE.DealerSignUpBE;
import com.careager.BL.DealerSignUpBL;
import com.careager.careager.R;

import java.util.ArrayList;
import java.util.List;

public class DealerSignupCategory extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recList;
    Button btnNext;

    DealerCategoryListAdapter objDealerCategoryListAdapter;

    Toolbar toolbar;

    ProgressDialog progressDialog;
    DealerSignUpBE objDealerSignUpBE;
    DealerCategoryBE objDealerCategoryBE;
    DealerSignUpBL objDealerSignUpBL;

    List listCategory=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer_signup_category);

        initialize();

        new GetCategory().execute();


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

    private void initialize(){
        recList = (RecyclerView) findViewById(R.id.categoryList);
        btnNext= (Button) findViewById(R.id.btnDealerNext);
        recList.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        progressDialog=new ProgressDialog(this);
        objDealerSignUpBE=new DealerSignUpBE();
        objDealerSignUpBL=new DealerSignUpBL();
        objDealerCategoryBE=new DealerCategoryBE();

         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnNext.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnDealerNext:
                if(objDealerCategoryListAdapter.mSelectedItem!=-1){
                int pos=objDealerCategoryListAdapter.mSelectedItem;
                objDealerSignUpBE.setDealerCategory(objDealerCategoryListAdapter.listCategory.get(pos).toString());
                startActivity(new Intent(getApplicationContext(),DealerSignup.class).putExtra("DealerSignupBE",objDealerSignUpBE).putExtra("DealerCategoryBE",objDealerCategoryBE));
                }
                else {
                    Snackbar snack = Snackbar
                            .make(findViewById(R.id.dealer_root_category),
                                    getResources().getString(R.string.dealer_no_category_message),
                                    Snackbar.LENGTH_LONG).setText(getResources().getString(R.string.dealer_no_category_message));
                    ViewGroup group = (ViewGroup) snack.getView();
                    TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    group.setBackgroundColor(getResources().getColor(R.color.redColor));
                    snack.show();

                }
        }
    }

    class GetCategory extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            objDealerCategoryListAdapter=new DealerCategoryListAdapter(getApplicationContext(),objDealerSignUpBL,objDealerSignUpBE,objDealerCategoryBE);
            return "result";
        }

        @Override
        protected void onPostExecute(String s) {
            try{
            recList.setAdapter(objDealerCategoryListAdapter);
            }
            catch (NullPointerException e){

            }
            catch (Exception e){

            }
            finally {
                progressDialog.dismiss();
            }

        }
    }


}

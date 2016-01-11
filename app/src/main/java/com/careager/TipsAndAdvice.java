package com.careager;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.careager.Adapter.TipsAdviceAdapter;
import com.careager.Configuration.Util;
import com.careager.careager.R;

public class TipsAndAdvice extends AppCompatActivity {

    RecyclerView recList;

    Toolbar toolbar;

    ProgressDialog progressDialog;

    TipsAdviceAdapter objTipsAdviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_and_advice);

        initialize();

        if(Util.isInternetConnection(TipsAndAdvice.this))
            new GetTips().execute();
    }

    private void initialize(){

        recList = (RecyclerView) findViewById(R.id.tips_list);


        recList.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);



        progressDialog=new ProgressDialog(TipsAndAdvice.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

    private class GetTips extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected String doInBackground(String... params) {
            objTipsAdviceAdapter=new TipsAdviceAdapter(getApplicationContext());
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                recList.setAdapter(objTipsAdviceAdapter);
            }catch (NullPointerException e){

            }
            catch (Exception e){

            }
            finally {
                progressDialog.dismiss();
            }
        }
    }
}

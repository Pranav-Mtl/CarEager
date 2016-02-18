package com.careager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.careager.BL.ForumAskQuestionBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.careager.R;

public class ForumAskQuestion extends AppCompatActivity implements View.OnClickListener {

    EditText etQuestion;
    Spinner spnCategory;
    Button btnDone;

    String strQuestion,strCategory;

    ForumAskQuestionBL objForumAskQuestionBL;

    ProgressDialog progressDialog;

    Intent intent;

    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_ask_question);
        initialize();

        if(Util.isInternetConnection(ForumAskQuestion.this)){
          new GetCategory().execute();
        }
    }

    private void initialize(){
        etQuestion= (EditText) findViewById(R.id.forum_question);
        spnCategory= (Spinner) findViewById(R.id.forum_category);
        btnDone= (Button) findViewById(R.id.forum_submit);

        intent=getIntent();

        objForumAskQuestionBL=new ForumAskQuestionBL();
        progressDialog=new ProgressDialog(ForumAskQuestion.this);

        userID=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_ID);

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
            case R.id.forum_submit:
                if(etQuestion.getText().toString().trim().length()==0){
                    etQuestion.setError("Required");
                }
                else if(spnCategory.getSelectedItem().toString().equalsIgnoreCase("Select Category")){
                    Toast.makeText(getApplicationContext(),"Please select category",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(Util.isInternetConnection(ForumAskQuestion.this)){
                        new sendQuestion().execute(userID, etQuestion.getText().toString(), spnCategory.getSelectedItem().toString());
                    }
                }

                break;
        }
    }

    private class GetCategory extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            objForumAskQuestionBL.getForumQuestionCategory();
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                ArrayAdapter<String> adapterBrand = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, Constant.categoryQuestion);
                adapterBrand.setDropDownViewResource(R.layout.spinner_item);
                spnCategory.setAdapter(adapterBrand);
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }


    private class sendQuestion extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objForumAskQuestionBL.askQuestion(params[0], params[1], params[2]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)) {
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"Something went wrong.Please try again?",Toast.LENGTH_SHORT).show();
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }
}

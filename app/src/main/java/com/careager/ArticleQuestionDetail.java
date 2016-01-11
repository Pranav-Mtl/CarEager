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
import android.widget.TextView;

import com.careager.Adapter.ArticleCommentAdapter;
import com.careager.Adapter.ForumCommentAdapter;
import com.careager.BE.ArticleQuestionDetailBE;
import com.careager.BE.ForumQuestionDetailBE;
import com.careager.BL.ArticleQuestionDetailBL;
import com.careager.BL.ForumQuestionDetailBL;
import com.careager.Configuration.Util;
import com.careager.careager.R;

public class ArticleQuestionDetail extends AppCompatActivity {

    TextView tvTitle,tvDescription,tvPosted;

    RecyclerView recList;

    Toolbar toolbar;

    ProgressDialog progressDialog;


    String ID;

    ArticleQuestionDetailBE objArticleQuestionDetailBE;
    ArticleQuestionDetailBL objArticleQuestionDetailBL;

    ArticleCommentAdapter objArticleCommentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_question_detail);
        initialize();

        if(Util.isInternetConnection(ArticleQuestionDetail.this))
            new GetDetail().execute(ID);
    }

    private void initialize(){
        tvTitle= (TextView) findViewById(R.id.article_detail_title);
        tvDescription= (TextView) findViewById(R.id.article_detail_description);
        tvPosted= (TextView) findViewById(R.id.article_detail_posted);

        recList = (RecyclerView) findViewById(R.id.article_question_detail);


        recList.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ID=getIntent().getStringExtra("ID");

        objArticleQuestionDetailBE=new ArticleQuestionDetailBE();
        objArticleQuestionDetailBL=new ArticleQuestionDetailBL();

        progressDialog=new ProgressDialog(ArticleQuestionDetail.this);

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

    private class GetDetail extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            objArticleQuestionDetailBL.getArticleDetail(params[0],objArticleQuestionDetailBE);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                tvTitle.setText(objArticleQuestionDetailBE.getTitle());
                tvDescription.setText(objArticleQuestionDetailBE.getDescription());
                tvPosted.setText("Posted on: "+objArticleQuestionDetailBE.getTimestamp());

                objArticleCommentAdapter=new ArticleCommentAdapter(getApplicationContext());
                recList.setAdapter(objArticleCommentAdapter);

            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }
}

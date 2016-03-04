package com.careager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.careager.Adapter.ForumCommentAdapter;
import com.careager.BE.ForumQuestionDetailBE;
import com.careager.BL.ForumQuestionDetailBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.careager.R;

public class ForumQuestionDetail extends AppCompatActivity {

    TextView tvTitle,tvDescription,tvPosted;

    EditText etComment;
    LinearLayout llSend,llComment;

    RecyclerView recList;

    Toolbar toolbar;

    ProgressDialog progressDialog;

    ForumQuestionDetailBL objForumQuestionDetailBL;
    ForumQuestionDetailBE objForumQuestionDetailBE;

    String ID;
    String userID,userType;

    ForumCommentAdapter objForumCommentAdapter;

    String strComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_question_detail);

        initialize();

        if(Util.isInternetConnection(ForumQuestionDetail.this))
        new GetDetail().execute(ID);

        llSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strComment=etComment.getText().toString();

                if(strComment.length()>0){

                    if(Util.isInternetConnection(ForumQuestionDetail.this)){
                        new SendComment().execute(userID,strComment,ID);
                    }
                }
            }
        });

    }

    private void initialize(){
        tvTitle= (TextView) findViewById(R.id.forum_detail_title);

        tvPosted= (TextView) findViewById(R.id.forum_detail_posted);

        recList = (RecyclerView) findViewById(R.id.forum_question_detail);
        etComment= (EditText) findViewById(R.id.et_commment);
        llSend= (LinearLayout) findViewById(R.id.ll_send_comment);
        llComment= (LinearLayout) findViewById(R.id.article_detail_comment);


        recList.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ID=getIntent().getStringExtra("ID");

        objForumQuestionDetailBL=new ForumQuestionDetailBL();
        objForumQuestionDetailBE=new ForumQuestionDetailBE();

        progressDialog=new ProgressDialog(ForumQuestionDetail.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(userID!=null)
            if(userType!=null)
                if(userType.equalsIgnoreCase(Constant.strLoginUser)){
                    llComment.setVisibility(View.VISIBLE);
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

    private class GetDetail extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            objForumQuestionDetailBL.getForumDetail(params[0],objForumQuestionDetailBE);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                tvTitle.setText(objForumQuestionDetailBE.getTitle());

                tvPosted.setText("Posted by "+objForumQuestionDetailBE.getName()+" on "+objForumQuestionDetailBE.getTimestamp());

                objForumCommentAdapter=new ForumCommentAdapter(getApplicationContext());
                recList.setAdapter(objForumCommentAdapter);

            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }

    private class SendComment extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objForumQuestionDetailBL.sendForumComment(params[0], params[1], params[2]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    startActivity(new Intent(getApplicationContext(),ArticleQuestionDetail.class).putExtra("ID",ID));
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "Something went wrong? Please try again?", Toast.LENGTH_SHORT).show();
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
                progressDialog.dismiss();
            }
        }
    }
}

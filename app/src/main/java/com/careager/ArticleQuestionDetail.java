package com.careager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.careager.Adapter.ArticleCommentAdapter;
import com.careager.Adapter.ForumCommentAdapter;
import com.careager.BE.ArticleQuestionDetailBE;
import com.careager.BE.ForumQuestionDetailBE;
import com.careager.BL.ArticleQuestionDetailBL;
import com.careager.BL.ForumQuestionDetailBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.squareup.picasso.Picasso;

public class ArticleQuestionDetail extends AppCompatActivity {

    TextView tvTitle,tvDescription,tvPosted;
    EditText etComment;
    LinearLayout llSend,llComment;

    RecyclerView recList;

    Toolbar toolbar;

    ProgressDialog progressDialog;

    ImageView ivImage;


    String ID;

    ArticleQuestionDetailBE objArticleQuestionDetailBE;
    ArticleQuestionDetailBL objArticleQuestionDetailBL;

    ArticleCommentAdapter objArticleCommentAdapter;

    int xx,yy;

    String strComment;

    String userID,userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_question_detail);
        initialize();

        if(Util.isInternetConnection(ArticleQuestionDetail.this))
            new GetDetail().execute(ID);

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogImage(ArticleQuestionDetail.this);
            }
        });

        llSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strComment=etComment.getText().toString();

                if(strComment.length()>0){

                    if(Util.isInternetConnection(ArticleQuestionDetail.this)){
                        new SendComment().execute(userID,strComment,ID);
                    }
                }
            }
        });
    }

    private void initialize(){
        tvTitle= (TextView) findViewById(R.id.article_detail_title);
        ivImage= (ImageView) findViewById(R.id.article_detail_image);
        tvPosted= (TextView) findViewById(R.id.article_detail_posted);
        tvDescription= (TextView) findViewById(R.id.article_detail_description);
        recList = (RecyclerView) findViewById(R.id.article_question_detail);
        etComment= (EditText) findViewById(R.id.et_commment);
        llSend= (LinearLayout) findViewById(R.id.ll_send_comment);
        llComment= (LinearLayout) findViewById(R.id.article_detail_comment);


        recList.setHasFixedSize(true);

        final LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ID=getIntent().getStringExtra("ID");

        userID=Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SP_LOGIN_ID);
        userType=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_TYPE);

        objArticleQuestionDetailBE=new ArticleQuestionDetailBE();
        objArticleQuestionDetailBL=new ArticleQuestionDetailBL();

        progressDialog=new ProgressDialog(ArticleQuestionDetail.this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        popupSize();

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
                Picasso.with(getApplicationContext())
                        .load(objArticleQuestionDetailBE.getBaseURL()+objArticleQuestionDetailBE.getImage())
                        .placeholder(R.drawable.ic_default_loading)
                        .error(R.drawable.ic_default_loading)
                        .into(ivImage);

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

    /*----------------------*/

    private class SendComment extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objArticleQuestionDetailBL.sendArticleComment(params[0], params[1], params[2]);
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
                    Toast.makeText(getApplicationContext(),"Something went wrong? Please try again?",Toast.LENGTH_SHORT).show();
            }catch (NullPointerException e){

            }catch (Exception e){

            }finally {
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

    /* popup for no internet */
    private void showDialogImage(Context context){
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
                .load(objArticleQuestionDetailBE.getBaseURL()+objArticleQuestionDetailBE.getImage())
                .placeholder(R.drawable.ic_default_loading)
                .error(R.drawable.ic_default_loading)
                .into(inImage);


        dialog.show();
    }
}

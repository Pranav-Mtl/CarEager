package com.careager;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.careager.careager.R;

public class CarInfoWebView extends AppCompatActivity {
     WebView webView;

    //String url="http://ec2-52-76-48-31.ap-southeast-1.compute.amazonaws.com/careager/model/";

    String url="http://careager.com/blog/carinfo_list_webview/";

    String model,ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info_web_view);

        initialize();
    }

    private void initialize(){
        webView = (WebView) findViewById(R.id.carinfo_webView);
        model=getIntent().getStringExtra("Model");
        model=model.replace(" ","-");
        //ID=getIntent().getStringExtra("ID");
        //url=url+ID;
        url=url+model;
        startWebView(url);
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



    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                try{
                    if (progressDialog == null) {
                        // in standard case YourActivity.this
                        progressDialog = new ProgressDialog(CarInfoWebView.this);
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                    }

                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        //progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });


        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);


        //Load url in webview
        webView.loadUrl(url);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        webView.stopLoading();
        webView.clearHistory();
        webView.clearView();
        webView.clearCache(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        webView.stopLoading();
        webView.clearHistory();
        webView.clearView();
    }
}

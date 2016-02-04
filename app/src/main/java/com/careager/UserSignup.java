package com.careager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careager.BE.UserSignupBE;
import com.careager.BL.UserSignupBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.careager.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

public class UserSignup extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    Button btnDone;
    EditText etEmail,etMobile,etPassword,etConfirmPassword,etName;
    LinearLayout llSignIn;

    String strEmail,strMobile,strPassword,strConfirmPassword,strName;

    boolean flagDetails;

    UserSignupBE objUserSignupBE;
    UserSignupBL objUserSignupBL;
    ProgressDialog mProgressDialog;

    private GoogleApiClient mGoogleApiClient;

    private static final String TAG = "SignInActivity";



    String userType;

    LoginButton btnFB;
    CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 9001;

    SignInButton signInButton;

    int xx,yy;

    String deviceId;
    GoogleCloudMessaging gcmObj;


    Context applicationContext;
    String gcmID;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_user_signup);
        initialize();


        btnFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                                Log.v("LoginActivity", response.toString());
                                try {
                                    Log.v("email", object.getString("email"));

                                    if (Util.isInternetConnection(UserSignup.this))
                                        new ValidateUserSocial().execute(object.getString("email"),object.getString("name"), gcmID, deviceId);

                                    LoginManager.getInstance().logOut();


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
                System.out.println("LoginResult" + loginResult);
            }

            @Override
            public void onCancel() {
                // App code
                System.out.println("Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                exception.printStackTrace();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_signup, menu);
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

    /* initialize all widgets */

    private void initialize(){

        btnDone= (Button) findViewById(R.id.signup_done);
        etName= (EditText) findViewById(R.id.signup_name);
        etEmail= (EditText) findViewById(R.id.signup_email);
        etMobile= (EditText) findViewById(R.id.signup_mobile);
        etPassword= (EditText) findViewById(R.id.signup_password);
        etConfirmPassword= (EditText) findViewById(R.id.signup_conf_password);
        llSignIn= (LinearLayout) findViewById(R.id.signup_signin);

        btnFB = (LoginButton) findViewById(R.id.fb_login_button);



        btnFB.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(this);

        /* fb initialization*/

        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



        /* google initialization*/


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

        setGooglePlusButtonText(signInButton,"Sign up");


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        etMobile.setText("+91", TextView.BufferType.EDITABLE);
        etMobile.setSelection(etMobile.getText().length());

        objUserSignupBE=new UserSignupBE();
        objUserSignupBL=new UserSignupBL();
        mProgressDialog=new ProgressDialog(UserSignup.this);

        btnDone.setOnClickListener(this);
        llSignIn.setOnClickListener(this);
        applicationContext=getApplicationContext();

        userType=getIntent().getExtras().getString("UserType");
        gcmID=Util.getSharedPrefrenceValue(UserSignup.this, Constant.SP_GCM_ID);
        deviceId=Util.getSharedPrefrenceValue(UserSignup.this,Constant.SP_DEVICE_ID);
        if(gcmID==null){
            if (checkPlayServices()) {
                registerInBackground();
            }
        }

        popupSize();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.signup_done:
                if(validateDetails()){
                    objUserSignupBE.setName(strName);
                    objUserSignupBE.setEmail(strEmail);
                    strMobile=strMobile.substring(3);
                    objUserSignupBE.setMobile(strMobile);
                    objUserSignupBE.setPassword(strPassword);
                    objUserSignupBE.setDeviceID(deviceId);
                    objUserSignupBE.setGcmID(gcmID);

                    Util.hideSoftKeyboard(UserSignup.this);
                    if(Util.isInternetConnection(UserSignup.this))
                    new InsertDetails().execute();
                    else
                        showDialogInternet(UserSignup.this);
                }
                break;
            case R.id.signup_signin:
                startActivity(new Intent(getApplicationContext(),UserLogin.class).putExtra("UserType",userType));
                break;
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    /*  validate user inputs after sign up button pressed */
    private boolean validateDetails(){
        flagDetails=true;
        strName=etName.getText().toString();
        strEmail=etEmail.getText().toString();
        strPassword=etPassword.getText().toString();
        strMobile=etMobile.getText().toString();
        strConfirmPassword=etConfirmPassword.getText().toString();

        if(strName.trim().length()==0){
            etName.setError("Required");
            flagDetails=false;
        }

        if(strEmail.trim().length()==0){
            etEmail.setError("Required");
            flagDetails=false;
        }
        else {
            if(!Util.isEmailValid(strEmail)){
                etEmail.setError("Invalid Email-Id");
                flagDetails=false;
            }
        }

        if(strMobile.trim().length()!=13){
            etMobile.setError("Invalid Mobile No.");
            flagDetails=false;
        }

        if(strPassword.trim().length()==0){
            etPassword.setError("Required");
            flagDetails=false;
        }

        if(strConfirmPassword.trim().length()==0){
            etConfirmPassword.setError("Password Mismatch");
            flagDetails=false;
        }
        else {
            if(!strPassword.equals(strConfirmPassword)){
                etConfirmPassword.setError("Password Mismatch");
                flagDetails=false;
            }
        }

        return flagDetails;

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class InsertDetails extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objUserSignupBL.insertSignUpDetails(objUserSignupBE,getApplicationContext());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try
            {
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.SP_LOGIN_TYPE,userType);
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                }
                else {
                    Snackbar snack = Snackbar
                            .make(findViewById(R.id.user_signup_root),
                                    getResources().getString(R.string.failure_signup_message),
                                    Snackbar.LENGTH_LONG).setText(getResources().getString(R.string.failure_signup_message));
                    ViewGroup group = (ViewGroup) snack.getView();
                    TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    group.setBackgroundColor(getResources().getColor(R.color.redColor));
                    snack.show();

                }

            }catch (NullPointerException e){
                showDialogResponse(UserSignup.this);
            }
            catch (Exception e){

            }
            finally {
                mProgressDialog.dismiss();
            }

        }
    }


    /*---------------------------------------------------------------*/

    private void popupSize(){
        Display display = getWindowManager().getDefaultDisplay();

        int width = display.getWidth();
        int height = display.getHeight();

        // System.out.println("width" + width + "height" + height);

        if(width>=1000 && height>=1500){
            xx=700;
            yy=650;
        }
        else if(width>=700 && height>=1000)
        {
            xx=550;
            yy=500;
        }
        else
        {
            xx=450;
            yy=400;
        }

    }
    /* popup for no internet */
    private void showDialogInternet(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
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

        tvTitle.setText(getResources().getString(R.string.no_internet_title));
        tvMsg.setText(getResources().getString(R.string.no_internet_message));
        btnClosePopup.setText(getResources().getString(R.string._no_internet_cancel));
        btnsave.setText(getResources().getString(R.string._no_internet_ok));

        btnClosePopup.setVisibility(View.GONE);


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


                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

    /* popup for no internet */
    private void showDialogResponse(Context context){
        // x -->  X-Cordinate
        // y -->  Y-Cordinate

        final TextView tvMsg,tvTitle;
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
                //finish();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           new InsertDetails().execute();
                                           dialog.dismiss();
                                       }
                                   }

        );


        dialog.show();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

                finish();
            }
            return false;
        } else {

        }
        return true;
    }

    /*--------------------------GCM KEY ----------------------------------------*/
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging
                                .getInstance(applicationContext);
                    }
                    gcmID = gcmObj
                            .register(Constant.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + gcmID;

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(gcmID)) {
                    //Toast.makeText(getApplicationContext(),"GSM"+gcmID,Toast.LENGTH_SHORT).show();
                    Util.setSharedPrefrenceValue(applicationContext,Constant.PREFS_NAME,Constant.SP_GCM_ID,gcmID);
                } else {
                    /*Toast.makeText(
                       applicationContext,
                            "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();*/
                }
            }
        }.execute(null, null, null);
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.d("GOOGLE API",acct.getEmail());

            new ValidateUserSocial().execute(acct.getEmail(), acct.getDisplayName(),gcmID,deviceId);
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }
    // [END handleSignInResult]

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    private class ValidateUserSocial extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Authenticating...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objUserSignupBL.insertSignUpDetailsSocial(params[0],params[1],params[2],params[3],getApplicationContext());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                Log.d("REsponse",s);
                if(Constant.WS_RESPONSE_SUCCESS.equalsIgnoreCase(s)){
                    Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.SP_LOGIN_TYPE,userType);
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                }
                else {
                    Snackbar snack = Snackbar
                            .make(findViewById(R.id.user_signup_root),
                                    getResources().getString(R.string.failure_signup_message),
                                    Snackbar.LENGTH_LONG).setText(getResources().getString(R.string.failure_signup_message));
                    ViewGroup group = (ViewGroup) snack.getView();
                    TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    group.setBackgroundColor(getResources().getColor(R.color.redColor));
                    snack.show();

                }
            }
            catch (NullPointerException e){

            }
            catch (Exception e){

            }
            finally {
                mProgressDialog.dismiss();
            }
        }
    }


    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }

}


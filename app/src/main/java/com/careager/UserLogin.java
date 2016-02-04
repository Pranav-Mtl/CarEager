package com.careager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.*;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careager.BE.DealerLoginBE;
import com.careager.BL.DealerLoginBL;
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
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class UserLogin extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener

    {

    EditText etEmail,etPassword;
    Button btnDone;
    LinearLayout llForgot,llSignUp;

    String strEmail,strPassword;

    DealerLoginBE objUserLoginBE;
    DealerLoginBL objUserLoginBL;

    String userType;

    ProgressDialog mProgressDialog;
        LoginButton btnFB;
    ImageButton btnGPlus;

    CallbackManager callbackManager;

        String email;


        private static final String TAG = "SignInActivity";


        private GoogleApiClient mGoogleApiClient;

        String deviceId;
        GoogleCloudMessaging gcmObj;

        LinearLayout llSocial;


        Context applicationContext;
        String gcmID;
        private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        private boolean mIntentInProgress;
        private ConnectionResult mConnectionResult;

        private static final int RC_SIGN_IN = 9001;

        SignInButton signInButton;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_user_login);
            initialize();


            try {
                PackageInfo info = getPackageManager().getPackageInfo(
                        "com.laundry.oneclickwash",
                        PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }


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

                                        if(Util.isInternetConnection(UserLogin.this))
                                        new ValidateUserSocial().execute(object.getString("email"),gcmID,deviceId);

                                        LoginManager.getInstance().logOut();



                                    }
                                    catch (Exception e)
                                    {
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
        getMenuInflater().inflate(R.menu.menu_user_login, menu);
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
        etEmail= (EditText) findViewById(R.id.signin_email);
        etPassword= (EditText) findViewById(R.id.signin_password);
        btnFB = (LoginButton) findViewById(R.id.fb_login_button);
        llSocial= (LinearLayout) findViewById(R.id.ll_social);

        btnFB.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
         signInButton = (SignInButton) findViewById(R.id.sign_in_button);

        btnDone= (Button) findViewById(R.id.signin_done);

        llForgot= (LinearLayout) findViewById(R.id.signin_forgot);
        llSignUp= (LinearLayout) findViewById(R.id.signin_signup);

        signInButton.setOnClickListener(this);

        objUserLoginBE=new DealerLoginBE();
        objUserLoginBL=new DealerLoginBL();
        mProgressDialog=new ProgressDialog(UserLogin.this);

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

        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(gso.getScopeArray());


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        userType=getIntent().getExtras().get("UserType").toString();

        if(userType.equalsIgnoreCase(Constant.strLoginUser)){
            signInButton.setVisibility(View.VISIBLE);
            btnFB.setVisibility(View.VISIBLE);
        }
        else
        {
            llSocial.setVisibility(View.GONE);
            signInButton.setVisibility(View.GONE);
            btnFB.setVisibility(View.GONE);
        }

        btnDone.setOnClickListener(this);
        llSignUp.setOnClickListener(this);
        llForgot.setOnClickListener(this);

        applicationContext=getApplicationContext();

        gcmID=Util.getSharedPrefrenceValue(UserLogin.this, Constant.SP_GCM_ID);
        deviceId=Util.getSharedPrefrenceValue(UserLogin.this,Constant.SP_DEVICE_ID);
        if(gcmID==null){
            if (checkPlayServices()) {
                registerInBackground();
            }
        }

        //signOut();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signin_done:
                if(validateDetails()){
                    objUserLoginBE.setEmail(strEmail);
                    objUserLoginBE.setPassword(strPassword);
                    objUserLoginBE.setDeviceID(deviceId);
                    objUserLoginBE.setGcmID(gcmID);
                    if(Util.isInternetConnection(UserLogin.this)){
                        Util.hideSoftKeyboard(UserLogin.this);
                        new ValidateUser().execute(userType);
                    }
                }
                break;
            case R.id.signin_forgot:
                break;
            case R.id.signin_signup:
                if(userType.equalsIgnoreCase(Constant.strLoginBusiness))
                    startActivity(new Intent(getApplicationContext(),DealerSignupCategory.class));
                else
                startActivity(new Intent(getApplicationContext(),UserSignup.class).putExtra("UserType",userType));
                break;
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private boolean validateDetails(){
        boolean flagDetails=true;
        strEmail=etEmail.getText().toString();
        strPassword=etPassword.getText().toString();

        if(strEmail.trim().length()==0){
            etEmail.setError("Required");
            flagDetails=false;
        }

        if(strPassword.trim().length()==0){
            etPassword.setError("Required");
            flagDetails=false;
        }

        return flagDetails;


    }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }

        private class ValidateUser extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            String result=objUserLoginBL.validateSigninDetails(objUserLoginBE, params[0],getApplicationContext());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                Log.d("REsponse",s);
                if(s.equalsIgnoreCase(Constant.WS_RESPONSE_SUCCESS)){
                    Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.SP_LOGIN_TYPE,userType);
                    if(userType.equalsIgnoreCase(Constant.strLoginUser))
                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                    else
                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                }
                else {

                    /*Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
                    System.out.println(format.format(new BigDecimal("100000000")));*/

                    Snackbar snack = Snackbar.make(findViewById(R.id.login_root),
                            getResources().getString(R.string.failure_login_message),
                            Snackbar.LENGTH_LONG).setText(getResources().getString(R.string.failure_login_message)).setActionTextColor(getResources().getColor(R.color.redColor));
                    ViewGroup group = (ViewGroup) snack.getView();
                    TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextColor(Color.WHITE);
                    group.setBackgroundColor(getResources().getColor(R.color.redColor));
                    snack.show();

                 /*                  TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                                         View view = snackbar.getView();
                                       tv.setTextColor(Color.WHITE);*/

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

       /*-------------*/

        private class ValidateUserSocial extends AsyncTask<String,String,String>{

            @Override
            protected void onPreExecute() {
                mProgressDialog.show();
                mProgressDialog.setMessage("Authenticating...");
                mProgressDialog.setCancelable(false);
            }

            @Override
            protected String doInBackground(String... params) {
                String result=objUserLoginBL.validateSigninDetailsSocial(params[0],params[1],params[2], getApplicationContext());
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                try{
                    Log.d("REsponse",s);
                    if(s.equalsIgnoreCase(Constant.WS_RESPONSE_SUCCESS)){
                        Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.SP_LOGIN_TYPE,userType);
                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                    }
                    else {

                    /*Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
                    System.out.println(format.format(new BigDecimal("100000000")));*/

                        Snackbar snack = Snackbar.make(findViewById(R.id.login_root),
                                getResources().getString(R.string.failure_login_message),
                                Snackbar.LENGTH_LONG).setText(getResources().getString(R.string.failure_login_message)).setActionTextColor(getResources().getColor(R.color.redColor));
                        ViewGroup group = (ViewGroup) snack.getView();
                        TextView tv = (TextView) group.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextColor(Color.WHITE);
                        group.setBackgroundColor(getResources().getColor(R.color.redColor));
                        snack.show();

                 /*                  TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                                         View view = snackbar.getView();
                                       tv.setTextColor(Color.WHITE);*/

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

        /*@Override
        public void onStart() {
            super.onStart();

            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }
*/
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

                new ValidateUserSocial().execute(acct.getEmail(),gcmID,deviceId);
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


}

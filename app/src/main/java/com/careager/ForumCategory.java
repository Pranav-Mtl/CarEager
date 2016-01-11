package com.careager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careager.BL.ForumCategoryBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.careager.R;

public class ForumCategory extends AppCompatActivity implements View.OnClickListener {
    LinearLayout llDesign,llElectronic,llRepair,llEngine,llChassis,llTransmission;

    TextView tvDesignOne,tvDesignTwo,tvElectronicOne,tvElectronicTwo,tvRepairOne,tvRepairTwo,tvEngineOne,tvEngineTwo,tvChassisOne,tvChassisTwo,tvTransmissionOne,tvTransmissionTwo;
    TextView tvDateDesignOne,tvDateDesignTwo,tvDateElectronicOne,tvDateElectronicTwo,tvDateRepairOne,tvDateRepairTwo,tvDateEngineOne,tvDateEngineTwo,tvDateChassisOne,tvDateChassisTwo,tvDateTransmissionOne,tvDateTransmissionTwo;

    ForumCategoryBL objForumCategoryBL;

    FloatingActionButton btnChat;

    String userId,userType;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_category);
        initialize();

        if(Util.isInternetConnection(ForumCategory.this))
            new GetCategory().execute();
    }

    private void initialize(){
        btnChat= (FloatingActionButton) findViewById(R.id.chatFabButton);
        llDesign= (LinearLayout) findViewById(R.id.ll_design);
        llElectronic= (LinearLayout) findViewById(R.id.ll_electronic);
        llRepair= (LinearLayout) findViewById(R.id.ll_repair);
        llEngine= (LinearLayout) findViewById(R.id.ll_engine);
        llChassis= (LinearLayout) findViewById(R.id.ll_chassic);
        llTransmission= (LinearLayout) findViewById(R.id.ll_transmission);

        tvDesignOne= (TextView) findViewById(R.id.tv_design_one);
        tvDesignTwo= (TextView) findViewById(R.id.tv_design_two);

        tvElectronicOne= (TextView) findViewById(R.id.tv_electronic_one);
        tvElectronicTwo= (TextView) findViewById(R.id.tv_electronic_two);

        tvRepairOne= (TextView) findViewById(R.id.tv_repair_one);
        tvRepairTwo= (TextView) findViewById(R.id.tv_repair_two);

        tvEngineOne= (TextView) findViewById(R.id.tv_engine_one);
        tvEngineTwo= (TextView) findViewById(R.id.tv_engine_two);

        tvChassisOne= (TextView) findViewById(R.id.tv_chassic_one);
        tvChassisTwo= (TextView) findViewById(R.id.tv_chassic_two);

        tvTransmissionOne= (TextView) findViewById(R.id.tv_transmission_one);
        tvTransmissionTwo= (TextView) findViewById(R.id.tv_transmission_two);
/*----------------------------------------------------------------------------------------*/
        tvDateDesignOne= (TextView) findViewById(R.id.tv_design_dateone);
        tvDateDesignTwo= (TextView) findViewById(R.id.tv_design_datetwo);

        tvDateElectronicOne= (TextView) findViewById(R.id.tv_electronic_dateone);
        tvDateElectronicTwo= (TextView) findViewById(R.id.tv_electronic_datetwo);

        tvDateRepairOne= (TextView) findViewById(R.id.tv_repair_dateone);
        tvDateRepairTwo= (TextView) findViewById(R.id.tv_repair_datetwo);

        tvDateEngineOne= (TextView) findViewById(R.id.tv_engine_dateone);
        tvDateEngineTwo= (TextView) findViewById(R.id.tv_engine_datetwo);

        tvDateChassisOne= (TextView) findViewById(R.id.tv_chassic_dateone);
        tvDateChassisTwo= (TextView) findViewById(R.id.tv_chassic_datetwo);

        tvDateTransmissionOne= (TextView) findViewById(R.id.tv_transmission_dateone);
        tvDateTransmissionTwo= (TextView) findViewById(R.id.tv_transmission_datetwo);

        objForumCategoryBL=new ForumCategoryBL();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressDialog=new ProgressDialog(ForumCategory.this);

        llDesign.setOnClickListener(this);
        llElectronic.setOnClickListener(this);
        llEngine.setOnClickListener(this);
        llRepair.setOnClickListener(this);
        llChassis.setOnClickListener(this);
        llTransmission.setOnClickListener(this);

        btnChat.setOnClickListener(this);

        userId=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_ID);
        userType=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_LOGIN_TYPE);

        if(userId!=null)
            if(userType!=null)
                if(userType.equalsIgnoreCase(Constant.strLoginUser))
                    btnChat.setVisibility(View.VISIBLE);


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
            case R.id.ll_design:
                startActivity(new Intent(getApplicationContext(),ForumQuestionList.class).putExtra("Category",Constant.categoryDesign));
                break;
            case R.id.ll_electronic:
                startActivity(new Intent(getApplicationContext(), ForumQuestionList.class).putExtra("Category", Constant.categoryElectronics));
                break;
            case R.id.ll_repair:
                startActivity(new Intent(getApplicationContext(),ForumQuestionList.class).putExtra("Category", Constant.categoryRepair));
                break;
            case R.id.ll_engine:
                startActivity(new Intent(getApplicationContext(),ForumQuestionList.class).putExtra("Category", Constant.categoryEngine));
                break;
            case R.id.ll_chassic:
                startActivity(new Intent(getApplicationContext(),ForumQuestionList.class).putExtra("Category", Constant.categoryChassis));
                break;
            case R.id.ll_transmission:
                startActivity(new Intent(getApplicationContext(), ForumQuestionList.class).putExtra("Category", Constant.categoryTransmission));
                break;
            case R.id.chatFabButton:
                startActivity(new Intent(getApplicationContext(),ForumUserList.class));
                break;
        }
    }

    private class GetCategory extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            progressDialog.show();
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
        }

        @Override
        protected String doInBackground(String... params) {
            objForumCategoryBL.getForumCategory();
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                /*-----------------*/
                if(Constant.designID!=null){
                    llDesign.setVisibility(View.VISIBLE);
                    for(int i=0;i<Constant.designID.length;i++){
                        if(i==0){
                            tvDesignOne.setVisibility(View.VISIBLE);
                            tvDateDesignOne.setVisibility(View.VISIBLE);
                            tvDesignOne.setText(Constant.designTitle[i]);
                            tvDateDesignOne.setText("Posted on "+Constant.designDate[i]);
                        }
                        else if(i==1){
                            tvDesignTwo.setVisibility(View.VISIBLE);
                            tvDateDesignTwo.setVisibility(View.VISIBLE);
                            tvDesignTwo.setText(Constant.designTitle[i]);
                            tvDateDesignTwo.setText("Posted on "+Constant.designDate[i]);
                        }
                    }
                }
            /*-----------------------------*/
                if(Constant.electronicID!=null){
                    llElectronic.setVisibility(View.VISIBLE);
                    for(int i=0;i<Constant.electronicID.length;i++){
                        if(i==0){
                            tvElectronicOne.setVisibility(View.VISIBLE);
                            tvDateElectronicOne.setVisibility(View.VISIBLE);
                            tvElectronicOne.setText(Constant.electronicTitle[i]);
                            tvDateElectronicOne.setText("Posted on "+Constant.electronicDate[i]);
                        }
                        else if(i==1){
                            tvElectronicTwo.setVisibility(View.VISIBLE);
                            tvDateElectronicTwo.setVisibility(View.VISIBLE);
                            tvElectronicTwo.setText(Constant.electronicTitle[i]);
                            tvDateElectronicTwo.setText("Posted on "+Constant.electronicDate[i]);
                        }
                    }
                }

                /*-----------------------------*/
                if(Constant.engineID!=null){
                    llEngine.setVisibility(View.VISIBLE);
                    for(int i=0;i<Constant.engineID.length;i++){
                        if(i==0){
                            tvEngineOne.setVisibility(View.VISIBLE);
                            tvDateEngineOne.setVisibility(View.VISIBLE);
                            tvEngineOne.setText(Constant.engineTitle[i]);
                            tvDateEngineOne.setText("Posted on "+Constant.engineDate[i]);
                        }
                        else if(i==1){
                            tvEngineTwo.setVisibility(View.VISIBLE);
                            tvDateEngineTwo.setVisibility(View.VISIBLE);
                            tvEngineTwo.setText(Constant.engineTitle[i]);
                            tvDateEngineTwo.setText("Posted on "+Constant.engineDate[i]);
                        }
                    }
                }

                /*-----------------------------*/
                if(Constant.repairID!=null){
                    llRepair.setVisibility(View.VISIBLE);
                    for(int i=0;i<Constant.repairID.length;i++){
                        if(i==0){
                            tvRepairOne.setVisibility(View.VISIBLE);
                            tvDateRepairOne.setVisibility(View.VISIBLE);
                            tvRepairOne.setText(Constant.repairTitle[i]);
                            tvDateRepairOne.setText("Posted on "+Constant.repairDate[i]);
                        }
                        else if(i==1){
                            tvRepairTwo.setVisibility(View.VISIBLE);
                            tvDateRepairTwo.setVisibility(View.VISIBLE);
                            tvRepairTwo.setText(Constant.repairTitle[i]);
                            tvDateRepairTwo.setText("Posted on "+Constant.repairDate[i]);
                        }
                    }
                }

                 /*-----------------------------*/
                if(Constant.chassisID!=null){
                    llChassis.setVisibility(View.VISIBLE);
                    for(int i=0;i<Constant.chassisID.length;i++){
                        if(i==0){
                            tvChassisOne.setVisibility(View.VISIBLE);
                            tvDateChassisOne.setVisibility(View.VISIBLE);
                            tvChassisOne.setText(Constant.chassisTitle[i]);
                            tvDateChassisOne.setText("Posted on "+Constant.chassisDate[i]);
                        }
                        else if(i==1){
                            tvChassisTwo.setVisibility(View.VISIBLE);
                            tvDateChassisTwo.setVisibility(View.VISIBLE);
                            tvChassisTwo.setText(Constant.chassisTitle[i]);
                            tvDateChassisTwo.setText("Posted on "+Constant.chassisDate[i]);
                        }
                    }
                }

                 /*-----------------------------*/
                if(Constant.transmissionID!=null){
                    llTransmission.setVisibility(View.VISIBLE);
                    for(int i=0;i<Constant.transmissionID.length;i++){
                        if(i==0){
                            tvTransmissionOne.setVisibility(View.VISIBLE);
                            tvDateTransmissionOne.setVisibility(View.VISIBLE);
                            tvTransmissionOne.setText(Constant.transmissionTitle[i]);
                            tvDateTransmissionOne.setText("Posted on "+Constant.transmissionDate[i]);
                        }
                        else if(i==1){
                            tvTransmissionTwo.setVisibility(View.VISIBLE);
                            tvDateTransmissionTwo.setVisibility(View.VISIBLE);
                            tvTransmissionTwo.setText(Constant.transmissionTitle[i]);
                            tvDateTransmissionTwo.setText("Posted on "+Constant.transmissionDate[i]);
                        }
                    }
                }


            }catch (NullPointerException e){

            }catch (Exception e){

            }
            finally {
                progressDialog.dismiss();
            }
        }
    }

}

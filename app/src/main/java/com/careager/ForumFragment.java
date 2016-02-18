package com.careager;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.careager.BL.ForumCategoryBL;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.careager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment implements View.OnClickListener {

    LinearLayout llDesign,llElectronic,llRepair,llEngine,llChassis,llTransmission;

    TextView tvDesignOne,tvDesignTwo,tvElectronicOne,tvElectronicTwo,tvRepairOne,tvRepairTwo,tvEngineOne,tvEngineTwo,tvChassisOne,tvChassisTwo,tvTransmissionOne,tvTransmissionTwo;
    TextView tvDateDesignOne,tvDateDesignTwo,tvDateElectronicOne,tvDateElectronicTwo,tvDateRepairOne,tvDateRepairTwo,tvDateEngineOne,tvDateEngineTwo,tvDateChassisOne,tvDateChassisTwo,tvDateTransmissionOne,tvDateTransmissionTwo;

    ForumCategoryBL objForumCategoryBL;

    FloatingActionButton btnChat;

    String userId,userType;

    ProgressDialog progressDialog;

    Button btnAskQuestion;


    public ForumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View item= inflater.inflate(R.layout.fragment_forum, container, false);

        initialize(item);

        if(Util.isInternetConnection(getActivity()))
            new GetCategory().execute();
        return item;
    }

    private void initialize(View view){
        btnChat= (FloatingActionButton) view.findViewById(R.id.chatFabButton);
        llDesign= (LinearLayout) view.findViewById(R.id.ll_design);
        llElectronic= (LinearLayout) view.findViewById(R.id.ll_electronic);
        llRepair= (LinearLayout) view.findViewById(R.id.ll_repair);
        llEngine= (LinearLayout) view.findViewById(R.id.ll_engine);
        llChassis= (LinearLayout) view.findViewById(R.id.ll_chassic);
        llTransmission= (LinearLayout) view.findViewById(R.id.ll_transmission);
        btnAskQuestion= (Button) view.findViewById(R.id.forum_ask_question_button);

        btnAskQuestion.setOnClickListener(this);

        tvDesignOne= (TextView) view.findViewById(R.id.tv_design_one);
        tvDesignTwo= (TextView) view.findViewById(R.id.tv_design_two);

        tvElectronicOne= (TextView) view.findViewById(R.id.tv_electronic_one);
        tvElectronicTwo= (TextView) view.findViewById(R.id.tv_electronic_two);

        tvRepairOne= (TextView) view.findViewById(R.id.tv_repair_one);
        tvRepairTwo= (TextView) view.findViewById(R.id.tv_repair_two);

        tvEngineOne= (TextView) view.findViewById(R.id.tv_engine_one);
        tvEngineTwo= (TextView) view.findViewById(R.id.tv_engine_two);

        tvChassisOne= (TextView) view.findViewById(R.id.tv_chassic_one);
        tvChassisTwo= (TextView) view.findViewById(R.id.tv_chassic_two);

        tvTransmissionOne= (TextView) view.findViewById(R.id.tv_transmission_one);
        tvTransmissionTwo= (TextView) view.findViewById(R.id.tv_transmission_two);
/*----------------------------------------------------------------------------------------*/
        tvDateDesignOne= (TextView) view.findViewById(R.id.tv_design_dateone);
        tvDateDesignTwo= (TextView) view.findViewById(R.id.tv_design_datetwo);

        tvDateElectronicOne= (TextView) view.findViewById(R.id.tv_electronic_dateone);
        tvDateElectronicTwo= (TextView) view.findViewById(R.id.tv_electronic_datetwo);

        tvDateRepairOne= (TextView) view.findViewById(R.id.tv_repair_dateone);
        tvDateRepairTwo= (TextView) view.findViewById(R.id.tv_repair_datetwo);

        tvDateEngineOne= (TextView) view.findViewById(R.id.tv_engine_dateone);
        tvDateEngineTwo= (TextView) view.findViewById(R.id.tv_engine_datetwo);

        tvDateChassisOne= (TextView) view.findViewById(R.id.tv_chassic_dateone);
        tvDateChassisTwo= (TextView) view.findViewById(R.id.tv_chassic_datetwo);

        tvDateTransmissionOne= (TextView) view.findViewById(R.id.tv_transmission_dateone);
        tvDateTransmissionTwo= (TextView) view.findViewById(R.id.tv_transmission_datetwo);

        objForumCategoryBL=new ForumCategoryBL();



        progressDialog=new ProgressDialog(getActivity());

        llDesign.setOnClickListener(this);
        llElectronic.setOnClickListener(this);
        llEngine.setOnClickListener(this);
        llRepair.setOnClickListener(this);
        llChassis.setOnClickListener(this);
        llTransmission.setOnClickListener(this);

        btnChat.setOnClickListener(this);

        userId= Util.getSharedPrefrenceValue(getActivity(), Constant.SP_LOGIN_ID);
        userType=Util.getSharedPrefrenceValue(getActivity(),Constant.SP_LOGIN_TYPE);

        if(userId!=null)
            if(userType!=null)
                if(userType.equalsIgnoreCase(Constant.strLoginUser)) {
                    //btnChat.setVisibility(View.VISIBLE);
                    btnAskQuestion.setVisibility(View.VISIBLE);
                }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_design:
                startActivity(new Intent(getActivity(),ForumQuestionList.class).putExtra("Category",Constant.categoryDesign));
                break;
            case R.id.ll_electronic:
                startActivity(new Intent(getActivity(), ForumQuestionList.class).putExtra("Category", Constant.categoryElectronics));
                break;
            case R.id.ll_repair:
                startActivity(new Intent(getActivity(),ForumQuestionList.class).putExtra("Category", Constant.categoryRepair));
                break;
            case R.id.ll_engine:
                startActivity(new Intent(getActivity(),ForumQuestionList.class).putExtra("Category", Constant.categoryEngine));
                break;
            case R.id.ll_chassic:
                startActivity(new Intent(getActivity(),ForumQuestionList.class).putExtra("Category", Constant.categoryChassis));
                break;
            case R.id.ll_transmission:
                startActivity(new Intent(getActivity(), ForumQuestionList.class).putExtra("Category", Constant.categoryChassis));
                break;
            case R.id.chatFabButton:
                startActivity(new Intent(getActivity(),ForumUserList.class));
                break;
            case R.id.forum_ask_question_button:
                startActivityForResult(new Intent(getActivity(), ForumAskQuestion.class), 1);
                break;
        }
    }


    private class GetCategory extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {

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

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode== Activity.RESULT_OK){
            if(Util.isInternetConnection(getActivity()))
                new GetCategory().execute();

        }
    }
}

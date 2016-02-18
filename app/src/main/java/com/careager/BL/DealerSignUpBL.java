package com.careager.BL;

import android.content.Context;

import com.careager.BE.DealerSignUpBE;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 11/5/2015.
 */
public class DealerSignUpBL {

    DealerSignUpBE objDealerSignUpBE;
    Context mContext;

    public String insertSignUpDetails(DealerSignUpBE dealerSignUpBE,Context context){
        mContext=context;
        objDealerSignUpBE=dealerSignUpBE;
        String result=callWsUrl();   // call webservice
        String status=validate(result);             // parse json
        return status;
    }


    private String callWsUrl(){

        if(objDealerSignUpBE.getDealerCompany()==null){
            objDealerSignUpBE.setDealerCompany("");
        }

        String URL="name="+objDealerSignUpBE.getName()+"&email="+objDealerSignUpBE.getEmail()+"&password="+objDealerSignUpBE.getPassword()+"&contact_no="+objDealerSignUpBE.getContact()+"&location="+objDealerSignUpBE.getLocation()+"&state="+objDealerSignUpBE.getState()+"&company="+objDealerSignUpBE.getDealerCompany()+"&category="+objDealerSignUpBE.getDealerCategory()+"&gcm_id="+objDealerSignUpBE.getGcmId()+"&device_id="+objDealerSignUpBE.getDeviceId()+"&latitude="+objDealerSignUpBE.getLatitude()+"&longitude="+objDealerSignUpBE.getLongitude();
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH,URL, Constant.WS_DEALER_SIGNUP);
        return txtJson;
    }

    private String validate(String result){

        String status="";
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            status=jsonObject.get("status").toString();
            if(status.equalsIgnoreCase(Constant.WS_RESPONSE_SUCCESS)){
                Util.setSharedPrefrenceValue(mContext, Constant.PREFS_NAME, Constant.SP_LOGIN_ID, jsonObject.get("showroom_id").toString());
            }
            status=status+","+jsonObject.get("message").toString();

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return status;
    }
}

package com.careager.BL;

import android.content.Context;
import android.util.Log;

import com.careager.BE.DealerLoginBE;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 11/5/2015.
 */
public class DealerLoginBL {

    DealerLoginBE objDealerLoginBE;
    Context mContext;


    public String validateSigninDetails(DealerLoginBE DealerLoginBE,String userType,Context context){
        mContext=context;
        objDealerLoginBE=DealerLoginBE;
        String result=callWsUrl(userType);   // call webservice
        String status=validate(result);             // parse json
        return status;

}

    private String callWsUrl(String strUserType){


        String URL="email="+objDealerLoginBE.getEmail()+"&password="+objDealerLoginBE.getPassword()+"&gcm_id="+objDealerLoginBE.getGcmID()+"&device_id="+objDealerLoginBE.getDeviceID();
        String txtJson="";
        if(strUserType.equalsIgnoreCase(Constant.strLoginBusiness)) {
             txtJson = RestFullWS.serverRequest(Constant.WS_PATH,URL, Constant.WS_DEALER_SIGNIN);
        }
        else if(strUserType.equalsIgnoreCase(Constant.strLoginUser)) {
            txtJson = RestFullWS.serverRequest(Constant.WS_PATH_USER,URL, Constant.WS_USER_SIGNIN);
        }
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
                Log.d("STATUS",status);
                Log.d("SHOWROOM_ID",jsonObject.get("showroom_id").toString());
                Util.setSharedPrefrenceValue(mContext,Constant.PREFS_NAME,Constant.SP_LOGIN_ID,jsonObject.get("showroom_id").toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }
}

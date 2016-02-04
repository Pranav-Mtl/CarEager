package com.careager.BL;

import android.content.Context;

import com.careager.BE.UserSignupBE;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by Pranav Mittal on 10/31/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class UserSignupBL {

    UserSignupBE objUserSignupBE;
    Context mContext;

    public String insertSignUpDetails(UserSignupBE userSignupBE,Context context){
        mContext=context;
        objUserSignupBE=userSignupBE;
        String result=callWsUrl();   // call webservice
        String status=validate(result);             // parse json
        return status;
    }


    private String callWsUrl(){

       // http://workfromhomejobs.co.in/user_webservices/user_register?name=sandeep&email=sandeepdnp28@gmail.com&contact_no=323232332&password=hello1

        String URL="email="+objUserSignupBE.getEmail()+"&password="+objUserSignupBE.getPassword()+"&contact_no="+objUserSignupBE.getMobile()+"&name="+objUserSignupBE.getName()+"&gcm_id="+objUserSignupBE.getGcmID()+"&device_id="+objUserSignupBE.getDeviceID();
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_USER,URL, Constant.WS_USER_SIGNUP);
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

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return status;
    }


    public String insertSignUpDetailsSocial(String email,String name,String gcm,String device,Context context){
        mContext=context;

        String result=callWsUrlSocial(email, name, gcm, device);   // call webservice
        String status=validate(result);             // parse json
        return status;
    }


    private String callWsUrlSocial(String email,String name,String gcm,String device){

       //http://careager.com/user_webservices/social_signup?name=sandeep&email=sandeepdnp@gmail.com&gcm_id=323&device_id=490934890439

        String URL="name="+name+"&email="+email+"&gcm_id="+gcm+"&device_id="+device;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_USER,URL, Constant.WS_SOCIAL_SIGNUP);
        return txtJson;
    }

}

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
 * Created by appslure on 13-12-2015.
 */
public class DealerContactBL {

    public String sendMessage(String category,String name,String mobile,String email,String message,String policy,String registration,String id){

        String result=callWsUrl(category,name,mobile,email,message,policy,registration,id);   // call webservice
        String status=validate(result);             // parse json
        return status;
    }


    private String callWsUrl(String category,String name,String mobile,String email,String message,String policy,String registration,String id){

        String URL="name="+name+"&email="+email+"&phone="+mobile+"&category="+category+"&message="+message+"&showroom_id="+id+"&policy_no="+policy+"&vehicle_no="+registration;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_Contact_owner);
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


        } catch (Exception e) {
            e.getLocalizedMessage();
        }

        return status;
    }
}

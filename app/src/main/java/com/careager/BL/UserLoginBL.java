package com.careager.BL;

import com.careager.BE.UserLoginBE;
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
public class UserLoginBL {

    UserLoginBE objUserLoginBE;

    public String validateSignInDetails(UserLoginBE userLoginBE){

        objUserLoginBE=userLoginBE;
        String result=callWsUrl();   // call webservice
        String status=validate(result);             // parse json
        return status;
    }


    private String callWsUrl(){



        String URL="email="+objUserLoginBE.getEmail()+"&password="+objUserLoginBE.getPassword();
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH,URL, Constant.WS_USER_SIGNIN);
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

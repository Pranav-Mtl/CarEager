package com.careager.BL;

import com.careager.BE.UserLoginBE;
import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 12-01-2016.
 */
public class SettingsBL {
    public String updateDetails(String id,String type,String name,String address,String oldPass,String newPass){


        String result=callWsUrl(id,type,name,address,oldPass,newPass);   // call webservice
        String status=validate(result);             // parse json
        return status;
    }


    private String callWsUrl(String id,String type,String name,String address,String oldPass,String newPass){



        String URL="id="+id+"&type="+type+"&name="+name+"&location="+address+"&old_password="+oldPass+"&new_password="+newPass;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_UPDATE_SETTINGS);
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

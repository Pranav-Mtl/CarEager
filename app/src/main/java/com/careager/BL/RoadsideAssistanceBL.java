package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 26-01-2016.
 */
public class RoadsideAssistanceBL {

    public String sendRoadSide(String name,String message,String latitude,String longitude){

        String result=callWsUrl(name,message,latitude,longitude);   // call webservice
        String status=validate(result);             // parse json
        return status;
    }


    private String callWsUrl(String name,String message,String latitude,String longitude){
//http://careager.com/careager_webservices/roadside_assistance?name=name&description=description&latitude=12&longitude=32
        String URL="name="+name+"&description="+message+"&latitude="+latitude+"&longitude="+longitude;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_ROADSIDE);
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

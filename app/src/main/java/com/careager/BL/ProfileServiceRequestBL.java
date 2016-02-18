package com.careager.BL;

import com.careager.BE.ProfileServicesRequestBE;
import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 07-01-2016.
 */
public class ProfileServiceRequestBL {

    ProfileServicesRequestBE objProfileServicesRequestBE;
    public String getServices(ProfileServicesRequestBE profileServicesRequestBE){

        objProfileServicesRequestBE=profileServicesRequestBE;

        String result=callWS();
        String status=validate(result);
        return status;

    }

    /* CALL WEB SERVICE */
    private String callWS(){

        String URL="&showroom_id="+objProfileServicesRequestBE.getShowroomID()+"&name="+objProfileServicesRequestBE.getName()+"&email="+objProfileServicesRequestBE.getEmail()+"&phone="+objProfileServicesRequestBE.getContact()+"&vehicle_no="+objProfileServicesRequestBE.getVehicle()+"&date="+objProfileServicesRequestBE.getDate()+"&services="+objProfileServicesRequestBE.getServices()+"&message="+objProfileServicesRequestBE.getDescription();
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_REQUEST_SERVICE);
        return txtJson;

    }

    /* PARSE MAIN JSON */
    private String  validate(String result){


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

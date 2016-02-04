package com.careager.BL;

import com.careager.BE.ClaimBusinessBE;
import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 18-01-2016.
 */
public class ClaimBusinessBL {

    ClaimBusinessBE objClaimBusinessBE;
    public String sendClaimBusiness(ClaimBusinessBE claimBusinessBE){

        objClaimBusinessBE=claimBusinessBE;
        String result=callWsUrl();   // call webservice
        String status=validate(result);             // parse json
        return status;
    }


    private String callWsUrl(){
        //http://www.careager.com/careager/careager_webservices/claim_business?showroom_id=4&business_email=d@dk.com&business_contact_no=0987654321&business_location=delhi&establish_year=1233&designation=ceo&name=bill&email=sandeep&contact_no=2888828321&location=Janak

        String URL="showroom_id="+objClaimBusinessBE.getBusinessID()+"&business_email="+objClaimBusinessBE.getBusinessEmail()+"&business_contact_no="+objClaimBusinessBE.getBusinessMobile()+"&business_location="+objClaimBusinessBE.getBusinessLocation()+"&establish_year="+objClaimBusinessBE.getBusinessYear()+"&designation="+objClaimBusinessBE.getPersonalDesignation()+"&name="+objClaimBusinessBE.getPersonalName()+"&email="+objClaimBusinessBE.getPersonalEmail()+"&contact_no="+objClaimBusinessBE.getPersonalMobile()+"&location="+objClaimBusinessBE.getPersonalLocation();
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_CLAIM_BUSINESS);
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

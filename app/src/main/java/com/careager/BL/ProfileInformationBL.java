package com.careager.BL;




import com.careager.BE.ProfileInformationBE;
import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 9/8/2015.
 */
public class ProfileInformationBL {

    String userId,phone,pass,email;
    String finalValue;
    public String status;

    ProfileInformationBE profileInformationBE;

    public String getData(String userId,String userType,ProfileInformationBE objProfileInformationBE)
    {
        this.userId=userId;
        profileInformationBE=objProfileInformationBE;
        try {

            String result = fetRecord(userId,userType);
            finalValue  = validate(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalValue;
    }

    private String fetRecord(String userId,String userType)
    {
        String URL="id="+userId+"&type="+userType;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_SETTINGS);
        return txtJson;
    }


    private String validate(String strValue)
    {
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(strValue);
            JSONArray jsonArrayObject = (JSONArray)obj;
            JSONObject jsonObject1=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());//

            profileInformationBE.setName(jsonObject1.get("name").toString());
            profileInformationBE.setEmail(jsonObject1.get("email").toString());
            profileInformationBE.setAddress(jsonObject1.get("location").toString());
            profileInformationBE.setPhone(jsonObject1.get("contact_no").toString());
        } catch (Exception e) {

        }
        return status;
    }






}
package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 05-01-2016.
 */
public class AddBusinessBL {
    //http://ec2-52-76-48-31.ap-southeast-1.compute.amazonaws.com/careager/careager_webservices/add_business?name=name&email=mm@mm.com&category=category&contact_no=0987654321&location=location
    public String addBusiness(String name,String email,String category,String contact,String lat,String longt,String address){

        String result=callWS(name,email,category,contact,lat,longt,address);
        String status=validate(result);

        return status;

    }

    /* CALL WEB SERVICE */
    private String callWS(String name,String email,String category,String contact,String lat,String longt,String address){

        //http://ec2-52-76-48-31.ap-southeast-1.compute.amazonaws.com/careager/user_webservices/forum_user_chatlist?user_id=1
        String URL="name="+name+"&email="+email+"&category="+category+"&contact_no="+contact+"&lat="+lat+"&lng="+longt+"&location="+address;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_ADD_BUSINESS);
        return txtJson;

    }

    /* PARSE MAIN JSON */
    private String validate(String result){

        String text="";
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            text=jsonObject.get("status").toString();


        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        return text;
    }

}

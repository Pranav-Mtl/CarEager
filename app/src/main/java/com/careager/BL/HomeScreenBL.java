package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 12-12-2015.
 */
public class HomeScreenBL {

    String userID;
    public void getAllData(String userid,String userType){
        userID=userid;
        String result=callWS(userid,userType);
        validate(result);
    }
    private String callWS(String userID,String userType){
        String URL = "";
        String txtJson="";
        if(userID==null) {
             txtJson = RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_LOGO);
        }else {
            URL="user_id="+userID+"&type="+userType;
            txtJson = RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_USER_DETAIL);
        }
        return txtJson;
    }

    private void validate(String result){
        String baseURL;
        String sale;
        String userDetail;
        String category;

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());

            if(userID==null) {
                Constant.carLogoUrl = jsonObject.get("base_url").toString();
                sale = jsonObject.get("brand_detail").toString();
                category=jsonObject.get("category").toString();
                parseLOGO(sale);
                parseCategory(category);
            }
            else {
                Constant.carLogoUrl = jsonObject.get("base_url").toString();
                Constant.IMAGE_URL=jsonObject.get("base_url_avatar").toString();
                sale = jsonObject.get("brand_detail").toString();
                userDetail=jsonObject.get("user_detail").toString();
                category=jsonObject.get("category").toString();
                parseLOGO(sale);
                parseUSER(userDetail);
                parseCategory(category);
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }

    private void parseLOGO(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

           // Constant.carLogo=new String[jsonArrayObject.size()+1];
            Constant.carName=new String[jsonArrayObject.size()+1];
            Constant.carID=new String[jsonArrayObject.size()];

            Constant.carName[0]="Select Car Maker";
            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
               // Constant.carLogo[i+1]=jsonObject.get("maker_logo").toString();
                Constant.carName[i+1]=jsonObject.get("maker_name").toString();
                Constant.carID[i]=jsonObject.get("maker_id").toString();

            }


        }
        catch (Exception e){

        }
    }

    private void parseCategory(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.homeCategory=new String[jsonArrayObject.size()];


            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.homeCategory[i]=jsonObject.get("category").toString();


            }


        }
        catch (Exception e){

        }
    }

    private void parseUSER(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
                Constant.NAME=jsonObject.get("name").toString();
                Constant.IMAGE=jsonObject.get("avatar").toString();






        }
        catch (Exception e){

        }
    }
}

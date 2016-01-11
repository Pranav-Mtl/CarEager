package com.careager.BL;

import android.content.Context;

import com.careager.BE.DealerProfileBE;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 02-12-2015.
 */
public class DealerProfileSaleBL {

    DealerProfileBE objDealerProfileBE;
    Context mContext;

    public void getProfileData(String userID,DealerProfileBE dealerProfileBE,Context context){
        mContext=context;
        objDealerProfileBE=dealerProfileBE;
        String result=callWS(userID);
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(String userID){

        String URL="id="+userID;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_DEALER_SALE);
        return txtJson;

    }

    /* PARSE MAIN JSON */
    private void validate(String result){


        String baseURL;
        String sale;

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());



            baseURL=jsonObject.get("base_url").toString();
            sale=jsonObject.get("sales_info").toString();


            parseBaseURL(baseURL);
            parseSale(sale);

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }


     /* PARSE JSON TO GET ALL BASE URL */

    private void parseBaseURL(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            objDealerProfileBE.setProfileBaseURL(jsonObject.get("profile_avatar").toString());
            objDealerProfileBE.setSaleBaseURL(jsonObject.get("sales_info_feature_image").toString());
        }
        catch (Exception e){

        }
    }

    private void parseSale(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.saleID=new String[jsonArrayObject.size()];
            Constant.saleImage=new String[jsonArrayObject.size()];
            Constant.saleMaker=new String[jsonArrayObject.size()];
            Constant.salePrice=new String[jsonArrayObject.size()];
            Constant.saleTitle=new String[jsonArrayObject.size()];
            Constant.saleYear=new String[jsonArrayObject.size()];
            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.saleID[i]=jsonObject.get("stock_id").toString();
                Constant.saleImage[i]=jsonObject.get("feature_image").toString();
                Constant.saleMaker[i]=jsonObject.get("maker").toString();
                Constant.salePrice[i]=jsonObject.get("price").toString();
                Constant.saleTitle[i]=jsonObject.get("vehicle_title").toString();
                Constant.saleYear[i]=jsonObject.get("manufacture_year").toString();

            }


        }
        catch (Exception e){

        }
    }
}

package com.careager.BL;

import com.careager.BE.DealerCategoryBE;
import com.careager.Configuration.Util;
import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 12-12-2015.
 */
public class DealerCategoryBL {

    DealerCategoryBE objDealerCategoryBE;

    public void getAllCategory(DealerCategoryBE dealerCategoryBE){
        objDealerCategoryBE=dealerCategoryBE;
        String result=callWS();
        validate(result);
    }

    private String callWS(){
        String text="";
        String URL="";

            text = RestFullWS.serverRequest(Constant.WS_PATH, URL, Constant.WS_CATEGORY);

        return text;
    }

    private void validate(String result){
        String status="";
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            objDealerCategoryBE.setCategoryJSON(jsonObject.get("category").toString());
            objDealerCategoryBE.setCompanyJSON(jsonObject.get("makerlist").toString());
            objDealerCategoryBE.setStatesJSON(jsonObject.get("state").toString());


        } catch (Exception e) {
            e.getLocalizedMessage();
        }


    }
}

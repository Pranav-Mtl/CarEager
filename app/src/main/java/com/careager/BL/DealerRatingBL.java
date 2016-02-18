package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 10-02-2016.
 */
public class DealerRatingBL {

    public String sendRating(String userID,String dealerID,String review,String rating){

        String result=callWsUrl(userID,dealerID,review,rating);   // call webservice
        String status=validate(result);             // parse json
        return status;
    }


    private String callWsUrl(String userID,String dealerID,String review,String rating){

        String URL="showroom_id="+dealerID+"&user_id="+userID+"&rating="+rating+"&comment="+review;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_DEALER_REVIEW);
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

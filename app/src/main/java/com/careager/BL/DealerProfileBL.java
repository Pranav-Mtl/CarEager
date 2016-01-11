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
 * Created by appslure on 24-11-2015.
 */
public class DealerProfileBL {

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
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_DEALER_PROFILE);
        return txtJson;
    }

    /* PARSE MAIN JSON */
    private void validate(String result){

        String status="";
        String profile;
        String baseURL;
        String reviews;
        String category;
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());


            profile=jsonObject.get("profile").toString();
            baseURL=jsonObject.get("base_url").toString();
            reviews=jsonObject.get("review").toString();
            category=jsonObject.get("tab").toString();

            Util.setSharedPrefrenceValue(mContext,Constant.PREFS_NAME,Constant.SP_DEALER_CATEGORY,category);

            parseBaseURL(baseURL);
            parseProfile(profile);
            parseReviews(reviews);

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

    /* PARSE JSON TO GET ALL PROFILE INFO. */

    private void parseProfile(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());
            objDealerProfileBE.setName(jsonObject.get("name").toString());
            objDealerProfileBE.setImage(jsonObject.get("avatar").toString());
            objDealerProfileBE.setOverview(jsonObject.get("overview").toString());
            objDealerProfileBE.setLocation(jsonObject.get("location").toString());
            objDealerProfileBE.setEmail(jsonObject.get("email").toString());
            objDealerProfileBE.setPhone(jsonObject.get("contact_no").toString());
            objDealerProfileBE.setCareagerRating(jsonObject.get("careager_rating").toString());
            objDealerProfileBE.setTotalRating(jsonObject.get("total").toString());
            objDealerProfileBE.setRating(jsonObject.get("avg").toString());


        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }

    /* PARSE JSON TO GET DEALER REVIEWS */

    private void parseReviews(String result){

        if(!result.equals("[]")) {
            JSONParser jsonP = new JSONParser();
            try {
                Object obj = jsonP.parse(result);
                JSONArray jsonArrayObject = (JSONArray) obj;
                Constant.reviewImage = new String[jsonArrayObject.size()];
                Constant.reviewDate = new String[jsonArrayObject.size()];
                Constant.reviewComment = new String[jsonArrayObject.size()];
                Constant.reviewName = new String[jsonArrayObject.size()];
                Constant.reviewRating = new int[jsonArrayObject.size()];

                for (int i = 0; i < jsonArrayObject.size(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                    Constant.reviewRating[i]=Integer.valueOf(jsonObject.get("rating").toString());
                    Constant.reviewDate[i]=jsonObject.get("timestamp").toString();
                    Constant.reviewComment[i]=jsonObject.get("comment").toString();
                    Constant.reviewName[i]=jsonObject.get("name").toString();
                    Constant.reviewImage[i]=jsonObject.get("avatar").toString();

                }


            } catch (Exception e) {
                e.getLocalizedMessage();
            }
        }

    }
}

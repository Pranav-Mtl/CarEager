package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 26-12-2015.
 */
public class LatestOffersBL {
    public void getOffers(){
        String result=callWS();
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(){

        String URL="";
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_LATEST_OFFERS);
        return txtJson;

    }

    /* PARSE MAIN JSON */
    private void validate(String result){


        String baseURL;
        String carOffer,showroomOffer,carEagerOffer;

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());

            carOffer=jsonObject.get("car_offers").toString();
            showroomOffer=jsonObject.get("showroom_offers").toString();
            carEagerOffer=jsonObject.get("careager_offers").toString();

            Constant.carOfferBaseURL=jsonObject.get("base_url_car_offers").toString();
            Constant.showroomOfferBaseURL=jsonObject.get("base_url_showroom_offers").toString();
            Constant.carEagerOfferBaseURL=jsonObject.get("base_url_careager_offers").toString();


            //URL(baseURL);
            parseSOffers(carOffer);
            parseShowRoom(showroomOffer);
            parseCarEager(carEagerOffer);

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }



    private void parseSOffers(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.carOfferID=new String[jsonArrayObject.size()];
            Constant.carOfferImage=new String[jsonArrayObject.size()];
            Constant.carOfferPosted=new String[jsonArrayObject.size()];
            Constant.carOffer=new String[jsonArrayObject.size()];
            Constant.carOfferTitle=new String[jsonArrayObject.size()];
            Constant.carOfferShowroomID=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.carOfferID[i]=jsonObject.get("stock_id").toString();
                Constant.carOfferImage[i]=jsonObject.get("feature_image").toString();
                Constant.carOfferPosted[i]=jsonObject.get("name").toString();
                Constant.carOffer[i]=jsonObject.get("offer_description").toString();
                Constant.carOfferTitle[i]=jsonObject.get("vehicle_title").toString();
                Constant.carOfferShowroomID[i]=jsonObject.get("showroom_id").toString();



            }


        }
        catch (Exception e){

        }
    }


    private void parseShowRoom(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.showroomOfferID=new String[jsonArrayObject.size()];
            Constant.showroomOfferImage=new String[jsonArrayObject.size()];
            Constant.showroomID=new String[jsonArrayObject.size()];
            Constant.showroomOffer=new String[jsonArrayObject.size()];


            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.showroomOfferID[i]=jsonObject.get("offer_id").toString();
                Constant.showroomOfferImage[i]=jsonObject.get("offer_image").toString();
                Constant.showroomID[i]=jsonObject.get("showroom_id").toString();
                Constant.showroomOffer[i]=jsonObject.get("offer_description").toString();




            }


        }
        catch (Exception e){

        }
    }

    private void parseCarEager(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.carEagerOfferID=new String[jsonArrayObject.size()];
            Constant.carEagerOfferImage=new String[jsonArrayObject.size()];
            Constant.carEagerID=new String[jsonArrayObject.size()];
            Constant.carEagerOffer=new String[jsonArrayObject.size()];


            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.carEagerOfferID[i]=jsonObject.get("offer_id").toString();
                Constant.carEagerOfferImage[i]=jsonObject.get("offer_image").toString();
                Constant.carEagerID[i]=jsonObject.get("showroom_id").toString();
                Constant.carEagerOffer[i]=jsonObject.get("offer_description").toString();




            }


        }
        catch (Exception e){

        }
    }

    private void URL(String result){


        String baseURL;
        String sale;

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());


            Constant.offerBaseURL=jsonObject.get("profile_avatar").toString();





        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }



}

package com.careager.BL;

import com.careager.BE.DealerProfileSaleDetailBE;
import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.Serializable;

/**
 * Created by appslure on 02-12-2015.
 */
public class DealerProfileSaleDetailBL  {

    DealerProfileSaleDetailBE objDealerProfileSaleDetailBE;
    public void getVehicleDetail(DealerProfileSaleDetailBE dealerProfileSaleDetailBE,String id){
        objDealerProfileSaleDetailBE=dealerProfileSaleDetailBE;
        String result=callWS(id);
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(String id){

        String URL="id="+id;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_DEALER_VEHICLE_DETAIL);
        return txtJson;

    }

    /* PARSE MAIN JSON */
    private void validate(String result){


        String baseURL;
        String detail;

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());

            baseURL=jsonObject.get("base_url").toString();
            detail=jsonObject.get("details").toString();

            parseBaseURL(baseURL);
            parseSale(detail);

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }

    /* parse base url */
    private void parseBaseURL(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

            objDealerProfileSaleDetailBE.setBaseUrl(jsonObject.get("sales_info_feature_image").toString());
            objDealerProfileSaleDetailBE.setDealerAvatar(jsonObject.get("profile_avatar").toString());
        }
        catch (Exception e){

        }
    }

    /* parse sale json */
    private void parseSale(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

            objDealerProfileSaleDetailBE.setTitle(jsonObject.get("vehicle_title").toString());
            objDealerProfileSaleDetailBE.setPrice(jsonObject.get("price").toString());
            objDealerProfileSaleDetailBE.setMaker(jsonObject.get("maker").toString());
            objDealerProfileSaleDetailBE.setModel(jsonObject.get("model").toString());
            objDealerProfileSaleDetailBE.setVehicleType(jsonObject.get("vehicle_type").toString());
            objDealerProfileSaleDetailBE.setYear(jsonObject.get("manufacture_year").toString());
            objDealerProfileSaleDetailBE.setColor(jsonObject.get("vehicle_color").toString());
            objDealerProfileSaleDetailBE.setStatus(jsonObject.get("vehicle_status").toString());
            objDealerProfileSaleDetailBE.setMileage(jsonObject.get("mileage").toString());
            objDealerProfileSaleDetailBE.setGearType(jsonObject.get("gear_type").toString());
            objDealerProfileSaleDetailBE.setFuelType(jsonObject.get("fuel_type").toString());
            objDealerProfileSaleDetailBE.setInStock(jsonObject.get("in_stock").toString());
            objDealerProfileSaleDetailBE.setInteriour(jsonObject.get("interior_features").toString());
            objDealerProfileSaleDetailBE.setExteriour(jsonObject.get("exterior_features").toString());
            objDealerProfileSaleDetailBE.setSafety(jsonObject.get("safety_features").toString());
            objDealerProfileSaleDetailBE.setExtra(jsonObject.get("extra_features").toString());
            objDealerProfileSaleDetailBE.setDescription(jsonObject.get("description").toString());
            objDealerProfileSaleDetailBE.setImage(jsonObject.get("feature_image").toString());
            objDealerProfileSaleDetailBE.setShowRoomID(jsonObject.get("showroom_id").toString());

            objDealerProfileSaleDetailBE.setDealerImage(jsonObject.get("avatar").toString());
            objDealerProfileSaleDetailBE.setDealerName(jsonObject.get("name").toString());
            objDealerProfileSaleDetailBE.setDealerLocation(jsonObject.get("location").toString());
            objDealerProfileSaleDetailBE.setDealerOverview(jsonObject.get("overview").toString());
            objDealerProfileSaleDetailBE.setDealerRating(jsonObject.get("careager_rating").toString());

        }
        catch (Exception e){

        }
    }


}

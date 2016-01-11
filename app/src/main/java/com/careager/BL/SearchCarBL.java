package com.careager.BL;

import android.content.Context;

import com.careager.BE.DealerProfileBE;
import com.careager.BE.FilterBE;
import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by appslure on 07-12-2015.
 */
public class SearchCarBL {

   FilterBE objFilterBE;

    public void getSearchCarList(String location,String min,String max){



        String result=callWS(location,min,max);
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(String location,String min,String max){

        String URL="location="+location+"&min="+min+"&max="+max;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_SEARCH_CAR_LIST);
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

            sale=jsonObject.get("search_result").toString();
            baseURL=jsonObject.get("base_url").toString();

            parseURL(baseURL);
            parseSale(sale);

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }

    private void parseURL(String result) {
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            Constant.searchBaseURL=jsonObject.get("profile_avatar").toString();
        } catch (Exception e) {
        }

    }


    private void parseSale(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.searchID=new String[jsonArrayObject.size()];
            Constant.searchImage=new String[jsonArrayObject.size()];
            Constant.searchMaker=new String[jsonArrayObject.size()];
            Constant.searchPrice=new String[jsonArrayObject.size()];
            Constant.searchTitle=new String[jsonArrayObject.size()];
            Constant.searchYear=new String[jsonArrayObject.size()];
            Constant.searchDescription=new String[jsonArrayObject.size()];
            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.searchID[i]=jsonObject.get("stock_id").toString();
                Constant.searchImage[i]=jsonObject.get("feature_image").toString();
                Constant.searchMaker[i]=jsonObject.get("maker").toString();
                Constant.searchPrice[i]=jsonObject.get("price").toString();
                Constant.searchTitle[i]=jsonObject.get("vehicle_title").toString();
                Constant.searchYear[i]=jsonObject.get("manufacture_year").toString();
                Constant.searchDescription[i]=jsonObject.get("description").toString();

            }


        }
        catch (Exception e){

        }
    }

    public void getFilteredList(FilterBE FilterBE){
        objFilterBE=FilterBE;
        String result=callFilteredWS();
        validate(result);

    }

    private String callFilteredWS(){

       //http://ec2-52-76-48-31.ap-southeast-1.compute.amazonaws.com/careager/careager_webservices/search_old_new?location=jaipur&min=1&max=8&brand=Alfa%20Romeo&model=mito&color=red&vehicle_status=new&vehicle_type=Hatchback&fuel_type=CNG
        String URL="location="+objFilterBE.getLocation()+"&min="+objFilterBE.getMinPrice()+"&max="+objFilterBE.getMaxPrice()+"&brand="+objFilterBE.getBrand()+"&model="+objFilterBE.getModel()+"&color="+objFilterBE.getColor()+"&vehicle_status="+objFilterBE.getCar()+"&vehicle_type="+objFilterBE.getCarType()+"&fuel_type="+objFilterBE.getFuel();

        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_FILTER);
        return txtJson;

    }
}

package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 13-12-2015.
 */
public class SearchServiceBL {
    public void getSearchServiceList(String location,String category){
        String result=callWS(location,category);
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(String location,String category){

        String URL="location="+location+"&category="+category;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_SEARCH_SERVICE_LIST);
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

            sale=jsonObject.get("result").toString();
            Constant.serviceURL=jsonObject.get("base_url").toString();


            parseSale(sale);

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }



    private void parseSale(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.serviceID=new String[jsonArrayObject.size()];
            Constant.serviceAddress=new String[jsonArrayObject.size()];
            Constant.serviceImage=new String[jsonArrayObject.size()];
            Constant.serviceCompany=new String[jsonArrayObject.size()];
            Constant.serviceName=new String[jsonArrayObject.size()];
            Constant.serviceContact=new String[jsonArrayObject.size()];
            Constant.serviceOverview=new String[jsonArrayObject.size()];
            Constant.serviceLatitude=new Double[jsonArrayObject.size()];
            Constant.serviceLongitude=new Double[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.serviceID[i]=jsonObject.get("showroom_id").toString();
                Constant.serviceAddress[i]=jsonObject.get("location").toString();
                Constant.serviceImage[i]=jsonObject.get("avatar").toString();
                Constant.serviceName[i]=jsonObject.get("name").toString();
                Constant.serviceContact[i]=jsonObject.get("contact_no").toString();
                Constant.serviceOverview[i]=jsonObject.get("overview").toString();
                Constant.serviceLatitude[i]=Double.valueOf(jsonObject.get("latitude").toString());
                Constant.serviceLongitude[i]=Double.valueOf(jsonObject.get("longitude").toString());


            }


        }
        catch (Exception e){

        }
    }
}

package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 13-12-2015.
 */
public class CarInfoListBL {

    public void getCarInfoList(String maker){
        String result=callWS(maker);
        validate(result);
    }
    private String callWS(String maker){
        String URL="brand="+maker;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_MODEL);
        return txtJson;

    }

    private void validate(String result){
        String baseURL;
        String carList;

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());


            carList=jsonObject.get("result").toString();



            parseCar(carList);

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }

    private void parseCar(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.modelID=new String[jsonArrayObject.size()];
            Constant.modelMaker=new String[jsonArrayObject.size()];
            Constant.modelName=new String[jsonArrayObject.size()];
            Constant.modelOverview=new String[jsonArrayObject.size()];
            Constant.modelMinPrice=new String[jsonArrayObject.size()];
            Constant.modelMaxPrice=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.modelID[i]=jsonObject.get("id").toString();
                Constant.modelName[i]=jsonObject.get("model_name").toString();
                Constant.modelMaker[i]=jsonObject.get("maker_name").toString();
                Constant.modelOverview[i]=jsonObject.get("overview").toString();
                Constant.modelMinPrice[i]=jsonObject.get("min_price").toString();
                Constant.modelMaxPrice[i]=jsonObject.get("max_price").toString();


            }


        }
        catch (Exception e){

        }
    }
}

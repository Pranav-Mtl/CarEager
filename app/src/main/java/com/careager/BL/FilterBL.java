package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by appslure on 10-12-2015.
 */
public class FilterBL {

    String brand,fuel,color,vehicleType,model;

    public List<String> listMaker;
    public List<String> listMakerID;
    public List<String> listFuel;
    public List<String> listColor;
    public List<String> listVehicleType;
    public List<String> listModel;

    public void getFilterData(){

        listMaker=new ArrayList<String>();
        listMakerID=new ArrayList<String>();
        listFuel=new ArrayList<String>();
        listColor=new ArrayList<String>();
        listVehicleType=new ArrayList<String>();
        listModel=new ArrayList<>();

        listMaker.add("Select Brand/Maker");
        listFuel.add("Select Fuel Type");
        listColor.add("Select Color");
        listVehicleType.add("Select Car Type");
        listModel.add("Select Model");

        String result=callWS();
        validate(result);
    }

    /* CALL WEB SERVICE */
    private String callWS(){

        String URL="";
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_FILTER_DATA);
        return txtJson;

    }

    /* PARSE MAIN JSON */
    private void validate(String result){



        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject=(JSONObject)jsonP.parse(jsonArrayObject.get(0).toString());

            brand=jsonObject.get("brand").toString();
            fuel=jsonObject.get("fuel_type").toString();
            vehicleType=jsonObject.get("vehicle_type").toString();
            color=jsonObject.get("color").toString();


            parseMaker(brand);
            parseFuel(fuel);
            parseVehicleType(vehicleType);
            parseColor(color);



        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }

    /* parse maker json*/
    private void parseMaker(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
               listMaker.add(jsonObject.get("maker_name").toString());
                listMakerID.add(jsonObject.get("maker_id").toString());

            }
        }
        catch (Exception e){

        }
    }

    /* parse fuel type json*/
    private void parseFuel(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                listFuel.add(jsonObject.get("fuel_type_name").toString());

            }
        }
        catch (Exception e){

        }
    }

    /* parse color json*/
    private void parseColor(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                listColor.add(jsonObject.get("color").toString());

            }
        }
        catch (Exception e){

        }
    }

    /* parse vehicleType json*/
    private void parseVehicleType(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                listVehicleType.add(jsonObject.get("vehicle_type_name").toString());

            }
        }
        catch (Exception e){

        }
    }

    /*----------------MODEL -----------------------------*/

    public void getModelData(String modelID){

        listModel=new ArrayList<String>();
        listModel.add("Select Model");

        String result=callWSModel(modelID);
        parseModel(result);
    }

    /* CALL WEB SERVICE */
    private String callWSModel(String modelID){

        String URL="maker_id="+modelID;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_FILTER_MODEL);
        return txtJson;

    }

    private void parseModel(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            String model=jsonObject.get("model").toString();

            obj=jsonP.parse(model);
            jsonArrayObject= (JSONArray) obj;
            for(int i=0;i<jsonArrayObject.size();i++){
                 jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                 listModel.add(jsonObject.get("model_name").toString());

            }
        }
        catch (Exception e){

        }
    }

}

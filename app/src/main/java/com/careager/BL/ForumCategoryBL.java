package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 10-01-2016.
 */
public class ForumCategoryBL {

    public void getForumCategory(){

        String result=callWS();
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(){

        String URL="";
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_FORUM_CATEGORY);
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

            String design=jsonObject.get(Constant.categoryDesign).toString();
            String electric=jsonObject.get(Constant.categoryElectronics).toString();
            String repair=jsonObject.get(Constant.categoryRepair).toString();
            String engine=jsonObject.get(Constant.categoryEngine).toString();
            String transmission=jsonObject.get(Constant.categoryTransmission).toString();
            String chassis=jsonObject.get(Constant.categoryChassis).toString();


            if(!design.equalsIgnoreCase("[]"))
                parseDesign(design);

            if(!electric.equalsIgnoreCase("[]"))
                parseElectric(electric);

            if(!repair.equalsIgnoreCase("[]"))
                parseRepair(repair);

            if(!engine.equalsIgnoreCase("[]"))
                parseEngine(engine);

            if(!transmission.equalsIgnoreCase("[]"))
                parseTransmission(transmission);

            if(!chassis.equalsIgnoreCase("[]"))
                parseChassis(chassis);



        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }

    private void parseDesign(String result) {
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.designID=new String[jsonArrayObject.size()];
            Constant.designTitle=new String[jsonArrayObject.size()];
            Constant.designName=new String[jsonArrayObject.size()];
            Constant.designDate=new String[jsonArrayObject.size()];

            for (int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.designTitle[i]=jsonObject.get("title").toString();
                Constant.designID[i]=jsonObject.get("id").toString();
                Constant.designName[i]=jsonObject.get("name").toString();
                Constant.designDate[i]=jsonObject.get("date").toString();

            }

        } catch (Exception e) {
        }

    }

    private void parseElectric(String result) {
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.electronicID=new String[jsonArrayObject.size()];
            Constant.electronicTitle=new String[jsonArrayObject.size()];
            Constant.electronicName=new String[jsonArrayObject.size()];
            Constant.electronicDate=new String[jsonArrayObject.size()];

            for (int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.electronicTitle[i]=jsonObject.get("title").toString();
                Constant.electronicID[i]=jsonObject.get("id").toString();
                Constant.electronicName[i]=jsonObject.get("name").toString();
                Constant.electronicDate[i]=jsonObject.get("date").toString();

            }

        } catch (Exception e) {
        }

    }

    private void parseEngine(String result) {
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.engineID=new String[jsonArrayObject.size()];
            Constant.engineTitle=new String[jsonArrayObject.size()];
            Constant.engineName=new String[jsonArrayObject.size()];
            Constant.engineDate=new String[jsonArrayObject.size()];

            for (int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.engineTitle[i]=jsonObject.get("title").toString();
                Constant.engineID[i]=jsonObject.get("id").toString();
                Constant.engineName[i]=jsonObject.get("name").toString();
                Constant.engineDate[i]=jsonObject.get("date").toString();

            }

        } catch (Exception e) {
        }

    }

    private void parseRepair(String result) {
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.repairID=new String[jsonArrayObject.size()];
            Constant.repairTitle=new String[jsonArrayObject.size()];
            Constant.repairName=new String[jsonArrayObject.size()];
            Constant.repairDate=new String[jsonArrayObject.size()];

            for (int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.repairTitle[i]=jsonObject.get("title").toString();
                Constant.repairID[i]=jsonObject.get("id").toString();
                Constant.repairName[i]=jsonObject.get("name").toString();
                Constant.repairDate[i]=jsonObject.get("date").toString();

            }

        } catch (Exception e) {
        }

    }

    private void parseTransmission(String result) {
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.transmissionID=new String[jsonArrayObject.size()];
            Constant.transmissionTitle=new String[jsonArrayObject.size()];
            Constant.transmissionName=new String[jsonArrayObject.size()];
            Constant.transmissionDate=new String[jsonArrayObject.size()];

            for (int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.transmissionID[i]=jsonObject.get("title").toString();
                Constant.transmissionTitle[i]=jsonObject.get("id").toString();
                Constant.transmissionName[i]=jsonObject.get("name").toString();
                Constant.transmissionDate[i]=jsonObject.get("date").toString();

            }

        } catch (Exception e) {
        }

    }

    private void parseChassis(String result) {
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.chassisID=new String[jsonArrayObject.size()];
            Constant.chassisTitle=new String[jsonArrayObject.size()];
            Constant.chassisName=new String[jsonArrayObject.size()];
            Constant.chassisDate=new String[jsonArrayObject.size()];

            for (int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.chassisID[i]=jsonObject.get("title").toString();
                Constant.chassisTitle[i]=jsonObject.get("id").toString();
                Constant.chassisName[i]=jsonObject.get("name").toString();
                Constant.chassisDate[i]=jsonObject.get("date").toString();

            }

        } catch (Exception e) {
        }

    }
}

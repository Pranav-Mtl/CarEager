package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 05-01-2016.
 */
public class TipsAdviceBL {

    public void getTipsList(){

        String result=callWS();
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(){

        String URL="";
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_TIPS_ADVICE);
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

            Constant.tipsURL=jsonObject.get("base_url").toString();
            String tipsDetail=jsonObject.get("detail").toString();
            if(!tipsDetail.equalsIgnoreCase("[]"))
               parseTips(tipsDetail);


        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }

    private void parseTips(String result) {
        JSONParser jsonP = new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.tipsTitle=new String[jsonArrayObject.size()];
            Constant.tipsContent=new String[jsonArrayObject.size()];
            Constant.tipsImage=new String[jsonArrayObject.size()];
            Constant.tipsDate=new String[jsonArrayObject.size()];
            for (int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.tipsTitle[i]=jsonObject.get("title").toString();
                Constant.tipsContent[i]=jsonObject.get("content").toString();
                Constant.tipsImage[i]=jsonObject.get("image").toString();
                Constant.tipsDate[i]=jsonObject.get("date").toString();

            }

        } catch (Exception e) {
        }

    }


}

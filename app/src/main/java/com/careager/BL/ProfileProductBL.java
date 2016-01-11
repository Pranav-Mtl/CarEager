package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 13-12-2015.
 */
public class ProfileProductBL {

    public void getProduct(String id){
        String result=callWS(id);
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(String id){

        String URL="id="+id;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_PROFILE_PRODUCT);
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

            sale=jsonObject.get("spare_part").toString();
            Constant.productBaseURL=jsonObject.get("base_url").toString();



            parseSOffers(sale);

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }



    private void parseSOffers(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj = jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.productDescription=new String[jsonArrayObject.size()];
            Constant.productID=new String[jsonArrayObject.size()];
            Constant.productImage=new String[jsonArrayObject.size()];
            Constant.productName=new String[jsonArrayObject.size()];
            Constant.productPrice=new String[jsonArrayObject.size()];


            for(int i=0;i<jsonArrayObject.size();i++){
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                Constant.productID[i]=jsonObject.get("product_id").toString();
                Constant.productDescription[i]=jsonObject.get("product_desc").toString();
                Constant.productImage[i]=jsonObject.get("product_img").toString();
                Constant.productName[i]=jsonObject.get("product_name").toString();
                Constant.productPrice[i]=jsonObject.get("product_price").toString();



            }


        }
        catch (Exception e){

        }
    }


}

package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 04-01-2016.
 */
public class ArticleQuestionBL {
    public void getArticleList(){

        String result=callWS();
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(){

        //http://ec2-52-76-48-31.ap-southeast-1.compute.amazonaws.com/careager/user_webservices/forum_user_chatlist?user_id=1
        String URL="";
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_ARTICLE_LIST);
        return txtJson;

    }

    /* PARSE MAIN JSON */
    private void validate(String result){

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

            obj=jsonP.parse(jsonObject.get("result").toString());
            jsonArrayObject= (JSONArray) obj;


            Constant.articleID=new String[jsonArrayObject.size()];
            Constant.articleTitle=new String[jsonArrayObject.size()];
            Constant.articleDescription=new String[jsonArrayObject.size()];
            Constant.articleDate=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {
                jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                Constant.articleID[i] = jsonObject.get("id").toString();
                Constant.articleTitle[i] = jsonObject.get("title").toString();
                Constant.articleDescription[i] = jsonObject.get("description").toString();
                Constant.articleDate[i] = jsonObject.get("date").toString();

            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }
}

package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

/**
 * Created by appslure on 05-02-2016.
 */
public class ForumAskQuestionBL {


    public void getForumQuestionCategory(){

        String result=callWS();
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(){

        String URL="";
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_FORUM_QUESTION_CATEGORY);
        return txtJson;

    }

    /* PARSE MAIN JSON */
    private void validate(String result){

        if(!result.equalsIgnoreCase("[]")) {

            JSONParser jsonP = new JSONParser();
            try {
                Object obj = jsonP.parse(result);
                JSONArray jsonArrayObject = (JSONArray) obj;
                Constant.categoryQuestion = new ArrayList();
                Constant.categoryQuestion.add("Select Category");
                for (int i = 0; i < jsonArrayObject.size(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
                    Constant.categoryQuestion.add(jsonObject.get("tag").toString());
                }

            } catch (Exception e) {
                e.getLocalizedMessage();
            }
        }

    }

    /*--------------------------------------------*/

    public String askQuestion(String userID,String question,String category){

        String result=callWSQuestion( userID,question,category);
        String status=validateQuestion(result);

        return status;

    }

    /* CALL WEB SERVICE */
    private String callWSQuestion(String userID,String question,String category){

        //http://careager.com/careager_webservices/ask_question?user_id=1&title=title&category=category
        String URL="user_id="+userID+"&title="+question+"&category="+category;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_FORUM_SEND_QUESTION);
        return txtJson;

    }

    /* PARSE MAIN JSON */
    private String validateQuestion(String result){

       String status="";
            JSONParser jsonP = new JSONParser();
            try {
                Object obj = jsonP.parse(result);
                JSONArray jsonArrayObject = (JSONArray) obj;

                    JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
                    status=jsonObject.get("status").toString();


            } catch (Exception e) {
                e.getLocalizedMessage();
            }

            return status;
    }

}

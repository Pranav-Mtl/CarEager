package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 04-01-2016.
 */
public class ForumQuestionBL {
    public void getForumList(String category){

        String result=callWS(category);
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(String category){

        //http://ec2-52-76-48-31.ap-southeast-1.compute.amazonaws.com/careager/user_webservices/forum_user_chatlist?user_id=1
        String URL="&category="+category;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_FORUM_LIST);
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


            Constant.forumID=new String[jsonArrayObject.size()];
            Constant.forumTitle=new String[jsonArrayObject.size()];

            Constant.forumDate=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {
                 jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                Constant.forumID[i] = jsonObject.get("id").toString();
                Constant.forumTitle[i] = jsonObject.get("title").toString();
                Constant.forumDate[i] = jsonObject.get("date").toString();

            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }
}

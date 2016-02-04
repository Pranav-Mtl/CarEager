package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 26-01-2016.
 */
public class DealerChatListBL {

    public void getSearchCarList(String user_id){

        String result=callWS(user_id);
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(String user_id){

       // http://careager.com/careager_webservices/showroomChatList?id=4
        String URL="id="+user_id;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_DEALER_CHAT_LIST);
        return txtJson;

    }

    /* PARSE MAIN JSON */
    private void validate(String result){

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.forumUserID=new String[jsonArrayObject.size()];
            Constant.forumUserName=new String[jsonArrayObject.size()];
            Constant.forumUserChat=new String[jsonArrayObject.size()];
            Constant.forumUserDate=new String[jsonArrayObject.size()];
            Constant.forumUserImage=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                Constant.forumUserID[i] = jsonObject.get("user_id").toString();
                Constant.forumUserName[i] = jsonObject.get("name").toString();
                Constant.forumUserChat[i] = jsonObject.get("message").toString();
                Constant.forumUserDate[i] = jsonObject.get("timestamp").toString();
                Constant.forumUserImage[i] = jsonObject.get("avatar").toString();

            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }
}

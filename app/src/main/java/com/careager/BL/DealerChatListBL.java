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

    public void getSearchCarList(String user_id,String userType){

        String result=callWS(user_id,userType);
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(String user_id,String userType){

       // http://careager.com/careager_webservices/showroomChatList?id=4
        String URL="id="+user_id+"&user_type="+userType;
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

    public void getSearchUserList(String user_id,String name,String type){

        String result=callWSSearch(user_id, name, type);
        validateSearch(result);

    }

    /* CALL WEB SERVICE */
    private String callWSSearch(String user_id,String name,String type){

        //http://ec2-52-76-48-31.ap-southeast-1.compute.amazonaws.com/careager/user_webservices/forum_user_chatlist?user_id=1
        String URL="id="+user_id+"&term="+name+"&type="+type;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH, URL, Constant.WS_USER_BUSINESS_SEARCH);
        return txtJson;

    }

    /* PARSE MAIN JSON */
    private void validateSearch(String result){

        if(!result.equalsIgnoreCase("[]")) {
            JSONParser jsonP = new JSONParser();
            try {
                Object obj = jsonP.parse(result);
                JSONArray jsonArrayObject = (JSONArray) obj;

                Constant.forumUserIDSearch = new String[jsonArrayObject.size()];
                Constant.forumUserNameSearch = new String[jsonArrayObject.size()];
                Constant.forumUserChatSearch = new String[jsonArrayObject.size()];
                Constant.forumUserDateSearch = new String[jsonArrayObject.size()];
                Constant.forumUserImageSearch = new String[jsonArrayObject.size()];

                for (int i = 0; i < jsonArrayObject.size(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());

                    Constant.forumUserIDSearch[i] = jsonObject.get("showroom_id").toString();
                    Constant.forumUserNameSearch[i] = jsonObject.get("name").toString();
                   /* Constant.forumUserChatSearch[i] = jsonObject.get("message").toString();
                    Constant.forumUserDateSearch[i] = jsonObject.get("timestamp").toString();
                    Constant.forumUserImageSearch[i] = jsonObject.get("avatar").toString();*/

                }

            } catch (Exception e) {
                e.getLocalizedMessage();
            }
        }
        else
            Constant.forumUserNameSearch = new String[0];


    }
}

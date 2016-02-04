package com.careager.BL;

import com.careager.BE.ForumQuestionDetailBE;
import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Created by appslure on 04-01-2016.
 */
public class ForumQuestionDetailBL {

    ForumQuestionDetailBE objForumQuestionDetailBE;

    public void getForumDetail(String userID,ForumQuestionDetailBE forumQuestionDetailBE){

        objForumQuestionDetailBE=forumQuestionDetailBE;
        String result=callWS(userID);
        validate(result);

    }

    /* CALL WEB SERVICE */
    private String callWS(String userID){

        //http://ec2-52-76-48-31.ap-southeast-1.compute.amazonaws.com/careager/user_webservices/forum_user_chatlist?user_id=1
        String URL="&id="+userID;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_FORUM_DETAIL);
        return txtJson;

    }

    /* PARSE MAIN JSON */
    private void validate(String result){

        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());

            parseDetail(jsonObject.get("detail").toString());
            String comment=jsonObject.get("comment").toString();
            if(!comment.equalsIgnoreCase("[]")){
                parseComment(comment);
            }


        } catch (Exception e) {
            e.getLocalizedMessage();
        }

    }
    private void parseDetail(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;
            JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(0).toString());
            objForumQuestionDetailBE.setTitle(jsonObject.get("title").toString());
            objForumQuestionDetailBE.setName(jsonObject.get("name").toString());
            objForumQuestionDetailBE.setTimestamp(jsonObject.get("date").toString());

        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }

    private void parseComment(String result){
        JSONParser jsonP=new JSONParser();
        try {
            Object obj =jsonP.parse(result);
            JSONArray jsonArrayObject = (JSONArray) obj;

            Constant.forumCommentName=new String[jsonArrayObject.size()];
            Constant.forumCommentComment=new String[jsonArrayObject.size()];
            Constant.forumCommentTimestamp=new String[jsonArrayObject.size()];

            for(int i=0;i<jsonArrayObject.size();i++) {
                JSONObject jsonObject = (JSONObject) jsonP.parse(jsonArrayObject.get(i).toString());
               Constant.forumCommentName[i]=jsonObject.get("name").toString();
                Constant.forumCommentComment[i]=jsonObject.get("comment").toString();
                Constant.forumCommentTimestamp[i]=jsonObject.get("timestamp").toString();
            }

        } catch (Exception e) {
            e.getLocalizedMessage();
        }
    }
}

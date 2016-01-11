package com.careager.BL;

import com.careager.Constant.Constant;
import com.careager.WS.RestFullWS;

/**
 * Created by appslure on 14-12-2015.
 */
public class ProfileServicesBL {

    public String getServices(String userID){
        String result=callWS(userID);
        return result;
    }
    private String callWS(String userid){
        //http://ec2-52-76-48-31.ap-southeast-1.compute.amazonaws.com/careager/careager_webservices/service?id=4
        String URL="id="+userid;
        String txtJson= RestFullWS.serverRequest(Constant.WS_PATH_CAREAGER, URL, Constant.WS_PROFILE_SERVICES);
        return txtJson;

    }
}

package com.careager.Constant;

import com.careager.careager.R;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Pranav Mittal on 10/31/2015.
 * Appslure WebSolution LLP
 * www.appslure.com
 */
public class Constant {

    public static  String STREMAILADDREGEX="^[_A-Za-z0-9]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,4})$"; //EMAIL REGEX

    public static final String MSG_KEY = "m";
    public static final String GOOGLE_PROJ_ID ="38470423534";//38470423534

    ////////////////       WEB SERVICE URL      //////////////////////\

//Google ID: 38470423534-7m62l1don0p9mdl7hkkk7ub1u1seigg6.apps.googleusercontent.com

    public static String WS_HTTP="Http";
    public static String WS_DOMAIN_NAME="www.careager.com";
    public static String WS_PATH="/webservices/";
    public static String WS_PATH_CAREAGER="/careager_webservices/";
    public static String WS_PATH_USER="/user_webservices/";

    /*  SHARED PREFERENCE FILES */

    public static  String PREFS_NAME = "MyPrefsFile";   //SHARED PREFERENCE FILE NAME

    /* car info logo height width*/
    public static int CategoryImageWidth=150;
    public static int CategoryImageHeight=150;

    public static String categoryDesign="Car Queries";
    public static String categoryElectronics="Car Reviews";
    public static String categoryRepair="Upcoming -or- Latest";
    public static String categoryChassis="Engineering/Technology";
    public static String categoryEngine="CarEager official";




    /* WEB SERVICES  */

    public static String WS_DEALER_SIGNUP="dealer_register";
    public static String WS_DEALER_SIGNIN="dealer_login";
    public static String WS_USER_SIGNUP="user_register";
    public static String WS_SOCIAL_SIGNUP="checksociallogin";
    public static String WS_USER_SIGNIN="user_login";
    public static String WS_USER_SIGNIN_SOCIAL="checksociallogin";
    public static String WS_DEALER_PROFILE="profile";
    public static String WS_DEALER_SALE="sales";
    public static String WS_DEALER_VEHICLE_DETAIL="vehicle_details";
    public static String WS_SEARCH_CAR_LIST="search_old_new";
    public static String WS_FILTER_DATA="filter_data";
    public static String WS_FILTER_MODEL="brand_model";
    public static String WS_CATEGORY="signup_category";
    public static String WS_LOGO="brand_logo";
    public static String WS_USER_DETAIL="user_detail";
    public static String WS_LOGOUT="logout";
    public static String WS_MODEL="model";
    public static String WS_SEARCH_SERVICE_LIST="service_search";
    public static String WS_LATEST_OFFERS="latest_offers_app";
    public static String WS_Contact_owner="category_email";

    public static String WS_CLAIM_BUSINESS="claim_business";
    public static String WS_PROFILE_SERVICES="service";
    public static String WS_PROFILE_PRODUCT="spare_part";

    public static String WS_FILTER="search_old_new";
    public static String WS_CHAT="careager_chat";
    public static String WS_USER_CHAT_LIST="forum_user_chatlist";
    public static String WS_USER_CHAT_SEARCH="forum_user_search_chatlist";
    public static String WS_USER_BUSINESS_SEARCH="showroomOruserSearch";
    public static String WS_DEALER_CHAT_LIST="showroomChatList";
    public static String WS_FORUM_CHAT="forum_chat";
    public static String WS_FORUM_LIST="forum";
    public static String WS_ARTICLE_LIST="article";
    public static String WS_FORUM_DETAIL="forum_detail";
    public static String WS_ARTICLE_DETAIL="article_detail";
    public static String WS_TIPS_ADVICE="tip_advice";
    public static String WS_ADD_BUSINESS="add_business";
    public static String WS_REQUEST_SERVICE="request_quote";
    public static String WS_FORUM_CATEGORY="forum_categories";
    public static String WS_FORUM_QUESTION_CATEGORY="forum_category";
    public static String WS_FORUM_SEND_QUESTION="ask_question";
    public static String WS_SETTINGS="setting";
    public static String WS_UPDATE_SETTINGS="update_setting";
    public static String WS_UPDATE_COMMENT="update_coment";
    public static String WS_FORUM_COMMENT="question_comment";
    public static String WS_VERIFIED_BUSINESS="add_business_validation";

    public static String WS_DEALER_REVIEW="business_rating";

    public static String WS_ROADSIDE="roadside_assistance";

    public static boolean flatGCM=false;
    public static boolean flatFORUM=false;



    /* SHARED PREFERENCE VARIABLES */

    public static String SP_LOGIN_ID="login";
    public static String SP_LOGIN_TYPE="type";
    public static String SP_DEALER_CATEGORY="category";
    public static String SP_GCM_ID="gcm";
    public static String SP_DEVICE_ID="device";



    /* USER SELECTION TYPE FROM HOW IT WORKS SCREEN */

    public static String strLoginBusiness="Business Owner";
    public static String strLoginUser="User";

    /* WEBSERVICE RESPONSES */

    public static String WS_RESPONSE_SUCCESS="Success";
    public static String WS_RESPONSE_FAILURE="Failure";


    /* MENU ITEMS AFTER DEALER SIGN IN*/

    public static String TITLES_LOGIN[] = {"Offers & Discounts","Tips & Advice","Add Local Business","Roadside Assistance","About CarEager","Business Chat","Settings","Logout"};

    public static int ICONS[] = {R.drawable.ic_side_offers,R.drawable.ic_side_tips,R.drawable.ic_side_business,R.drawable.ic_side_roadside,R.drawable.ic_about,R.drawable.ic_chat,R.drawable.ic_side_settings,R.drawable.ic_side_logout};

    public static String TITLES_LOGOUT[] = {"Offers & Discounts","Tips & Advice","Add Local Business","Roadside Assistance","About CarEager"};

    public static int ICONS_LOGOUT[] = {R.drawable.ic_side_offers,R.drawable.ic_side_tips,R.drawable.ic_side_business,R.drawable.ic_side_roadside,R.drawable.ic_about};

     /* MENU ITEMS AFTER DEALER SIGN IN*/

    public static String NAME="Sign in";
    public static String IMAGE="image";
    public static String IMAGE_URL;

    public static String USER_LOGIN="login";
    public static String USER_LOGOUT="logout";


    /* Dealer Review variables MAX two*/

    public static String reviewBaseURL;
    public static String reviewImage[];
    public static String reviewName[];
    public static String reviewComment[];
    public static int reviewRating[];
    public static String reviewDate[];

    /* Dealer added category */

    public static String categoryID[];
    public static String categoryName[];

    /*  Dealer profile sales  */

    public static String saleID[];
    public static String saleImage[];
    public static String saleTitle[];
    public static String saleYear[];
    public static String saleMaker[];
    public static String salePrice[];

    public static String galleryBaseURL;
    public static String galleryImages[];

    public static String categoryTab[];



    /*  Search Car list */
    public static String searchBaseURL;
    public static String searchID[];
    public static String searchImage[];
    public static String searchTitle[];
    public static String searchYear[];
    public static String searchMaker[];
    public static String searchPrice[];
    public static String searchDescription[];

    /* car logo */
    public static String carLogoUrl;
    public static String carLogo[];
    public static String carName[];
    public static String carID[];

    /* car model */
    public static String modelID[];
    public static String modelName[];
    public static String modelMaker[];
    public static String modelOverview[];
    public static String modelMinPrice[];
    public static String modelMaxPrice[];

    /* Search service list */
    public static String serviceURL;
    public static String serviceID[];
    public static String serviceAddress[];
    public static String serviceImage[];
    public static String serviceName[];
    public static String serviceContact[];
    public static String serviceCompany[];
    public static String serviceOverview[];
    public static Double serviceLatitude[];
    public static Double serviceLongitude[];
    public static String  serviceCover[];
    public static String  serviceCategory[];

    /* Latest offer  list */
    public static String offerBaseURL;
    public static int offerSize;
    public static String offerID[];
    public static String offerImage[];
    public static String offerDescription[];
    public static String offerPosted[];
    public static String offerDate[];


    /* Latest car offer  list */
    public static String carOfferBaseURL;
    public static String carOfferID[];
    public static String carOfferImage[];
    public static String carOffer[];
    public static String carOfferPosted[];
    public static String carOfferTitle[];
    public static String carOfferShowroomID[];

    public static String showroomOfferBaseURL;
    public static String showroomOfferID[];
    public static String showroomOfferImage[];
    public static String showroomOffer[];
    public static String showroomID[];


    public static String carEagerOfferBaseURL;
    public static String carEagerOfferID[];
    public static String carEagerOfferImage[];
    public static String carEagerOffer[];
    public static String carEagerID[];

    /* Tags list*/
    public static String tagSales="car sales";
    public static String tagService="service and repair";
    public static String tagProduct="product sales";
    public static String tagInsurance="insurance";

    public static String productBaseURL;
    public static String productID[];
    public static String productImage[];
    public static String productName[];
    public static String productDescription[];
    public static String productPrice[];

    /* FORUM USER LIST FOR CHAT */

    public static String forumUserID[];
    public static String forumUserName[];
    public static String forumUserImage[];
    public static String forumUserChat[];
    public static String forumUserDate[];

    /* FORUM USER search FOR CHAT */

    public static String forumUserIDSearch[];
    public static String forumUserNameSearch[];
    public static String forumUserImageSearch[];
    public static String forumUserChatSearch[];
    public static String forumUserDateSearch[];


    /* FORUM QUESTION LIST */

    public static String forumID[];
    public static String forumTitle[];
    public static String forumDescription[];
    public static String forumDate[];

    /* ARTICLE QUESTION LIST */

    public static String articleID[];
    public static String articleTitle[];
    public static String articleDescription[];
    public static String articleDate[];
    public static String articleAuthor[];

    /* FORUM DETAIL COMMENT LIST*/

    public static String forumCommentName[];
    public static String forumCommentComment[];
    public static String forumCommentTimestamp[];

     /* ARTICLE DETAIL COMMENT LIST*/

    public static String articleCommentName[];
    public static String articleCommentComment[];
    public static String articleCommentTimestamp[];


    /* TIPS AND ADVICE LIST */

    public static String tipsURL;
    public static String tipsTitle[];
    public static String tipsContent[];
    public static String tipsImage[];
    public static String tipsDate[];


    public static String homeCategory[];


    public static String designID[];
    public static String designTitle[];
    public static String designName[];
    public static String designDate[];

    public static String electronicID[];
    public static String electronicTitle[];
    public static String electronicName[];
    public static String electronicDate[];

    public static String engineID[];
    public static String engineTitle[];
    public static String engineName[];
    public static String engineDate[];

    public static String repairID[];
    public static String repairTitle[];
    public static String repairName[];
    public static String repairDate[];

    public static String transmissionID[];
    public static String transmissionTitle[];
    public static String transmissionName[];
    public static String transmissionDate[];

    public static String chassisID[];
    public static String chassisTitle[];
    public static String chassisName[];
    public static String chassisDate[];


    public static ArrayList categoryQuestion;


}

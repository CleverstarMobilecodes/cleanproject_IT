package com.mobiledi.earnit.utils;

import com.mobiledi.earnit.BuildConfig;

/**
 * Created by praks on 08/07/17.
 */

public final class AppConstant {
    public final static String BASE_URL_QA = "http://35.162.48.144:8080/earnit-api";
    public final static String BASE_URL_PROD = "https://api.myearnitapp.com:8443/earnit-api";
    public static final String BASE_URL = BuildConfig.DEBUG ? BASE_URL_QA : BASE_URL_PROD;

    public final static String LOGIN_API = "/login";
    public final static String TASKS_API = "/tasks";
    public final static String CHILDREN_API = "/childrens/";
    public final static String GOAL_API = "/goals/";
    public final static String SIGNUP_PARENT = "/signup/parent";
    public final static String ADD_CHILD_PARENT = "/signup/child";
    public final static String UPDATE_CHILD = "/children";
    public final static String UPDATE_PARENT = "/parent";
    public final static String ADD_GOAL = "/goals";

    public final static String APPLICATION_JSON = "application/json";
    public final static String DATE_FORMAT = "MM/dd @ h:mm a";
    public final static String IMAGE_DATE_FORMAT = "yyyyMMdd_HHmmss";



    //OBJECT IDENTIFIER
    public final static String PARENT_OBJECT = "ParentObject";
    public final static String CHILD_OBJECT = "childObject";
    public final static String OTHER_CHILD_OBJECT = "otherChildObject";
    public final static String TASK_OBJECT = "taskObject";
    public final static String TASK_list_OBJECT = "taskListObject";
    public final static String CHILD_TASK_list = "childTaskList";
    public final static String GOAL_OBJECT = "goalObject";


    //USER TYPES
    public final static String PARENT = "PARENT";
    public final static String CHILD = "CHILD";
    public final static String TYPE = "userType";

    //TASK STATUS Created, Overdue, Completed, Closed, Rejected
    public final static String COMPLETED = "Completed";
    public final static String APPROVED = "Closed";
    public final static String DECLINED = "Rejected";
    public final static String DUE = "Created";
    public final static String OVER_DUE = "Overdue";
    public final static String NON_COMPLETED_APPROVED = "Pending Approval";
    public final static String PAST_DUE = "Past Due";

    public final static String ADD = "Add";
    public final static String EDIT = "Edit";


    public final static String MODE = "mode";
    public final static String SAVE = "Save";
    public final static String UPDATE = "Update";
    public final static String TASK_STATUS = "task_status";
    public final static String CASH = "cash";
    public final static String POINTS = "points";


    //JSON KEY NAMES
    public final static String ID = "id";
    public final static String AMOUNT = "amount";
    public final static String GOAL_NAME = "name";
    public final static String TALLY = "tally";
    public final static String TALLY_PERCENT = "tallyPercent";
    public final static String UPDATE_DATE = "updateDate";
    public final static String CASH_BALANCE = "cash";

    public final static String TASKS = "tasks";
    public final static String CREATE_DATE = "createDate";
    public final static String EXPIRY_DATE = "expiryDate";
    public final static String EMAIL = "email";
    public final static String PASSWORD = "password";
    public final static String NAME = "name";
    public final static String DUE_DATE = "dueDate";
    public final static String STATUS = "status";
    public final static String DESCRIPTION = "description";
    public final static String ALLOWANCE = "allowance";
    public final static String GOAL = "goal";
    public final static String AVATAR = "avatar";
    public final static String FIRST_NAME = "firstName";
    public final static String LAST_NAME = "lastName";
    public final static String CHILDREN = "children";
    public final static String PICTURE_REQUIRED = "pictureRequired";
    public final static String ACCOUNT_CODE = "accountCode";
    public final static String ACCOUNT = "account";
    public final static String PHONE = "phone";
    public final static String FCM_TOKEN = "fcmToken";

    public final static String TASK_COMMENTS = "taskComments";
    public final static String READ_STATUS = "readStatus";
    public final static String COMMENT = "comment";
    public final static String PICTURE_URL = "pictureUrl";
    public final static String MESSAGE = "message";
    public final static String REPEAT = "repeat";
    public final static String REPITITION_SCHEDULE = "repititionSchedule";

    public final static String FETCH_GOAL = "fetch_goal";
    public final static String USERNAME = "user_ame";
    public final static String CHILD_MAP = "child_map";
    public final static String CHILD_ID = "child_id";
    public final static String DATE_PICKER_DIALOG = "Datepickerdialog";

    // App Screens
    public final static String FROM_SCREEN = "from_screen";
    public final static String SCREEN = "screen";
    public final static String PARENT_SCREEN = "parent_screen";
    public final static String CHECKED_IN_SCREEN = "checked_in_screen";
    public final static String CHECKED_IN_TASK_APPROVAL__SCREEN = "checked_in_task_approval_screen";
    public final static String PARENT_DASHBOARD = "parent_dashboard";
    public final static String CURRENT_ACTIVITY_INSTANCE = "current_activity";
    public final static String TO_EDIT = "edit_task";
    public final static String LOGIN_SCREEN = "login_screen";
    public final static String CHILD_DASHBOARD_SCREEN = "child_dashboard_screen";
    public final static String SIGN_UP_SCREEN = "sign_up_screen";
    public final static String ADD_TASK = "add_task";
    public final static String GOAL_SCREEN = "goal_screen";
    public final static String BALANCE_SCREEN = "balance_screen";
    public final static String INITIAL_PARENT_PROFILE_SCREEN = "initial_parent_profile_screen";
    public final static String TASK_APPROVAL_SCREEN= "task_approval_screen";

    //Drawer MENU LIST

    public final static String HOME = "Home";
    public final static String PROFILE = "Account";
    public final static String SETTING = "Settings";
    public final static String LOGOUT = "Logout";


    //app content refresh time

    public final static int REFRESH_INTERVAL = 600000; //10 mins

    //AWS CREDENTIALS
    public static final String ACCESS_KEY_KEY = "AKIAJIN35A42G33VAWQA";
    public static final String SECRET_ACCESS_KEY = "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";
    public static final String BUCKET_NAME_PROD = "earnitapp";
    public static final String BUCKET_NAME_QA = "earnitapp-dev";
    public static final String BUCKET_NAME =  BuildConfig.DEBUG ? BUCKET_NAME_QA : BUCKET_NAME_PROD;


    public static final String PARENT_IMAGE_FOLDER = "profile/parent";
    public static final String CHILD_IMAGE_FOLDER= "profile/child";
    public static final String TASK_IMAGE_FOLDER = "tasks";

    public static final String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String VIA = "Add Photo";
    public static final String GALLERY = "Take Photo";
    public static final String LIBRARY = "Choose from Library";
    public static final String EXIT = "Cancel";
    public static final String YES = "Yes";
    public static final String NO = "No";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String FILE_IMAGE = "file_image";


    public static final String SUFFIX ="/" ;
    public static final String AVATAR_IMAGE = "avatar_image";
    public static final String PARENT_AVATAR = "parent_avatar";
    public static final String URL = "endpoint_url";

    // FCM SHARE PREFERANCES
    public static final String FIREBASE_PREFERENCE = "share_token";
    public static final String TOKEN_ID = "token_id";

    public static final String TOKEN_BODY = "token_body";
    public static final String TOKEN_DATA = "token_data";

    //NOTIFICATION TAGS
    public static final String NOTIFICATION_TAG = "NOTIFICATION_TAG";
    public static final String TASK_CREATED = "TASK_CREATED";
    public static final String TASK_UPDATED = "TASK_UPDATED";
    public static final String TASK_COMPLETED = "TASK_COMPLETED";
    public static final String TASK_CLOSED = "TASK_CLOSED";
    public static final String TASK_REJECTED = "TASK_REJECTED";
    public static final String TASK_TO_OVERDUE = "TASK_TO_OVERDUE";
    public static final String GOAL_REACHED = "GOAL_REACHED";

    public static final int FEB_ICON_SIZE = 15 ;

    public final static String IS_LOGOUT = "is_logout";
    public final static String DIAL_CODE = "dial_code";
    public final static String CODE = "code";
    public final static String COUNTRIES = "countries";
    public final static String D_COUNTRIES_DIAL = "+1";
    public final static String D_COUNTRIES_CODE = "US";


    public final static String COUNTRY_JSON = "{\n" +
            "\"countries\":[{\n" +
            "  \"name\": \"United States\",\n" +
            "  \"dial_code\": \"+1\",\n" +
            "  \"code\": \"US\"\n" +
            "},\n" +
            "  {\n" +
            "    \"name\": \"Afghanistan\",\n" +
            "    \"dial_code\": \"+93\",\n" +
            "    \"code\": \"AF\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Aland Islands\",\n" +
            "    \"dial_code\": \"+358\",\n" +
            "    \"code\": \"AX\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Albania\",\n" +
            "    \"dial_code\": \"+355\",\n" +
            "    \"code\": \"AL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Algeria\",\n" +
            "    \"dial_code\": \"+213\",\n" +
            "    \"code\": \"DZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"AmericanSamoa\",\n" +
            "    \"dial_code\": \"+1 684\",\n" +
            "    \"code\": \"AS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Andorra\",\n" +
            "    \"dial_code\": \"+376\",\n" +
            "    \"code\": \"AD\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Angola\",\n" +
            "    \"dial_code\": \"+244\",\n" +
            "    \"code\": \"AO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Anguilla\",\n" +
            "    \"dial_code\": \"+1 264\",\n" +
            "    \"code\": \"AI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Antarctica\",\n" +
            "    \"dial_code\": \"+672\",\n" +
            "    \"code\": \"AQ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Antigua and Barbuda\",\n" +
            "    \"dial_code\": \"+1268\",\n" +
            "    \"code\": \"AG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Argentina\",\n" +
            "    \"dial_code\": \"+54\",\n" +
            "    \"code\": \"AR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Armenia\",\n" +
            "    \"dial_code\": \"+374\",\n" +
            "    \"code\": \"AM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Aruba\",\n" +
            "    \"dial_code\": \"+297\",\n" +
            "    \"code\": \"AW\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Australia\",\n" +
            "    \"dial_code\": \"+61\",\n" +
            "    \"code\": \"AU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Austria\",\n" +
            "    \"dial_code\": \"+43\",\n" +
            "    \"code\": \"AT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Azerbaijan\",\n" +
            "    \"dial_code\": \"+994\",\n" +
            "    \"code\": \"AZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Bahamas\",\n" +
            "    \"dial_code\": \"+1 242\",\n" +
            "    \"code\": \"BS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Bahrain\",\n" +
            "    \"dial_code\": \"+973\",\n" +
            "    \"code\": \"BH\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Bangladesh\",\n" +
            "    \"dial_code\": \"+880\",\n" +
            "    \"code\": \"BD\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Barbados\",\n" +
            "    \"dial_code\": \"+1 246\",\n" +
            "    \"code\": \"BB\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Belarus\",\n" +
            "    \"dial_code\": \"+375\",\n" +
            "    \"code\": \"BY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Belgium\",\n" +
            "    \"dial_code\": \"+32\",\n" +
            "    \"code\": \"BE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Belize\",\n" +
            "    \"dial_code\": \"+501\",\n" +
            "    \"code\": \"BZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Benin\",\n" +
            "    \"dial_code\": \"+229\",\n" +
            "    \"code\": \"BJ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Bermuda\",\n" +
            "    \"dial_code\": \"+1 441\",\n" +
            "    \"code\": \"BM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Bhutan\",\n" +
            "    \"dial_code\": \"+975\",\n" +
            "    \"code\": \"BT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Bolivia, Plurinational State of\",\n" +
            "    \"dial_code\": \"+591\",\n" +
            "    \"code\": \"BO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Bosnia and Herzegovina\",\n" +
            "    \"dial_code\": \"+387\",\n" +
            "    \"code\": \"BA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Botswana\",\n" +
            "    \"dial_code\": \"+267\",\n" +
            "    \"code\": \"BW\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Brazil\",\n" +
            "    \"dial_code\": \"+55\",\n" +
            "    \"code\": \"BR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"British Indian Ocean Territory\",\n" +
            "    \"dial_code\": \"+246\",\n" +
            "    \"code\": \"IO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Brunei Darussalam\",\n" +
            "    \"dial_code\": \"+673\",\n" +
            "    \"code\": \"BN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Bulgaria\",\n" +
            "    \"dial_code\": \"+359\",\n" +
            "    \"code\": \"BG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Burkina Faso\",\n" +
            "    \"dial_code\": \"+226\",\n" +
            "    \"code\": \"BF\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Burundi\",\n" +
            "    \"dial_code\": \"+257\",\n" +
            "    \"code\": \"BI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Cambodia\",\n" +
            "    \"dial_code\": \"+855\",\n" +
            "    \"code\": \"KH\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Cameroon\",\n" +
            "    \"dial_code\": \"+237\",\n" +
            "    \"code\": \"CM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Canada\",\n" +
            "    \"dial_code\": \"+1\",\n" +
            "    \"code\": \"CA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Cape Verde\",\n" +
            "    \"dial_code\": \"+238\",\n" +
            "    \"code\": \"CV\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Cayman Islands\",\n" +
            "    \"dial_code\": \"+ 345\",\n" +
            "    \"code\": \"KY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Central African Republic\",\n" +
            "    \"dial_code\": \"+236\",\n" +
            "    \"code\": \"CF\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Chad\",\n" +
            "    \"dial_code\": \"+235\",\n" +
            "    \"code\": \"TD\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Chile\",\n" +
            "    \"dial_code\": \"+56\",\n" +
            "    \"code\": \"CL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"China\",\n" +
            "    \"dial_code\": \"+86\",\n" +
            "    \"code\": \"CN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Christmas Island\",\n" +
            "    \"dial_code\": \"+61\",\n" +
            "    \"code\": \"CX\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Cocos (Keeling) Islands\",\n" +
            "    \"dial_code\": \"+61\",\n" +
            "    \"code\": \"CC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Colombia\",\n" +
            "    \"dial_code\": \"+57\",\n" +
            "    \"code\": \"CO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Comoros\",\n" +
            "    \"dial_code\": \"+269\",\n" +
            "    \"code\": \"KM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Congo\",\n" +
            "    \"dial_code\": \"+242\",\n" +
            "    \"code\": \"CG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Congo, The Democratic Republic of the Congo\",\n" +
            "    \"dial_code\": \"+243\",\n" +
            "    \"code\": \"CD\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Cook Islands\",\n" +
            "    \"dial_code\": \"+682\",\n" +
            "    \"code\": \"CK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Costa Rica\",\n" +
            "    \"dial_code\": \"+506\",\n" +
            "    \"code\": \"CR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Cote d'Ivoire\",\n" +
            "    \"dial_code\": \"+225\",\n" +
            "    \"code\": \"CI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Croatia\",\n" +
            "    \"dial_code\": \"+385\",\n" +
            "    \"code\": \"HR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Cuba\",\n" +
            "    \"dial_code\": \"+53\",\n" +
            "    \"code\": \"CU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Cyprus\",\n" +
            "    \"dial_code\": \"+357\",\n" +
            "    \"code\": \"CY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Czech Republic\",\n" +
            "    \"dial_code\": \"+420\",\n" +
            "    \"code\": \"CZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Denmark\",\n" +
            "    \"dial_code\": \"+45\",\n" +
            "    \"code\": \"DK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Djibouti\",\n" +
            "    \"dial_code\": \"+253\",\n" +
            "    \"code\": \"DJ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Dominica\",\n" +
            "    \"dial_code\": \"+1 767\",\n" +
            "    \"code\": \"DM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Dominican Republic\",\n" +
            "    \"dial_code\": \"+1 849\",\n" +
            "    \"code\": \"DO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Ecuador\",\n" +
            "    \"dial_code\": \"+593\",\n" +
            "    \"code\": \"EC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Egypt\",\n" +
            "    \"dial_code\": \"+20\",\n" +
            "    \"code\": \"EG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"El Salvador\",\n" +
            "    \"dial_code\": \"+503\",\n" +
            "    \"code\": \"SV\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Equatorial Guinea\",\n" +
            "    \"dial_code\": \"+240\",\n" +
            "    \"code\": \"GQ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Eritrea\",\n" +
            "    \"dial_code\": \"+291\",\n" +
            "    \"code\": \"ER\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Estonia\",\n" +
            "    \"dial_code\": \"+372\",\n" +
            "    \"code\": \"EE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Ethiopia\",\n" +
            "    \"dial_code\": \"+251\",\n" +
            "    \"code\": \"ET\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Falkland Islands (Malvinas)\",\n" +
            "    \"dial_code\": \"+500\",\n" +
            "    \"code\": \"FK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Faroe Islands\",\n" +
            "    \"dial_code\": \"+298\",\n" +
            "    \"code\": \"FO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Fiji\",\n" +
            "    \"dial_code\": \"+679\",\n" +
            "    \"code\": \"FJ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Finland\",\n" +
            "    \"dial_code\": \"+358\",\n" +
            "    \"code\": \"FI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"France\",\n" +
            "    \"dial_code\": \"+33\",\n" +
            "    \"code\": \"FR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"French Guiana\",\n" +
            "    \"dial_code\": \"+594\",\n" +
            "    \"code\": \"GF\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"French Polynesia\",\n" +
            "    \"dial_code\": \"+689\",\n" +
            "    \"code\": \"PF\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Gabon\",\n" +
            "    \"dial_code\": \"+241\",\n" +
            "    \"code\": \"GA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Gambia\",\n" +
            "    \"dial_code\": \"+220\",\n" +
            "    \"code\": \"GM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Georgia\",\n" +
            "    \"dial_code\": \"+995\",\n" +
            "    \"code\": \"GE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Germany\",\n" +
            "    \"dial_code\": \"+49\",\n" +
            "    \"code\": \"DE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Ghana\",\n" +
            "    \"dial_code\": \"+233\",\n" +
            "    \"code\": \"GH\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Gibraltar\",\n" +
            "    \"dial_code\": \"+350\",\n" +
            "    \"code\": \"GI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Greece\",\n" +
            "    \"dial_code\": \"+30\",\n" +
            "    \"code\": \"GR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Greenland\",\n" +
            "    \"dial_code\": \"+299\",\n" +
            "    \"code\": \"GL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Grenada\",\n" +
            "    \"dial_code\": \"+1 473\",\n" +
            "    \"code\": \"GD\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Guadeloupe\",\n" +
            "    \"dial_code\": \"+590\",\n" +
            "    \"code\": \"GP\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Guam\",\n" +
            "    \"dial_code\": \"+1 671\",\n" +
            "    \"code\": \"GU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Guatemala\",\n" +
            "    \"dial_code\": \"+502\",\n" +
            "    \"code\": \"GT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Guernsey\",\n" +
            "    \"dial_code\": \"+44\",\n" +
            "    \"code\": \"GG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Guinea\",\n" +
            "    \"dial_code\": \"+224\",\n" +
            "    \"code\": \"GN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Guinea-Bissau\",\n" +
            "    \"dial_code\": \"+245\",\n" +
            "    \"code\": \"GW\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Guyana\",\n" +
            "    \"dial_code\": \"+595\",\n" +
            "    \"code\": \"GY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Haiti\",\n" +
            "    \"dial_code\": \"+509\",\n" +
            "    \"code\": \"HT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Holy See (Vatican City State)\",\n" +
            "    \"dial_code\": \"+379\",\n" +
            "    \"code\": \"VA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Honduras\",\n" +
            "    \"dial_code\": \"+504\",\n" +
            "    \"code\": \"HN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Hong Kong\",\n" +
            "    \"dial_code\": \"+852\",\n" +
            "    \"code\": \"HK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Hungary\",\n" +
            "    \"dial_code\": \"+36\",\n" +
            "    \"code\": \"HU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Iceland\",\n" +
            "    \"dial_code\": \"+354\",\n" +
            "    \"code\": \"IS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"India\",\n" +
            "    \"dial_code\": \"+91\",\n" +
            "    \"code\": \"IN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Indonesia\",\n" +
            "    \"dial_code\": \"+62\",\n" +
            "    \"code\": \"ID\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Iran, Islamic Republic of Persian Gulf\",\n" +
            "    \"dial_code\": \"+98\",\n" +
            "    \"code\": \"IR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Iraq\",\n" +
            "    \"dial_code\": \"+964\",\n" +
            "    \"code\": \"IQ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Ireland\",\n" +
            "    \"dial_code\": \"+353\",\n" +
            "    \"code\": \"IE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Isle of Man\",\n" +
            "    \"dial_code\": \"+44\",\n" +
            "    \"code\": \"IM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Israel\",\n" +
            "    \"dial_code\": \"+972\",\n" +
            "    \"code\": \"IL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Italy\",\n" +
            "    \"dial_code\": \"+39\",\n" +
            "    \"code\": \"IT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Jamaica\",\n" +
            "    \"dial_code\": \"+1 876\",\n" +
            "    \"code\": \"JM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Japan\",\n" +
            "    \"dial_code\": \"+81\",\n" +
            "    \"code\": \"JP\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Jersey\",\n" +
            "    \"dial_code\": \"+44\",\n" +
            "    \"code\": \"JE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Jordan\",\n" +
            "    \"dial_code\": \"+962\",\n" +
            "    \"code\": \"JO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Kazakhstan\",\n" +
            "    \"dial_code\": \"+7 7\",\n" +
            "    \"code\": \"KZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Kenya\",\n" +
            "    \"dial_code\": \"+254\",\n" +
            "    \"code\": \"KE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Kiribati\",\n" +
            "    \"dial_code\": \"+686\",\n" +
            "    \"code\": \"KI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Korea, Democratic People's Republic of Korea\",\n" +
            "    \"dial_code\": \"+850\",\n" +
            "    \"code\": \"KP\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Korea, Republic of South Korea\",\n" +
            "    \"dial_code\": \"+82\",\n" +
            "    \"code\": \"KR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Kuwait\",\n" +
            "    \"dial_code\": \"+965\",\n" +
            "    \"code\": \"KW\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Kyrgyzstan\",\n" +
            "    \"dial_code\": \"+996\",\n" +
            "    \"code\": \"KG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Laos\",\n" +
            "    \"dial_code\": \"+856\",\n" +
            "    \"code\": \"LA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Latvia\",\n" +
            "    \"dial_code\": \"+371\",\n" +
            "    \"code\": \"LV\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Lebanon\",\n" +
            "    \"dial_code\": \"+961\",\n" +
            "    \"code\": \"LB\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Lesotho\",\n" +
            "    \"dial_code\": \"+266\",\n" +
            "    \"code\": \"LS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Liberia\",\n" +
            "    \"dial_code\": \"+231\",\n" +
            "    \"code\": \"LR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Libyan Arab Jamahiriya\",\n" +
            "    \"dial_code\": \"+218\",\n" +
            "    \"code\": \"LY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Liechtenstein\",\n" +
            "    \"dial_code\": \"+423\",\n" +
            "    \"code\": \"LI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Lithuania\",\n" +
            "    \"dial_code\": \"+370\",\n" +
            "    \"code\": \"LT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Luxembourg\",\n" +
            "    \"dial_code\": \"+352\",\n" +
            "    \"code\": \"LU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Macao\",\n" +
            "    \"dial_code\": \"+853\",\n" +
            "    \"code\": \"MO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Macedonia\",\n" +
            "    \"dial_code\": \"+389\",\n" +
            "    \"code\": \"MK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Madagascar\",\n" +
            "    \"dial_code\": \"+261\",\n" +
            "    \"code\": \"MG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Malawi\",\n" +
            "    \"dial_code\": \"+265\",\n" +
            "    \"code\": \"MW\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Malaysia\",\n" +
            "    \"dial_code\": \"+60\",\n" +
            "    \"code\": \"MY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Maldives\",\n" +
            "    \"dial_code\": \"+960\",\n" +
            "    \"code\": \"MV\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Mali\",\n" +
            "    \"dial_code\": \"+223\",\n" +
            "    \"code\": \"ML\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Malta\",\n" +
            "    \"dial_code\": \"+356\",\n" +
            "    \"code\": \"MT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Marshall Islands\",\n" +
            "    \"dial_code\": \"+692\",\n" +
            "    \"code\": \"MH\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Martinique\",\n" +
            "    \"dial_code\": \"+596\",\n" +
            "    \"code\": \"MQ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Mauritania\",\n" +
            "    \"dial_code\": \"+222\",\n" +
            "    \"code\": \"MR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Mauritius\",\n" +
            "    \"dial_code\": \"+230\",\n" +
            "    \"code\": \"MU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Mayotte\",\n" +
            "    \"dial_code\": \"+262\",\n" +
            "    \"code\": \"YT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Mexico\",\n" +
            "    \"dial_code\": \"+52\",\n" +
            "    \"code\": \"MX\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Micronesia, Federated States of Micronesia\",\n" +
            "    \"dial_code\": \"+691\",\n" +
            "    \"code\": \"FM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Moldova\",\n" +
            "    \"dial_code\": \"+373\",\n" +
            "    \"code\": \"MD\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Monaco\",\n" +
            "    \"dial_code\": \"+377\",\n" +
            "    \"code\": \"MC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Mongolia\",\n" +
            "    \"dial_code\": \"+976\",\n" +
            "    \"code\": \"MN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Montenegro\",\n" +
            "    \"dial_code\": \"+382\",\n" +
            "    \"code\": \"ME\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Montserrat\",\n" +
            "    \"dial_code\": \"+1664\",\n" +
            "    \"code\": \"MS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Morocco\",\n" +
            "    \"dial_code\": \"+212\",\n" +
            "    \"code\": \"MA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Mozambique\",\n" +
            "    \"dial_code\": \"+258\",\n" +
            "    \"code\": \"MZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Myanmar\",\n" +
            "    \"dial_code\": \"+95\",\n" +
            "    \"code\": \"MM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Namibia\",\n" +
            "    \"dial_code\": \"+264\",\n" +
            "    \"code\": \"NA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Nauru\",\n" +
            "    \"dial_code\": \"+674\",\n" +
            "    \"code\": \"NR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Nepal\",\n" +
            "    \"dial_code\": \"+977\",\n" +
            "    \"code\": \"NP\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Netherlands\",\n" +
            "    \"dial_code\": \"+31\",\n" +
            "    \"code\": \"NL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Netherlands Antilles\",\n" +
            "    \"dial_code\": \"+599\",\n" +
            "    \"code\": \"AN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"New Caledonia\",\n" +
            "    \"dial_code\": \"+687\",\n" +
            "    \"code\": \"NC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"New Zealand\",\n" +
            "    \"dial_code\": \"+64\",\n" +
            "    \"code\": \"NZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Nicaragua\",\n" +
            "    \"dial_code\": \"+505\",\n" +
            "    \"code\": \"NI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Niger\",\n" +
            "    \"dial_code\": \"+227\",\n" +
            "    \"code\": \"NE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Nigeria\",\n" +
            "    \"dial_code\": \"+234\",\n" +
            "    \"code\": \"NG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Niue\",\n" +
            "    \"dial_code\": \"+683\",\n" +
            "    \"code\": \"NU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Norfolk Island\",\n" +
            "    \"dial_code\": \"+672\",\n" +
            "    \"code\": \"NF\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Northern Mariana Islands\",\n" +
            "    \"dial_code\": \"+1 670\",\n" +
            "    \"code\": \"MP\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Norway\",\n" +
            "    \"dial_code\": \"+47\",\n" +
            "    \"code\": \"NO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Oman\",\n" +
            "    \"dial_code\": \"+968\",\n" +
            "    \"code\": \"OM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Pakistan\",\n" +
            "    \"dial_code\": \"+92\",\n" +
            "    \"code\": \"PK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Palau\",\n" +
            "    \"dial_code\": \"+680\",\n" +
            "    \"code\": \"PW\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Palestinian Territory, Occupied\",\n" +
            "    \"dial_code\": \"+970\",\n" +
            "    \"code\": \"PS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Panama\",\n" +
            "    \"dial_code\": \"+507\",\n" +
            "    \"code\": \"PA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Papua New Guinea\",\n" +
            "    \"dial_code\": \"+675\",\n" +
            "    \"code\": \"PG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Paraguay\",\n" +
            "    \"dial_code\": \"+595\",\n" +
            "    \"code\": \"PY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Peru\",\n" +
            "    \"dial_code\": \"+51\",\n" +
            "    \"code\": \"PE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Philippines\",\n" +
            "    \"dial_code\": \"+63\",\n" +
            "    \"code\": \"PH\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Pitcairn\",\n" +
            "    \"dial_code\": \"+872\",\n" +
            "    \"code\": \"PN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Poland\",\n" +
            "    \"dial_code\": \"+48\",\n" +
            "    \"code\": \"PL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Portugal\",\n" +
            "    \"dial_code\": \"+351\",\n" +
            "    \"code\": \"PT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Puerto Rico\",\n" +
            "    \"dial_code\": \"+1 939\",\n" +
            "    \"code\": \"PR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Qatar\",\n" +
            "    \"dial_code\": \"+974\",\n" +
            "    \"code\": \"QA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Romania\",\n" +
            "    \"dial_code\": \"+40\",\n" +
            "    \"code\": \"RO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Russia\",\n" +
            "    \"dial_code\": \"+7\",\n" +
            "    \"code\": \"RU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Rwanda\",\n" +
            "    \"dial_code\": \"+250\",\n" +
            "    \"code\": \"RW\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Reunion\",\n" +
            "    \"dial_code\": \"+262\",\n" +
            "    \"code\": \"RE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Saint Barthelemy\",\n" +
            "    \"dial_code\": \"+590\",\n" +
            "    \"code\": \"BL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Saint Helena, Ascension and Tristan Da Cunha\",\n" +
            "    \"dial_code\": \"+290\",\n" +
            "    \"code\": \"SH\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Saint Kitts and Nevis\",\n" +
            "    \"dial_code\": \"+1 869\",\n" +
            "    \"code\": \"KN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Saint Lucia\",\n" +
            "    \"dial_code\": \"+1 758\",\n" +
            "    \"code\": \"LC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Saint Martin\",\n" +
            "    \"dial_code\": \"+590\",\n" +
            "    \"code\": \"MF\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Saint Pierre and Miquelon\",\n" +
            "    \"dial_code\": \"+508\",\n" +
            "    \"code\": \"PM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Saint Vincent and the Grenadines\",\n" +
            "    \"dial_code\": \"+1 784\",\n" +
            "    \"code\": \"VC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Samoa\",\n" +
            "    \"dial_code\": \"+685\",\n" +
            "    \"code\": \"WS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"San Marino\",\n" +
            "    \"dial_code\": \"+378\",\n" +
            "    \"code\": \"SM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Sao Tome and Principe\",\n" +
            "    \"dial_code\": \"+239\",\n" +
            "    \"code\": \"ST\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Saudi Arabia\",\n" +
            "    \"dial_code\": \"+966\",\n" +
            "    \"code\": \"SA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Senegal\",\n" +
            "    \"dial_code\": \"+221\",\n" +
            "    \"code\": \"SN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Serbia\",\n" +
            "    \"dial_code\": \"+381\",\n" +
            "    \"code\": \"RS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Seychelles\",\n" +
            "    \"dial_code\": \"+248\",\n" +
            "    \"code\": \"SC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Sierra Leone\",\n" +
            "    \"dial_code\": \"+232\",\n" +
            "    \"code\": \"SL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Singapore\",\n" +
            "    \"dial_code\": \"+65\",\n" +
            "    \"code\": \"SG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Slovakia\",\n" +
            "    \"dial_code\": \"+421\",\n" +
            "    \"code\": \"SK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Slovenia\",\n" +
            "    \"dial_code\": \"+386\",\n" +
            "    \"code\": \"SI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Solomon Islands\",\n" +
            "    \"dial_code\": \"+677\",\n" +
            "    \"code\": \"SB\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Somalia\",\n" +
            "    \"dial_code\": \"+252\",\n" +
            "    \"code\": \"SO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"South Africa\",\n" +
            "    \"dial_code\": \"+27\",\n" +
            "    \"code\": \"ZA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"South Georgia and the South Sandwich Islands\",\n" +
            "    \"dial_code\": \"+500\",\n" +
            "    \"code\": \"GS\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Spain\",\n" +
            "    \"dial_code\": \"+34\",\n" +
            "    \"code\": \"ES\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Sri Lanka\",\n" +
            "    \"dial_code\": \"+94\",\n" +
            "    \"code\": \"LK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Sudan\",\n" +
            "    \"dial_code\": \"+249\",\n" +
            "    \"code\": \"SD\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Suriname\",\n" +
            "    \"dial_code\": \"+597\",\n" +
            "    \"code\": \"SR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Svalbard and Jan Mayen\",\n" +
            "    \"dial_code\": \"+47\",\n" +
            "    \"code\": \"SJ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Swaziland\",\n" +
            "    \"dial_code\": \"+268\",\n" +
            "    \"code\": \"SZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Sweden\",\n" +
            "    \"dial_code\": \"+46\",\n" +
            "    \"code\": \"SE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Switzerland\",\n" +
            "    \"dial_code\": \"+41\",\n" +
            "    \"code\": \"CH\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Syrian Arab Republic\",\n" +
            "    \"dial_code\": \"+963\",\n" +
            "    \"code\": \"SY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Taiwan\",\n" +
            "    \"dial_code\": \"+886\",\n" +
            "    \"code\": \"TW\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Tajikistan\",\n" +
            "    \"dial_code\": \"+992\",\n" +
            "    \"code\": \"TJ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Tanzania, United Republic of Tanzania\",\n" +
            "    \"dial_code\": \"+255\",\n" +
            "    \"code\": \"TZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Thailand\",\n" +
            "    \"dial_code\": \"+66\",\n" +
            "    \"code\": \"TH\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Timor-Leste\",\n" +
            "    \"dial_code\": \"+670\",\n" +
            "    \"code\": \"TL\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Togo\",\n" +
            "    \"dial_code\": \"+228\",\n" +
            "    \"code\": \"TG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Tokelau\",\n" +
            "    \"dial_code\": \"+690\",\n" +
            "    \"code\": \"TK\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Tonga\",\n" +
            "    \"dial_code\": \"+676\",\n" +
            "    \"code\": \"TO\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Trinidad and Tobago\",\n" +
            "    \"dial_code\": \"+1 868\",\n" +
            "    \"code\": \"TT\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Tunisia\",\n" +
            "    \"dial_code\": \"+216\",\n" +
            "    \"code\": \"TN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Turkey\",\n" +
            "    \"dial_code\": \"+90\",\n" +
            "    \"code\": \"TR\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Turkmenistan\",\n" +
            "    \"dial_code\": \"+993\",\n" +
            "    \"code\": \"TM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Turks and Caicos Islands\",\n" +
            "    \"dial_code\": \"+1 649\",\n" +
            "    \"code\": \"TC\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Tuvalu\",\n" +
            "    \"dial_code\": \"+688\",\n" +
            "    \"code\": \"TV\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Uganda\",\n" +
            "    \"dial_code\": \"+256\",\n" +
            "    \"code\": \"UG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Ukraine\",\n" +
            "    \"dial_code\": \"+380\",\n" +
            "    \"code\": \"UA\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"United Arab Emirates\",\n" +
            "    \"dial_code\": \"+971\",\n" +
            "    \"code\": \"AE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"United Kingdom\",\n" +
            "    \"dial_code\": \"+44\",\n" +
            "    \"code\": \"GB\"\n" +
            "  },\n" +
            "\n" +
            "  {\n" +
            "    \"name\": \"Uruguay\",\n" +
            "    \"dial_code\": \"+598\",\n" +
            "    \"code\": \"UY\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Uzbekistan\",\n" +
            "    \"dial_code\": \"+998\",\n" +
            "    \"code\": \"UZ\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Vanuatu\",\n" +
            "    \"dial_code\": \"+678\",\n" +
            "    \"code\": \"VU\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Venezuela, Bolivarian Republic of Venezuela\",\n" +
            "    \"dial_code\": \"+58\",\n" +
            "    \"code\": \"VE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Vietnam\",\n" +
            "    \"dial_code\": \"+84\",\n" +
            "    \"code\": \"VN\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Virgin Islands, British\",\n" +
            "    \"dial_code\": \"+1 284\",\n" +
            "    \"code\": \"VG\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Virgin Islands, U.S.\",\n" +
            "    \"dial_code\": \"+1 340\",\n" +
            "    \"code\": \"VI\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Wallis and Futuna\",\n" +
            "    \"dial_code\": \"+681\",\n" +
            "    \"code\": \"WF\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Yemen\",\n" +
            "    \"dial_code\": \"+967\",\n" +
            "    \"code\": \"YE\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Zambia\",\n" +
            "    \"dial_code\": \"+260\",\n" +
            "    \"code\": \"ZM\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"name\": \"Zimbabwe\",\n" +
            "    \"dial_code\": \"+263\",\n" +
            "    \"code\": \"ZW\"\n" +
            "  }\n" +
            "]}";
}

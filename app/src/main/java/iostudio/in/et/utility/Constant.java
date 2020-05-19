package iostudio.in.et.utility;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;


public class Constant {


    public static final String email = "email";
    public static final String country_name = "country_name";
    public static final String country_code = "country_code";
    public static final String country_flag = "country_flag";
    public static final String country_phone = "country_phone";
    public static final String REGISTER_USER_VERIFY_DEEPLINK = "REGISTER_USER_VERIFY_DEEPLINK";
    public static final String FORGOT_PASSWORD_VERIFY_DEEPLINK = "FORGOT_PASSWORD_VERIFY_DEEPLINK";
    public static final String token = "token";
    public static final String token_key_forgot_pwd = "token";
    public static final String hash_key = "key";
    public static final String password = "password";
    public static final String socialPermissions =
            "[{‘user_birthday’:1, ‘user_friends’:1, ‘email’:1, ‘publish_actions’:1, ‘public_profile’:1}]";
    public static final String ref = "ref";
    public static final String dest = "dest";
    public static final String imageArrayList = "imageArrayList";
    public static final String companyDetails = "companyDetails";
    public static final String productDetails = "productDetails";
    public static final String productList = "productList";
    public static final String enquiry="enquiry";
    public static final String customerID="customerID";
    public static final String customerName="customerName";
    public static final String SELECTED_CONTRACTOR_IDS = "SELECTED_CONTRACTOR_IDS";
    public static final String SELECTED_CONTRACTOR = "SELECTED_CONTRACTOR";
    public static final String maxAttendanceCount="maxAttendanceCount";
    public static final String contractorSelectedIDDataArrayList="contractorSelectedIDDataArrayList";
    public static final String contractorDataArrayList="contractorDataArrayList";
    public static final String attendanceDate="attendanceDate";
    public static final String shift="shift";

    public static final class INTENT {

        public static final String PHONE_NUMBER = "phone_number";
        public static final String TYPE = "type";
        public static final String MEETING_OBJECT="meeting_object";
        public static final String CLIENT_OBJECT="client_object";
        public static final String ACCOUNT_OBJECT="account_object";
        public static String ID="id";
        public static String DO_LOAD_TERMS="DO_LOAD_TERMS";
    }


    @Retention(RetentionPolicy.SOURCE)
    @StringDef({SuperSlurrped.NORMAL, SuperSlurrped.SUPER})
    public @interface SuperSlurrped {
        String NORMAL = "0";
        String SUPER = "1";
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LayoutType.CELL_TYPE_1, LayoutType.CELL_TYPE_2})
    public @interface LayoutType {
        int CELL_TYPE_1 = 1;
        int CELL_TYPE_2 = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({DashboardModule.ABOUTUS, DashboardModule.PRODUCT, DashboardModule.CHAT, DashboardModule.PORTFOLIO, DashboardModule.STORES, DashboardModule.SERVICES})
    public @interface DashboardModule {
        String ABOUTUS = "aboutus";
        String PRODUCT = "product";
        String CHAT = "chat";
        String SERVICES = "services";
        String STORES = "stores";
        String PORTFOLIO = "portfolio";
        String CALL = "call";
    }

    public class FireTB {
        public static final String APP_SETTING = "as";
        public static final String ACCESS = "acc";
        public static final String PHONE_NUMBER = "pn";
        public static final String DATE_TIME = "dt";
        public static final String DB_NAME = "dn";
        public static final String IS_CUSTOMER = "ic";
        public static final String NAME = "n";
        public static final String BACKGROUND_IMAGE = "bg";
        public static final String PORTFOLIO_STYLE = "pfs";
        public static final String PRODUCT_STYLE = "ps";
        public static final String LISTING_STYLE = "ls";
        public static final String LOGO_URL = "lg";
        public static final String IMAGE_URL = "i";
        public static final String PORTFOLIO = "pf";
        public static final String PRODUCTS = "p";
        public static final String CATEGORY_ID = "catid";
        public static final String CREATED_AT = "date";
        public static final String DESCRIPTION = "des";
        public static final String FACEBOOK_LINK = "fb";
        public static final String INSTAGRAM_LINK = "insta";
        public static final String TWITTER_LINK = "tw";
        public static final String WEBSITE = "web";
        public static final String DISCOUNT_PRICE = "dp";
        public static final String DISCOUNT_PERCENTAGE = "dptg";
        public static final String ADDITIONAL_DETAILS = "ad";
        public static final String END_TIME = "et";
        public static final String START_TIME = "st";
        public static final String ADDRESS = "add";
        public static final String EMAIL_ADDRESS = "ed";
        public static final String MESSAGE = "msg";
        public static final String COVER_IMAGE = "ci";
        public static final String COVER_VIDEO = "cv";
        public static final String CONTACT_NO = "con";
        public static final String WORKING_HOURS = "wh";
        public static final String LONGITUDE = "lng";
        public static final String LOCATION = "loc";
        public static final String LATTITUDE = "lat";
        public static final String PRODUCT_ID = "id";
        public static final String PRICE = "p";
        public static final String CATEGORIES = "cat";
        public static final String ID = "id";
        public static final String ORDER_NUMBER = "on";
        public static final String PRODUCT_COUNT = "pc";
        public static final String KEY = "k";
        public static final String VALUE = "v";
        public static final String IMAGES_ARRAY = "ia";
        public static final String STORE_LOCATOR = "sl";
        public static final String NOTIFICATIONS = "nf";
        public static final String NOTIFICATION_TOKEN = "nt";
        public static final String COMPANY_DETAILS = "com";
        public static final String USER = "usr";
        public static final String TITLE="t";
        public static final String CLICK_TYPE="ct";
        public static final String CLICK_DESCRIPTION="cd";
    }

    public static final String title = "title";
    public static final long ANIM_DURATION_SLURRP = 300;
    public static final long ANIM_DURATION_SLURRP_HALF = 150;
    public static final String deepLinkOfApp = "https://p296y.app.goo.gl/NLtk";
    public static final int SELECT_COUNTRY_CODE_RESULT = 111;
    public static final int SELECT_USER_IMAGE_DIALOG = 112;
    public static final int AUDIO_SEARCH = 113;
    public static final int SEARCH_AIRLINES = 114;
    public static final int ADD_TRAVELLER = 115;
    public static final int PREFERENCE_UPDATE = 116;
    public static final int SELECT_TRAVELLERS = 117;
    public static final int ASSIGN_GROUP = 118;
    public static final int ADD_FAV_HOTEL = 119;
    public static final int VIEW_ALL_HOTEL = 120;
    public static final int BOOKING_INQ_SENT = 121;
    public static final int SEARCH_CONTRACTOR = 122;
    public static final int USER_IMAGE_DIALOG_UPDATE = 152;
    public static final String image = "image";
    public static final String position = "position";


    public static final int PICK_IMAGE = 12345;
    public static final int TAKE_PICTURE = 6352;

    String LAT = "lat";
    String LNG = "lng";
    public static final String km = "km";
    public static final String m = "m";

    public static final String lat = "25.2048888";
    public static final String lng = "55.2708888";

    public interface API {

        String BASEURL_API = "https://apis.climbstaff.com";

        String APPLY_LEAVE = "/attendance/leave-grant";
        String LOGIN = "/user/login";
        String LOGIN_VERIFY = "/user/login-verify";
        String CLIENT_ADD = "/client/add";
        String ACCOUNT_ADD = "/account/add";
        String ACCOUNT_UPDATE= "/account/update";
        String CLIENT_UPDATE = "/client/update";
        String CLIENT_TYPE = "/client/type/{userID}";
        String ACCOUNT_TYPE = "/account/type/list/{userID}";
        String ACCOUNT_FIND_ENTRY = "/account/find/entry";
        String LEAVE_TYPE = "/attendance/leave-reason";
        String LEAD_LIST = "/client/show/{userID}";
        String HOME_DATA = "/attendance/home/{userID}";
        String MEETING_DETAILS = "/meeting/details/{meeting_id}";
        String PROFILE_DETAILS = "/user/profile/{meeting_id}";
        String SUPPORT_TERMS = "/support/terms";
        String MEETING_DELETE = "/user/profile/{meeting_id}";
        String ACCOUNT_DETAILS = "/account/{meeting_id}";
        String CLIENT_LOCATION = "/client/location/{id}";
        String ACCOUNT_HOME = "/account/home/{id}";
        String CLIENT_DETAILS = "/client/details/{meeting_id}";
        String MEETING_CANCEL = "/meeting/cancel/{meeting_id}";
        String HOME_STATUS = "/attendance/initiate";
        String MEETING_DATA = "/meeting/show";
        String MEETING_OTHER_LOCATION = "/meeting/otherlocation";
        String MEETING_RESCHEDULE= "/meeting/reschedule";
        String MEETING_ADD= "/meeting/add";
        String ADD_NOTE= "/meeting/note/add";
        String ACCOUNT_ADD_NOTE= "/account/note/add";
        String CLIENT_DELETE= "/client/remove";
        String ACCOUNT_DELETE= "/account/remove";
        String CLIENT_LOCATION_UPDATE= "/client/update-location";
        String MEETING_STATUS = "meeting/status";
        String UPDATE_FIREBASE_DEVICE_ID = "/user/firebasedevice";
    }


    public interface API_ACTION {
        String REQUEST_reshare = "API_ACTION_REQUEST_reshare";
    }

    public class ResultCode {
        public static final int searchActivity = 800;
    }

    public class Events {

        public static final String facebook_login_clicked = "facebook_login_clicked";
    }

    public class EventKey {
        public static final String userID = "userID";
    }

    public static class INTENT_RESULT {

        public static final int CREATE_EXPENSE=890;
        public static final int CONTACT_UPDATE = 891;
        public static final int ACCOUNT_INFO = 892;
    }
}

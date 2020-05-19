package iostudio.in.et.retrofit.request;

import java.util.HashMap;
import java.util.Map;

public class RetrofitRequest {

    /**
     * Static variables which is being used as APIs params
     */
    public static final String ACTION_USERID = "userID";
    public static final String ACTION_LATITUDE = "latitude";
    public static final String ACTION_ZONEID = "zoneID";
    public static final String ACTION_NAME = "name";
    public static final String ACTION_EMAIL_ADDRESS = "emailAddress";
    public static final String ACTION_PASSWORD = "password";
    public static final String ACTION_NEW_PASSWORD = "newPassword";
    public static final String ACTION_DEVICE_TYPE = "deviceType";
    public static final String ACTION_DEVICE_ID = "deviceID";
    public static final String ACTION_NOTIFICATION_TOKEN = "notificationToken";
    public static final String ACTION_FACEBOOK_ID = "facebookID";
    public static final String ACTION_FACEBOOK_ACCESS_TOKEN = "facebookAccessToken";
    public static final String ACTION_FACEBOOK_FRIEND_CSV = "facebookFriendCSV";
    public static final String ACTION_GOOGLE_PLUS_ID = "googlePlusID";
    public static final String ACTION_BUSINESS_ID = "businessID";
    public static final String ACTION_QUERY_TEXT = "queryText";
    public static final String ACTION_TYPE_ID = "typeID";
    public static final String ACTION_TYPE = "type";
    private static final String ACTION_comment = "comment";
    private static final String ACTION_URL = "url";
    public static final String ACTION_lastUpdatedDate = "lastUpdatedDate";
    private static final String ACTION_typeID = "typeID";
    private static final String ACTION_type = "type";
    private static final String ACTION_category = "category";
    private static final String ACTION_phoneNumber = "phoneNumber";
    private static final String ACTION_deviceID = "deviceID";
    private static final String ACTION_deviceType = "deviceType";
    private static final String ACTION_firebaseID = "firebaseID";
    public static final String ACTION_userID = "user_id";
    public static final String ACTION_customer_id = "customer_id";
    public static final String ACTION_password = "pass";
    public static final String ACTION_email = "email";
    public static final String ACTION_creditsApplied = "creditsApplied";
    private static final String ACTION_queryText = "queryText";
    private static final String ACTION_offerCode = "offerCode";
    private static final String ACTION_businessID = "businessID";
    private static final String ACTION_sortBy = "sortBy";
    private static final String ACTION_highlightIDCSV = "highlightIDCSV";
    private static final String ACTION_serviceIDCSV = "serviceIDCSV";
    public static final String ACTION_dealID = "dealID";
    private static final String ACTION_appointmentTime = "appointmentTime";
    private static final String ACTION_appointmentDate = "appointmentDate";
    private static final String ACTION_voucherServiceIDCSV = "voucherServiceIDCSV";
    private static final String ACTION_rating = "rating";
    private static final String ACTION_reviewText = "reviewText";
    private static final String ACTION_categoryID = "categoryID";
    private static final String ACTION_venueTypeID = "venueTypeID";
    private static final String ACTION_code = "code";
    public static final String ACTION_couponCode = "couponCode";
    private static final String ACTION_isLoyaltyDeal = "isLoyaltyDeal";
    private static final String ACTION_FINAL_PRICE = "finalPrice";
    private static final String ACTION_MOBILE_NO = "mobile_no";
    private static final String ACTION_date = "date";
    private static final String ACTION_time = "time";
    private static final String ACTION_latitude = "latitude";
    private static final String ACTION_longitude = "longitude";
    private static final String ACTION_BUSINESS_RATING = "businessRating";
    private static final String ACTION_BUSINESS_REVIEW = "businessReview";
    private static final String ACTION_FABOGO_TAG = "fabogoTag";
    private static final String ACTION_FABOGO_DESCRIPTION = "fabogoDescription";
    private static final String ACTION_VOUCER_CSV = "voucherServiceIDCSV";
    private static final String ACTION_FABOGO_RATING = "fabogoRating";
    private static final String ACTION_isVenueClosed = "isVenueClosed";
    private static final String ACTION_referralCode = "referralCode";
    private static final String ACTION_name = "name";
    private static final String ACTION_countryCode = "countryCode";
    private static final String ACTION_otp = "otp";
    private static final String ACTION_imei_no = "imei_no";
    private static final String ACTION_campaignID = "campaignID";
    private static final String ACTION_voucherCode = "voucherCode";
    private static final String ACTION_minPriceRange = "minPriceRange";
    private static final String ACTION_maxPriceRange = "maxPriceRange";
    private static final String ACTION_packageID = "packageID";
    public static final String ACTION_secretKey = "secretKey";
    private static final String ACTION_onlyPackageVenue = "onlyPackageVenue";
    public static final String ACTION_FIRST_NAME = "first_name";
    public static final String ACTION_LAST_NAME = "last_name";
    public static final String ACTION_LABEL = "label";
    private static final String ACTION_mobile = "mobile";
    private static final String ACTION_isd = "isd";
    private static final String ACTION_country = "country";
    private static final String ACTION_ref = "ref";
    public static final String ACTION_SORT_ORDER = "sort_order";
    public static final String ACTION_Q = "q";
    private static final String ACTION_userName = "userName";
    private static final String ACTION_id = "id";
    private static final String ACTION_authKey = "authKey";
    public static final String ACTION_picture = "picture";
    public static final String ACTION_dob = "dob";
    public static final String ACTION_permissions = "permissions";
    public static final String ACTION_hash_key = "hash_key";
    public static final String ACTION_token = "token";
    public static final String ACTION_key = "key";
    public static final String ACTION_login = "login";
    public static final String ACTION_DISPLAY_ORDER = "display_order";
    public static final String ACTION_LABELS = "labels";
    public static final String ACTION_REMOVED_LABELS = "removed_labels";
    public static final String ACTION_PAX_IDS = "pax_ids[%d]";
    private static final String ACTION_HOTEL_MIN_STAR = "hotel_min_star";
    private static final String ACTION_HOTEL_MAX_STAR = "hotel_max_star";
    private static final String ACTION_lat = "lat";
    private static final String ACTION_lng = "lng";
    private static final String ACTION_remark = "remark";
    private static final String ACTION_network = "network";
    private static final String ACTION_battery = "battery";
    private static final String ACTION_imei = "imei";
    private static final String ACTION_contractorid = "contractor_id";
    private static final String ACTION_serviceid = "service_id";
    private static final String ACTION_attendate = "attendance_date";
    private static final String ACTION_shift = "shift";
    private static final String ACTION_status = "status";
    private static final String ACTION_user_id = "user_id";
    private static final String ACTION_device_id = "device_id";
    private static final String ACTION_client_type = "client_type";
    private static final String ACTION_created_by = "created_by";
    private static final String ACTION_model_no = "model_no";

    private static final String ACTION_leave_reason = "leave_reason";
    private static final String ACTION_leave_start = "leave_start";
    private static final String ACTION_leave_end = "leave_end";
    private static final String ACTION_company_name = "company_name";
    private static final String ACTION_company_address = "company_address";
    private static final String ACTION_owner_name = "owner_name";
    private static final String ACTION_owner_mobile = "owner_mobile";
    private static final String ACTION_created_at = "created_at";
    private static final String ACTION_logtime = "logtime";
    private static final String ACTION_end_date = "end_date";
    private static final String ACTION_start_date = "start_date";
    private static final String ACTION_meeting_id = "meeting_id";
    private static final String ACTION_meeting_status = "meeting_status";
    private static final String ACTION_note = "note";
    private static final String ACTION_reschedule_date = "reschedule_date";
    private static final String ACTION_reason = "reason";
    private static final String ACTION_client_id = "client_id";
    private static final String ACTION_meeting_date = "meeting_date";
    private static final String ACTION_schedule_date = "schedule_date";
    private static final String ACTION_type_id = "type_id";
    private static final String ACTION_amount = "amount";
    private static final String ACTION_entry_date = "entry_date";
    private static final String ACTION_account_id = "account_id";
    private static final String ACTION_updation_date = "updation_date";
    private static final String ACTION_address = "address";
    private static final String ACTION_accountID = "account_id";
    private static final String ACTION_companyID = "company_id";
    private static final String ACTION_ENTRYID = "entry_id";


    /**
     * get Signup params to hit Api
     *
     * @param name
     * @param emailAddress
     * @param password
     * @param deviceType
     * @param deviceID
     * @param notificationToken
     * @return
     */
    public static Map<String, String> getSignUp(final String name, final String emailAddress,
                                                final String password,
                                                final String deviceType, final String deviceID, final String notificationToken) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_NAME, name);
        params.put(ACTION_EMAIL_ADDRESS, emailAddress);
        params.put(ACTION_PASSWORD, password);
        params.put(ACTION_DEVICE_TYPE, deviceType);
        params.put(ACTION_DEVICE_ID, deviceID);
        params.put(ACTION_NOTIFICATION_TOKEN, notificationToken);

        return params;
    }

    /**
     * get login Enquiry params to hit related Api
     *
     * @param phoneNumber
     * @param deviceID
     * @param deviceType
     * @param firebaseID
     * @return
     */
    public static Map<String, String> loginWithPhone(final String phoneNumber,
                                                     final String deviceID,
                                                     final String deviceType,
                                                     final String firebaseID,
                                                     final String referralCode,
                                                     final String name) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_phoneNumber, phoneNumber);
        params.put(ACTION_deviceID, deviceID);
        params.put(ACTION_deviceType, deviceType);
        params.put(ACTION_firebaseID, firebaseID);
        params.put(ACTION_referralCode, referralCode);
        params.put(ACTION_name, name);

        return params;
    }

    /**
     * get login Enquiry params to hit related Api
     *
     * @param deviceID
     * @param deviceType
     * @param firebaseID
     * @return
     */
    public static Map<String, String> loginWithPhoneSkip(
            final String deviceID,
            final String deviceType,
            final String firebaseID,
            final String referralCode,
            final String name) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_deviceID, deviceID);
        params.put(ACTION_deviceType, deviceType);
        params.put(ACTION_firebaseID, firebaseID);
        params.put(ACTION_referralCode, referralCode);
        params.put(ACTION_name, name);

        return params;
    }

    public static Map<String, String> loginWithPhone(final String phoneNumber,
                                                     final String deviceID,
                                                     final String deviceType,
                                                     final String firebaseID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_phoneNumber, phoneNumber);
        params.put(ACTION_deviceID, deviceID);
        params.put(ACTION_deviceType, deviceType);
        params.put(ACTION_firebaseID, firebaseID);

        return params;
    }


    /**
     * get Forgot Password params to hit related Api
     *
     * @param userID
     * @param zoneID
     * @return
     */
    public static Map<String, String> getDashboard(final String userID, final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_ZONEID, zoneID);

        return params;
    }

    public static Map<String, String> checkValidateCode(final String couponCode,
                                                        final String countryCode, String phoneNumber) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_couponCode, couponCode);
        params.put(ACTION_countryCode, countryCode);
        params.put(ACTION_phoneNumber, phoneNumber);

        return params;
    }

    public static Map<String, String> resendOTPRequest(final String userID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);

        return params;
    }

    public static Map<String, String> verifyOTPReq(final String userID, final String otp,
                                                   final String referralCode, final String countryCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_otp, otp);
        params.put(ACTION_referralCode, referralCode);
        params.put(ACTION_countryCode, countryCode);

        return params;
    }

    public static Map<String, String> getReviewPopup(final String userID, final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_ZONEID, zoneID);
        return params;
    }

    public static Map<String, String> getCampaignReq(final String campaignID,
                                                     final String userID,
                                                     final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_campaignID, campaignID);
        params.put(ACTION_userID, userID);
        params.put(ACTION_ZONEID, zoneID);
        return params;
    }

    public static Map<String, String> getAppVersion() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }


    public static Map<String, String> updatePurchaseDeal(final String paytmResponse) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("", paytmResponse);
        return params;
    }

    /**
     * get Forgot Password params to hit related Api
     *
     * @param userID
     * @return
     */
    public static Map<String, String> getUserProfile(final String userID, final String zoneID,
                                                     final String businessID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_ZONEID, zoneID);
        params.put(ACTION_businessID, businessID);

        return params;
    }


    public static Map<String, String> cancelService(final String voucherServiceIDCSV,
                                                    final String userID,
                                                    final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_voucherServiceIDCSV, voucherServiceIDCSV);
        params.put(ACTION_userID, userID);
        params.put(ACTION_ZONEID, zoneID);

        return params;
    }

    public static Map<String, String> checkStatus(final String voucherServiceIDCSV,
                                                  final String userID,
                                                  final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_voucherServiceIDCSV, voucherServiceIDCSV);
        params.put(ACTION_userID, userID);
        params.put(ACTION_ZONEID, zoneID);
        return params;
    }

    public static Map<String, String> getVoucherForCode(final String code,
                                                        final String userID,
                                                        final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_code, code);
        params.put(ACTION_userID, userID);
        params.put(ACTION_ZONEID, zoneID);
        return params;
    }


    public static Map<String, String> buildUserID(final String userID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        return params;
    }

    public static Map<String, String> loginRequest(final String userName) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_MOBILE_NO, userName);
        return params;
    }

    public static Map<String, String> statusRequest(final String userID,
                                                    final String logtime,
                                                    final String battery,
                                                    final String lat,
                                                    final String lng,
                                                    final String status) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_logtime, logtime);
        params.put(ACTION_battery, battery);
        params.put(ACTION_lat, lat);
        params.put(ACTION_lng, lng);
        params.put(ACTION_status, status);
        return params;
    }

    public static Map<String, String> meetingRequest(final String userID,
                                                     final String start_date,
                                                     final String end_date) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_start_date, start_date);
        params.put(ACTION_end_date, end_date);
        return params;
    }

    public static Map<String, String> accountRequest(final String userID,
                                                     final String type_id,
                                                     final String start_date,
                                                     final String end_date) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_type_id, type_id);
        params.put(ACTION_start_date, start_date);
        params.put(ACTION_end_date, end_date);
        return params;
    }

    public static Map<String, String> meetingLocationRequest(final String userID,
                                                             final String meeting_id,
                                                             final String meeting_date,
                                                             final String meeting_status,
                                                             final String note) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_meeting_id, meeting_id);
        params.put(ACTION_meeting_date, meeting_date);
        params.put(ACTION_meeting_status, meeting_status);
        params.put(ACTION_note, note);
        return params;
    }

    public static Map<String, String> clientLocationRequest(final String id,
                                                            //final String address,
                                                            final String lat,
                                                            final String lng,
                                                            final String date) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_client_id, id);
        params.put(ACTION_lat, lat);
        params.put(ACTION_lng, lng);
        params.put(ACTION_updation_date, date);
        return params;
    }

    public static Map<String, String> meetingReschdeuleRequest(final String userID,
                                                               final String meeting_id,
                                                               final String date,
                                                               final String note) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_meeting_id, meeting_id);
        params.put(ACTION_reschedule_date, date);
        params.put(ACTION_reason, note);
        return params;
    }

    public static Map<String, String> meetingAddRequest(final String userID,
                                                        final String client_id,
                                                        final String schedule_date,
                                                        final String created_at) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_client_id, client_id);
        params.put(ACTION_schedule_date, schedule_date);
        params.put(ACTION_created_at, created_at);
        return params;
    }


    public static Map<String, String> deleteClientRequest(final String userID,
                                                          final String clientID,
                                                          final String date) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_client_id, clientID);
        params.put(ACTION_reschedule_date, date);
        return params;
    }

    public static Map<String, String> deleteAccountRequest(final String userID,
                                                           final String account_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_account_id, account_id);
        return params;
    }

    public static Map<String, String> deleteMeetingRequest(final String userID,
                                                           final String id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_account_id, id);
        return params;
    }


    public static Map<String, String> addNote(final String userID,
                                              final String meeting_id,
                                              final String note) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_meeting_id, meeting_id);
        params.put(ACTION_reason, note);
        return params;
    }

    public static Map<String, String> addMeetingNote(final String userID,
                                                     final String meeting_id,
                                                     final String note) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_meeting_id, meeting_id);
        params.put(ACTION_note, note);
        return params;
    }

    public static Map<String, String> addAccountNote(final String accountID,
                                                     final String note,
                                                     final String entry_date) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_accountID, accountID);
        params.put(ACTION_note, note);
        params.put(ACTION_entry_date, entry_date);
        return params;
    }

    public static Map<String, String> applyLeaveRequest(final String userID, String reason, String fromDate, String toDate) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_user_id, userID);
        params.put(ACTION_leave_reason, reason);
        params.put(ACTION_leave_start, fromDate);
        params.put(ACTION_leave_end, toDate);
        return params;
    }

    public static Map<String, String> createLeadRequest(final String userID, final String clientType,
                                                        String companyName, String companyAddress,
                                                        String ownerName, String ownerMobile,
                                                        String createdAt, String lat, String lng,
                                                        String remark) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_user_id, userID);
        params.put(ACTION_client_type, clientType);
        params.put(ACTION_company_name, companyName);
        params.put(ACTION_company_address, companyAddress);
        params.put(ACTION_owner_name, ownerName);
        params.put(ACTION_owner_mobile, ownerMobile);
        params.put(ACTION_created_at, createdAt);
        params.put(ACTION_lat, lat);
        params.put(ACTION_lng, lng);
        params.put(ACTION_remark, remark);
        return params;
    }


    public static Map<String, String> createAccountRequest(final String userID,
                                                           final String type_id,
                                                           String amount,
                                                           String name,
                                                           String entry_date,
                                                           String note) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_user_id, userID);
        params.put(ACTION_type_id, type_id);
        params.put(ACTION_amount, amount);
        params.put(ACTION_name, name);
        params.put(ACTION_entry_date, entry_date);
        params.put(ACTION_note, note);
        return params;
    }

    public static Map<String, String> updateAccountRequest(final String userID,
                                                           final String account_id,
                                                           String amount,
                                                           String name,
                                                           String updation_date) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_user_id, userID);
        params.put(ACTION_account_id, account_id);
        params.put(ACTION_amount, amount);
        params.put(ACTION_name, name);
        params.put(ACTION_updation_date, updation_date);
        return params;
    }

    public static Map<String, String> updateClientRequest(final String userID,
                                                          final String clientType,
                                                          String companyName,
                                                          String companyAddress,
                                                          String ownerName,
                                                          String ownerMobile,
                                                          String client_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_user_id, userID);
        params.put(ACTION_client_type, clientType);
        params.put(ACTION_company_name, companyName);
        params.put(ACTION_company_address, companyAddress);
        params.put(ACTION_owner_name, ownerName);
        params.put(ACTION_owner_mobile, ownerMobile);
        params.put(ACTION_client_id, client_id);
        return params;
    }

    public static Map<String, String> otpRequest(final String userName,
                                                 final String otp, String imei_no, String model_no,String device_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_MOBILE_NO, userName);
        params.put(ACTION_otp, otp);
        params.put(ACTION_imei_no, imei_no);
        params.put(ACTION_model_no, model_no);
        params.put(ACTION_device_id, device_id);
        return params;
    }

    public static Map<String, String> locationRequest(final String userID,
                                                      final String lat,
                                                      final String lng,
                                                      final String remark,
                                                      final String network,
                                                      final String battery,
                                                      final String imei) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_user_id, userID);
        params.put(ACTION_lat, lat);
        params.put(ACTION_lng, lng);
        params.put(ACTION_remark, remark);
        params.put(ACTION_network, network);
        params.put(ACTION_battery, battery);
        params.put(ACTION_imei, imei);

        http:
//api-marserp.uat-pands.com:8051/activity/insertlocation
        return params;
    }

    public static Map<String, String> customerRequest(final String userID) {
        Map<String, String> params = new HashMap<String, String>();
        //params.put(ACTION_userID, userID);
        return params;
    }

    public static Map<String, String> attendanceRequest(final String userID,
                                                        final String custid,
                                                        final String contractorid,
                                                        final String serviceid,
                                                        final String attendate,
                                                        final String shift,
                                                        final String status) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_created_by, userID);
        params.put(ACTION_customer_id, custid);
        params.put(ACTION_contractorid, contractorid);
        params.put(ACTION_serviceid, serviceid);
        params.put(ACTION_attendate, attendate);
        params.put(ACTION_shift, shift);
        params.put(ACTION_status, status);

        return params;
    }

    public static Map<String, String> forgotPasswordRequest(final String email) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_email, email);
        return params;
    }

    public static Map<String, String> resetPasswordRequest(final String email,
                                                           String pwd, String hash_key, String token) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_email, email);
        params.put(ACTION_password, pwd);
        params.put(ACTION_token, token);
        params.put(ACTION_hash_key, hash_key);
        return params;
    }


    public static Map<String, String> createProfileRequest(final String email,
                                                           final String userPwd,
                                                           final String first_name,
                                                           final String last_name,
                                                           final String isd,
                                                           final String mobile,
                                                           final String country,
                                                           final String ref) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_email, email);
        params.put(ACTION_password, userPwd);
        params.put(ACTION_FIRST_NAME, first_name);
        params.put(ACTION_LAST_NAME, last_name);
        params.put(ACTION_isd, isd);
        params.put(ACTION_mobile, mobile);
        params.put(ACTION_country, country);
        params.put(ACTION_ref, ref);
        return params;
    }

    public static Map<String, String> logout() {
        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> registerRequest(final String userName) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_email, userName);
        return params;
    }

    public static Map<String, String> registerValidEmailRequest(final String userName) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_login, userName);
        return params;
    }

    public static Map<String, String> verifyRegistrationTokenRequest(final String token) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_ref, token);
        return params;
    }

    public static Map<String, String> verifyResetPasswordTokenRequest(final String key,
                                                                      final String token) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_key, key);
        params.put(ACTION_token, token);
        return params;
    }

    public static Map<String, String> makeBookingRequest(final String appointmentTime,
                                                         final String appointmentDate,
                                                         final String voucherServiceIDCSV,
                                                         final String isVenueClosed,
                                                         final String zoneID,
                                                         final String userID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_appointmentTime, appointmentTime);
        params.put(ACTION_appointmentDate, appointmentDate);
        params.put(ACTION_voucherServiceIDCSV, voucherServiceIDCSV);
        params.put(ACTION_isVenueClosed, isVenueClosed);
        params.put(ACTION_ZONEID, zoneID);
        params.put(ACTION_userID, userID);
        return params;
    }

    public static Map<String, String> getSlotRequest(final String userID,
                                                     final String businessID,
                                                     final String date,
                                                     final String time,
                                                     final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_businessID, businessID);
        params.put(ACTION_date, date);
        params.put(ACTION_time, time);
        params.put(ACTION_ZONEID, zoneID);
        return params;
    }

    public static Map<String, String> makeReview(final String userID,
                                                 final String businessID,
                                                 final String rating,
                                                 final String reviewText,
                                                 final String zoneID) {

        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_businessID, businessID);
        params.put(ACTION_rating, rating);
        params.put(ACTION_reviewText, reviewText);
        params.put(ACTION_ZONEID, zoneID);
        return params;
    }

    public static Map<String, String> getExistingReview(final String userID,
                                                        final String businessID) {

        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_businessID, businessID);
        return params;
    }

    public static Map<String, String> getTravellersGroupList() {

        Map<String, String> params = new HashMap<String, String>();
        return params;
    }

    public static Map<String, String> hotelStarPrefReq(final String hotel_min_star, final String hotel_max_star) {

        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_HOTEL_MIN_STAR, hotel_min_star);
        params.put(ACTION_HOTEL_MAX_STAR, hotel_max_star);
        return params;
    }

    public static Map<String, String> getHotelList(String query) {

        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_Q, query);
        return params;
    }

    public static Map<String, String> deleteTraveller(final String travellerIDs) {

        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_PAX_IDS, travellerIDs);
        return params;
    }

    public static Map<String, String> assignTraveller(final String travellerIDs,
                                                      final String label) {

        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_PAX_IDS, travellerIDs);
        params.put(ACTION_LABEL, label);
        return params;
    }

    public static Map<String, String> saveTravellerPref(final String sort_order,
                                                        final String display_order, final String labels, final String removed_labels) {

        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_SORT_ORDER, sort_order);
        params.put(ACTION_DISPLAY_ORDER, display_order);
        params.put(ACTION_LABELS, labels);
        params.put(ACTION_REMOVED_LABELS, removed_labels);

        return params;
    }

    public static Map<String, String> getAirlineList() {

        Map<String, String> params = new HashMap<String, String>();
        return params;
    }


    public static Map<String, String> getOfferCode(final String userID, final String offerCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_offerCode, offerCode);

        return params;
    }

    public static Map<String, String> couponCode(final String userID,
                                                 final String couponCode,
                                                 final String dealID,
                                                 final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_userID, userID);
        params.put(ACTION_couponCode, couponCode);
        params.put(ACTION_dealID, dealID);
        params.put(ACTION_ZONEID, zoneID);

        return params;
    }

    public static Map<String, String> getSearchForZoneData(final String queryText,
                                                           final String userID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_queryText, queryText);
        params.put(ACTION_userID, userID);

        return params;
    }

    /**
     * get Forgot Password params to hit related Api
     *
     * @param emailAddress
     * @param newPassword
     * @return
     */
    public static Map<String, String> getForgotPassword(final String emailAddress,
                                                        final String newPassword) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_EMAIL_ADDRESS, emailAddress);
        params.put(ACTION_NEW_PASSWORD, newPassword);

        return params;
    }

    /**
     * get facebook COnnect params to hit related Api
     *
     * @param name
     * @param emailAddress
     * @param deviceType
     * @param deviceID
     * @param notificationToken
     * @param facebookID
     * @param facebookAccessToken
     * @param facebookFriendCSV
     * @return
     */
    public static Map<String, String> getFacebookConnect(final String name,
                                                         final String emailAddress,
                                                         final String deviceType, final String deviceID, final String notificationToken,
                                                         final String facebookID, final String facebookAccessToken,
                                                         final String facebookFriendCSV) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_NAME, name);
        params.put(ACTION_EMAIL_ADDRESS, emailAddress);
        params.put(ACTION_DEVICE_TYPE, deviceType);
        params.put(ACTION_DEVICE_ID, deviceID);
        params.put(ACTION_NOTIFICATION_TOKEN, notificationToken);
        params.put(ACTION_FACEBOOK_ID, facebookID);
        params.put(ACTION_FACEBOOK_ACCESS_TOKEN, facebookAccessToken);
        params.put(ACTION_FACEBOOK_FRIEND_CSV, facebookFriendCSV);

        return params;
    }

    /**
     * get Google Connect params to hit related Api
     *
     * @param name
     * @param emailAddress
     * @param deviceType
     * @param deviceID
     * @param notificationToken
     * @param googlePlusID
     * @return
     */
    public static Map<String, String> getGoogleConnect(final String name, final String emailAddress,
                                                       final String deviceType, final String deviceID, final String notificationToken,
                                                       final String googlePlusID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_NAME, name);
        params.put(ACTION_EMAIL_ADDRESS, emailAddress);
        params.put(ACTION_DEVICE_TYPE, deviceType);
        params.put(ACTION_DEVICE_ID, deviceID);
        params.put(ACTION_NOTIFICATION_TOKEN, notificationToken);
        params.put(ACTION_GOOGLE_PLUS_ID, googlePlusID);

        return params;
    }


    public static Map<String, String> buyVoucherRequest(final String userID,
                                                        final String dealID,
                                                        final String couponCode,
                                                        final String isLoyaltyDeal,
                                                        final String creditsApplied,
                                                        final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_dealID, dealID);
        params.put(ACTION_userID, userID);
        params.put(ACTION_couponCode, couponCode);
        params.put(ACTION_isLoyaltyDeal, isLoyaltyDeal);
        params.put(ACTION_creditsApplied, creditsApplied);
        params.put(ACTION_ZONEID, zoneID);
        return params;
    }

    public static Map<String, String> buyVoucherCheckoutRequest(final String userID,
                                                                final String voucherCode,
                                                                final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_voucherCode, voucherCode);
        params.put(ACTION_userID, userID);
        params.put(ACTION_ZONEID, zoneID);
        return params;
    }

    public static Map<String, String> buyLoyaltyDealRequest(final String userID,
                                                            final String dealID,
                                                            final String couponCode,
                                                            final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_dealID, dealID);
        params.put(ACTION_userID, userID);
        params.put(ACTION_couponCode, couponCode);
        params.put(ACTION_ZONEID, zoneID);
        return params;
    }

    public static Map<String, String> buyDealCashRequest(final String userID,
                                                         final String dealID,
                                                         final String couponCode,
                                                         final String creditsApplied,
                                                         final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_dealID, dealID);
        params.put(ACTION_userID, userID);
        params.put(ACTION_couponCode, couponCode);
        params.put(ACTION_creditsApplied, creditsApplied);
        params.put(ACTION_ZONEID, zoneID);

        return params;
    }

    public static Map<String, String> buyFreeDealRequest(final String userID,
                                                         final String dealID,
                                                         final String couponCode,
                                                         String creditsApplied,
                                                         String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_dealID, dealID);
        params.put(ACTION_userID, userID);
        params.put(ACTION_couponCode, couponCode);
        params.put(ACTION_creditsApplied, creditsApplied);
        params.put(ACTION_ZONEID, zoneID);
        return params;
    }


    public static Map<String, String> createComment(final String userID, final String comment,
                                                    final String type, final String typeID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_USERID, userID);
        params.put(ACTION_comment, comment);
        params.put(ACTION_TYPE, type);
        params.put(ACTION_TYPE_ID, typeID);

        return params;
    }


    public static Map<String, String> getAllUserNotification(final String userID,
                                                             final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_USERID, userID);
        params.put(ACTION_ZONEID, zoneID);

        return params;
    }


    public static Map<String, String> getAllServices() {
        Map<String, String> params = new HashMap<String, String>();

        return params;
    }

    public static Map<String, String> priceFilterRange(final String zoneID,
                                                       final String userID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_ZONEID, zoneID);
        params.put(ACTION_userID, userID);

        return params;
    }


    /**
     * get Popular for Search params to hit related Api
     *
     * @param userID
     * @param zoneID
     * @return
     */
    public static Map<String, String> getPopularForSearch(final String userID,
                                                          final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_USERID, userID);
        params.put(ACTION_ZONEID, zoneID);

        return params;
    }

    /**
     * get Search params to hit related Api
     *
     * @param userID
     * @param zoneId
     * @param queryText
     * @return
     */
    public static Map<String, String> getSearch(final String userID, final String zoneId,
                                                final String queryText) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_USERID, userID);
        params.put(ACTION_ZONEID, zoneId);
        params.put(ACTION_QUERY_TEXT, queryText);

        return params;
    }


    /**
     * get Individual Post params to hit related Api
     *
     * @param userID
     * @param typeID
     * @param type
     * @return
     */
    public static Map<String, String> getIndividualPost(final String userID, final String typeID,
                                                        final String type) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_USERID, userID);
        params.put(ACTION_TYPE_ID, typeID);
        params.put(ACTION_TYPE, type);

        return params;
    }

    public static Map<String, String> getIndividualPostUser(final String userID,
                                                            final String typeID, final String type, final String zoneId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_USERID, userID);
        params.put(ACTION_TYPE_ID, typeID);
        params.put(ACTION_TYPE, type);
        params.put(ACTION_ZONEID, zoneId);

        return params;
    }


    /**
     * @param userId
     * @param finalPrice
     */
    public static Map<String, String> validateUserCredit(final String userId,
                                                         final String finalPrice,
                                                         final String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_USERID, userId);
        params.put(ACTION_FINAL_PRICE, finalPrice);
        params.put(ACTION_ZONEID, zoneID);
        return params;
    }

    public static Map<String, String> getUpdateFirebaseDeviceIdRequest(final String deviceId,
                                                        final String userId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_device_id, deviceId);
        params.put(ACTION_user_id, userId);

        return params;
    }

    public static Map<String, String> rateVenueAndFabogo(String userID,
                                                         String businessId,
                                                         String businessRating,
                                                         String businessReview,
                                                         String fabogoTag,
                                                         String fabogoDescription,
                                                         String voucherServiceIDCSV,
                                                         String fabogoRating,
                                                         String zoneID) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_USERID, userID);
        params.put(ACTION_BUSINESS_ID, businessId);
        params.put(ACTION_BUSINESS_RATING, businessRating);
        params.put(ACTION_BUSINESS_REVIEW, businessReview);
        params.put(ACTION_FABOGO_TAG, fabogoTag);
        params.put(ACTION_FABOGO_DESCRIPTION, fabogoDescription);
        params.put(ACTION_VOUCER_CSV, voucherServiceIDCSV);
        params.put(ACTION_FABOGO_RATING, fabogoRating);
        params.put(ACTION_ZONEID, zoneID);
        return params;
    }



    public static Map<String, String> deleteprofileImageRequest(final String userID, final String company_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_user_id, userID);
        params.put(ACTION_companyID, company_id);

        return params;
    }


    public static Map<String, String> deleteaccountprofileImageRequest(final String userID, final String company_id,String entry_id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(ACTION_user_id, userID);
        params.put(ACTION_companyID, company_id);
        params.put(ACTION_ENTRYID, entry_id);

        return params;
    }
}
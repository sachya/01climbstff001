package iostudio.in.et.retrofit.api;


import com.google.gson.JsonObject;

import java.util.Map;

import iostudio.in.et.retrofit.response.Account;
import iostudio.in.et.retrofit.response.AccountEntryData;
import iostudio.in.et.retrofit.response.AccountInfo;
import iostudio.in.et.retrofit.response.Client;
import iostudio.in.et.retrofit.response.ClientInfo;
import iostudio.in.et.retrofit.response.CommonResponse;
import iostudio.in.et.retrofit.response.Contractor;
import iostudio.in.et.retrofit.response.Customer;
import iostudio.in.et.retrofit.response.Home;
import iostudio.in.et.retrofit.response.Login;
import iostudio.in.et.retrofit.response.Meeting;
import iostudio.in.et.retrofit.response.MeetingInfo;
import iostudio.in.et.retrofit.response.MeetingType;
import iostudio.in.et.retrofit.response.Profile;
import iostudio.in.et.retrofit.response.ServiceType;
import iostudio.in.et.retrofit.response.Terms;
import iostudio.in.et.retrofit.response.Type;
import iostudio.in.et.utility.Constant;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by MotoBeans on 12/16/2015.
 */
public interface KApp<T> {

    /**
     * Description :
     *
     * @param options
     * @return
     */

    @FormUrlEncoded
    @POST("/api/signUp")
    Call<CommonResponse> getSignUp(@FieldMap Map<String, String> options);


    @Headers({
            "Content-type: application/json",
            "Authorization: key=AIzaSyBWWJlNiMo17zXqQfRuz53tc22cjLKcKZQ"
    })
    @POST("https://fcm.googleapis.com/fcm/send")
    Call<CommonResponse> sendNotification(@Body RequestBody typedJsonString);


    @FormUrlEncoded
    @POST(Constant.API.LOGIN)
    Call<Login> getLoginUser(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.APPLY_LEAVE)
    Call<CommonResponse> requestLeave(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.ACCOUNT_ADD)
    Call<AccountEntryData> requestAccountEntryAdd(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.ACCOUNT_UPDATE)
    Call<CommonResponse> requestAccountEntryUpdate(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.UPDATE_FIREBASE_DEVICE_ID)
    Call<CommonResponse> requestToUpdateFirebaseDeviceId(@FieldMap Map<String, String> request);

    @Multipart
    @POST(Constant.API.ACCOUNT_ADD)
    Call<CommonResponse> requestAccountEntryAddWithAttachment(@Part MultipartBody.Part file, @Part("user_id") RequestBody userId
            , @Part("type_id") RequestBody typeId
            , @Part("amount") RequestBody amount
            , @Part("name") RequestBody name
            , @Part("entry_date") RequestBody entryDate
            , @Part("note") RequestBody note);

    @FormUrlEncoded
    @POST(Constant.API.CLIENT_ADD)
    Call<CommonResponse> requestClientAdd(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.CLIENT_UPDATE)
    Call<CommonResponse> requestClientUpdate(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.CLIENT_LOCATION_UPDATE)
    Call<CommonResponse> requestClientLocationUpdate(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.LOGIN_VERIFY)
    Call<Login> getLoginVerifyUser(@FieldMap Map<String, String> request);


    @GET(Constant.API.CLIENT_TYPE)
    Call<Type> getClientTypeData(@Path("userID") String userID);

    @GET(Constant.API.ACCOUNT_TYPE)
    Call<Type> getaccountTypeData(@Path("userID") String userID);


    @GET(Constant.API.MEETING_STATUS)
    Call<MeetingType> getMeetingTypeData();


    @GET(Constant.API.LEAVE_TYPE)
    Call<Type> getLeaveTypeData(@QueryMap Map<String, String> request);


    @GET(Constant.API.LEAD_LIST)
    Call<Client> getLeadList(@Path("userID") String userID);

    @GET(Constant.API.HOME_DATA)
    Call<Home> getHomeData(@Path("userID") String userID);

    @FormUrlEncoded
    @POST(Constant.API.HOME_STATUS)
    Call<Home> getHomeStatus(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.ACCOUNT_FIND_ENTRY)
    Call<Account> getAccountData(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.MEETING_DATA)
    Call<Meeting> getMeetingData(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.MEETING_OTHER_LOCATION)
    Call<CommonResponse> meetingOtherLocationData(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.MEETING_RESCHEDULE)
    Call<CommonResponse> meetingReschdeule(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.MEETING_ADD)
    Call<CommonResponse> meetingAdd(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.ADD_NOTE)
    Call<CommonResponse> addNote(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.ACCOUNT_ADD_NOTE)
    Call<CommonResponse> accountAddNote(@FieldMap Map<String, String> request);


    @FormUrlEncoded
    @POST(Constant.API.CLIENT_DELETE)
    Call<CommonResponse> clientDelete(@FieldMap Map<String, String> request);

    @FormUrlEncoded
    @POST(Constant.API.ACCOUNT_DELETE)
    Call<CommonResponse> accountDelete(@FieldMap Map<String, String> request);


    @GET(Constant.API.SUPPORT_TERMS)
    Call<Terms> getTerms();

    @GET(Constant.API.PROFILE_DETAILS)
    Call<Profile> getProfileInformation(@Path("meeting_id") String meeting_id);

    @GET(Constant.API.MEETING_DELETE)
    Call<CommonResponse> deleteMeeting(@Path("meeting_id") String meeting_id);

    @GET(Constant.API.MEETING_DETAILS)
    Call<MeetingInfo> getMeetingInformation(@Path("meeting_id") String meeting_id);

    @GET(Constant.API.ACCOUNT_HOME)
    Call<AccountInfo> getAccountHome(@Path("id") String id);

    @GET(Constant.API.ACCOUNT_DETAILS)
    Call<AccountInfo> getAccountInformation(@Path("meeting_id") String meeting_id);

    @GET(Constant.API.CLIENT_LOCATION)
    Call<Client> getClientLocation(@Path("id") String id);

    @GET(Constant.API.CLIENT_DETAILS)
    Call<ClientInfo> getClientInformation(@Path("meeting_id") String meeting_id);

    @GET(Constant.API.MEETING_CANCEL)
    Call<CommonResponse> meetingCancelInformation(@Path("meeting_id") String meeting_id);


    @GET()
    Call<Customer> getCustomerData(@Url String url, @QueryMap Map<String, String> request);


    @GET()
    Call<ServiceType> getServiceTypeData(@Url String url, @QueryMap Map<String, String> request);

    @GET()
    Call<Contractor> getAttendanceData(@Url String url);


    @GET()
    Call<Contractor> getContactorData(@Url String url, @QueryMap Map<String, String> request);

    @Multipart
    @POST(Constant.API.PROFILE_ADD)
    Call<CommonResponse> requestAPROFILE_ADD(@Part MultipartBody.Part file, @Part("user_id") RequestBody userId, @Part("company_id") RequestBody company_id);


    @GET(Constant.API.DELETE_IMAGE_PROFILE)
    Call<CommonResponse> deleteImage(@QueryMap Map<String, String> request);


    @GET(Constant.API.DELETE_Account_IMAGE_PROFILE)
    Call<CommonResponse> deleteAccountEntryImage(@QueryMap Map<String, String> request);

      @Multipart
    @POST(Constant.API.ACCOUNT_IMAGE_ADD)
    Call<CommonResponse> requestAcoountImage(@Part MultipartBody.Part file, @Part("user_id") RequestBody userId, @Part("company_id") RequestBody company_id,@Part("entry_id") RequestBody entry_id );

}

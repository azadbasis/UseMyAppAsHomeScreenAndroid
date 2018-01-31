package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils;


import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.LoginResponse;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.MeetingRoomResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Sadi on 11/18/2017.
 */

public interface Api {

    //http://redb.sreda.gov.bd/home";
    //String BASE_URL = "http://192.168.0.119/renewableenergy/api/";
  //  String BASE_URL = "http://192.168.0.115/sreda_api/";
    String BASE_URL = "http://192.168.0.116/sreda_api/";


    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginUser(
            @Field("login_email") String email,
            @Field("login_password") String password
    );

    @FormUrlEncoded
    @POST("save-events")
    Call<LoginResponse> saveEvents(
            @Field("employee_id") String employee_id,
            @Field("title") String title,
            @Field("from_time") String from_time,
            @Field("to_time") String to_time,
            @Field("description") String description
    );

    @FormUrlEncoded
    @POST("save-meeting-room-booking")
    Call<LoginResponse> saveMeetingRoomBooking(
            @Field("employee_id") String employee_id,
            @Field("reference_no") String reference_no,
            @Field("room_id") String room_id,
            @Field("booking_type") String booking_type,
            @Field("booking_date") String booking_date,
            @Field("booking_start_time") String booking_start_time,
            @Field("booking_end_time") String booking_end_time,
            @Field("chairperson_name") String chairperson_name,
            @Field("number_of_member") String number_of_member,
            @Field("subject") String subject,
            @Field("preference_no") String preference_no,
            @Field("issue_no") String issue_no,
            @Field("booking_purpose") String booking_purpose,
            @Field("notice") String notice

    );


    @GET("meeting-room-booking")
    Call<MeetingRoomResponse> getRoomList(

    );
//
//    @GET("fuel_generation_report")
//    Call<Info_FuelGenResponse> getFuel(
//            @Query("email") String email,
//            @Query("password") String password
//
//    );
//
//
//    @GET("get_technology_names")
//    Call<Info_GetTechnologyNames> getTechnologyName(
//            @Query("email") String email,
//            @Query("password") String password
//
//    );
//
//    @GET("technology_wise_generation_report")
//    Call<Info_TechWiseGenReportResponse> getTechnologyNameReport(
//            @Query("email") String email,
//            @Query("password") String password
//
//    );
//
//    @GET("get_re_large_database_report")
//    Call<Info_Large_Database_Report> getLargeProjectReport(
//            @Query("email") String email,
//            @Query("password") String password
//
//    );



}

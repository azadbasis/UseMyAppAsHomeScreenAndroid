package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils;


import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.LeaveTypeResponse;
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
    String BASE_URL = "http://sreda.gov.bd/sreda_api/";
    //String BASE_URL = "http://192.168.0.115/sreda_api/";


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
            @Field("fee") String fee,
            @Field("discount") String discount,
            @Field("total_amount") String total_amount,
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

    @FormUrlEncoded
    @POST("add_leave_application")
    Call<LoginResponse> leaveApplication(
            @Field("employee_id") String employee_id,
            @Field("leave_type_id") String leave_type_id,
            @Field("request_from_date") String request_from_date,
            @Field("request_to_date") String request_to_date,
            @Field("number_of_days") String number_of_days,
            @Field("purpose") String purpose,
            @Field("emergency_contact_details") String emergency_contact_details,
            @Field("application_date") String application_date
    );



    @GET("leave_types")
    Call<LeaveTypeResponse> getLeave_types(

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

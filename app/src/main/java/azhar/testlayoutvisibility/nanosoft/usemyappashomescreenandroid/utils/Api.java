package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils;


import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Sadi on 11/18/2017.
 */

public interface Api {

    //http://redb.sreda.gov.bd/home";
    //String BASE_URL = "http://192.168.0.119/renewableenergy/api/";
    String BASE_URL = "http://192.168.0.115/sreda_api/";


    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginUser(
            @Field("login_email") String email,
            @Field("login_password") String password
    );
//
//
//    @GET("capacity_report")
//    Call<Info_CapacityResponse> getCapacity(
//            @Query("email") String email,
//            @Query("password") String password
//
//    );
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

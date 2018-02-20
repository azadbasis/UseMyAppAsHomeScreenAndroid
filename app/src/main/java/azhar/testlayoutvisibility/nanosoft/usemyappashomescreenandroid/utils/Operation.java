package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.LeaveTypeResponse;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.LoginResponse;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.MeetingRoomResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Nanosoft-Android on 1/15/2018.
 */

public class Operation {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    Activity activity;

    public Operation(Context context,Activity activity) {
        this.context = context;
        this.activity = activity;

    }

    public Operation(Context context) {
        this.context = context;
    }
    public void saveString(String key, String value) {
        sharedPreferences = context.getSharedPreferences("OFFICE_KIT", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public String getString(String key, String defaultvalue) {
        sharedPreferences = context.getSharedPreferences("OFFICE_KIT", MODE_PRIVATE);

        return sharedPreferences.getString(key, defaultvalue);
    }



    public void loginWithServer(final Context con, final String userEmail, final String userPassword) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);
        Call<LoginResponse> call = api.loginUser(userEmail,userPassword);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
               // progressBar.setVisibility(View.GONE);
                LoginResponse loginResponse = response.body();

                if(loginResponse!=null){
                    if(loginResponse.getStatus_code().equalsIgnoreCase("200")){
                        PersistentUser.setLogin(con);
                        AppConstant.loginResponse = loginResponse;
                        PersistData.setStringData(con,AppConstant.employee_id,loginResponse.getEmployee_info().getEmployee_id());
                        PersistData.setStringData(con,AppConstant.userEmail,userEmail);
                        PersistData.setStringData(con,AppConstant.userPassword,userPassword);Log.e("title",""+loginResponse.getEvents().get(0).getFrom_time());
//                    activity.startActivity(new Intent(con,MainActivityChat.class));
//                    activity.finish();
                    }
                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
               // progressBar.setVisibility(View.GONE);
            }
        });
    }



    public void meetingRoomGet(){


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);
        Call<MeetingRoomResponse> call = api.getRoomList();

        call.enqueue(new Callback<MeetingRoomResponse>() {
            @Override
            public void onResponse(Call<MeetingRoomResponse> call, Response<MeetingRoomResponse> response) {

                MeetingRoomResponse meetingRoomResponse = response.body();

                Log.e("room",""+meetingRoomResponse.getMeeting_room_booking().get(0).getRoom_title());
            }

            @Override
            public void onFailure(Call<MeetingRoomResponse> call, Throwable t) {

            }
        });
    }


    public void meetingRoomRequisition(String employee_id, String reference_no, String room_id,
                                       String booking_type, String  fee,String discount, String total_amount,  String booking_date, String booking_start_time,
                                       String booking_end_time, String chairperson_name, String number_of_member,
                                       String subject, String preference_no, String issue_no, String booking_purpose,
                                       String notice){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);
        Call<LoginResponse> call = api.saveMeetingRoomBooking(employee_id, reference_no,
                room_id,booking_type,fee,discount,total_amount,booking_date,booking_start_time,booking_end_time,chairperson_name,
                number_of_member,subject,preference_no,issue_no,booking_purpose,notice);


        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                if(loginResponse.getStatus_code().equalsIgnoreCase("200")){
                    Toast.makeText(context, "Meeting room requisition done!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }


    public void getLeaveType(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);
        Call<LeaveTypeResponse> call = api.getLeave_types();

        call.enqueue(new Callback<LeaveTypeResponse>() {
            @Override
            public void onResponse(Call<LeaveTypeResponse> call, Response<LeaveTypeResponse> response) {

                LeaveTypeResponse leaveTypeResponse = response.body();

                if(leaveTypeResponse!=null){
                    AppConstant.leaveTypeName.clear();
                    for(int i=0; i<leaveTypeResponse.getLeave_types().size();i++){

                        AppConstant.leaveTypeName.add(leaveTypeResponse.getLeave_types().get(i).getName());
                    }

                    AppConstant.liveTypeList = leaveTypeResponse.getLeave_types();
                    Log.e("type id",""+leaveTypeResponse.getLeave_types().get(0).getId());
                }

            }

            @Override
            public void onFailure(Call<LeaveTypeResponse> call, Throwable t) {

            }
        });
    }


    public void sendLeaveApplication(String employee_id, String leave_type_id, String request_from_date,
                                     String request_to_date, String number_of_days, String purpose,
                                     String emergency_contact_details, String application_date,String workingDay,String unit_id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);
        Call<LoginResponse> call = api.leaveApplication(employee_id, leave_type_id,
                request_from_date,request_to_date,number_of_days,purpose,emergency_contact_details,application_date,workingDay,unit_id);


        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                if(loginResponse!=null){
                    if(loginResponse.getStatus_code().equalsIgnoreCase("200")){
                        Toast.makeText(context, "Leave application submitted!", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    }
                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }
}

package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.MainActivity;
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

                if(loginResponse.getStatus_code().equalsIgnoreCase("200")){
                    PersistentUser.setLogin(con);
                    AppConstant.loginResponse = loginResponse;
                    PersistData.setStringData(con,AppConstant.employee_id,loginResponse.getEmployee_info().getEmployee_id());
                    PersistData.setStringData(con,AppConstant.userEmail,userEmail);
                    PersistData.setStringData(con,AppConstant.userPassword,userPassword);
                    Log.e("title",""+loginResponse.getEvents().get(0).getTitle());
//                    activity.startActivity(new Intent(con,MainActivity.class));
//                    activity.finish();
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


}

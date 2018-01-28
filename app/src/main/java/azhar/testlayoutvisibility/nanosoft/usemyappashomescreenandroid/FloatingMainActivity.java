package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.timessquare.CalendarPickerView;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.adapter.ScheduleAdapter;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.LoginResponse;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.ScheduleEvents;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.Api;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.AppConstant;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.Operation;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.PersistData;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission_group.CAMERA;
import static android.widget.LinearLayout.VERTICAL;
import static com.squareup.timessquare.CalendarPickerView.SelectionMode.MULTIPLE;

/**
 * Created by NanoSoft on 1/25/2018.
 */

public class FloatingMainActivity extends AppCompatActivity {

    Context con;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private TextView tvChat,tvMeetingNotice,tvCamera;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private CalendarPickerView calendar_view;
    private List<Date> eventDates = new ArrayList<>();
    private List<String> eventDetails = new ArrayList<>();
    //RealmResults<Events> resultsEvents;
    List<ScheduleEvents> resultsEvents = new ArrayList<>();
    private Realm mRealm;
    private Calendar nextYear;
    private Date today;
    private RecyclerView recycler_shedule;
    private String fromHourMin;
    private String toHourMin;
    private Dialog dialog;
    FloatingActionMenu flbMenuScan;
    FloatingActionButton flbQrCode, flbCamera,flbMeetingNotice,flbMeetingRoom,flbMeetingMinutes;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        con = this;

        if(checkPermission()){
            //queryLabels();
        }else if(!checkPermission()){
            requestPermission();
        }
        Operation operation = new Operation(con);
        operation.loginWithServer(con, PersistData.getStringData(con, AppConstant.userEmail),PersistData.getStringData(con, AppConstant.userPassword));
        initUi();



    }

    @TargetApi(Build.VERSION_CODES.N)
    private void initUi() {
        calendar_view = (CalendarPickerView) findViewById(R.id.calendar_view);

        //getting current
        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
        today = new Date();

//add one year to calendar from todays date
        calendar_view.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);

        calendar_view.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {


                Date dob=date;//this will take date as Fri Jan 06 00:00:00 IST 2012

                final String OLD_FORMAT = "yyyy-MM-dd";  //wants t convert date in this format
                Date d = null;
                SimpleDateFormat newDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                try {
                    d =newDateFormat.parse(newDateFormat.format(dob));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                newDateFormat.applyPattern(OLD_FORMAT);
                String new_date=newDateFormat.format(d);

                eventDialogue(new_date);

            }

            @Override
            public void onDateUnselected(Date date) {
//                calendar_view.init(today, nextYear.getTime())
//                        .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
                //Toast.makeText(getApplicationContext(),"UnSelected Date is : " +date.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        resultsEvents = AppConstant.loginResponse.getEvents();
        getEvents(resultsEvents);




        flbMenuScan = (FloatingActionMenu) findViewById(R.id.flbMenuScan);
        flbQrCode = (FloatingActionButton) findViewById(R.id.flbQrCode);
        flbCamera = (FloatingActionButton) findViewById(R.id.flbCamera);
        flbMeetingNotice = (FloatingActionButton) findViewById(R.id.flbMeetingNotice);
        flbMeetingRoom = (FloatingActionButton) findViewById(R.id.flbMeetingRoom);
        flbMeetingMinutes = (FloatingActionButton) findViewById(R.id.flbMeetingMinutes);

        flbCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked

            }
        });
        flbQrCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked

                startActivity(new Intent(con,QrCodeScannerActivity.class));
            }
        });
        flbMeetingNotice.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked

                startActivity(new Intent(con,MeetingNoticeActivity.class));
            }
        });
        flbMeetingRoom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu second item clicked

                startActivity(new Intent(con,MeetingNoticeActivity.class));
            }
        });
    }

    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        //int result5 = ContextCompat.checkSelfPermission(getApplicationContext(), CON);

        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED &&
                result3 == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, CAMERA,READ_PHONE_STATE,GET_ACCOUNTS}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readPhoneAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean getaccount = grantResults[3] == PackageManager.PERMISSION_GRANTED;


                    //queryLabels();
                    if (locationAccepted && cameraAccepted && readPhoneAccepted && getaccount) {

                        // Snackbar.make(view, "Permission Granted, Now you can access location data and camera.", Snackbar.LENGTH_LONG).show();
                    } else {

                        //Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, CAMERA, GET_ACCOUNTS},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(con)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void eventDialogue(final String date) {

//        int month = date.getMonth()+1;
//        int year = date.getYear()+1900;
//        String todate = date.getDate()+"/"+month+"/"+year;
        dialog = new Dialog(con);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogue_shedule);
        final TextView tvToday = (TextView) dialog.findViewById(R.id.tvToday);
        tvToday.setText("Today (" + date + ") Events:");
        final EditText etTitle = (EditText) dialog.findViewById(R.id.etTitle);
        final EditText etFromTime = (EditText) dialog.findViewById(R.id.etFromTime);
        final EditText etToTime = (EditText) dialog.findViewById(R.id.etToTime);
        final TextInputLayout txtInputTime = (TextInputLayout) dialog.findViewById(R.id.txtInputTime);
        Button btnAdd = (Button) dialog.findViewById(R.id.btnAdd);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        etFromTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                final DateTime inspected_at = DateTime.now();
                int hour = inspected_at.getHourOfDay();
                int minutes = inspected_at.getMinuteOfHour();
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(dialog.getContext(), new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String format;

                        if (hourOfDay == 0) {

                            hourOfDay += 12;

                            format = "AM";
                        } else if (hourOfDay == 12) {

                            format = "PM";

                        } else if (hourOfDay > 12) {

                            hourOfDay -= 12;

                            format = "PM";

                        } else {

                            format = "AM";
                        }
                        etFromTime.setText(hourOfDay + ":" + minute + " " + format);
                        fromHourMin = hourOfDay + ":" + minute + ":" + "00";
                    }

                }, hour, minutes, false);//Yes 24 hour time
                mTimePicker.setTitle("Select From Time");
                mTimePicker.show();
            }
        });

        etToTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                final DateTime inspected_at = DateTime.now();
                int hour = inspected_at.getHourOfDay();
                int minutes = inspected_at.getMinuteOfHour();
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(dialog.getContext(), new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String format;

                        if (hourOfDay == 0) {

                            hourOfDay += 12;

                            format = "AM";
                        } else if (hourOfDay == 12) {

                            format = "PM";

                        } else if (hourOfDay > 12) {

                            hourOfDay -= 12;

                            format = "PM";

                        } else {

                            format = "AM";
                        }
                        etToTime.setText(hourOfDay + ":" + minute + " " + format);
                        toHourMin = hourOfDay + ":" + minute + ":" + "00";
                    }

                }, hour, minutes, false);//Yes 24 hour time
                mTimePicker.setTitle("Select To Time");
                mTimePicker.show();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etTitle.getText().toString())) {
                    Toast.makeText(con, "Event title empty!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etFromTime.getText().toString())) {
                    Toast.makeText(con, "From time empty!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etToTime.getText().toString())) {
                    Toast.makeText(con, "To time empty!", Toast.LENGTH_SHORT).show();
                } else {

                    String from = date + " " + fromHourMin;
                    String to = date + " " + toHourMin;

                    saveEventToServer(PersistData.getStringData(con, AppConstant.employee_id),
                            etTitle.getText().toString(), from, to, "");
                }


            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar_view.init(today, nextYear.getTime()).inMode(MULTIPLE).withHighlightedDates(eventDates);
                dialog.dismiss();
            }
        });
        recycler_shedule = (RecyclerView) dialog.findViewById(R.id.recycler_shedule);

        LinearLayoutManager llm = new LinearLayoutManager(con);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);

        recycler_shedule.addItemDecoration(decoration);
        recycler_shedule.setLayoutManager(llm);

        List<ScheduleEvents> todayEvents = new ArrayList<>();
        for (int i = 0; i < resultsEvents.size(); i++) {
            String stdate = resultsEvents.get(i).getFrom_time();
            String[] parts = stdate.split(" ");

            if (date.toString().equalsIgnoreCase(parts[0])) {
                todayEvents.add(resultsEvents.get(i));
            }
        }


        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(con, todayEvents);
        recycler_shedule.setAdapter(scheduleAdapter);

        dialog.show();
    }

    private void saveEventToServer(String employee_id,String title,String from_time,
                                   String to_time,String description) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //Here we are using the GsonConverterFactory to directly convert json data to object
                .build();

        Api api = retrofit.create(Api.class);
        Call<LoginResponse> call = api.saveEvents(employee_id,title,from_time,to_time,description);
        call.enqueue(new Callback<LoginResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                LoginResponse loginResponse = response.body();
                if(loginResponse!=null){
                    if(loginResponse.getStatus_code().equalsIgnoreCase("200")){
                        Toast.makeText(con, "Event save to server.", Toast.LENGTH_SHORT).show();
                        resultsEvents = loginResponse.getEvents();
                        getEvents(loginResponse.getEvents());
                        dialog.dismiss();
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getEvents(List<ScheduleEvents> listEvents) {



        int todaymilis= (int) today.getTime();

        for(int i = 0; i<listEvents.size();i++){
            String stdate = listEvents.get(i).getFrom_time();

            String[] parts = stdate.split(" ");
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-DD");
            try {
                Date date = (Date)formatter.parse(parts[0]);
                int listodaymilis= (int) date.getTime();
                if(listodaymilis>todaymilis){
                    eventDates.add(date);
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }

            calendar_view.init(today, nextYear.getTime()).inMode(MULTIPLE).withHighlightedDates(eventDates);
        }
    }

}
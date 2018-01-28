package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;
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

public class MainActivity extends AppCompatActivity{

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_demo);
        auth = FirebaseAuth.getInstance();
        con = this;
        mRealm = Realm.getInstance(con);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(tokenReceiver,
                new IntentFilter("tokenReceiver"));

        Log.e("token",PersistData.getStringData(con,AppConstant.fcm_token));
        if(checkPermission()){
            //queryLabels();
        }else if(!checkPermission()){
            requestPermission();
        }
        Operation operation = new Operation(con);
        operation.loginWithServer(con, PersistData.getStringData(con, AppConstant.userEmail),PersistData.getStringData(con, AppConstant.userPassword));
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void initialize() {

        tvChat = (TextView)findViewById(R.id.tvChat);
        tvCamera = (TextView)findViewById(R.id.tvCamera);
        tvMeetingNotice = (TextView)findViewById(R.id.tvMeetingNotice);

        tvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //scanButtonDialogue();
                startActivity(new Intent(con,ScanActivity.class));
                startActivity(new Intent(con,QrCodeScannerActivity.class));
            }
        });
        tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(con,UserListActivity.class));
                setContentView(R.layout.meeting_room_booking);
            }
        });

        tvMeetingNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(con,MeetingNoticeActivity.class));
            }
        });

        calendar_view = (CalendarPickerView) findViewById(R.id.calendar_view);

        //getting current
        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);
         today = new Date();

//add one year to calendar from todays date
        calendar_view.init(today, nextYear.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
//action while clicking on a date
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


    @Override
    protected void onStart() {


        super.onStart();
    }

    public void exitFromApp() {
        final CharSequence[] items = {"NO", "YES"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit from app?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        return;
                    case 1:
                        finish();

                        break;
                    default:
                        return;
                }
            }
        });
        builder.show();
        builder.create();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            exitFromApp();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private static final int IMAGE_CAPTURE = 102;
    public static final int CAMERA_PIC_REQUEST = 1;//firstly define this
    private static final int VIDEO_CAPTURE = 101;

    public void openOSApplication(View view) {
        int id = view.getId();
        if (id == R.id.tvCall) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_CONTACTS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (id == R.id.tvMail) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

//            Intent emailIntent = new Intent(Intent.ACTION_SEND);
//            emailIntent.setType("text/plain");
//            startActivity(emailIntent);
        }
        if (id == R.id.tvInbox) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_MESSAGING);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (id == R.id.tvChat) {

        }
        if (id == R.id.tvCar) {
            startActivity(new Intent(MainActivity.this, CarActivity.class));
        }
        if (id == R.id.tvSMS) {
            Uri sms_uri = Uri.parse("smsto:");
            Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
            sms_intent.putExtra("", "");
            startActivity(sms_intent);
        }


        if (id == R.id.tvLoginLogout) {
            logOutDialogue();
        }
        if (id == R.id.tvProfile) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));

        }

    }

    private void logOutDialogue() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        auth.signOut();
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setMessage("Are you sure want to logout?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    BroadcastReceiver tokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String token = intent.getStringExtra("token");
            if(token != null)
            {
                PersistData.setStringData(context, AppConstant.fcm_token,token);
                Log.e("token",token);
            }

        }
    };


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void eventDialogue(final String date){

//        int month = date.getMonth()+1;
//        int year = date.getYear()+1900;
//        String todate = date.getDate()+"/"+month+"/"+year;
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialogue_shedule);
        final TextView tvToday = (TextView)dialog.findViewById(R.id.tvToday);
        tvToday.setText("Today ("+date+") Events:");
        final EditText etTitle = (EditText)dialog.findViewById(R.id.etTitle);
        final EditText etFromTime = (EditText)dialog.findViewById(R.id.etFromTime);
        final EditText etToTime = (EditText)dialog.findViewById(R.id.etToTime);
        final TextInputLayout txtInputTime = (TextInputLayout)dialog.findViewById(R.id.txtInputTime);
        Button btnAdd = (Button)dialog.findViewById(R.id.btnAdd);
        Button btnCancel = (Button)dialog.findViewById(R.id.btnCancel);
        etFromTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                final DateTime inspected_at = DateTime.now();
                int hour    = inspected_at.getHourOfDay();
                int minutes = inspected_at.getMinuteOfHour();
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(dialog.getContext(), new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String format;

                        if (hourOfDay == 0) {

                            hourOfDay += 12;

                            format = "AM";
                        }
                        else if (hourOfDay == 12) {

                            format = "PM";

                        }
                        else if (hourOfDay > 12) {

                            hourOfDay -= 12;

                            format = "PM";

                        }
                        else {

                            format = "AM";
                        }
                        etFromTime.setText(hourOfDay+":"+minute+" "+format );
                        fromHourMin = hourOfDay+":"+minute+":"+"00";
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
                int hour    = inspected_at.getHourOfDay();
                int minutes = inspected_at.getMinuteOfHour();
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(dialog.getContext(), new TimePickerDialog.OnTimeSetListener() {

                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String format;

                        if (hourOfDay == 0) {

                            hourOfDay += 12;

                            format = "AM";
                        }
                        else if (hourOfDay == 12) {

                            format = "PM";

                        }
                        else if (hourOfDay > 12) {

                            hourOfDay -= 12;

                            format = "PM";

                        }
                        else {

                            format = "AM";
                        }
                        etToTime.setText(hourOfDay+":"+minute+" "+format );
                        toHourMin = hourOfDay+":"+minute+":"+"00";
                    }

                }, hour, minutes, false);//Yes 24 hour time
                mTimePicker.setTitle("Select To Time");
                mTimePicker.show();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(TextUtils.isEmpty(etTitle.getText().toString())) {
                   Toast.makeText(con, "Event title empty!", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(etFromTime.getText().toString())){
                   Toast.makeText(con, "From time empty!", Toast.LENGTH_SHORT).show();
               }else if(TextUtils.isEmpty(etToTime.getText().toString())){
                   Toast.makeText(con, "To time empty!", Toast.LENGTH_SHORT).show();
               }else {

                   String from = date+" "+fromHourMin;
                   String to = date+" "+toHourMin;

                   saveEventToServer(PersistData.getStringData(con,AppConstant.employee_id),
                           etTitle.getText().toString(),from,to,"");
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

        List <ScheduleEvents> todayEvents = new ArrayList<>();
        for(int i = 0; i<resultsEvents.size();i++){
            String stdate = resultsEvents.get(i).getFrom_time();
            String[] parts = stdate.split(" ");

            if(date.toString().equalsIgnoreCase(parts[0])){
                todayEvents.add(resultsEvents.get(i));
            }
        }


        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(con,todayEvents);
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

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void deleteEvents(final Date eventDate) {
//
//        mRealm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                eventDates.clear();
//                RealmResults<Events> rows = realm.where(Events.class).equalTo("eventDate", eventDate).findAll();
//                rows.clear();
//                resultsEvents = mRealm.where(Events.class).findAll();
//                if(resultsEvents.size()>0){
//                    for(int i =0; i<resultsEvents.size();i++){
//                        eventDates.add(resultsEvents.get(i).getEventDate());
//                    }
//                    calendar_view.init(today, nextYear.getTime()).inMode(MULTIPLE).withHighlightedDates(eventDates);
//                }else {
//                    calendar_view.init(today, nextYear.getTime())
//                            .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
//                }
//
//            }
//        });
//
//
//    }

//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private void addEvent(Date eventDate, String eventTitle,String time) {
//        mRealm.beginTransaction();
//        Events events = mRealm.createObject(Events.class);
//        events.setEventTitle(eventTitle);
//        events.setEventTime(time);
//        events.setEventDate(eventDate);
//        mRealm.commitTransaction();
//        resultsEvents = mRealm.where(Events.class).findAll();
//
//        List <Events> todayEvents = new ArrayList<>();
//        for(int i =0; i<resultsEvents.size();i++){
//            eventDates.add(resultsEvents.get(i).getEventDate());
//                if(eventDate.toString().equalsIgnoreCase(resultsEvents.get(i).getEventDate().toString())){
//                    todayEvents.add(resultsEvents.get(i));
//                }
//        }
//        ScheduleAdapter scheduleAdapter = new ScheduleAdapter(con,todayEvents);
//        recycler_shedule.setAdapter(scheduleAdapter);
//        calendar_view.init(today, nextYear.getTime()).inMode(MULTIPLE).withHighlightedDates(eventDates);
//        calendar_view.setSelector(R.drawable.circle_accent);
//        Toast.makeText(con,"Event Added!",Toast.LENGTH_SHORT).show();
//    }


//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void updateEvent(Date eventDate, String eventTitle) {
//        Events editPersonDetails = mRealm.where(Events.class).equalTo("eventDate", eventDate).findFirst();
//        mRealm.beginTransaction();
//        editPersonDetails.setEventTitle(eventTitle);
//        editPersonDetails.setEventDate(eventDate);
//        mRealm.commitTransaction();
//        resultsEvents = mRealm.where(Events.class).findAll();
//
//        for(int i =0; i<resultsEvents.size();i++){
//            eventDates.add(resultsEvents.get(i).getEventDate());
//        }
//        calendar_view.init(today, nextYear.getTime()).inMode(MULTIPLE).withHighlightedDates(eventDates);
//
////        new Handler().postDelayed(new Runnable() {
////            @Override
////            public void run() {
////
////            }
////        },500);
//    }

    // Get the account list, and pick the first one
    final String ACCOUNT_TYPE_GOOGLE = "com.google";
    final String[] FEATURES_MAIL = {
            "service_mail"
    };

    public void showMeetingMinute(View view) {
        setContentView(R.layout.meeting_minute);
    }


    public static final class LabelColumns {
        public static final String CANONICAL_NAME = "canonicalName";
        public static final String NAME = "name";
        public static final String NUM_CONVERSATIONS = "numConversations";
        public static final String NUM_UNREAD_CONVERSATIONS = "numUnreadConversations";
    }

    public void queryLabels(){
        String account="shah.kutub1@gmail.com";
        Uri LABELS_URI = Uri.parse("content://gmail-ls/labels/");
        Uri ACCOUNT_URI = Uri.withAppendedPath(LABELS_URI, account);
        ContentResolver contentResolver=MainActivity.this.getContentResolver();
        Cursor cursor = contentResolver.query(ACCOUNT_URI, null, null, null, null);

        //iterate over all labels in the account
        if (cursor.moveToFirst()) {
            int unreadColumn = cursor.getColumnIndex(LabelColumns.NUM_UNREAD_CONVERSATIONS);
            int nameColumn = cursor.getColumnIndex(LabelColumns.NAME);
            do {
                String name = cursor.getString(nameColumn);
                String unread = cursor.getString(unreadColumn);//here's the value you need
                Log.e("read mai",""+name+""+unread);
            } while (cursor.moveToNext());
        }
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
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    private void scheduleMultipleAlarms(){

        Intent intent = new Intent(con, MainActivity.class);
//        final int id = (int) System.currentTimeMillis();
//        PendingIntent appIntent = PendingIntent.getBroadcast(this, id, intent,PendingIntent.FLAG_ONE_SHOT);

//        intent.putExtra("row_id", id);
//        final int alarmId = (int) System.currentTimeMillis();
//        PendingIntent sender = PendingIntent.getBroadcast(a, alarmId, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager am = (AlarmManager) a.getSystemService(a.ALARM_SERVICE);
//        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void scanButtonDialogue(){

        Dialog dialogScan = new Dialog(con);
        dialogScan.setContentView(R.layout.scan_buttons);
        dialogScan.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        ArcMenu arcMenu;
        arcMenu = (ArcMenu) dialogScan.findViewById(R.id.arcMenu);
        arcMenu.setBackgroundResource(R.drawable.scanbg);
        arcMenu.setRadius(getResources().getDimension(R.dimen._200sdp));
        arcMenu.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onMenuOpened() {
                //Snackbar.make(arcMenu, "Menu Opened", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onMenuClosed() {
                //Snackbar.make(arcMenu, "Menu Closed", Snackbar.LENGTH_SHORT).show();
            }
        });
        dialogScan.show();

    }

}




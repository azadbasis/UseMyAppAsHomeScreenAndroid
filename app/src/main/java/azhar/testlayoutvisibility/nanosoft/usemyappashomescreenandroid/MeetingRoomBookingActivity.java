package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.RoomNameInfo;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.PersistData;

public class MeetingRoomBookingActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    private Spinner spinnerRoomName, spinnerBookingType;
    List<String> roomNameStrings = new ArrayList<>();
    List<String> bookingTypeStrings = new ArrayList<>();
    List<String> roomMoneyString = new ArrayList<>();
    List<String> roomNoStrings = new ArrayList<>();
    LinearLayout layoutPayment;

    TextView tvStartDate, tvEndDate;
    int mYear, mMonth, mDay, mHour, mMinute;
    int cYear, cMonth, cDay, cHour, cMinute;
    String dateTime;

//    private static final String ROOM_INFO_URL = "http://192.168.0.116/sreda_api/meeting_room";
    private static final String ROOM_INFO_URL = "http://192.168.0.115/sreda_api/meeting_room";
    private String id, room_number, room_title, create_date, create_by, update_by, status, amount;
    RoomNameInfo roomNameInfo;
    List<RoomNameInfo> roomNameInfoList = new ArrayList<>();
    EditText tietxt_reference_no, tietxt_chairperson_name, tietxt_number_of_member, tietxt_subject,tietxt_fee,tietxt_discount,tietxt_total_amount, tietxt_preference_no, tietxt_issue_no, tietxt_booking_purpose, tietxt_notice;
    private String mEmployeeId,myReferenceNo, myChairpersonName, myNumberOfMember, mySubject, myPrefernceNo, myIssueNo, myBookingPurpose, myNotice,mBookingType,mBookingDate,mBookingStartTime,mBookingEndTime ;
private String roomName,roomId,roomMoney, mtime;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_room_booking_main);

        bindActivity();
        mAppBarLayout.addOnOffsetChangedListener(this);
        mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_share) {
                    SubmitRoomBookingInfo();
                }
                return false;
            }
        });

        getTextFromInterface();

    }


    private void SubmitRoomBookingInfo() {
        mEmployeeId= PersistData.getStringData(this,"employee_id");
        Toast.makeText(this, "submit data"+roomName+"\n"+roomMoney+"\n"+roomId+"\n"+mEmployeeId, Toast.LENGTH_SHORT).show();


    }

    private void getTextFromInterface() {
        myReferenceNo = tietxt_reference_no.getText().toString();
        myChairpersonName = tietxt_chairperson_name.getText().toString();
        myNumberOfMember = tietxt_number_of_member.getText().toString();
        mySubject = tietxt_subject.getText().toString();
        myPrefernceNo = tietxt_preference_no.getText().toString();
        myIssueNo = tietxt_issue_no.getText().toString();
        myBookingPurpose = tietxt_booking_purpose.getText().toString();
        myNotice = tietxt_notice.getText().toString();
    }

    private void getRoomBookingInfo() {

        JsonObjectRequest objectRequest = new JsonObjectRequest(ROOM_INFO_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("room_info", response.toString());

                try {
                    JSONObject object = new JSONObject(String.valueOf(response));
                    JSONArray array = new JSONArray(object.getString("meeting_room"));
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object1 = (JSONObject) array.get(i);
                        String id = object1.getString("id");
                        String room_number = object1.getString("room_number");
                        String room_title = object1.getString("room_title");
                        String create_date = object1.getString("create_date");
                        String update_by = object1.getString("update_by");
                        String status = object1.getString("status");
                        String amount = object1.getString("amount");

                        roomNameInfo = new RoomNameInfo(id, room_number, room_title, create_date, create_by, update_by, status, amount);
                        roomNameInfoList.add(roomNameInfo);
                    }
                    for (int j = 0; j < roomNameInfoList.size(); j++) {
                        roomNameStrings.add(roomNameInfoList.get(j).getRoom_title());

                    }
                    for (int k = 0; k < roomNameInfoList.size(); k++) {
                        roomMoneyString.add(roomNameInfoList.get(k).getAmount());
                    }
                    for (int l = 0; l < roomNameInfoList.size(); l++) {
                        roomNoStrings.add(roomNameInfoList.get(l).getId());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(objectRequest);
    }

    private void bindActivity() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        layoutPayment = (LinearLayout) findViewById(R.id.layoutPayment);




        tietxt_total_amount = (EditText) findViewById(R.id.tietxt_total_amount);
        tietxt_fee = (EditText) findViewById(R.id.tietxt_fee);
        tietxt_discount = (EditText) findViewById(R.id.tietxt_discount);

        tietxt_reference_no = (EditText) findViewById(R.id.tietxt_reference_no);
        tietxt_chairperson_name = (EditText) findViewById(R.id.tietxt_chairperson_name);
        tietxt_number_of_member = (EditText) findViewById(R.id.tietxt_number_of_member);
        tietxt_subject = (EditText) findViewById(R.id.tietxt_subject);
        tietxt_preference_no = (EditText) findViewById(R.id.tietxt_preference_no);
        tietxt_issue_no = (EditText) findViewById(R.id.tietxt_issue_no);
        tietxt_booking_purpose = (EditText) findViewById(R.id.tietxt_booking_purpose);
        tietxt_notice = (EditText) findViewById(R.id.tietxt_notice);
        getRoomBookingInfo();
        roomNameStrings.add("--please select--");
        ArrayAdapter<String> roomNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roomNameStrings);
        spinnerRoomName = (Spinner) findViewById(R.id.spinnerRoomName);
        spinnerRoomName.setAdapter(roomNameAdapter);
        spinnerRoomName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if(i>0){

                    roomName = spinnerRoomName.getSelectedItem().toString();
                     roomId=roomNoStrings.get(i-1).toString();
                     roomMoney=roomMoneyString.get(i-1).toString();


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bookingTypeStrings.add("--please select--");
        bookingTypeStrings.add("Internal");
        bookingTypeStrings.add("External");

        ArrayAdapter<String> bookingTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bookingTypeStrings);
        spinnerBookingType = (Spinner) findViewById(R.id.spinnerBookingType);
        spinnerBookingType.setAdapter(bookingTypeAdapter);

        spinnerBookingType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {
                     mBookingType = spinnerBookingType.getSelectedItem().toString();
                    if (mBookingType.equalsIgnoreCase("External")) {
                        layoutPayment.setVisibility(View.VISIBLE);
                        tietxt_fee.setText(roomMoney);

                    } else {
                        layoutPayment.setVisibility(View.GONE);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        DateFormat dateFormatter = new SimpleDateFormat("dd-M-yyyy hh:mm");
        dateFormatter.setLenient(false);
        Date today = new Date();
        dateTime = dateFormatter.format(today);
        tvStartDate.setText(dateTime);
        tvEndDate.setText(dateTime);

    }

    MenuItem menu_share;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu_share = menu.findItem(R.id.menu_share);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    public void setMeetingStartDateTime(View view) {
        datePicker(tvStartDate);
        mBookingStartTime=mtime;
        Toast.makeText(this, "mBookingStartTime"+mBookingStartTime, Toast.LENGTH_SHORT).show();
    }

    private void datePicker(final TextView textViewDate) {

// Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        cYear = year;
                        cMonth = monthOfYear + 1;
                        cDay = dayOfMonth;
                        dateTime = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        mBookingDate=dateTime;
                        // textViewDate.setText(dateTime);
                        tiemPicker(textViewDate);


                    }

                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void tiemPicker(final TextView textViewTime) {

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        cHour = hourOfDay;
                        cMinute = minute;
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
                         mtime=hourOfDay + ":" + minute + " " + format;
                        textViewTime.setText(dateTime + "    " + hourOfDay + ":" + minute + " " + format);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void setMeetingEndDateTime(View view) {
        datePicker(tvEndDate);
        mBookingEndTime=mtime;

    }
}

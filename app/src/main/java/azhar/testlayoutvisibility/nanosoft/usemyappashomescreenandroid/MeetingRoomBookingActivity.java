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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.Operation;

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
    ArrayList<String> roomNameStrings = new ArrayList<>();
    ArrayList<String> bookingTypeStrings = new ArrayList<>();
    LinearLayout layoutPayment;

    TextView tvStartDate, tvEndDate;
    int mYear, mMonth, mDay, mHour, mMinute;
    int cYear, cMonth, cDay, cHour, cMinute;
    String dateTime;


    EditText tietxt_reference_no, tietxt_chairperson_name, tietxt_number_of_member, tietxt_subject, tietxt_preference_no, tietxt_issue_no, tietxt_booking_purpose, tietxt_notice;
    private String myReferenceNo, myChairpersonName, myNumberOfMember, mySubject, myPrefernceNo, myIssueNo, myBookingPurpose, myNotice;

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
                if(item.getItemId()==R.id.menu_share){
                 SubmitRoomBookingInfo();
                }
                return false;
            }
        });

        getTextFromInterface();
        getRoomBookingInfo();
    }
private static final String   ROOM_INFO_URL="http://192.168.0.116/sreda_api/meeting_room";
    private void getRoomBookingInfo() {

        JsonObjectRequest objectRequest=new JsonObjectRequest(ROOM_INFO_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("room_info",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(objectRequest);
    }

    private void SubmitRoomBookingInfo() {
        Operation operation=new Operation(this);
        String employee_id=operation.getString("employee_id","");
        /*String employee_id,String ,String room_id,
                                        String booking_type,String booking_date, String booking_start_time,
                                        String booking_end_time,String chairperson_name,String number_of_member,
                                        String subject,String preference_no,String issue_no,String booking_purpose,
                                        String notice*/
      //  operation.meetingRoomRequisition(employee_id,myReferenceNo,my);
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

    private void bindActivity() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);


        tietxt_reference_no = (EditText) findViewById(R.id.tietxt_reference_no);
        tietxt_chairperson_name = (EditText) findViewById(R.id.tietxt_chairperson_name);
        tietxt_number_of_member = (EditText) findViewById(R.id.tietxt_number_of_member);
        tietxt_subject = (EditText) findViewById(R.id.tietxt_subject);
        tietxt_preference_no = (EditText) findViewById(R.id.tietxt_preference_no);
        tietxt_issue_no = (EditText) findViewById(R.id.tietxt_issue_no);
        tietxt_booking_purpose = (EditText) findViewById(R.id.tietxt_booking_purpose);
        tietxt_notice = (EditText) findViewById(R.id.tietxt_notice);

        roomNameStrings.add("--please select--");
        roomNameStrings.add("Mohananda");
        roomNameStrings.add("Chondona");
        roomNameStrings.add("Gomoti");
        ArrayAdapter<String> roomNameAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roomNameStrings);
        spinnerRoomName = (Spinner) findViewById(R.id.spinnerRoomName);
        spinnerRoomName.setAdapter(roomNameAdapter);
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
                    String text = spinnerBookingType.getSelectedItem().toString();
                    if (text.equalsIgnoreCase("External")) {
                        layoutPayment.setVisibility(View.VISIBLE);
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
        menu_share=menu.findItem(R.id.menu_share);
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

                        textViewTime.setText(dateTime + "    " + hourOfDay + ":" + minute + " " + format);


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    public void setMeetingEndDateTime(View view) {
        datePicker(tvEndDate);
    }
}

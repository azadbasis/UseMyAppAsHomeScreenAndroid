package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    TextView tvStartDate,tvEndDate;
    int mYear, mMonth, mDay, mHour, mMinute;
    int cYear, cMonth, cDay, cHour, cMinute;
    String dateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_room_booking_main);

        bindActivity();

        mAppBarLayout.addOnOffsetChangedListener(this);

        mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);
    }

    private void bindActivity() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        layoutPayment = (LinearLayout) findViewById(R.id.layoutPayment);
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
        tvStartDate=(TextView)findViewById(R.id.tvStartDate);
        tvEndDate=(TextView)findViewById(R.id.tvEndDate);
        DateFormat dateFormatter = new SimpleDateFormat("dd-M-yyyy hh:mm");
        dateFormatter.setLenient(false);
        Date today = new Date();
        dateTime = dateFormatter.format(today);
        tvStartDate.setText(dateTime);
        tvEndDate.setText(dateTime);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

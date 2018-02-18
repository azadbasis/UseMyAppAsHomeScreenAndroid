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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.Api;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.App;

public class CarActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;

    public static final String JSON_USER_URL = Api.BASE_URL + "users";
    public static final String JSON_PURPOSE_URL = Api.BASE_URL + "journey_purpose";
    public static final String JSON_VEHICLE_REQUISITION_URL = Api.BASE_URL + "save_vehicle_requisition";

    private EditText tietxt_location_to, tietxt_location_from;
    private TextView tvEndDate, tvStartDate;
    private Spinner spinnerJourneyPurpose, spinnerEmployeeName;
    int mYear, mMonth, mDay, mHour, mMinute;
    int cYear, cMonth, cDay, cHour, cMinute;
    String dateTime, mTime;
    List<String> getEmployeeName, getEmployeeId, getPurposeName, getPurposeId;
    private String employeeName, employeeId, employee_id;
    private String purposeName, purposeId, journey_purpose_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_requisition);
        getEmployeeName = new ArrayList<>();
        getEmployeeId = new ArrayList<>();
        getPurposeName = new ArrayList<>();
        getPurposeId = new ArrayList<>();

        bindActivity();
        getEmployee();
        getJourneyPurpose();
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

    }

    private void getJourneyPurpose() {


        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, JSON_PURPOSE_URL, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject object = new JSONObject(String.valueOf(response));
                            JSONArray array = object.getJSONArray("meeting_room");
                            JSONObject jsonObject;
                            for (int i = 0; i < array.length(); i++) {
                                jsonObject = (JSONObject) array.get(i);
                                purposeId = jsonObject.getString("id");
                                purposeName = jsonObject.getString("name");
                                getPurposeName.add(purposeName);
                                getPurposeId.add(purposeId);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                    }
                });


// Access the RequestQueue through your singleton class.
        App.getInstance().addToRequestQueue(jsObjRequest);

    }


    private void getEmployee() {
        JsonArrayRequest arrayRequest = new JsonArrayRequest(JSON_USER_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Array request", response.toString());
                        JSONObject jsonObject;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                jsonObject = response.getJSONObject(i);
                                employeeName = jsonObject.getString("employee_name");
                                employeeId = jsonObject.getString("employee_id");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            getEmployeeName.add(employeeName);
                            getEmployeeId.add(employeeId);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CarActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        App.getInstance().addToRequestQueue(arrayRequest);
    }

    private void bindActivity() {
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);


        tietxt_location_to = (EditText) findViewById(R.id.tietxt_location_to);
        tietxt_location_from = (EditText) findViewById(R.id.tietxt_location_from);

        spinnerJourneyPurpose = (Spinner) findViewById(R.id.spinnerJourneyPurpose);

        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);


        // DateFormat dateFormatter = new SimpleDateFormat("dd-M-yyyy hh:mm");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-M-dd hh:mm");
        dateFormatter.setLenient(false);
        Date today = new Date();
        dateTime = dateFormatter.format(today);
        tvStartDate.setText(dateTime);
        tvEndDate.setText(dateTime);

        getEmployeeName.add("--Select One--");
        ArrayAdapter<String> employeeAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, getEmployeeName);
        spinnerEmployeeName = (Spinner) findViewById(R.id.spinnerEmployeeName);
        spinnerEmployeeName.setAdapter(employeeAdapter);
        spinnerEmployeeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                if (i > 0) {
                    String name = getEmployeeName.get(i);
                    employee_id = getEmployeeId.get(i - 1);
                    Toast.makeText(CarActivity.this, "Employee " + name + "\n" + employee_id, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinnerJourneyPurpose = (Spinner) findViewById(R.id.spinnerJourneyPurpose);
        getPurposeName.add("--Select One--");
        ArrayAdapter<String> journeyPurposeAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, getPurposeName);
        spinnerJourneyPurpose.setAdapter(journeyPurposeAdapter);
        spinnerJourneyPurpose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    String name = getPurposeName.get(i);
                    journey_purpose_id = getPurposeId.get(i - 1);
                    Toast.makeText(CarActivity.this, "Purpose " + name + "\n" + journey_purpose_id, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /*1. journey_purpose
2. save_vehicle_requisition
employee_id
journey_purpose_id
journey_start_date
journey_end_date
journey_location_from
journey_location_to
*/
    }

    private void SubmitRoomBookingInfo() {

        submitCarRequisitionData();
        // Toast.makeText(this, "HERE IS CAR REQUISITION", Toast.LENGTH_SHORT).show();
    }

    private void submitCarRequisitionData() {

        StringRequest request = new StringRequest(Request.Method.POST, JSON_VEHICLE_REQUISITION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(CarActivity.this, "CAR REQUISITION" + response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> myData = new HashMap<>();
                myData.put("employee_id", employee_id);
                myData.put("journey_purpose_id", journey_purpose_id);
                myData.put("journey_start_date", tvStartDate.getText().toString());
                myData.put("journey_end_date", tvEndDate.getText().toString());
                myData.put("journey_location_from", tietxt_location_from.getText().toString());
                myData.put("journey_location_to", tietxt_location_to.getText().toString());

                return myData;
            }
        };
        App.getInstance().addToRequestQueue(request);
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

    public void setJourneyStartDateTime(View view) {
        datePicker(tvStartDate);

    }

    public void setJourneyEndDateTime(View view) {
        datePicker(tvEndDate);
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
                        //   dateTime = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        dateTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

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
                        mTime = hourOfDay + ":" + minute + " " + format;
                        textViewTime.setText(dateTime + "    " + hourOfDay + ":" + minute + " " + format);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}

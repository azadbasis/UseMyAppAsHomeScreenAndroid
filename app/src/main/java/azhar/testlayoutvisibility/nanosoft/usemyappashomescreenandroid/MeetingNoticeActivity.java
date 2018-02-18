package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.adapter.OffecialBeanAdapter;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.OfficialBean;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.App;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.Operation;

public class MeetingNoticeActivity extends AppCompatActivity {

    ArrayList<OfficialBean> officialBeanArrayList;
    private RecyclerView recyclerViewOffecial;
//    public static final String JSON_USER_URL = "http://192.168.0.115/sreda_api/users/";
    public static final String JSON_URL = "http://sreda.gov.bd/sreda_api/users";
    private ArrayList<String> mEntries;
    OffecialBeanAdapter offecialBeanAdapter;
    EditText etmYEmailID, etMeetingAddress, etChairedBy, etMeetingSubject, etMeetingDiscussion,etsignatureBy;
    TextView tvMeetingDateTime,tvMeetingDate,tvStartTime,tvEndTime;
    String date_time = "";
    int mYear, mMonth, mDay, mHour, mMinute;
    int cYear, cMonth, cDay, cHour, cMinute;
    String dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_notice_fragment);
        recyclerViewOffecial = (RecyclerView) findViewById(R.id.recyclerViewOffecial);
        mEntries = new ArrayList<>();
        officialBeanArrayList = new ArrayList<>();
        volleyJsonArrayRequest();
        init();

    }

    private void init() {
        etmYEmailID = (EditText) findViewById(R.id.etmYEmailID);
        etMeetingAddress = (EditText) findViewById(R.id.etMeetingAddress);
        etChairedBy = (EditText) findViewById(R.id.etChairedBy);
        etsignatureBy = (EditText) findViewById(R.id.etsignatureBy);
        etMeetingSubject = (EditText) findViewById(R.id.etMeetingSubject);
        etMeetingDiscussion = (EditText) findViewById(R.id.etMeetingDiscussion);
        tvMeetingDateTime = (TextView) findViewById(R.id.tvMeetingDateTime);
        tvMeetingDate = (TextView) findViewById(R.id.tvMeetingDate);
        tvStartTime = (TextView) findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) findViewById(R.id.tvEndTime);
        DateFormat dateFormatter = new SimpleDateFormat("dd-M-yyyy hh:mm");
        dateFormatter.setLenient(false);
        Date today = new Date();
        dateTime = dateFormatter.format(today);
        tvMeetingDateTime.setText(dateTime);
        //tvMeetingDate.setText(dateTime);


    }

    private void volleyStringRequest() {

        StringRequest stringRequest = new StringRequest(JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Result", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MeetingNoticeActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void volleyJsonArrayRequest() {

        JsonArrayRequest arrayRequest = new JsonArrayRequest(JSON_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Array request", response.toString());
                        JSONObject jsonObject;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                jsonObject = response.getJSONObject(i);
                              //  mEntries.add(jsonObject.toString());
                                String name = jsonObject.getString("employee_name");
                                String email = jsonObject.getString("employee_email");
                                String phone = jsonObject.getString("employee_phone");
                                String mobile = jsonObject.getString("employee_mobile");
                                String image = jsonObject.getString("employee_image");
                                String designation = jsonObject.getString("employee_designation");
                                //  Boolean isSelected=true;
                                OfficialBean officialBean = new OfficialBean(image, name, designation, email, phone, mobile);
                                officialBeanArrayList.add(officialBean);


                                RecyclerView.LayoutManager mLayoutManager;
                                mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                                recyclerViewOffecial.setLayoutManager(mLayoutManager);
                                offecialBeanAdapter = new OffecialBeanAdapter(MeetingNoticeActivity.this, officialBeanArrayList);

                                recyclerViewOffecial.setAdapter(offecialBeanAdapter);
//                                Toast.makeText(getApplicationContext(), "JSON_OBJECT" + name + "\n" + email + "\n" + phone + "\n" + mobile + "\n" + image + "\n" + designation, Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MeetingNoticeActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

       // RequestQueue requestQueue = Volley.newRequestQueue(App.getAppContext());
      //  requestQueue.add(arrayRequest);
        App.getInstance().addToRequestQueue(arrayRequest);

    }

    @SuppressLint("LongLogTag")
    public void sendEmail() {
        Log.i("Send email", "");

        Operation operation = new Operation(this);
        String myEmail = operation.getString("filteredEmail", "") + "," + etmYEmailID.getText().toString();

        String subject = etMeetingSubject.getText().toString();
        String heading = etChairedBy.getText().toString();
        String discussion = etMeetingDiscussion.getText().toString();

        String mailbody= "Hi dear,"+ "\n"+"You are requested to attend this meeting" +
                " chair by "+heading+" about "+subject+" will held ,at "+etMeetingAddress.getText().toString()
                +"on "+tvMeetingDate.getText().toString()+" from "+tvStartTime.getText().toString()
                +" to "+tvEndTime.getText().toString()+"."+"\n"+"\n"+"Best Regards"+"\n"
                +etsignatureBy.getText().toString();
        String discussionForm = heading + "\n" + "\n" + discussion;
        String[] TO = {myEmail};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mailbody);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MeetingNoticeActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


    public void sendEmail(View view) {
        sendEmail();
    }

    public void setMeetingDate(View view) {
        datePicker();
    }

    public void setMeetingStartTime(View view) {
        tiemPicker(tvStartTime);
    }

    public void setMeetingEndTime(View view) {
        tiemPicker(tvEndTime);
    }
    private void datePicker() {

// Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                       // date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
//*************Call Time Picker Here ********************

                        cYear = year;
                        cMonth = monthOfYear + 1;
                        cDay = dayOfMonth;
                        tvMeetingDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        //tiemPicker();
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void tiemPicker(final TextView tvTime) {

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
                        //dateTime = hourOfDay + ":" + minute + " " + format;

                        tvTime.setText(hourOfDay + ":" + minute + " " + format);

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }
}

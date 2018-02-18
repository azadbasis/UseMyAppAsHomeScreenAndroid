package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.AppConstant;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.Operation;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.PersistData;

/**
 * Created by NanoSoft on 1/29/2018.
 */

public class LeaveActivity extends AppCompatActivity {
    Context con;

    private Spinner spinnerLeaveType;
    private Button btnSendLeave;
    private EditText etCurrentBalance,etFromDate,etToDate,etNumberOfday,etPurpose,etEmergencyContact;
    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;
    int maxDay;
    int minDay;
    String type_id,currntbalace,fromdate,todate,numberofdays,purpose,emergencycontact;
    Operation operation = new Operation(con);
    private int fromDay=0;
    private int toDay=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_application);
        con=this;
        initUi();

    }

    private void initUi() {
        AppConstant.leaveTypeName.add(0,"select Leave Type.");

        operation.getLeaveType();

        btnSendLeave = (Button)findViewById(R.id.btnSendLeave);
        etCurrentBalance = (EditText)findViewById(R.id.etToDate);
        etFromDate = (EditText)findViewById(R.id.etFromDate);
        etToDate = (EditText)findViewById(R.id.etToDate);
        etNumberOfday = (EditText)findViewById(R.id.etNumberOfday);
        etPurpose = (EditText)findViewById(R.id.etPurpose);
        etEmergencyContact = (EditText)findViewById(R.id.etEmergencyContact);
        spinnerLeaveType = (Spinner)findViewById(R.id.spinnerLeaveType);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, AppConstant.leaveTypeName);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLeaveType.setAdapter(dataAdapter);
        spinnerLeaveType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String typeName = spinnerLeaveType.getSelectedItem().toString();

                if(position>0){
                    for(int i=0; i<AppConstant.liveTypeList.size();i++){

                        if(AppConstant.liveTypeList.get(i).getName().equalsIgnoreCase(typeName)){
                            type_id = AppConstant.liveTypeList.get(i).getId();
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerFrom();

            }
        });

        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerTo();
            }
        });

        btnSendLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = Calendar.getInstance().getTime();
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyy");
                String today = formatter.format(date);

                fromdate = etFromDate.getText().toString();
                todate = etToDate.getText().toString();
                numberofdays = etNumberOfday.getText().toString();
                purpose = etPurpose.getText().toString();
                emergencycontact = etEmergencyContact.getText().toString();
                if(TextUtils.isEmpty(type_id)){
                    Toast.makeText(con, "Select leave type.", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(fromdate)){
                    Toast.makeText(con, "Insert from date.", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(todate)){
                    Toast.makeText(con, "Insert to date.", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(numberofdays)){
                    Toast.makeText(con, "Insert number of days.", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(purpose)){
                    Toast.makeText(con, "Insert leave purpose.", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(emergencycontact)){
                    Toast.makeText(con, "Insert emergency contact.", Toast.LENGTH_SHORT).show();
                }else {
                    operation.sendLeaveApplication(PersistData.getStringData(con,AppConstant.employee_id),type_id,fromdate,
                            todate,numberofdays, purpose,
                            emergencycontact,today);
                }

            }
        });


    }


    private void datePickerFrom(){

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        etFromDate.setText(date_time);
                        fromdate = date_time;
                        if(!TextUtils.isEmpty(fromdate) && !TextUtils.isEmpty(todate)){
                            calculateDay(todate,fromdate);
                        }else {
                           etNumberOfday.setText("");
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void datePickerTo(){

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        etToDate.setText(date_time);
                        todate = date_time;

                        if(!TextUtils.isEmpty(fromdate) && !TextUtils.isEmpty(todate)){
                            calculateDay(todate,fromdate);
                        }else {
                            etNumberOfday.setText("");
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }



    private void calculateDay(String dayTo, String dayFrom){
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
//        String inputString1 = "23 01 1997";
//        String inputString2 = "27 04 1997";

        try {
            Date date1 = myFormat.parse(dayFrom);
            Date date2 = myFormat.parse(dayTo);

            if(date2.getTime()>date1.getTime()){
                long diff = date2.getTime() - date1.getTime();
                int days = (int) (diff / (1000*60*60*24));
                numberofdays = String.valueOf(days);
                etNumberOfday.setText(numberofdays);
            }else {
                numberofdays = "";
                etNumberOfday.setText(numberofdays);
                etToDate.setText("");
                Toast.makeText(con, "To date should be after from date", Toast.LENGTH_SHORT).show();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}

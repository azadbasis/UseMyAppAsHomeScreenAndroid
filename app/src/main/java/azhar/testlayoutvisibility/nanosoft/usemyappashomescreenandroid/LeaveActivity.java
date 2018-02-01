package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by NanoSoft on 1/29/2018.
 */

public class LeaveActivity extends AppCompatActivity {
    Context con;

    private List<String> listLeaveType = new ArrayList<>();
    private Spinner spinnerLeaveType;
    private Button btnSendLeave;
    private EditText etCurrentBalance,etFromDate,etToDate,etNumberOfday,etPurpose,etEmergencyContact;
    String date_time = "";
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_application);
        con=this;
        initUi();

    }

    private void initUi() {

        btnSendLeave = (Button)findViewById(R.id.btnSendLeave);
        etCurrentBalance = (EditText)findViewById(R.id.etToDate);
        etFromDate = (EditText)findViewById(R.id.etFromDate);
        etToDate = (EditText)findViewById(R.id.etToDate);
        etNumberOfday = (EditText)findViewById(R.id.etNumberOfday);
        etPurpose = (EditText)findViewById(R.id.etPurpose);
        etEmergencyContact = (EditText)findViewById(R.id.etEmergencyContact);
        spinnerLeaveType = (Spinner)findViewById(R.id.spinnerLeaveType);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listLeaveType);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLeaveType.setAdapter(dataAdapter);
        spinnerLeaveType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // if()
            }
        });


        etFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(etFromDate);
            }
        });

        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker(etToDate);
            }
        });

        btnSendLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }


    private void datePicker(final EditText etdate){

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        etdate.setText(date_time);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


}

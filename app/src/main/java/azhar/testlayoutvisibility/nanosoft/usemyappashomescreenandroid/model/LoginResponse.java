package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NanoSoft on 1/21/2018.
 */

public class LoginResponse {

    private String status_code;

    public String getStatus_code() {
        return status_code;
    }

    private EmployeeInfo employee_info = new EmployeeInfo();

    public EmployeeInfo getEmployee_info() {
        return employee_info;
    }

    public void setEmployee_info(EmployeeInfo employee_info) {
        this.employee_info = employee_info;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    private List<ScheduleEvents> events = new ArrayList<>();

    public List<ScheduleEvents> getEvents() {
        return events;
    }

    public void setEvents(List<ScheduleEvents> events) {
        this.events = events;
    }
}

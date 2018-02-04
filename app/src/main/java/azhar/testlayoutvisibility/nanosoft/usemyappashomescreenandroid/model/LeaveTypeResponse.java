package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NanoSoft on 2/4/2018.
 */

public class LeaveTypeResponse {

    private List<LeaveTypeInfo> leave_types = new ArrayList<>();

    public List<LeaveTypeInfo> getLeave_types() {
        return leave_types;
    }

    public void setLeave_types(List<LeaveTypeInfo> leave_types) {
        this.leave_types = leave_types;
    }
}

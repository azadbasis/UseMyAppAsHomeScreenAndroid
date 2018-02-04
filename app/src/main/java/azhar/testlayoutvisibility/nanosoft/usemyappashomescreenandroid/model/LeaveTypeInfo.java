package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model;

/**
 * Created by NanoSoft on 2/4/2018.
 */

public class LeaveTypeInfo {


//            "id": "9",
//                    "name": "Earn Leave with Full Pay",
//                    "num_of_days": "33",
//                    "carry_forward": "1",
//                    "status": "1"

    private String id,name,num_of_days,status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum_of_days() {
        return num_of_days;
    }

    public void setNum_of_days(String num_of_days) {
        this.num_of_days = num_of_days;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

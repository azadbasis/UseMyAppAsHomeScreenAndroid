package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model;

/**
 * Created by Nanosoft-Android on 1/30/2018.
 */

public class RoomNameInfo {

    /*"meeting_room": [
    {
      "id": "15",
      "": "1001",
      "": "Mohananda",
      "": "2016-10-15 15:26:22",
      "": "3",
      "": "0000-00-00 00:00:00",
      "": "0",
      "": "1",
      "": "20000"*/

    private String id;
    private String room_number;
    private String room_title;
    private String create_date;
    private String create_by;
    private String update_by;
    private String status;
    private String amount;

    public RoomNameInfo(String id, String room_number, String room_title, String create_date, String create_by, String update_by, String status, String amount) {
        this.id = id;
        this.room_number = room_number;
        this.room_title = room_title;
        this.create_date = create_date;
        this.create_by = create_by;
        this.update_by = update_by;
        this.status = status;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getRoom_title() {
        return room_title;
    }

    public void setRoom_title(String room_title) {
        this.room_title = room_title;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

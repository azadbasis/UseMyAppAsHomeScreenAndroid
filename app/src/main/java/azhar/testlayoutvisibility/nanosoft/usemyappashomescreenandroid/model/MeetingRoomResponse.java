package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NanoSoft on 1/23/2018.
 */

public class MeetingRoomResponse {

    private List<RoomInfo> meeting_room_booking = new ArrayList<>();

    public List<RoomInfo> getMeeting_room_booking() {
        return meeting_room_booking;
    }

    public void setMeeting_room_booking(List<RoomInfo> meeting_room_booking) {
        this.meeting_room_booking = meeting_room_booking;
    }
}

package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.R;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.MeetingRoomResponse;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.RoomInfo;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.ScheduleEvents;


public class RoomCheckAdapter extends RecyclerView.Adapter<RoomCheckAdapter.MyChatViewHolder> {

    private List<RoomInfo> mEvents;

    private Context con;

    @Override
    public long getItemId(int position) {
        return position;
    }

    public RoomCheckAdapter(Context con, List<RoomInfo> mEvents) {
        this.con = con;
        this.mEvents = mEvents;
    }

    @Override
    public MyChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View rootView = LayoutInflater.from(con).inflate(R.layout.item_check_room, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new MyChatViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(MyChatViewHolder holder, int position) {
        configureMyChatViewHolder((RoomCheckAdapter.MyChatViewHolder) holder, position);
    }


    @Override
    public int getItemCount() {
        if (mEvents != null) {
            return mEvents.size();
        }
        return 0;
    }
    private void configureMyChatViewHolder(RoomCheckAdapter.MyChatViewHolder myChatViewHolder, int position) {
        RoomInfo event = mEvents.get(position);
//        String[] partstot = event.getTo_time().split(" ");
//        String[] partsfrom = event.getFrom_time().split(" ");
//
//        String fromtime = partsfrom[1];
//        String totime = partstot[1];

        myChatViewHolder.tvRoomName.setText(event.getRoom_title());
        myChatViewHolder.tvFromDateTime.setText(event.getBooking_start_time());
        myChatViewHolder.tvToDateTime.setText(event.getBooking_end_time());



    }

   class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRoomName, tvFromDateTime,tvToDateTime;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            tvRoomName = (TextView) itemView.findViewById(R.id.tvRoomName);
            tvFromDateTime = (TextView) itemView.findViewById(R.id.tvFromDateTime);
            tvToDateTime = (TextView) itemView.findViewById(R.id.tvToDateTime);
        }
    }


}

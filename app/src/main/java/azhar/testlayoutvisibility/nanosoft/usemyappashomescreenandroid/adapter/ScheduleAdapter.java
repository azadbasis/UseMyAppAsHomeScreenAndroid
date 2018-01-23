package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.R;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.database.Events;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.Chat;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.ScheduleEvents;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.MyChatViewHolder> {

    private List<ScheduleEvents> mEvents;

    private Context con;

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ScheduleAdapter(Context con,List<ScheduleEvents> mEvents) {
        this.con = con;
        this.mEvents = mEvents;
    }

    @Override
    public MyChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {






        View rootView = LayoutInflater.from(con).inflate(R.layout.raw_schedule, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootView.setLayoutParams(lp);
        return new MyChatViewHolder(rootView);

//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        RecyclerView.ViewHolder viewHolder = null;
//        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        View viewChatMine = layoutInflater.inflate(R.layout.raw_schedule, null, false);
//        viewChatMine.setLayoutParams(lp);
//        viewHolder = new MyChatViewHolder(viewChatMine);
//        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyChatViewHolder holder, int position) {
        configureMyChatViewHolder((ScheduleAdapter.MyChatViewHolder) holder, position);
    }


    @Override
    public int getItemCount() {
        if (mEvents != null) {
            return mEvents.size();
        }
        return 0;
    }
    private void configureMyChatViewHolder(ScheduleAdapter.MyChatViewHolder myChatViewHolder, int position) {
        ScheduleEvents event = mEvents.get(position);
        String[] partstot = event.getTo_time().split(" ");
        String[] partsfrom = event.getFrom_time().split(" ");

        String fromtime = partsfrom[1];
        String totime = partstot[1];

        myChatViewHolder.tvSchTitle.setText(event.getTitle());
        myChatViewHolder.tvFromTime.setText(fromtime);
        myChatViewHolder.tvToTime.setText(totime);



    }

   class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSchTitle, tvFromTime,tvToTime;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            tvSchTitle = (TextView) itemView.findViewById(R.id.tvSchTitle);
            tvFromTime = (TextView) itemView.findViewById(R.id.tvFromTime);
            tvToTime = (TextView) itemView.findViewById(R.id.tvToTime);
        }
    }


}

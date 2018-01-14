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


public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Events> mEvents;

    private Context con;

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ScheduleAdapter(Context con,List<Events> mEvents) {
        this.con = con;
        this.mEvents = mEvents;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        View viewChatMine = layoutInflater.inflate(R.layout.raw_schedule, parent, false);
        viewHolder = new MyChatViewHolder(viewChatMine);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
        Events event = mEvents.get(position);

        myChatViewHolder.tvSchTitle.setText(event.getEventTitle());
        myChatViewHolder.tvScTime.setText(event.getEventTime());
        myChatViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSchTitle, tvScTime;
        private ImageView imgDelete;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            tvSchTitle = (TextView) itemView.findViewById(R.id.tvSchTitle);
            tvScTime = (TextView) itemView.findViewById(R.id.tvScTime);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
        }
    }


}

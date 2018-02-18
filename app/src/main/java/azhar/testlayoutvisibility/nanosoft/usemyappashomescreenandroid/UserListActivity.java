package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.MainActivityChat;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.User;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.AppConstant;

public class UserListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    Context con;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewAllUserListing;
    private List<User> users = new ArrayList<>();
    private UserListingRecyclerAdapter mUserListingRecyclerAdapter;
    private ImageView imgGoBack;
    private TextView tvBtnGo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        con=this;
        AppConstant.userListSend.clear();
        initialize();
    }

    private void initialize() {
        tvBtnGo = (TextView)findViewById(R.id.tvBtnGo) ;

        tvBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(con,MainActivityChat.class));
            }
        });

        imgGoBack = (ImageView)findViewById(R.id.imgGoBack);
        imgGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerViewAllUserListing = (RecyclerView) findViewById(R.id.recycler_view_all_user_listing);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerViewAllUserListing.addItemDecoration(new DividerItemDecoration(con, DividerItemDecoration.VERTICAL));

        mUserListingRecyclerAdapter = new UserListingRecyclerAdapter(users);
        LinearLayoutManager llm = new LinearLayoutManager(con);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerViewAllUserListing.setLayoutManager(llm);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        getAllUsers();
    }

    private void  getAllUsers() {
        users.clear();
        FirebaseDatabase.getInstance().getReference().child(AppConstant.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    User user = dataSnapshotChild.getValue(User.class);
                    if (!TextUtils.equals(user.uid, FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        users.add(user);
                        for (int i = 0; i < users.size(); i++) {
                        }

                    }
                }

                Log.e("User size",""+users.size());
                mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
                mUserListingRecyclerAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    @Override
    public void onRefresh() {

        getAllUsers();
    }

    public class UserListingRecyclerAdapter extends RecyclerView.Adapter<UserListingRecyclerAdapter.ViewHolder> {
        private List<User> mUsers;

        public UserListingRecyclerAdapter(List<User> users) {
            this.mUsers = users;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_user, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final User user = mUsers.get(position);

            if(!TextUtils.isEmpty(user.name)){
                String alphabet = user.name;
//            holder.txtUserAlphabet.setText(alphabet);
                holder.tvUserName.setText(alphabet);
            }

            if(holder.checkbox.isChecked()){
                AppConstant.userListSend.add(user);
                AppConstant.registraion_ids.add(user.uid);
            }

            holder.reFullMainView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConstant.user_receiver = user;

                    AppConstant.ARG_RECEIVER = user.name;
                    AppConstant.ARG_RECEIVER_FIREBASE_TOKEN = user.firebaseToken;
                    AppConstant.ARG_RECEIVER_UID = user.uid;
                    startActivity(new Intent(con,MainActivityChat.class));
                }
            });
        }

        @Override
        public int getItemCount() {
            if (mUsers != null) {
                return mUsers.size();
            }
            return 0;
        }

        public User getUser(int position) {
            return mUsers.get(position);
        }

         class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvUserName;
            private CheckBox checkbox;
             RelativeLayout reFullMainView;

            ViewHolder(View itemView) {
                super(itemView);
                tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
                reFullMainView = (RelativeLayout)itemView.findViewById(R.id.reFullMainView);
//                txtUsername = (TextView) itemView.findViewById(R.id.text_view_username);
                checkbox = (CheckBox)itemView.findViewById(R.id.checkbox);
            }
        }
    }


}

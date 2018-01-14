package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.adapter.ChatRecyclerAdapter;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.fcm.FcmNotificationBuilder;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.Chat;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.User;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.AppConstant;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.PersistData;

public class ChatActivity extends AppCompatActivity {

    Context con;
    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;
    private TextView tvLogout;
    private Chat chatMsg = new Chat();
    private ProgressDialog mProgressDialog;
    private List <Chat> lsitChat = new ArrayList<>();
    private ChatRecyclerAdapter mChatRecyclerAdapter;
    private ImageView imgsend,imgGoBack;
    private List<String> registraion_ids = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        con = this;

        initialize();
    }

    private void initialize() {
        imgsend = (ImageView)findViewById(R.id.imgsend);
        imgGoBack = (ImageView)findViewById(R.id.imgGoBack);
        imgGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRecyclerViewChat = (RecyclerView) findViewById(R.id.recycler_view_chat);
        mETxtMessage = (EditText) findViewById(R.id.edit_text_message);
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new ChatRecyclerAdapter(lsitChat);
            LinearLayoutManager llm = new LinearLayoutManager(con);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerViewChat.setLayoutManager(llm);
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }


        getMessageFromFirebaseUser(PersistData.getStringData(con,AppConstant.uid), AppConstant.user_receiver.uid);
        imgsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mETxtMessage.getText().toString())){
                    String sender= PersistData.getStringData(con,AppConstant.userName);
                    String receiver= AppConstant.ARG_RECEIVER;
                    String senderUid = PersistData.getStringData(con,AppConstant.uid);
                    String receiverUid = AppConstant.ARG_RECEIVER_UID;
                    String receiverFirebaseToken = AppConstant.ARG_RECEIVER_FIREBASE_TOKEN;
                    String message = mETxtMessage.getText().toString();

                    sendMessageToFirebaseUser(new Chat(sender,receiver,senderUid,receiverUid,message),receiverFirebaseToken);
                    mETxtMessage.setText("");
                }else {
                    Toast.makeText(con,"Write something",Toast.LENGTH_SHORT).show();
                    mETxtMessage.setFocusable(true);
                }

            }
        });

    }


    public void getMessageFromFirebaseUser(String senderUid, String receiverUid) {
        final String room_type_1 = senderUid + "_" + receiverUid;
        final String room_type_2 = receiverUid + "_" + senderUid;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(AppConstant.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e("Room", "getMessageFromFirebaseUser: " + room_type_1 + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(AppConstant.ARG_CHAT_ROOMS)
                            .child(room_type_1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                            Chat chat = dataSnapshot.getValue(Chat.class);
                            lsitChat.add(chat);
                            mChatRecyclerAdapter.notifyDataSetChanged();
                           // chatMsg = chat;
//                            mChatRecyclerAdapter.add(chat);
                            mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                           // mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e("Room", "getMessageFromFirebaseUser: " + room_type_2 + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child(AppConstant.ARG_CHAT_ROOMS)
                            .child(room_type_2).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                           // chatMsg = chat;
                           // mChatRecyclerAdapter.add(chat);
                            lsitChat.add(chat);
                            mChatRecyclerAdapter.notifyDataSetChanged();
                            mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                } else {
                    Log.e("Room", "getMessageFromFirebaseUser: no such room available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
            }
        });
    }

    public void sendMessageToFirebaseUser(final Chat chat, final String receiverFirebaseToken) {
        final String room_type_1 = chat.senderUid + "_" + chat.receiverUid;
        final String room_type_2 = chat.receiverUid + "_" + chat.senderUid;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child(AppConstant.ARG_CHAT_ROOMS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e("", "sendMessageToFirebaseUser: " + room_type_1 + " exists");
                    databaseReference.child(AppConstant.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(Calendar.getInstance().getTime())).setValue(chat);
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e("", "sendMessageToFirebaseUser: " + room_type_2 + " exists");
                    databaseReference.child(AppConstant.ARG_CHAT_ROOMS).child(room_type_2).child(String.valueOf(Calendar.getInstance().getTime())).setValue(chat);
                } else {
                    Log.e("", "sendMessageToFirebaseUser: success");
                    databaseReference.child(AppConstant.ARG_CHAT_ROOMS).child(room_type_1).child(String.valueOf(Calendar.getInstance().getTime())).setValue(chat);
                    getMessageFromFirebaseUser(chat.senderUid, chat.receiverUid);
                }
                // send push notification to the receiver
                sendPushNotificationToReceiverSigle(chat.sender,
                        chat.message,
                        chat.senderUid,
                        PersistData.getStringData(con,AppConstant.fcm_token),
                        receiverFirebaseToken);


//                sendPushNotificationToReceiverMulti(chat.sender,
//                        chat.message,
//                        chat.senderUid,
//                        PersistData.getStringData(con,AppConstant.fcm_token),
//                        AppConstant.registraion_ids);
                //getMessageFromFirebaseUser(chat.senderUid, chat.receiverUid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void sendPushNotificationToReceiverSigle(String username,
                                                String message,
                                                String uid,
                                                String firebaseToken,
                                                String receiverFirebaseToken) {
        FcmNotificationBuilder.initialize()
                .title(username)
                .message(message)
                .username(username)
                .uid(uid)
                .firebaseToken(firebaseToken)
                .receiverFirebaseToken(receiverFirebaseToken)
                .send();
    }

    private void sendPushNotificationToReceiverMulti(String username,
                                                String message,
                                                String uid,
                                                String firebaseToken,
                                                List<String> regId) {
        FcmNotificationBuilder.initialize()
                .title(username)
                .message(message)
                .username(username)
                .uid(uid)
                .firebaseToken(firebaseToken)
                .registrationId(regId)
                .send();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseChatMainApp.setChatActivityOpen(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseChatMainApp.setChatActivityOpen(false);
    }
}

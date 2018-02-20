package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.ChatActivity;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.FirebaseChatMainApp;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.R;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.MainActivityChat;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.AppConstant;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.PersistData;


/**
 * Created by NanoSoft on 7/24/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Context con;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        con = this;

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.e("", "From: " + remoteMessage.getTo());

        if (remoteMessage.getData().size() > 0) {
            Log.e("", "Message data payload: " + remoteMessage.getData());

            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            String userName = remoteMessage.getData().get("userName");
            String uid = remoteMessage.getData().get("uid");
            String fcmToken = remoteMessage.getData().get("fcm_token");

            if((!userName.equalsIgnoreCase(PersistData.getStringData(con,AppConstant.userFcm)))
                    &&(!PersistData.getStringData(con,AppConstant.activity).equalsIgnoreCase("chat"))){
                sendNotification(userName,
                        message
                );
            } else {
                EventBus.getDefault().post(new PushNotificationEvent(title,
                        message,
                        userName,
                        uid,
                        fcmToken));
            }
            // Don't show notification if chat activity is open.
//            if (!FirebaseChatMainApp.isChatActivityOpen()) {
//                sendNotification(title,
//                        message,
//                        userName,
//                        uid,
//                        fcmToken);
//            } else {
//                EventBus.getDefault().post(new PushNotificationEvent(title,
//                        message,
//                        userName,
//                        uid,
//                        fcmToken));
//            }
        }
    }


    private void sendNotification(String sender,
                                  String message) {
        Intent intent = new Intent(this, MainActivityChat.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(sender+" send message.")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendNotificationTest(String title,
                                  String message,
                                  String receiver,
                                  String receiverUid,
                                  String firebaseToken) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(AppConstant.ARG_RECEIVER, receiver);
        intent.putExtra(AppConstant.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(AppConstant.ARG_RECEIVER_FIREBASE_TOKEN, firebaseToken);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}

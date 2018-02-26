package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joanzapata.pdfview.PDFView;

import java.io.File;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.MainActivity;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.R;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.model.ChatModel;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.util.Util;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;

/**
 * Created by Alessandro Barreto on 23/06/2016.
 */
public class ChatFirebaseAdapter extends FirebaseRecyclerAdapter<ChatModel,ChatFirebaseAdapter.MyChatViewHolder> {

    private static final int RIGHT_MSG = 0;
    private static final int LEFT_MSG = 1;
    private static final int RIGHT_MSG_IMG = 2;
    private static final int LEFT_MSG_IMG = 3;
    private Activity activity;

    private ClickListenerChatFirebase mClickListenerChatFirebase;

    private String nameUser;



    public ChatFirebaseAdapter(DatabaseReference ref, String nameUser,ClickListenerChatFirebase mClickListenerChatFirebase,Activity activity) {
        super(ChatModel.class, R.layout.item_message_left, MyChatViewHolder.class, ref);
        this.nameUser = nameUser;
        this.mClickListenerChatFirebase = mClickListenerChatFirebase;
        this.activity = activity;
    }

    public ChatFirebaseAdapter(DatabaseReference ref, String nameUser,ClickListenerChatFirebase mClickListenerChatFirebase) {
        super(ChatModel.class, R.layout.item_message_left, MyChatViewHolder.class, ref);
        this.nameUser = nameUser;
        this.mClickListenerChatFirebase = mClickListenerChatFirebase;

    }
    @Override
    public MyChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == RIGHT_MSG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right,parent,false);
            return new MyChatViewHolder(view);
        }else if (viewType == LEFT_MSG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left,parent,false);
            return new MyChatViewHolder(view);
        }else if (viewType == RIGHT_MSG_IMG){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right_img,parent,false);
            return new MyChatViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left_img,parent,false);
            return new MyChatViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatModel model = getItem(position);
        if (model.getMapModel() != null){
            if (model.getUserModel().getName().equals(nameUser)){
                return RIGHT_MSG_IMG;
            }else{
                return LEFT_MSG_IMG;
            }
        }else if (model.getFile() != null){
            if (model.getFile().getType().equals("img") && model.getUserModel().getName().equals(nameUser)){
                return RIGHT_MSG_IMG;
            }else{
                return LEFT_MSG_IMG;
            }
        }else if (model.getUserModel().getName().equals(nameUser)){
            return RIGHT_MSG;
        }else{
            return LEFT_MSG;
        }
    }

    @Override
    protected void populateViewHolder(MyChatViewHolder viewHolder, ChatModel model, int position) {
        viewHolder.setIvUser(model.getUserModel().getPhoto_profile());
        viewHolder.setTxtMessage(model.getMessage());
        viewHolder.setTvTimestamp(model.getTimeStamp());
        viewHolder.tvIsLocation(View.GONE);
        if (model.getFile() != null){
            viewHolder.fileName.setText(model.getFile().getName_file()+"");
            viewHolder.tvIsLocation(View.GONE);
            String filename = model.getFile().getName_file();
            if(filename.contains(".pdf")||filename.contains(".txt")
                        ||filename.contains(".xls")||filename.contains(".xlsx")
                        ||filename.contains(".xls")||filename.contains(".doc")
                        ||filename.contains(".docx")) {
                viewHolder.setIvChatFilePhoto();
            }else {
                viewHolder.setIvChatPhoto(model.getFile().getUrl_file());
            }

        }else if(model.getMapModel() != null){
            viewHolder.setIvChatPhoto(Util.local(model.getMapModel().getLatitude(),model.getMapModel().getLongitude()));
            viewHolder.tvIsLocation(View.VISIBLE);
        }
    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTimestamp,tvLocation,fileName;
        EmojiconTextView txtMessage;
        ImageView ivUser,ivChatPhoto;
        PDFView pdfView;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            tvTimestamp = (TextView)itemView.findViewById(R.id.timestamp);
            fileName = (TextView)itemView.findViewById(R.id.fileName);
            txtMessage = (EmojiconTextView)itemView.findViewById(R.id.txtMessage);
            tvLocation = (TextView)itemView.findViewById(R.id.tvLocation);
            ivChatPhoto = (ImageView)itemView.findViewById(R.id.img_chat);
            pdfView = (PDFView) itemView.findViewById(R.id.pdfview);
            ivUser = (ImageView)itemView.findViewById(R.id.ivUserChat);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ChatModel model = getItem(position);
            String downloadUrl = model.getFile().getUrl_file();
//            String fileType = model.getFile().getType();
//            //String filename = model.getFile().getName_file();
//            Log.e("fileType",""+fileType);
//            if (model.getMapModel() != null){
//                mClickListenerChatFirebase.clickImageMapChat(view,position,model.getMapModel().getLatitude(),model.getMapModel().getLongitude());
//            }else{
//                mClickListenerChatFirebase.clickImageChat(view,position,model.getUserModel().getName(),model.getUserModel().getPhoto_profile(),model.getFile().getUrl_file());
//            }



            if (model.getFile() != null){
//                String filename = model.getFile().getName_file();
//                if(filename.contains(".pdf")||filename.contains(".txt")
//                        ||filename.contains(".xls")||filename.contains(".xlsx")
//                        ||filename.contains(".xls")||filename.contains(".doc")
//                        ||filename.contains(".docx")) {

//                    if(filename.contains(".doc")
//                            ||filename.contains(".docx")){
//                        Intent intent = new Intent();
//                        intent.setDataAndType(Uri.parse(downloadUrl), "application/msword");
//                        activity.startActivity(new Intent(intent.ACTION_VIEW,Uri.parse(downloadUrl)));
//                    }

                    activity.startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(downloadUrl)));

               //}

//                else {
//                    mClickListenerChatFirebase.clickImageChat(view,position,model.getUserModel().getName(),model.getUserModel().getPhoto_profile(),model.getFile().getUrl_file());
//                }
            }

        }

        public void setTxtMessage(String message){
            if (txtMessage == null)return;
            txtMessage.setText(message);
        }


        public void setIvUser(String urlPhotoUser){
            if (ivUser == null)return;
            Glide.with(ivUser.getContext()).load(urlPhotoUser).centerCrop().transform(new CircleTransform(ivUser.getContext())).override(40,40).into(ivUser);
        }

        public void setTvTimestamp(String timestamp){
            if (tvTimestamp == null)return;
            tvTimestamp.setText(converteTimestamp(timestamp));
        }

        public void setIvChatPhoto(String url){
            if (ivChatPhoto == null)return;
            Glide.with(ivChatPhoto.getContext()).load(url)
                    .override(100, 100)
                    .fitCenter()
                    .into(ivChatPhoto);
            ivChatPhoto.setOnClickListener(this);
        }

        public void setIvChatFilePhoto(){
            if (ivChatPhoto == null)return;
            ivChatPhoto.setOnClickListener(this);
        }

        public void setIvChatPdf(String url){
            pdfView.fromAsset(url)
                    .pages(0, 2, 1, 3, 3, 3)
                    .defaultPage(1)
                    .showMinimap(false)
                    .enableSwipe(true)
                    .load();
        }


        public void tvIsLocation(int visible){
            if (tvLocation == null)return;
            tvLocation.setVisibility(visible);
        }

    }

    private CharSequence converteTimestamp(String mileSegundos){
        return DateUtils.getRelativeTimeSpanString(Long.parseLong(mileSegundos),System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }


    //Tuturial
    //https://code.tutsplus.com/tutorials/firebase-for-android-file-storage--cms-27376

    private void downloadFile( String url) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl(url);
        StorageReference  islandRef = storageRef.child("file.txt");

        File rootPath = new File(Environment.getExternalStorageDirectory(), "firebasefile");
        if(!rootPath.exists()) {
            rootPath.mkdirs();
        }

        final File localFile = new File(rootPath,"imageName.txt");

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Log.e("firebase ",";local tem file created  created " +localFile.toString());
                //  updateDb(timestamp,localFile.toString(),position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("firebase ",";local tem file not created  created " +exception.toString());
            }
        });
    }
}

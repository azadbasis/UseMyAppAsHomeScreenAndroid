package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.BuildConfig;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.LeaveActivity;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.MainActivity;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.MeetingMinutesActivity;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.MeetingNoticeActivity;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.MeetingRoomBookingActivity;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.R;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.fcm.FcmNotificationBuilder;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.adapter.ChatFirebaseAdapter;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.adapter.ClickListenerChatFirebase;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.model.ChatModel;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.model.FileModel;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.model.MapModel;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.model.UserModel;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.util.Util;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.view.FullScreenImageActivity;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.view.LoginActivityChat;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.User;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.AppConstant;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.PersistData;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.ReadAllFile;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class MainActivityChat extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, ClickListenerChatFirebase {

    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    private static final int PLACE_PICKER_REQUEST = 3;

    static final String TAG = MainActivityChat.class.getSimpleName();
    static final String CHAT_REFERENCE = "chatmodel";
    final static int PICK_PDF_CODE = 2342;;

    //Firebase and GoogleApiClient
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mFirebaseDatabaseReference;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    Context con;
    //CLass Model
    private UserModel userModel;
    String PathHolder;
    //Views UI
    private RecyclerView rvListMessage;
    private LinearLayoutManager mLinearLayoutManager;
    private ImageView btSendMessage,btEmoji,buttonFileAttach;
    private EmojiconEditText edMessage;
    private View contentRoot;
    private EmojIconActions emojIcon;
    String email;
    String matchEmail = "";
    List<UserModel> userList = new ArrayList<>();

    //File
    private File filePathImageCamera;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        con = this;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();



        if (!Util.verificaConexao(this)){
            Util.initToast(this,"Você não tem conexão com internet");
            finish();
        }else{
            bindViews();
            verificaUsuarioLogado();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();
        }

        initToolBar();
    }



    private void  getAllUsers() {
        AppConstant.fUserList.clear();
        AppConstant.registraion_ids.clear();
        FirebaseDatabase.getInstance().getReference().child(AppConstant.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    UserModel user = dataSnapshotChild.getValue(UserModel.class);
                    AppConstant.fUserList.add(user);
                    AppConstant.registraion_ids.add(user.getUserId());
                    //Toast.makeText(con, "userid "+user.getUserId(), Toast.LENGTH_SHORT).show();
                }

               // Toast.makeText(con, "userid"+AppConstant.registraion_ids.get(0), Toast.LENGTH_SHORT).show();


                UserModel   userModel1 = new UserModel(mFirebaseUser.getDisplayName(), mFirebaseUser.getUid(), mFirebaseUser.getEmail(),PersistData.getStringData(con, AppConstant.fcm_token));

                for (int i = 0; i <AppConstant.fUserList.size() ; i++) {
                    if(AppConstant.fUserList.get(i).getEmail().equalsIgnoreCase(mFirebaseUser.getEmail())){

                        matchEmail = AppConstant.fUserList.get(i).getEmail();
                        break;
                    }
                }

               // Toast.makeText(con, ""+AppConstant.fUserList.get(0).getEmail(), Toast.LENGTH_SHORT).show();
                if(!matchEmail.equalsIgnoreCase(mFirebaseUser.getEmail())){
                    mFirebaseDatabaseReference.child("users").push().setValue(userModel1);
                }
                email = mFirebaseUser.getEmail();



//                TinyDB tinydb = new TinyDB(con);
//                tinydb.putListString("MyUsers", AppConstant.fUserList);
                // tinydb.putList("MyUsers", AppConstant.fUserList);

//                for (int i = 0; i <AppConstant.fUserList.size() ; i++) {
//                    if(!AppConstant.fUserList.get(i).getEmail().equalsIgnoreCase(mFirebaseUser.getEmail())){
//                        mFirebaseDatabaseReference.child("users").push().setValue(userModel);
//                    }
//                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        StorageReference storageRef = storage.getReferenceFromUrl(Util.URL_STORAGE_REFERENCE).child(Util.FOLDER_STORAGE_IMG);

        if (requestCode == IMAGE_GALLERY_REQUEST){
            if (resultCode == RESULT_OK){
                Uri selectedImageUri = data.getData();
                PathHolder = ReadAllFile.getPath(getApplicationContext(), selectedImageUri);
                //Log.e("Path:",PathHolder);
                String lStr = PathHolder;
                lStr = lStr.substring(lStr.lastIndexOf("/"));
                File f = new File(PathHolder);
                Log.e("file name:",f.getName());
                if (selectedImageUri != null){
                    sendFileFirebase(storageRef,selectedImageUri,f.getName());
                }else{
                    //URI IS NULL
                }
            }
        }else if (requestCode == IMAGE_CAMERA_REQUEST){
            if (resultCode == RESULT_OK){
                if (filePathImageCamera != null && filePathImageCamera.exists()){
                    StorageReference imageCameraRef = storageRef.child(filePathImageCamera.getName()+"_camera");
                    sendFileFirebase(imageCameraRef,filePathImageCamera);
                }else{
                    //IS NULL
                }
            }
        }else if (requestCode == PLACE_PICKER_REQUEST){
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                if (place!=null){
                    LatLng latLng = place.getLatLng();
                    MapModel mapModel = new MapModel(latLng.latitude+"",latLng.longitude+"");
                    ChatModel chatModel = new ChatModel(userModel,Calendar.getInstance().getTime().getTime()+"",mapModel);
                    mFirebaseDatabaseReference.child(CHAT_REFERENCE).push().setValue(chatModel);
                }else{
                    //PLACE IS NULL
                }
            }
        }

        switch(requestCode) {

            case 7:

                if (resultCode == RESULT_OK) {

                    Uri uri = data.getData();
                    PathHolder = ReadAllFile.getPath(getApplicationContext(), uri);
                    Log.e("Path:",PathHolder);
                    String lStr = PathHolder;
                    lStr = lStr.substring(lStr.lastIndexOf("/"));

                    sendFileFirebase(storageRef, Uri.fromFile(new File(PathHolder)),lStr);
                }
                break;
        }


        //when the user choses the file
//        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            //if a file is selected
//            if (data.getData() != null) {
//                //uploading the file
//                //uploadFile(data.getData());
//                sendFileFirebase(storageRef,data.getData());
//            } else {
//                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
//            }
//        }
    }








    @Override
    protected void onStart() {
        super.onStart();


    }


    private void initToolBar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("SREDA OFFICE");
        toolbar.inflateMenu(R.menu.menu_logout);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
//                    case R.id.sendPhoto:
//                        verifyStoragePermissions();
////                photoCameraIntent();
//                        break;
//                    case R.id.sendPhotoGallery:
//                        photoGalleryIntent();
//                        break;
//                    case R.id.sendLocation:
//                        locationPlacesIntent();
//                        break;
                    case R.id.sign_out:
                        signOut();

                        return true;
                }
                return false;
            }
        });


    }



    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Util.initToast(this,"Google Play Services error.");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonMessage:
                sendMessageFirebase();
                break;
        }
    }

    @Override
    public void clickImageChat(View view, int position,String nameUser,String urlPhotoUser,String urlPhotoClick) {
        Intent intent = new Intent(this,FullScreenImageActivity.class);
        intent.putExtra("nameUser",nameUser);
        intent.putExtra("urlPhotoUser",urlPhotoUser);
        intent.putExtra("urlPhotoClick",urlPhotoClick);
        startActivity(intent);
    }

    @Override
    public void clickImageMapChat(View view, int position,String latitude,String longitude) {
        String uri = String.format("geo:%s,%s?z=17&q=%s,%s", latitude,longitude,latitude,longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }


    /**
     * Envia o arvquivo para o firebase
     */
    private void sendFileFirebase(StorageReference storageReference, final Uri file,final String name){
        if (storageReference != null){
            //final String name = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
            StorageReference imageGalleryRef = storageReference.child(name);
                UploadTask uploadTask = imageGalleryRef.putFile(file);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"onFailure sendFileFirebase "+e.getMessage());
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.i(TAG,"onSuccess sendFileFirebase");
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        String type = taskSnapshot.getMetadata().getContentType();
                        FileModel fileModel = new FileModel("img",downloadUrl.toString(),name,"");
                        ChatModel chatModel = new ChatModel(userModel,"",Calendar.getInstance().getTime().getTime()+"",fileModel);
                        mFirebaseDatabaseReference.child(CHAT_REFERENCE).push().setValue(chatModel);
                    }
                });
        }else{
            //IS NULL
        }

    }




    /**
     * Envia o arvquivo para o firebase
     */
    private void sendFileFirebase(StorageReference storageReference, final File file){
        if (storageReference != null){
            Uri photoURI = FileProvider.getUriForFile(MainActivityChat.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    file);
            UploadTask uploadTask = storageReference.putFile(photoURI);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG,"onFailure sendFileFirebase "+e.getMessage());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i(TAG,"onSuccess sendFileFirebase");
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    FileModel fileModel = new FileModel("img",downloadUrl.toString(),file.getName(),file.length()+"");
                    ChatModel chatModel = new ChatModel(userModel,"",Calendar.getInstance().getTime().getTime()+"",fileModel);
                    mFirebaseDatabaseReference.child(CHAT_REFERENCE).push().setValue(chatModel);
                }
            });
        }else{
            //IS NULL
        }

    }

    /**
     * Obter local do usuario
     */
    private void locationPlacesIntent(){
        try {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Enviar foto tirada pela camera
     */
    private void photoCameraIntent(){
        String nomeFoto = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();
        filePathImageCamera = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), nomeFoto+"camera.jpg");
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoURI = FileProvider.getUriForFile(MainActivityChat.this,
                BuildConfig.APPLICATION_ID + ".provider",
                filePathImageCamera);
        it.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
        startActivityForResult(it, IMAGE_CAMERA_REQUEST);
    }

    /**
     * Enviar foto pela galeria
     */

    private void pdfFilesend(){

    }

    private void getFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture_title)), 7);
    }

    private void fileIntent(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture_title)), IMAGE_GALLERY_REQUEST);
    }

    //this function will get the pdf from the storage
    private void getPDF() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }


    private void photoGalleryIntent(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture_title)), IMAGE_GALLERY_REQUEST);
    }

    /**
     * Enviar msg de texto simples para chat
     */
    private void sendMessageFirebase(){

        if(TextUtils.isEmpty(edMessage.getText().toString())){
            Toast.makeText(con, "Input your message!", Toast.LENGTH_SHORT).show();
        }else if(!TextUtils.isEmpty(edMessage.getText().toString())){
            yesNoAlert();
        }

    }

    private void yesNoAlert() {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        ChatModel model = new ChatModel(userModel,edMessage.getText().toString(), Calendar.getInstance().getTime().getTime()+"",null);
                        mFirebaseDatabaseReference.child(CHAT_REFERENCE).push().setValue(model);

                        FirebaseMessaging.getInstance().subscribeToTopic("news");
                        sendPushNotificationToReceiverMulti(mFirebaseUser.getDisplayName(),edMessage.getText().toString(),
                                mFirebaseUser.getUid(),PersistData.getStringData(con,AppConstant.fcm_token),AppConstant.registraion_ids);
                        edMessage.setText("");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(con);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
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
                .sendMulti();
    }


    /**
     * Ler collections chatmodel Firebase
     */
    private void lerMessagensFirebase(){
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        final ChatFirebaseAdapter firebaseAdapter = new ChatFirebaseAdapter(mFirebaseDatabaseReference.child(CHAT_REFERENCE),userModel.getName(),this, this);
        firebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    rvListMessage.scrollToPosition(positionStart);
                }
            }
        });
        rvListMessage.setLayoutManager(mLinearLayoutManager);
        rvListMessage.setAdapter(firebaseAdapter);
    }

    /**
     * Verificar se usuario está logado
     */
    private void verificaUsuarioLogado(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser == null){
            startActivity(new Intent(this, LoginActivityChat.class));
            finish();
        }else{
            getAllUsers();

            PersistData.setStringData(con,AppConstant.userFcm,mFirebaseUser.getDisplayName());
            PersistData.setStringData(con,AppConstant.activity,"chat");
          //  Toast.makeText(con, ""+mFirebaseUser.getUid(), Toast.LENGTH_SHORT).show();
            userModel = new UserModel(mFirebaseUser.getDisplayName(),mFirebaseUser.getPhotoUrl().toString(), mFirebaseUser.getUid(), mFirebaseUser.getEmail(),PersistData.getStringData(con, AppConstant.fcm_token));

//            for (int i = 0; i <AppConstant.fUserList.size() ; i++) {
//                if(AppConstant.fUserList.get(i).getEmail().equalsIgnoreCase(mFirebaseUser.getEmail())){
//
//                    matchEmail = AppConstant.fUserList.get(i).getEmail();
//                    break;
//                }
//            }
//
//            Toast.makeText(con, ""+AppConstant.fUserList.get(0).getEmail(), Toast.LENGTH_SHORT).show();
//            if(!matchEmail.equalsIgnoreCase(mFirebaseUser.getEmail())){
//                mFirebaseDatabaseReference.child("users").push().setValue(userModel);
//            }  email = mFirebaseUser.getEmail();


            lerMessagensFirebase();
        }
    }




    /**
     * Vincular views com Java API
     */
    private void bindViews(){
        contentRoot = findViewById(R.id.contentRoot);
        edMessage = (EmojiconEditText)findViewById(R.id.editTextMessage);
        btSendMessage = (ImageView)findViewById(R.id.buttonMessage);
        btSendMessage.setOnClickListener(this);
        btEmoji = (ImageView)findViewById(R.id.buttonEmoji);
        buttonFileAttach = (ImageView)findViewById(R.id.buttonFileAttach);
        buttonFileAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivityChat.this, buttonFileAttach);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_chat, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.sendPhoto:
                                verifyStoragePermissions();
//                photoCameraIntent();
                                break;
                            case R.id.sendPhotoGallery:

                                photoGalleryIntent();
                                //getPDF();
                                break;
                            case R.id.sendFile:
                                fileIntent();
                                break;
                            case R.id.sign_out:
//                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                                intent.setType("*/*");
//                                startActivityForResult(intent, 7);

                                return true;
                        }
                        return false;
                    }
                });

                popup.show();//showing popup menu
            }

        });

        emojIcon = new EmojIconActions(this,contentRoot,edMessage,btEmoji);
        emojIcon.ShowEmojIcon();
        rvListMessage = (RecyclerView)findViewById(R.id.messageRecyclerView);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setStackFromEnd(true);
    }


    /**
     * Sign Out no login
     */
    private void signOut(){
        mFirebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        //startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     */
    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(MainActivityChat.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    MainActivityChat.this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }else{
            // we already have permission, lets go ahead and call camera intent
            photoCameraIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_EXTERNAL_STORAGE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    photoCameraIntent();
                }
                break;
        }
    }
}

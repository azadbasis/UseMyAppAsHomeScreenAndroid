package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.R;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.MainActivityChat;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.model.UserModel;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.firebasefilesendcat.util.Util;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.User;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.AppConstant;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.PersistData;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.TinyDB;


public class LoginActivityChat extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    //Constants
    private static final String TAG = LoginActivityChat.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    Context con;
    //UI
    private SignInButton mSignInButton;
    private UserModel userModel;
    //Firebase and GoogleApiClient
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        con = this;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        //updateAdapter();
        //getAllUsers();
        if (mFirebaseUser != null){
            startActivity(new Intent(this, MainActivityChat.class));
            finish();
        }else{
            setContentView(R.layout.activity_login);

            if (!Util.verificaConexao(this)){
                Util.initToast(this,"Você não tem conexão com internet");
                finish();
            }

            mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
            mSignInButton.setOnClickListener(this);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this,this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();


        }




    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);


            } else {
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            default:
                return;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Util.initToast(this,"Google Play Services error.");
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        //mFirebaseDatabaseReference.child("users").push().setValue(userModel);


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Util.initToast(LoginActivityChat.this,"Authentication failed");
                        } else {
                            //verificaUsuarioLogado();
                            startActivity(new Intent(LoginActivityChat.this, MainActivityChat.class));
                            finish();
                        }
                    }
                });
    }


    private void verificaUsuarioLogado(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        userModel = new UserModel(mFirebaseUser.getDisplayName(), mFirebaseUser.getPhotoUrl().toString(), mFirebaseUser.getUid(),mFirebaseUser.getEmail(),PersistData.getStringData(con, AppConstant.fcm_token));
        mFirebaseDatabaseReference.child("users").push().setValue(userModel);

    }



    private void  getAllUsers() {
        AppConstant.fUserList.clear();
        FirebaseDatabase.getInstance().getReference().child(AppConstant.ARG_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    UserModel user = dataSnapshotChild.getValue(UserModel.class);
                    AppConstant.fUserList.add(user);

                }

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



}

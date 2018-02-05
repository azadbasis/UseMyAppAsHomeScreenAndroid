package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.model.User;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.AppConstant;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.PersistData;

public class SignUpActivity extends AppCompatActivity {


    Context con;
    private TextView signup;
    private TextView signin;
    private TextView fb;
    private TextView account;
    private EditText email,password,user;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = this;
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            //Toast.makeText(getApplicationContext(), fUser.getEmail().toString(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(con, UserListActivity.class));
            finish();
        }else {
            initUi();
        }



//        signin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                Intent it = new Intent(SignUpActivity.this, LoginActivity.class);
//                startActivity(it);
//                finish();
//            }
//        });

    }

    private void initUi() {

        setContentView(R.layout.activity_logout);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        signup = (TextView)findViewById(R.id.signup);
        signin = (TextView)findViewById(R.id.signin);
        fb = (TextView)findViewById(R.id.fb);
        account = (TextView)findViewById(R.id.account);
        email = (EditText)findViewById(R.id.email);
        email.setText(PersistData.getStringData(con,AppConstant.userEmail));
        password = (EditText)findViewById(R.id.password);
        password.setText(PersistData.getStringData(con,AppConstant.userPassword));
        user = (EditText)findViewById(R.id.user);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userEmail= email.getText().toString().trim();
                String userPass = password.getText().toString().intern();
                String userName = user.getText().toString().intern();

                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getApplicationContext(), "Enter User Name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userPass)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                PersistData.setStringData(con, AppConstant.userName,userName);

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(userEmail, userPass)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(con, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(con, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {

                                    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                                    if (fUser != null) {
//                                        // Name, email address, and profile photo Url
//                                        String name = user.getDisplayName();
//                                        String email = user.getEmail();
//                                        Uri photoUrl = user.getPhotoUrl();
//
//                                        // The user's ID, unique to the Firebase project. Do NOT use this value to
//                                        // authenticate with your backend server, if you have one. Use
//                                        // FirebaseUser.getToken() instead.
//                                        String uid = user.getUid();

                                        PersistData.setStringData(con,AppConstant.uid,fUser.getUid());

                                        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                                        User userModel = new User(fUser.getUid(), fUser.getEmail(),
                                                PersistData.getStringData(con,AppConstant.userName),
                                                PersistData.getStringData(con,AppConstant.fcm_token));
                                        database.child(AppConstant.ARG_USERS)
                                                .child(fUser.getUid())
                                                .setValue(userModel)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(con,"User added",Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                            startActivity(new Intent(con,UserListActivity.class));
                                                            finish();
                                                        } else {
                                                            Toast.makeText(con,"User not added",Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    }
                                                });
                                    }


                                }
                            }
                        });

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void addUser(String user){

    }

}


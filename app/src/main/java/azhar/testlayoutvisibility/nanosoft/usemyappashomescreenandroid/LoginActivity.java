package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.customfonts.MyEditText;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.AppConstant;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.PersistData;

public class LoginActivity extends AppCompatActivity {

    Context con;
    private TextView account,tvBtnSignIn,fb,signup;
    private MyEditText email,password,nameEditTxt;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = this;

        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser != null) {
            //Toast.makeText(getApplicationContext(), fUser.getEmail().toString(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(con, MainActivity.class));
            finish();
        }


        setContentView(R.layout.activity_login_logout);


        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        signup = (TextView)findViewById(R.id.signup);
        tvBtnSignIn = (TextView)findViewById(R.id.tvBtnSignIn);
        fb = (TextView)findViewById(R.id.fb);
        account = (TextView)findViewById(R.id.account);
        email = (MyEditText)findViewById(R.id.email);
        password = (MyEditText)findViewById(R.id.password);
        nameEditTxt = (MyEditText)findViewById(R.id.nameEditTxt);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        tvBtnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String username = nameEditTxt.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Enter use name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                PersistData.setStringData(con,AppConstant.userName,username);

                //authenticate user
                auth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        Toast.makeText(LoginActivity.this, "Password must be 6 digit!", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Sign In failed! You may Sign up first.", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(con,SignUpActivity.class));
                                    }
                                } else {
                                    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                                    PersistData.setStringData(con,AppConstant.uid,fUser.getUid());
                                    Toast.makeText(LoginActivity.this, "Sign In Successful!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(it);
                finish();
            }
        });



    }


    @Override
    protected void onStart() {



        super.onStart();
    }
}

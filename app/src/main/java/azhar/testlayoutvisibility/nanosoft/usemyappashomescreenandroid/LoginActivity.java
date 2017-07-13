package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {


    private TextView signup;
    private TextView signin;
    private TextView fb;
    private TextView account;
    private EditText email;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_logout);
        signup = (TextView)findViewById(R.id.signup);
        signin = (TextView)findViewById(R.id.signin);
        fb = (TextView)findViewById(R.id.fb);
        account = (TextView)findViewById(R.id.account);
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);



        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(LoginActivity.this, LogoutActivity.class);
                startActivity(it);
                finish();
            }
        });



    }




}

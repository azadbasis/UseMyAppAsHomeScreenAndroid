package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.AppConstant;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.Operation;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.PersistData;
import azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils.PersistentUser;

/**
 * Created by NanoSoft on 1/23/2018.
 */

public class SplashScreen extends AppCompatActivity{

    private static int SPLASH_TIME_OUT = 5000;
    Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        con = this;
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                if(PersistentUser.isLogged(con)){
                    Operation operation = new Operation(con);
                    operation.loginWithServer(con, PersistData.getStringData(con, AppConstant.userEmail),PersistData.getStringData(con, AppConstant.userPassword));
                    startActivity(new Intent(con,MainActivity.class));
                    finish();
                }else {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}

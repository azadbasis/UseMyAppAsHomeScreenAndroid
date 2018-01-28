package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;

/**
 * Created by NanoSoft on 1/25/2018.
 */

public class ScanActivity extends AppCompatActivity {

    ArcMenu arcMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_buttons);


        arcMenu = (ArcMenu) findViewById(R.id.arcMenu);
        arcMenu.setRadius(getResources().getDimension(R.dimen._200sdp));

        arcMenu.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onMenuOpened() {
                //Snackbar.make(arcMenu, "Menu Opened", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onMenuClosed() {
                //Snackbar.make(arcMenu, "Menu Closed", Snackbar.LENGTH_SHORT).show();
            }
        });

    }
}

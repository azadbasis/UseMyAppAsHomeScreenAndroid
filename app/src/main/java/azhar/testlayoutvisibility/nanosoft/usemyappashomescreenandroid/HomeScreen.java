package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Nanosoft-Android on 7/6/2017.
 */

public class HomeScreen extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT))
        {
            
            Intent intent1 = new Intent(context,LoginActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent1);
        }
    }
}

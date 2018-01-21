package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Nanosoft-Android on 1/15/2018.
 */

public class Operation {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public Operation(Context context) {
        this.context = context;

    }
    public void saveString(String key, String value) {
        sharedPreferences = context.getSharedPreferences("OFFICE_KIT", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public String getString(String key, String defaultvalue) {
        sharedPreferences = context.getSharedPreferences("OFFICE_KIT", MODE_PRIVATE);

        return sharedPreferences.getString(key, defaultvalue);
    }


}

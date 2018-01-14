package azhar.testlayoutvisibility.nanosoft.usemyappashomescreenandroid.utils;

/**
 * Created by NanoSoft on 6/17/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersistData {


    private static final String PREFS_FILE_NAME = "AppPreferences";


	/*
     * default value is empty string
	 */


    public static void setStringListData(final Context ctx, String key, List<String> list) {
        SharedPreferences prefs=ctx.getSharedPreferences(PREFS_FILE_NAME,Context.MODE_PRIVATE);
        Editor edit=prefs.edit();

        Set<String> set = new HashSet<String>();
        set.addAll(list);
        edit.putStringSet(key, set);
        edit.commit();
    }


    public static void getStringListData(final Context ctx, String key){
        SharedPreferences prefs=ctx.getSharedPreferences(PREFS_FILE_NAME,Context.MODE_PRIVATE);

        Set<String> set = prefs.getStringSet(key, null);
        List<String> sample=new ArrayList<String>(set);
    }


    public static String getStringData(final Context ctx, String key) {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
                .getString(key, "");
    }


    public static void setStringData(final Context ctx, final String key, final String data) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(key, data);
        editor.commit();
    }

	/*
	 * default value is -1
	 */


    public static int getIntData(final Context ctx, String key) {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
                .getInt(key, -1);
    }


    public static void setIntData(final Context ctx, final String key, final int value) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    /*
     * default value is false
     */
    public static boolean getBooleanData(final Context ctx, String key) {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
                .getBoolean(key, false);
    }


    public static void setBooleanData(final Context ctx, final String key, final boolean data) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putBoolean(key, data);
        editor.commit();
    }



	/*
	 * default value is 0.0f
	 */


    public static float getFloatData(final Context ctx, String key) {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
                .getFloat(key, 0.0f);
    }


    public static void setFloatData(final Context ctx, final String key, final float value) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putFloat(key, value);
        editor.commit();
    }


	/*
	 * default value is -1
	 */


    public static long getLongData(final Context ctx, String key) {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
                .getLong(key, -1);
    }


    public static void setLongData(final Context ctx, final String key, final long value) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.commit();

    }


}

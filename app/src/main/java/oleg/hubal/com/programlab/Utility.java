package oleg.hubal.com.programlab;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

/**
 * Created by User on 05.10.2016.
 */

public class Utility {
    public static void setFirstDownloadPref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(Constants.PREF_CHANNEL_DOWNLOADED, true).apply();
    }

    public static boolean getFirstDownloadPref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(Constants.PREF_CHANNEL_DOWNLOADED, false);
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }

    public static int calculatePercent(long percentage, long total) {
        return (int)((percentage * 100.0f) / total);
    }
}

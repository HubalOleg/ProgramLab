package oleg.hubal.com.programlab;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import oleg.hubal.com.programlab.data.TvProgramContract;

/**
 * Created by User on 05.10.2016.
 */

public class Utility {
    public static void setDownloadPref(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(Constants.PREF_CHANNEL_DOWNLOADED, true).apply();
    }

    public static boolean getDownloadPref(Context context) {
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

    public static String parseJSONToDate(Long jsonDate) {
        Timestamp stamp = new Timestamp(jsonDate);
        Date date = new Date(stamp.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
    }

    public static String[] parseDateToJSON(String textDate) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd");
        long currentDay = 0;
        long nextDay = 0;
        try {
            Date date = dateFormatter.parse(textDate);
            currentDay = date.getTime();
            nextDay = currentDay + 1000*60*60*24;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new String[] {String.valueOf(currentDay), String.valueOf(nextDay)};
    }

    public static TreeMap<String, ArrayList<String>> parseCursorToMap(Cursor cursor) {
        TreeMap<String, ArrayList<String>> programMap = new TreeMap<>();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String showID = cursor.getString(
                            cursor.getColumnIndex(TvProgramContract.ProgramEntry.COLUMN_SHOW_ID));
                    String tvShowName = cursor.getString(
                            cursor.getColumnIndex(TvProgramContract.ProgramEntry.COLUMN_SHOW_NAME));
                    Long date = cursor.getLong(
                            cursor.getColumnIndex(TvProgramContract.ProgramEntry.COLUMN_DATE));
                    String program = parseJSONToDate(date) + ": " + tvShowName;

                    if (programMap.containsKey(showID)) {
                        programMap.get(showID).add(program);
                    } else {
                        ArrayList<String> programList = new ArrayList<>();
                        programList.add(program);
                        programMap.put(showID, programList);
                    }
                } while (cursor.moveToNext());
            }
        }

        return programMap;
    }
}

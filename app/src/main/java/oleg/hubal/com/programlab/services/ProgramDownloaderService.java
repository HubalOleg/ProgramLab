package oleg.hubal.com.programlab.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;

import oleg.hubal.com.programlab.data.TvProgramContract;

/**
 * Created by User on 04.10.2016.
 */

public class ProgramDownloaderService extends IntentService {

    public ProgramDownloaderService() {
        super("ProgramDownloaderService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        Log.d("Log123", "start service");
//        ResultReceiver rec = intent.getParcelableExtra("receiver");
//
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String programJsonStr = null;

        try {
            URL url = new URL("https://t2dev.firebaseio.com/PROGRAM.json");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return;
            }
            programJsonStr = buffer.toString();
            getProgramDataFromJson(programJsonStr);
//            rec.send(Activity.RESULT_OK, null);
        } catch (IOException e) {
            Log.e("log123", "Error", e);
        } catch (JSONException e) {
            Log.e("log123", e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("log123", "Error closing stream", e);
                }
            }
        }
        return;
    }

    private void getProgramDataFromJson(String programJsonStr)
            throws JSONException {
        JSONObject programJson = new JSONObject(programJsonStr);
        Iterator<String> keys = programJson.keys();

        Vector<ContentValues> cVVector = new Vector<>();

        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject programByDayJson = programJson.getJSONObject(key);
            Iterator<String> keysByDay = programByDayJson.keys();

            while (keysByDay.hasNext()) {
                String oneKey = keysByDay.next();
                JSONObject oneProgramJson = programByDayJson.getJSONObject(oneKey);

                Long date = oneProgramJson.getLong("date");
                String showID = oneProgramJson.getString("showID");
                String tvShowName = oneProgramJson.getString("tvShowName");

                ContentValues programValues = new ContentValues();
                programValues.put(TvProgramContract.ProgramEntry.COLUMN_DATE, date);
                programValues.put(TvProgramContract.ProgramEntry.COLUMN_SHOW_ID, showID);
                programValues.put(TvProgramContract.ProgramEntry.COLUMN_SHOW_NAME, tvShowName);
                cVVector.add(programValues);
            }
        }
        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            getContentResolver().bulkInsert(TvProgramContract.ProgramEntry.CONTENT_URI, cvArray);
        }
        Log.d("log123", "getProgramDataFromJson");
    }
}

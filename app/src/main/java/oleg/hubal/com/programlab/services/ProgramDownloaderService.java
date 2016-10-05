package oleg.hubal.com.programlab.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Vector;

import cz.msebera.android.httpclient.Header;
import oleg.hubal.com.programlab.Constants;
import oleg.hubal.com.programlab.data.TvProgramContract;

/**
 * Created by User on 04.10.2016.
 */

public class ProgramDownloaderService extends IntentService {

    private final AsyncHttpClient client = new SyncHttpClient();

    public ProgramDownloaderService() {
        super("ProgramDownloaderService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        client.get(Constants.URL_PROGRAM, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    getProgramDataFromJson(response);
                } catch (JSONException e) {
                    Log.e("log123", e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        });
    }


    private void getProgramDataFromJson(JSONObject allProgramJsonObject)
            throws JSONException {
        Vector<ContentValues> programVector = new Vector<>();

        Iterator<String> allKeys = allProgramJsonObject.keys();

        while (allKeys.hasNext()) {
            String dailyKey = allKeys.next();
            JSONObject dailyProgramJsonObject = allProgramJsonObject.getJSONObject(dailyKey);
            Iterator<String> programKeys = dailyProgramJsonObject.keys();

            while (programKeys.hasNext()) {
                String programKey = programKeys.next();
                JSONObject programJsonObject = dailyProgramJsonObject.getJSONObject(programKey);

                Long date = programJsonObject.getLong(Constants.JSON_PROGRAM_DATE);
                String showID = programJsonObject.getString(Constants.JSON_PROGRAM_SHOW_ID);
                String tvShowName = programJsonObject.getString(Constants.JSON_PROGRAM_SHOW_NAME);

                ContentValues programValues = new ContentValues();
                programValues.put(TvProgramContract.ProgramEntry.COLUMN_DATE, date);
                programValues.put(TvProgramContract.ProgramEntry.COLUMN_SHOW_ID, showID);
                programValues.put(TvProgramContract.ProgramEntry.COLUMN_SHOW_NAME, tvShowName);
                programVector.add(programValues);
            }
        }
        if ( programVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[programVector.size()];
            programVector.toArray(cvArray);
            getContentResolver().bulkInsert(TvProgramContract.ProgramEntry.CONTENT_URI, cvArray);
        }
    }
}

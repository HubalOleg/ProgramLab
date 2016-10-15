package oleg.hubal.com.programlab.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
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
import oleg.hubal.com.programlab.R;
import oleg.hubal.com.programlab.Utility;
import oleg.hubal.com.programlab.data.TvProgramContract;

/**
 * Created by User on 04.10.2016.
 */

public class DownloadService extends IntentService {
    private final AsyncHttpClient client = new SyncHttpClient();

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;

    private final int mNotificationId = 1;
    private final int mTotalValue = 100;

    private int previousPercent = 0;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(getString(R.string.notif_title))
                .setContentText(getString(R.string.notif_downloading))
                .setSmallIcon(android.R.drawable.stat_sys_download);
        setNotificationProgress(mTotalValue, 0);

        switch(intent.getIntExtra(Constants.SERVICE_STATUS, -1)) {
            case Constants.SERVICE_FIRST_DOWNLOAD:
                loadChannel();;
                loadCategory();
                loadProgram();
                Utility.setFirstDownloadPref(this);
                mBuilder.setContentText(getString(R.string.notif_download_finished));
                break;
            case Constants.SERVICE_SYNCHRONIZE_DATA:
                deleteProgram();
                loadProgram();
                mBuilder.setContentText(getString(R.string.notif_synchronized));
                break;
        };

        mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
        mNotifyManager.notify(mNotificationId, mBuilder.build());

    }

    private void loadChannel() {
        client.get(Constants.URL_CHANNEL, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    getChannelDataFromJSON(response);
                } catch (JSONException e) {
                    Log.e("log123", e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadCategory() {
        client.get(Constants.URL_CATEGORY, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Vector<ContentValues> categoryVector = new Vector<>();

                Iterator<String> keys = response.keys();

                while (keys.hasNext()) {
                    ContentValues categoryValues = new ContentValues();
                    String category = keys.next();
                    categoryValues.put(TvProgramContract.CategoryEntry.COLUMN_NAME, category);
                    categoryVector.add(categoryValues);
                }
                if ( categoryVector.size() > 0 ) {
                    ContentValues[] cvArray = new ContentValues[categoryVector.size()];
                    categoryVector.toArray(cvArray);
                    getContentResolver().bulkInsert(
                            TvProgramContract.CategoryEntry.CONTENT_URI, cvArray);
                }
            }
        });
    }

    private void getChannelDataFromJSON(JSONObject allChannelsJSONObject)
            throws JSONException {
        Vector<ContentValues> channelVector = new Vector<>();

        Iterator<String> channelsKeys = allChannelsJSONObject.keys();

        while (channelsKeys.hasNext()) {
            String channelKey = channelsKeys.next();
            JSONObject channelJSONObject = allChannelsJSONObject.getJSONObject(channelKey);

            String name = channelJSONObject.getString(Constants.JSON_CHANNEL_NAME);
            String tvURL = channelJSONObject.getString(Constants.JSON_CHANNEL_TV_URL);
            String category = getCategory(channelJSONObject);

            ContentValues channelValues = new ContentValues();
            channelValues.put(TvProgramContract.ChannelEntry.COLUMN_NAME, name);
            channelValues.put(TvProgramContract.ChannelEntry.COLUMN_TV_URL, tvURL);
            channelValues.put(TvProgramContract.ChannelEntry.COLUMN_CATEGORY, category);
            channelVector.add(channelValues);
        }
        if ( channelVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[channelVector.size()];
            channelVector.toArray(cvArray);
            getContentResolver().bulkInsert(TvProgramContract.ChannelEntry.CONTENT_URI, cvArray);
        }
    }

    private String getCategory(JSONObject jsonChannel) {
        Iterator<String> objectTags = jsonChannel.keys();
        String category = "";
        while(objectTags.hasNext()) {
            category = objectTags.next();
        }
        return category;
    }

    private void loadProgram() {
        client.get(Constants.URL_PROGRAM, new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    getProgramDataFromJSON(response);
                } catch (JSONException e) {
                    Log.e("log123", e.getMessage(), e);
                    e.printStackTrace();
                }
            }
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                int percent = Utility.calculatePercent(bytesWritten, totalSize);
                if (percent - previousPercent > Constants.NOTIFICATION_PROGRESS_LIMIT) {
                    setNotificationProgress(mTotalValue, percent);
                    previousPercent = percent;
                }
            }
        });
    }

    private void getProgramDataFromJSON(JSONObject allProgramJSONObject)
            throws JSONException {
        mBuilder.setContentText(getString(R.string.notif_saving));
        setNotificationProgress(0, 0);

        Vector<ContentValues> programVector = new Vector<>();

        Iterator<String> allKeys = allProgramJSONObject.keys();

        while (allKeys.hasNext()) {
            String dailyKey = allKeys.next();
            JSONObject dailyProgramJSONObject = allProgramJSONObject.getJSONObject(dailyKey);
            Iterator<String> programKeys = dailyProgramJSONObject.keys();

            while (programKeys.hasNext()) {
                String programKey = programKeys.next();
                JSONObject programJSONObject = dailyProgramJSONObject.getJSONObject(programKey);

                Long date = programJSONObject.getLong(Constants.JSON_PROGRAM_DATE);
                String showID = programJSONObject.getString(Constants.JSON_PROGRAM_SHOW_ID);
                String tvShowName = programJSONObject.getString(Constants.JSON_PROGRAM_SHOW_NAME);

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

    private void deleteProgram() {
        getContentResolver().delete(TvProgramContract.CategoryEntry.CONTENT_URI, null, null);
    }

    private void setNotificationProgress(int max, int progress) {
        mBuilder.setProgress(max, progress, false);
        mNotifyManager.notify(mNotificationId, mBuilder.build());
    }
}

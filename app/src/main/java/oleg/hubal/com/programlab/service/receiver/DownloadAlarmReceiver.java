package oleg.hubal.com.programlab.service.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import oleg.hubal.com.programlab.Constants;
import oleg.hubal.com.programlab.Utility;
import oleg.hubal.com.programlab.service.DownloadService;

/**
 * Created by User on 21.10.2016.
 */

public class DownloadAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 15;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Utility.isNetworkConnected(context)) {
            Log.d("log123", "receive");
            Intent i = new Intent(context, DownloadService.class);
            i.putExtra(Constants.SERVICE_STATUS, Constants.SERVICE_SYNCHRONIZE_DATA);
            context.startService(i);
        }
    }
}

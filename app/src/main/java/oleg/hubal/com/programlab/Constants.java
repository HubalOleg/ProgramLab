package oleg.hubal.com.programlab;

/**
 * Created by User on 05.10.2016.
 */

public class Constants {
    public static final String JSON_PROGRAM_DATE =      "date";
    public static final String JSON_PROGRAM_SHOW_ID =   "showID";
    public static final String JSON_PROGRAM_SHOW_NAME = "tvShowName";

    public static final String JSON_CHANNEL_NAME =      "name";
    public static final String JSON_CHANNEL_TV_URL =    "tvURL";

    public static final String URL_PROGRAM  = "https://t2dev.firebaseio.com/PROGRAM.json";
    public static final String URL_CHANNEL  = "https://t2dev.firebaseio.com/CHANNEL.json";
    public static final String URL_CATEGORY = "https://t2dev.firebaseio.com/CATEGORY.json";

    public static final String SERVICE_STATUS = "status";

    public static final int SERVICE_FIRST_DOWNLOAD = 0;
    public static final int SERVICE_SYNCHRONIZE_DATA = 1;

    public static final String PREF_CHANNEL_DOWNLOADED = "channel_download_status";

    public static final int NOTIFICATION_PROGRESS_LIMIT = 4;

    public static final String CURSOR_CHANNEL_NAME = "name";
    public static final String CURSOR_CHANNEL_TV_URL = "tvURL";
    public static final String CURSOR_CHANNEL_CATEGORY = "category";
    public static final String CURSOR_CATEGORY_NAME =  "name";
}

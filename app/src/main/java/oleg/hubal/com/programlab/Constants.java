package oleg.hubal.com.programlab;

/**
 * Created by User on 05.10.2016.
 */

public class Constants {
    public static final String JSON_PROGRAM_DATE =      "date";
    public static final String JSON_PROGRAM_SHOW_ID =   "showID";
    public static final String JSON_PROGRAM_SHOW_NAME = "tvShowName";

    public static final String JSON_CHANNELS_NAME =      "name";
    public static final String JSON_CHANNELS_TV_URL =    "tvURL";

    public static final String URL_PROGRAM  = "https://t2dev.firebaseio.com/PROGRAM.json";
    public static final String URL_CHANNELS = "https://t2dev.firebaseio.com/CHANNEL.json";
    public static final String URL_CATEGORY = "https://t2dev.firebaseio.com/CATEGORY.json";

    public static final String SERVICE_STATUS = "status";

    public static final int SERVICE_FIRST_DOWNLOAD = 0;
    public static final int SERVICE_SYNCHRONIZE_DATA = 1;
    public static final int SERVICE_SYNCHRONIZE_DATA_WITH_RESULT = 2;

    public static final String PREF_CHANNEL_DOWNLOADED = "channel_download_status";

    public static final int NOTIFICATION_PROGRESS_LIMIT = 4;

    public static final String CURSOR_CHANNELS_NAME =        "name";
    public static final String CURSOR_CHANNELS_TV_URL =      "tvURL";
    public static final String CURSOR_CHANNELS_CATEGORY =    "category";
    public static final String CURSOR_CHANNELS_FAVORITE =    "favorite";

    public static final String CURSOR_CATEGORY_NAME =       "name";
    public static final String BUNDLE_CHANNELS_FILTER = "channels_filter";

    public static final String FAVORITE_CHANNELS = "favorite_channels";
    public static final String BUNDLE_PAGE_PROGRAM = "page_title";

    public static final String RECEIVER = "result_receiver";
    public static final int RECEIVER_SERVICE_START = 0;
    public static final int RECEIVER_SERVICE_FINISH = 1;
}

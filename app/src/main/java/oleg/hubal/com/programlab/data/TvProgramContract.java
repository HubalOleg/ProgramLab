package oleg.hubal.com.programlab.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by User on 02.10.2016.
 */

public class TvProgramContract {
    public static final String CONTENT_AUTHORITY =  "oleg.hubal.com.programlab";
    public static final Uri BASE_CONTENT_URI =      Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PROGRAM = "program";
//TODO another pathes

    public static final class ProgramEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROGRAM).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROGRAM;
        public static final String CONTENT_ITEM =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROGRAM;

        public static final String TABLE_NAME =         "program";
        public static final String COLUMN_DATE =        "date";
        public static final String COLUMN_SHOW_ID =     "showID";
        public static final String COLUMN_SHOW_NAME =   "tvShowName";

        public static Uri buildProgramUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
//TODO another entries
}

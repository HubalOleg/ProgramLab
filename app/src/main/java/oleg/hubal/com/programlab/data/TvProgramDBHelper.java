package oleg.hubal.com.programlab.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import oleg.hubal.com.programlab.data.TvProgramContract.FavoriteEntry;
import oleg.hubal.com.programlab.data.TvProgramContract.ProgramEntry;
import oleg.hubal.com.programlab.data.TvProgramContract.ChannelEntry;
import oleg.hubal.com.programlab.data.TvProgramContract.CategoryEntry;


/**
 * Created by User on 02.10.2016.
 */

public class TvProgramDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tvProgram.db";

    public TvProgramDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PROGRAM_TABLE = "CREATE TABLE " + ProgramEntry.TABLE_NAME + " (" +
                ProgramEntry._ID + " INTEGER PRIMARY KEY, " +
                ProgramEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                ProgramEntry.COLUMN_SHOW_ID + " TEXT NOT NULL, " +
                ProgramEntry.COLUMN_SHOW_NAME + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_CHANNEL_TABLE = "CREATE TABLE " + ChannelEntry.TABLE_NAME + " (" +
                ChannelEntry._ID + " INTEGER PRIMARY KEY, " +
                ChannelEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ChannelEntry.COLUMN_TV_URL + " TEXT NOT NULL, " +
                ChannelEntry.COLUMN_CATEGORY + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                CategoryEntry._ID + " INTEGER PRIMARY KEY, " +
                CategoryEntry.COLUMN_NAME + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID + " INTEGER PRIMARY KEY, " +
                FavoriteEntry.COLUMN_NAME + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_PROGRAM_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CHANNEL_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + ProgramEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + ChannelEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + CategoryEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + FavoriteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

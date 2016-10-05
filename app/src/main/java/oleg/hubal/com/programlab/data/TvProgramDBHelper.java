package oleg.hubal.com.programlab.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import oleg.hubal.com.programlab.data.TvProgramContract.ProgramEntry;


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
        final String SQL_CREATE_LOCATION_TABLE = "CREATE TABLE " + ProgramEntry.TABLE_NAME + " (" +
                ProgramEntry._ID + " INTEGER PRIMARY KEY, " +
                ProgramEntry.COLUMN_DATE + " INTEGER NOT NULL, " +
                ProgramEntry.COLUMN_SHOW_ID + " TEXT NOT NULL, " +
                ProgramEntry.COLUMN_SHOW_NAME + " TEXT NOT NULL " +
                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + ProgramEntry.TABLE_NAME);
    }
}

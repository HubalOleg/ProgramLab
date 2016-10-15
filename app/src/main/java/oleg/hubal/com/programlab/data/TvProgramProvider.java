package oleg.hubal.com.programlab.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by User on 02.10.2016.
 */

public class TvProgramProvider extends ContentProvider {
    private static final int PROGRAM =      100;
    private static final int PROGRAM_ID =   101;
    private static final int CHANNEL =      102;
    private static final int CHANNEL_ID =   103;
    private static final int CATEGORY =     104;
    private static final int CATEGORY_ID =  105;
    private static final int FAVORITE =     106;
    private static final int FAVORITE_ID =  107;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TvProgramDBHelper mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new TvProgramDBHelper(getContext());
        return true;
    }

    public static UriMatcher buildUriMatcher() {
        String content = TvProgramContract.CONTENT_AUTHORITY;

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, TvProgramContract.PATH_PROGRAM, PROGRAM);
        matcher.addURI(content, TvProgramContract.PATH_PROGRAM + "/#", PROGRAM_ID);
        matcher.addURI(content, TvProgramContract.PATH_CHANNEL, CHANNEL);
        matcher.addURI(content, TvProgramContract.PATH_CHANNEL + "/#", CHANNEL_ID);
        matcher.addURI(content, TvProgramContract.PATH_CATEGORY, CATEGORY);
        matcher.addURI(content, TvProgramContract.PATH_CATEGORY + "/#", CATEGORY_ID);
        matcher.addURI(content, TvProgramContract.PATH_FAVORITE, FAVORITE);
        matcher.addURI(content, TvProgramContract.PATH_FAVORITE + "/#", FAVORITE_ID);

        return matcher;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch(sUriMatcher.match(uri)) {
            case PROGRAM:
                return TvProgramContract.ProgramEntry.CONTENT_TYPE;
            case PROGRAM_ID:
                return TvProgramContract.ProgramEntry.CONTENT_ITEM;
            case CHANNEL:
                return TvProgramContract.ChannelEntry.CONTENT_TYPE;
            case CHANNEL_ID:
                return TvProgramContract.ChannelEntry.CONTENT_ITEM;
            case CATEGORY:
                return TvProgramContract.CategoryEntry.CONTENT_TYPE;
            case CATEGORY_ID:
                return TvProgramContract.CategoryEntry.CONTENT_ITEM;
            case FAVORITE:
                return TvProgramContract.FavoriteEntry.CONTENT_TYPE;
            case FAVORITE_ID:
                return TvProgramContract.FavoriteEntry.CONTENT_ITEM;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Cursor retCursor;
        long _id;
        switch(sUriMatcher.match(uri)) {
            case PROGRAM:
                retCursor = db.query(
                        TvProgramContract.ProgramEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case PROGRAM_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        TvProgramContract.ProgramEntry.TABLE_NAME,
                        projection,
                        TvProgramContract.ProgramEntry._ID + " = ?",
                        new String[]{String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case CHANNEL:
                retCursor = db.query(
                        TvProgramContract.ChannelEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CHANNEL_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        TvProgramContract.ChannelEntry.TABLE_NAME,
                        projection,
                        TvProgramContract.ChannelEntry._ID + " = ?",
                        new String[] {String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case CATEGORY:
                retCursor = db.query(
                        TvProgramContract.CategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CATEGORY_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        TvProgramContract.CategoryEntry.TABLE_NAME,
                        projection,
                        TvProgramContract.CategoryEntry._ID + " = ?",
                        new String[] {String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAVORITE:
                retCursor = db.query(
                        TvProgramContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case FAVORITE_ID:
                _id = ContentUris.parseId(uri);
                retCursor = db.query(
                        TvProgramContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        TvProgramContract.FavoriteEntry._ID + " = ?",
                        new String[] {String.valueOf(_id)},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch(sUriMatcher.match(uri)) {
            case PROGRAM:
                _id = db.insert(TvProgramContract.ProgramEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    returnUri = TvProgramContract.ProgramEntry.buildProgramUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case CHANNEL:
                _id = db.insert(TvProgramContract.ChannelEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    returnUri = TvProgramContract.ChannelEntry.buildChannelUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case CATEGORY:
                _id = db.insert(TvProgramContract.CategoryEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    returnUri = TvProgramContract.CategoryEntry.buildCategoryUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            case FAVORITE:
                _id = db.insert(TvProgramContract.FavoriteEntry.TABLE_NAME, null, values);
                if(_id > 0) {
                    returnUri = TvProgramContract.FavoriteEntry.buildFavoriteUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows;

        switch(sUriMatcher.match(uri)) {
            case PROGRAM:
                rows = db.delete(TvProgramContract.ProgramEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case CHANNEL:
                rows = db.delete(TvProgramContract.ChannelEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case CATEGORY:
                rows = db.delete(TvProgramContract.CategoryEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case FAVORITE:
                rows = db.delete(TvProgramContract.FavoriteEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(selection == null || rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int rows;

        Log.d("log123", String.valueOf(sUriMatcher.match(uri)));
        switch(sUriMatcher.match(uri)) {
            case PROGRAM:
                rows = db.update(TvProgramContract.ProgramEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case CHANNEL:
                rows = db.update(TvProgramContract.ChannelEntry.TABLE_NAME,
                        values, selection, selectionArgs);
            case CATEGORY:
                rows = db.update(TvProgramContract.CategoryEntry.TABLE_NAME,
                        values, selection, selectionArgs);
            case FAVORITE:
                rows = db.update(TvProgramContract.FavoriteEntry.TABLE_NAME,
                        values, selection, selectionArgs);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount;
        switch (match) {
            case PROGRAM:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TvProgramContract.ProgramEntry.TABLE_NAME,
                                null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case CHANNEL:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TvProgramContract.ChannelEntry.TABLE_NAME,
                                null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case CATEGORY:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TvProgramContract.CategoryEntry.TABLE_NAME,
                                null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case FAVORITE:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(TvProgramContract.FavoriteEntry.TABLE_NAME,
                                null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}

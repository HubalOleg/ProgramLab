package oleg.hubal.com.programlab.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Vector;

import oleg.hubal.com.programlab.OnChangeFavoriteState;
import oleg.hubal.com.programlab.R;
import oleg.hubal.com.programlab.adapter.ChannelAdapter;
import oleg.hubal.com.programlab.data.TvProgramContract;

/**
 * Created by User on 28.09.2016.
 */

public class ChannelFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        OnChangeFavoriteState {

    private RecyclerView mRecyclerView;
    private ChannelAdapter mChannelAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Vector<ContentValues> contentVector;

    private static final String[] CHANNEL_COLUMNS = {
            TvProgramContract.ChannelEntry._ID,
            TvProgramContract.ChannelEntry.COLUMN_NAME,
            TvProgramContract.ChannelEntry.COLUMN_TV_URL,
            TvProgramContract.ChannelEntry.COLUMN_CATEGORY
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mChannelAdapter = new ChannelAdapter(null, this);
        mRecyclerView.setAdapter(mChannelAdapter);

        getLoaderManager().initLoader(0, null, this);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        contentVector = new Vector<>();
    }

    @Override
    public void onStop() {
        super.onStop();

        ContentValues[] contentValues = new ContentValues[contentVector.size()];
        contentVector.toArray(contentValues);
        getContext().getContentResolver().bulkInsert(
                TvProgramContract.FavoriteEntry.CONTENT_URI,
                contentValues
        );
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                TvProgramContract.ChannelEntry.CONTENT_URI,
                CHANNEL_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mChannelAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mChannelAdapter.swapCursor(null);
    }

    @Override
    public void changeFavoriteState(String name, boolean state) {
        if (state) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TvProgramContract.FavoriteEntry.COLUMN_NAME, name);
            contentVector.add(contentValues);
        }
    }
}

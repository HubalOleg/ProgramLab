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
import android.widget.CompoundButton;

import oleg.hubal.com.programlab.Constants;
import oleg.hubal.com.programlab.R;
import oleg.hubal.com.programlab.adapter.ChannelsAdapter;
import oleg.hubal.com.programlab.data.TvProgramContract;

/**
 * Created by User on 28.09.2016.
 */

public class ChannelsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        CompoundButton.OnCheckedChangeListener {

    private RecyclerView mRecyclerView;
    private ChannelsAdapter mChannelAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String selection;
    private String[] selectionArgs;


    private static final String[] CHANNELS_COLUMNS = {
            TvProgramContract.ChannelsEntry._ID,
            TvProgramContract.ChannelsEntry.COLUMN_NAME,
            TvProgramContract.ChannelsEntry.COLUMN_TV_URL,
            TvProgramContract.ChannelsEntry.COLUMN_CATEGORY,
            TvProgramContract.ChannelsEntry.COLUMN_FAVORITE
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        setLoaderArguments();
        getLoaderManager().initLoader(0, null, this);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mChannelAdapter = new ChannelsAdapter(null, this);
        mRecyclerView.setAdapter(mChannelAdapter);

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                TvProgramContract.ChannelsEntry.CONTENT_URI,
                CHANNELS_COLUMNS,
                selection,
                selectionArgs,
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int intState = isChecked ? 1 : 0;
        String name = (String) buttonView.getTag();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TvProgramContract.ChannelsEntry.COLUMN_FAVORITE, intState);

        getContext().getContentResolver().update(
                TvProgramContract.ChannelsEntry.CONTENT_URI,
                contentValues,
                TvProgramContract.ChannelsEntry.COLUMN_NAME + "=?",
                new String[]{name}
        );
    }

    private void setLoaderArguments() {
        selection = null;
        selectionArgs = null;

        String filterArgs;
        Bundle args = getArguments();
        if (args != null) {
            filterArgs = args.getString(Constants.BUNDLE_CHANNELS_FILTER);

            switch (filterArgs) {
                case Constants.FAVORITE_CHANNELS:
                    selection = TvProgramContract.ChannelsEntry.COLUMN_FAVORITE + " = ?";
                    selectionArgs = new String[]{"1"};
                    break;
                default:
                    selection = TvProgramContract.ChannelsEntry.COLUMN_CATEGORY + " = ?";
                    selectionArgs = new String[]{ filterArgs };
                    break;
            }
        }
    }
}

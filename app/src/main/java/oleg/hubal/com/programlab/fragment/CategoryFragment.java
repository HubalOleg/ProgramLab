package oleg.hubal.com.programlab.fragment;

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

import oleg.hubal.com.programlab.R;
import oleg.hubal.com.programlab.adapter.CategoryAdapter;
import oleg.hubal.com.programlab.data.TvProgramContract;

/**
 * Created by User on 28.09.2016.
 */

public class CategoryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CategoryAdapter mCategoryAdapter;

    private static final String[] CATEGORY_COLUMNS = {
            TvProgramContract.CategoryEntry._ID,
            TvProgramContract.CategoryEntry.COLUMN_NAME
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mCategoryAdapter = new CategoryAdapter(null);
        mRecyclerView.setAdapter(mCategoryAdapter);

        getLoaderManager().initLoader(0, null, this);

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                TvProgramContract.CategoryEntry.CONTENT_URI,
                CATEGORY_COLUMNS,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCategoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCategoryAdapter.swapCursor(null);
    }
}

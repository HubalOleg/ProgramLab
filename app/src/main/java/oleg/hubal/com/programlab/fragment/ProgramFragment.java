package oleg.hubal.com.programlab.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeMap;

import oleg.hubal.com.programlab.Constants;
import oleg.hubal.com.programlab.R;
import oleg.hubal.com.programlab.Utility;
import oleg.hubal.com.programlab.adapter.ProgramPagerAdapter;
import oleg.hubal.com.programlab.data.TvProgramContract;
import oleg.hubal.com.programlab.service.DownloadService;
import oleg.hubal.com.programlab.service.receiver.DownloadResultReceiver;

/**
 * Created by User on 28.09.2016.
 */

public class ProgramFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        DatePickerDialog.OnDateSetListener {
    private TreeMap<String, ArrayList<String>> mProgramMap;
    private ProgramPagerAdapter mViewPagerAdapter;
    private ProgressDialog mDownloadDialog;
    private DatePickerDialog mDatePickerDialog;
    private String mSelection;
    private String[] mSelectionArgs;

    private static int mDay, mMonth, mYear;

    private static final String[] PROGRAM_COLUMNS = {
            TvProgramContract.ProgramEntry._ID,
            TvProgramContract.ProgramEntry.COLUMN_DATE,
            TvProgramContract.ProgramEntry.COLUMN_SHOW_ID,
            TvProgramContract.ProgramEntry.COLUMN_SHOW_NAME
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_program, container, false);

        setHasOptionsMenu(true);

        setDateArguments();

        mDatePickerDialog = new DatePickerDialog(getContext(), this, mYear, mMonth - 1, mDay);

        mProgramMap = new TreeMap<>();
        mViewPagerAdapter = new ProgramPagerAdapter(getChildFragmentManager(), mProgramMap);
        ViewPager vpProgram = (ViewPager) v.findViewById(R.id.vpProgram);
        vpProgram.setAdapter(mViewPagerAdapter);

        getLoaderManager().initLoader(0, null, this);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.program_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh_data:
                synchronizeDataWithResult();
                return true;
            case R.id.action_calendar:
                mDatePickerDialog.show();
                return true;
            default:
                return false;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getContext(),
                TvProgramContract.ProgramEntry.CONTENT_URI,
                PROGRAM_COLUMNS,
                mSelection,
                mSelectionArgs,
                null
                );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mProgramMap = Utility.parseCursorToMap(data);
        mViewPagerAdapter.swapData(mProgramMap);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mYear = year;
        mMonth = monthOfYear + 1;
        mDay = dayOfMonth;
        String date = mYear + "." + mMonth + "." + mDay;
        mSelectionArgs = Utility.parseDateToJSON(date);
        getLoaderManager().restartLoader(0, null, this);
    }

    private void synchronizeDataWithResult() {
        if (Utility.isNetworkConnected(getContext())) {
            DownloadResultReceiver resultReceiver = new DownloadResultReceiver(new Handler());
            resultReceiver.setReceiver(new DownloadResultReceiver.Receiver() {
                @Override
                public void onReceiveResult(int resultCode, Bundle resultData) {
                    switch (resultCode) {
                        case Constants.RECEIVER_SERVICE_START:
                            mDownloadDialog = new ProgressDialog(getContext());
                            mDownloadDialog.setCancelable(false);
                            mDownloadDialog.setTitle(getString(R.string.dialog_wait));
                            mDownloadDialog.setMessage(getString(R.string.dialog_synchronizing));
                            mDownloadDialog.show();
                            break;
                        case Constants.RECEIVER_SERVICE_FINISH:
                            mDownloadDialog.dismiss();
                            break;
                    }
                }
            });
            Intent i = new Intent(getContext(), DownloadService.class);
            i.putExtra(Constants.SERVICE_STATUS, Constants.SERVICE_SYNCHRONIZE_DATA_WITH_RESULT);
            i.putExtra(Constants.RECEIVER,resultReceiver);
            getActivity().startService(i);
        } else {
            Toast.makeText(getContext(), R.string.error_internet, Toast.LENGTH_LONG).show();
        }
    }

    private void setDateArguments() {
        mSelection = TvProgramContract.ProgramEntry.COLUMN_DATE + " >= ? AND " +
                TvProgramContract.ProgramEntry.COLUMN_DATE + " <= ? ";

        mYear = 2016;
        mMonth = 6;
        mDay = 28;

        mSelectionArgs = Utility.parseDateToJSON(mYear + "." + mMonth + "." + mDay);
    }
}

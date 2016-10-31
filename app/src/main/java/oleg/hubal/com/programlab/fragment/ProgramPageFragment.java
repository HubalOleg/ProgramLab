package oleg.hubal.com.programlab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import oleg.hubal.com.programlab.Constants;
import oleg.hubal.com.programlab.R;
import oleg.hubal.com.programlab.adapter.ProgramListAdapter;

/**
 * Created by User on 17.10.2016.
 */

public class ProgramPageFragment extends Fragment {

    private RecyclerView mProgramRecyclerView;
    private String[] mProgramList;

    public static ProgramPageFragment newInstance(String[] program) {
        ProgramPageFragment pageFragment = new ProgramPageFragment();
        Bundle args = new Bundle();
        args.putStringArray(Constants.BUNDLE_PAGE_PROGRAM, program);
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgramList = getArguments().getStringArray(Constants.BUNDLE_PAGE_PROGRAM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        mProgramRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mProgramRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mProgramRecyclerView.setLayoutManager(layoutManager);

        ProgramListAdapter programListAdapter = new ProgramListAdapter(mProgramList);
        mProgramRecyclerView.setAdapter(programListAdapter);

        return view;
    }
}

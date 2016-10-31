package oleg.hubal.com.programlab.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

import oleg.hubal.com.programlab.fragment.ProgramPageFragment;

/**
 * Created by User on 18.10.2016.
 */

public class ProgramPagerAdapter extends FragmentStatePagerAdapter {

    private TreeMap<String, ArrayList<String>> mProgramMap;
    private ArrayList<String> showIdList;

    public ProgramPagerAdapter(FragmentManager fm,
                               TreeMap<String, ArrayList<String>> programMap) {
        super(fm);
        init(programMap);
    }

    private void init(TreeMap<String, ArrayList<String>> programMap) {
        mProgramMap = programMap;
        Set<String> showIdSet = mProgramMap.keySet();
        showIdList = new ArrayList<>();
        showIdList.addAll(showIdSet);
    }

    @Override
    public Fragment getItem(int position) {
        String showID = showIdList.get(position);
        ArrayList<String> programList = mProgramMap.get(showID);
        String[] programArray = programList.toArray(new String[programList.size()]);
        return ProgramPageFragment.newInstance(programArray);
    }

    @Override
    public int getItemPosition(Object object) {
        return ProgramPagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mProgramMap.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return showIdList.size() == 0 ? "" : showIdList.get(position);
    }

    public void swapData(TreeMap<String, ArrayList<String>> programMap) {
        init(programMap);
        notifyDataSetChanged();
    }
}

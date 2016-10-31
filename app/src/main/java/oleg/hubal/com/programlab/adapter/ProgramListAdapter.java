package oleg.hubal.com.programlab.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import oleg.hubal.com.programlab.R;

/**
 * Created by User on 18.10.2016.
 */

public class ProgramListAdapter extends RecyclerView.Adapter<ProgramListAdapter.ViewHolder> {

    private String[] mProgramList;

    public ProgramListAdapter(String[] programList) {
        mProgramList = programList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.card_program, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvProgram.setText(mProgramList[position]);
    }

    @Override
    public int getItemCount() {
        return mProgramList.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvProgram;

        public ViewHolder(View itemView) {
            super(itemView);
            tvProgram = (TextView) itemView.findViewById(R.id.tvProgram);
        }
    }
}

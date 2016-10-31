package oleg.hubal.com.programlab.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import oleg.hubal.com.programlab.Constants;
import oleg.hubal.com.programlab.R;

import static android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Created by User on 15.10.2016.
 */

public class ChannelsAdapter extends CursorRecyclerAdapter<ChannelsAdapter.ChannelViewHolder> {

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public ChannelsAdapter(Cursor cursor, OnCheckedChangeListener onCheckedChangeListener) {
        super(cursor);
        mOnCheckedChangeListener = onCheckedChangeListener;
    }


    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_channel, parent, false);

        return new ChannelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(Constants.CURSOR_CHANNELS_NAME));
        String tvURL = cursor.getString(cursor.getColumnIndex(Constants.CURSOR_CHANNELS_TV_URL));
        String category = cursor.getString(cursor.getColumnIndex(Constants.CURSOR_CHANNELS_CATEGORY));
        boolean favorite = cursor.getInt(cursor.getColumnIndex(Constants.CURSOR_CHANNELS_FAVORITE)) == 1;

        holder.tvName.setText(name);
        holder.tvTvURL.setText(tvURL);
        holder.tvCategory.setText(category);

        holder.cbFavorite.setTag(name);
        holder.cbFavorite.setOnCheckedChangeListener(null);
        holder.cbFavorite.setChecked(favorite);
        holder.cbFavorite.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        return super.swapCursor(newCursor);
    }

    class ChannelViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvTvURL, tvCategory;
        CheckBox cbFavorite;

        ChannelViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvChannelName);
            tvTvURL = (TextView) itemView.findViewById(R.id.tvChannelURL);
            tvCategory = (TextView) itemView.findViewById(R.id.tvChannelCategory);
            cbFavorite = (CheckBox) itemView.findViewById(R.id.cbChannelFavorite);
        }
    }
}

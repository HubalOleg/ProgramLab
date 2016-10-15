package oleg.hubal.com.programlab.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import oleg.hubal.com.programlab.Constants;
import oleg.hubal.com.programlab.OnChangeFavoriteState;
import oleg.hubal.com.programlab.R;

/**
 * Created by User on 15.10.2016.
 */

public class ChannelAdapter extends CursorRecyclerAdapter<ChannelAdapter.ChannelViewHolder>
        implements CompoundButton.OnCheckedChangeListener {

    private String name, tvURL, category;

    private OnChangeFavoriteState mOnChangeFavoriteState;

    public ChannelAdapter(Cursor cursor, OnChangeFavoriteState onChangeFavoriteState) {
        super(cursor);
        mOnChangeFavoriteState = onChangeFavoriteState;
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_channel, parent, false);

        return new ChannelViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, Cursor cursor) {
        getCursorData(cursor);

        holder.tvName.setText(name);
        holder.tvTvURL.setText(tvURL);
        holder.tvCategory.setText(category);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        return super.swapCursor(newCursor);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mOnChangeFavoriteState.changeFavoriteState(name, isChecked);
    }

    private void getCursorData(Cursor cursor) {
        name = cursor.getString(cursor.getColumnIndex(Constants.CURSOR_CHANNEL_NAME));
        tvURL = cursor.getString(cursor.getColumnIndex(Constants.CURSOR_CHANNEL_TV_URL));
        category = cursor.getString(cursor.getColumnIndex(Constants.CURSOR_CHANNEL_CATEGORY));
    }

    class ChannelViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvTvURL, tvCategory;
        CheckBox chkFavorite;

        ChannelViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvChannelName);
            tvTvURL = (TextView) itemView.findViewById(R.id.tvChannelURL);
            tvCategory = (TextView) itemView.findViewById(R.id.tvChannelCategory);
            chkFavorite = (CheckBox) itemView.findViewById(R.id.chkChannelFavorite);

            chkFavorite.setOnCheckedChangeListener(ChannelAdapter.this);
        }
    }
}

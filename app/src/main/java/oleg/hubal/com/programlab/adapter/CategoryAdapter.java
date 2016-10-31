package oleg.hubal.com.programlab.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import oleg.hubal.com.programlab.Constants;
import oleg.hubal.com.programlab.R;

import static android.view.View.*;

/**
 * Created by User on 15.10.2016.
 */

public class CategoryAdapter extends CursorRecyclerAdapter<CategoryAdapter.CategoryViewHolder> {

    private OnClickListener mOnClickListener;

    public CategoryAdapter(Cursor cursor, OnClickListener onClickListener) {
        super(cursor);
        mOnClickListener = onClickListener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category, parent, false);

        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(Constants.CURSOR_CATEGORY_NAME));

        holder.itemView.setTag(name);
        holder.itemView.setOnClickListener(mOnClickListener);
        holder.tvName.setText(name);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        return super.swapCursor(newCursor);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        CategoryViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvCategoryName);
        }
    }
}

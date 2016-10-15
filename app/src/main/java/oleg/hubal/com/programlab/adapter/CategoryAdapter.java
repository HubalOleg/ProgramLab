package oleg.hubal.com.programlab.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import oleg.hubal.com.programlab.Constants;
import oleg.hubal.com.programlab.R;

/**
 * Created by User on 15.10.2016.
 */

public class CategoryAdapter extends
        CursorRecyclerAdapter<CategoryAdapter.CategoryViewHolder> {

    private String name;

    public CategoryAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_category, parent, false);

        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.CategoryViewHolder holder, Cursor cursor) {
        getCursorData(cursor);

        holder.tvName.setText(name);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        return super.swapCursor(newCursor);
    }

    private void getCursorData(Cursor cursor) {
        name = cursor.getString(cursor.getColumnIndex(Constants.CURSOR_CATEGORY_NAME));
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvTvURL, tvCategory;

        CategoryViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvCategoryName);
        }
    }
}

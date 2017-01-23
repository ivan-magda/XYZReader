package com.example.xyzreader.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.ui.ImageLoaderHelper;
import com.example.xyzreader.ui.view.DynamicHeightNetworkImageView;

public final class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {

    public interface OnClickHandler {
        void onClick(int position, long itemId);
    }

    private Cursor mCursor;
    private Context mContext;
    private OnClickHandler mOnClickHandler;

    public ArticleListAdapter(Cursor cursor, Context context, OnClickHandler onClickHandler) {
        mCursor = cursor;
        mContext = context;
        mOnClickHandler = onClickHandler;
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getLong(ArticleLoader.Query._ID);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.mTitleView.setText(mCursor.getString(ArticleLoader.Query.TITLE));
        holder.mSubtitleView.setText(
                DateUtils.getRelativeTimeSpanString(
                        mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                        System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_ALL).toString()
                        + " by "
                        + mCursor.getString(ArticleLoader.Query.AUTHOR));
        holder.mThumbnailView.setImageUrl(
                mCursor.getString(ArticleLoader.Query.THUMB_URL),
                ImageLoaderHelper.getInstance(mContext).getImageLoader()
        );
        holder.mThumbnailView.setAspectRatio(mCursor.getFloat(ArticleLoader.Query.ASPECT_RATIO));
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        DynamicHeightNetworkImageView mThumbnailView;
        TextView mTitleView;
        TextView mSubtitleView;

        ViewHolder(View view) {
            super(view);
            mThumbnailView = (DynamicHeightNetworkImageView) view.findViewById(R.id.thumbnail);
            mTitleView = (TextView) view.findViewById(R.id.article_title);
            mSubtitleView = (TextView) view.findViewById(R.id.article_subtitle);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnClickHandler != null) {
                mOnClickHandler.onClick(getAdapterPosition(), getItemId());
            }
        }

    }

}

package com.freelancing.ahmed.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.freelancing.ahmed.popularmovies.Interfaces.ItemClickListener;
import com.freelancing.ahmed.popularmovies.Models.Reviews;
import com.freelancing.ahmed.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 1/16/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {
    private Context mContext;
    int ID;
    String author;
    String content;

    List<Reviews.ReviewsResult> reviews;

    public ReviewsAdapter(Context context,List<Reviews.ReviewsResult> rList){
        mContext=context;
        reviews=rList;
    }


    //////////////////////////////////////// View Holder Class //////////////////////////
    public class ReviewsAdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView mReviewAuthor;
        public TextView mReviewContent;
        private ItemClickListener itemClickListener;

        public ReviewsAdapterViewHolder(View view) {
            super(view);
            // view.setOnClickListener(this);
            mReviewAuthor = (TextView) view.findViewById(R.id.tv_author);
            mReviewContent = (TextView) view.findViewById(R.id.tv_content);

        }
    }
    //////////////////////////////////////////////////


    @Override
    public ReviewsAdapter.ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_list_item, null);
        ReviewsAdapter.ReviewsAdapterViewHolder rcv = new ReviewsAdapter.ReviewsAdapterViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewsAdapterViewHolder holder, int position) {
        //Movies Movies = movies.get(position);

        author = reviews.get(position).getmAuthor();
        content = reviews.get(position).getmContent();

            holder.mReviewAuthor.setText(author);
            holder.mReviewContent.setText(content);

    }

    @Override
    public int getItemCount()
    {
        if (null == reviews)
            return 0;
        return reviews.size();
    }
    public void setReviewsData(List<Reviews.ReviewsResult> ReviewsData) {

        reviews = ReviewsData; // mMovies is a string Array
        notifyDataSetChanged();
    }
}

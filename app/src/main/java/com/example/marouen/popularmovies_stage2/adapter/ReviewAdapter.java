package com.example.marouen.popularmovies_stage2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marouen.popularmovies_stage2.R;
import com.example.marouen.popularmovies_stage2.model.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private static final String LOG_TAG = PosterAdapter.class.getSimpleName();

    private Context mContext;
    private List<Review> reviewList;


    public ReviewAdapter(Context mContext, List<Review> reviewList) {
        this.mContext = mContext;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rv_review_item, viewGroup, false);

        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int i) {

        Review review = reviewList.get(i);

        // Set review author text
        reviewViewHolder.reviewAuthorTextView.setText(review.getAuthor() + " :");

        // Set review content text
        reviewViewHolder.reviewContentTextView.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewAuthorTextView;
        TextView reviewContentTextView;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            reviewAuthorTextView = itemView.findViewById(R.id.tv_review_author);
            reviewContentTextView = itemView.findViewById(R.id.tv_review_content);
        }
    }
}

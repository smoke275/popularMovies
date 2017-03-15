package com.smokescreem.shash.popularmovies.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smokescreem.shash.popularmovies.R;
import com.smokescreem.shash.popularmovies.data.models.Review;

import java.util.List;

/**
 * Created by Shash on 3/14/2017.
 */

public class ReviewAdapter extends BaseAdapter {

    Context context;
    List<Review> reviewArray;
    public ReviewAdapter(Context context, List reviewArray) {
        this.context = context;
        this.reviewArray = reviewArray;
    }

    @Override
    public int getCount() {
        return reviewArray.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null || getItemViewType(position) == 0) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.review_layout, parent, false);
            }
            Review review = reviewArray.get(position);
            Log.d("Review", position + " " + review.getAuthor());
            TextView reviewAuthor = (TextView) convertView.findViewById(R.id.review_author);
            TextView reviewText = (TextView) convertView.findViewById(R.id.review_text);
            reviewAuthor.setText(review.getAuthor());
            reviewText.setText(review.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
}
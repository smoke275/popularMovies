package com.smokescreem.shash.popularmovies.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smokescreem.shash.popularmovies.R;
import com.smokescreem.shash.popularmovies.data.models.Video;
import com.smokescreem.shash.popularmovies.utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shash on 3/14/2017.
 */

public class TrailerAdapter extends BaseAdapter {

    Context context;
    List<Video> trailerArray;

    public TrailerAdapter(Context context, List trailerArray) {
        this.context = context;
        this.trailerArray = trailerArray;
    }

    @Override
    public int getCount() {
        return trailerArray.size();
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

        if (convertView == null || getItemViewType(position) == 0) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.trailer_layout, parent, false);
        }
        ImageView trailerImage = (ImageView) convertView.findViewById(R.id.trailer_image);
        TextView trailerText = (TextView) convertView.findViewById(R.id.trailer_text);
        Video trailer = trailerArray.get(position);

        String posterURL = Constants.MOVIESDB_YOUTUBE_IMG_BASE_URL_PREFIX + trailer.getKey() + Constants.MOVIESDB_YOUTUBE_IMG_BASE_URL_POSTFIX;
        Log.d("Trailer", posterURL);
        Picasso.with(context)
                .load(posterURL)
                .placeholder(android.R.drawable.ic_media_play)
                .into(trailerImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d("Trailer", "Success");
                    }

                    @Override
                    public void onError() {
                        Log.d("Trailer", "Failed");
                    }
                });

        trailerText.setText(trailer.getName());

        return convertView;
    }
}
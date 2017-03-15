package com.smokescreem.shash.popularmovies.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.smokescreem.shash.popularmovies.utils.Constants;
import com.smokescreem.shash.popularmovies.R;
import com.smokescreem.shash.popularmovies.data.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Shash on 11/2/2016.
 * A custom arrayAdapter to manage the incoming data and use it in the grid
 */

public class MovieInfoArrayAdapter extends ArrayAdapter<Movie> {
    private final String LOG_TAG=MovieInfoArrayAdapter.class.getSimpleName();
    private List<Movie> mMovieInfoList=null;

    public MovieInfoArrayAdapter(Context context, int resource, int textViewResourceId, List<Movie> objects) {
        super(context, resource, textViewResourceId, objects);
        this.mMovieInfoList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View currentView = convertView;
        if(currentView == null){
            /*
            Rendering a view incase it is not yet rendered
             */
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            currentView = inflater.inflate(R.layout.list_item_movies_images,null);
        }
        /*
        Fetching the movie data from the list using the position supplied by the function
         */
        Movie movieData = mMovieInfoList.get(position);
        if(movieData !=null){
            ImageView imageView = (ImageView)currentView.findViewById(R.id.list_item_movie_image_view);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Uri builtUri = Uri.parse(Constants.MOVIESDB_IMAGES_BASE_URL).buildUpon().
                    appendEncodedPath(movieData.getPosterPath())
                    .build();
            Picasso.with(getContext()).load(builtUri.toString()).into(imageView);
        }
        return currentView;
    }

    @Override
    public int getCount() {
        return mMovieInfoList.size();
    }
}

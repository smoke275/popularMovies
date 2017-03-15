package com.smokescreem.shash.popularmovies.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smokescreem.shash.popularmovies.R;
import com.smokescreem.shash.popularmovies.data.models.Movie;
import com.smokescreem.shash.popularmovies.data.provider.Columns;
import com.smokescreem.shash.popularmovies.data.provider.MovieProvider;
import com.smokescreem.shash.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shash on 12/17/2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Movie> mMovieList;
    private Context mContext = null;

    public RecyclerAdapter(List<Movie> movieList, Context context){
        mMovieList = movieList;
        mContext = context;
    }

    public void updateData(List<Movie> movieList){
        mMovieList.clear();
        mMovieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card_movie,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movieData = mMovieList.get(position);
        if(movieData !=null){
            ImageView imageView = holder.imageView;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Uri builtUri = Uri.parse(Constants.MOVIESDB_IMAGES_BASE_URL).buildUpon().
                    appendEncodedPath(movieData.getPosterPath())
                    .build();
            Picasso.with(imageView.getContext())
                    .load(builtUri.toString())
                    .resize(400,400)
                    .into(imageView);
        }
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.list_item_movie_image_view) ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Movie movieData = mMovieList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(Constants.MOVIE_DATA, movieData);
                    Cursor cursor = null;
                    try {
                        cursor = mContext.getContentResolver().query(
                                MovieProvider.Movies.CONTENT_URI,
                                null,
                                Columns.TITLE + "=?",
                                new String[]{movieData.getTitle()},
                                null
                        );
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                    bundle.putBoolean("is_favorite", cursor.getCount() > 0);
                    ((DetailsCallback)mContext).onItemSelected(bundle);
                }
            });

        }
    }

    public interface DetailsCallback{
        public void onItemSelected(Bundle bundle);
    }
}

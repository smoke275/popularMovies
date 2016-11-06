package com.smokescreem.shash.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.smokescreem.shash.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {

    @BindView(R.id.movieTitle)
    TextView movieTitle;
    @BindView(R.id.releaseDate)
    TextView releaseDate;
    @BindView(R.id.movieRatingText)
    TextView averageRating;
    @BindView(R.id.movieOverview)
    TextView movieOverview;
    @BindView(R.id.favouriteText)
    TextView favouriteText;
    @BindView(R.id.movieRating)
    RatingBar rating;
    @BindView(R.id.moviePoster)
    ImageView moviePoster;
    @BindView(R.id.favouriteIcon)
    ImageView favouriteIcon;
    @BindView(R.id.backgroundLayout)
    LinearLayout backgroundLayout;
    @BindView(R.id.favourite_layout)
    LinearLayout favouriteLayout;
    @BindView(R.id.trailer_holder)
    LinearLayout trailerList;
    @BindView(R.id.review_holder)
    LinearLayout reviewList;
    private boolean isFavourite = false;
    private boolean isTrailer = true;
    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        prepareView();
    }

    private void prepareView(){
        Intent intent = getActivity().getIntent();
        Movie movieData = (Movie)intent.getSerializableExtra(Constants.MOVIE_DATA);
        moviePoster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Uri builtUri = Uri.parse(Constants.MOVIESDB_IMAGES_BASE_URL).buildUpon().
                appendEncodedPath(movieData.getPosterPath())
                .build();
        Picasso.with(getContext()).load(builtUri.toString()).into(moviePoster);
        releaseDate.setText(movieData.getReleaseDate());
        rating.setRating(movieData.getVoteAverage().floatValue());
        movieTitle.setText(movieData.getOriginalTitle());
        movieOverview.setText(movieData.getOverview());
    }
}

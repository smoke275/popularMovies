package com.smokescreem.shash.popularmovies.ui.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.smokescreem.shash.popularmovies.BuildConfig;
import com.smokescreem.shash.popularmovies.R;
import com.smokescreem.shash.popularmovies.data.api.MovieApiClient;
import com.smokescreem.shash.popularmovies.data.api.MovieDbAPI;
import com.smokescreem.shash.popularmovies.data.models.Movie;
import com.smokescreem.shash.popularmovies.data.models.Review;
import com.smokescreem.shash.popularmovies.data.models.Video;
import com.smokescreem.shash.popularmovies.data.provider.Columns;
import com.smokescreem.shash.popularmovies.data.provider.MovieProvider;
import com.smokescreem.shash.popularmovies.ui.adapters.ReviewAdapter;
import com.smokescreem.shash.popularmovies.ui.adapters.TrailerAdapter;
import com.smokescreem.shash.popularmovies.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {
    private final String LOG_TAG=MovieDetailFragment.class.getSimpleName();

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
        Bundle info = getActivity().getIntent().getExtras();
        if (info == null)
            info = getArguments();
        if (info != null) {
            // if the info bundle sent from the MainActivity is not null then the different visual
            // elements are set to their respective information
            prepareView(info);
        }

    }

    public void updateMovieDetails(Bundle bundle){
        if (bundle != null) {
            // if the info bundle sent from the MainActivity is not null then the different visual
            // elements are set to their respective information
            prepareView(bundle);
        }
    }

    private void prepareView(Bundle bundle){
        final Movie movieData = bundle.getParcelable(Constants.MOVIE_DATA);
        moviePoster.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Uri builtUri = Uri.parse(Constants.MOVIESDB_IMAGES_BASE_URL).buildUpon().
                appendEncodedPath(movieData.getPosterPath())
                .build();
        Picasso.with(getContext()).load(builtUri.toString()).into(moviePoster);
        releaseDate.setText(movieData.getReleaseDate());
        rating.setRating(((Double)movieData.getVoteAverage()).floatValue());
        movieTitle.setText(movieData.getTitle());
        movieOverview.setText(movieData.getOverview());
        if (bundle.getBoolean("is_favorite",false)) {
            favouriteIcon.setImageResource(android.R.drawable.star_big_on);
            favouriteText.setText("Favorite");
            isFavourite = true;
        } else {
            favouriteIcon.setImageResource(android.R.drawable.star_big_off);
            favouriteText.setText("Set as Favorite");
            isFavourite = false;
        }

        MovieDbAPI movieDbAPI = MovieApiClient.getClient().create(MovieDbAPI.class);

        Call<Video.Response> trailerCall = movieDbAPI.getTrailer(movieData.getId(), BuildConfig.MOVIES_DB_API_KEY);

        trailerCall.enqueue(new Callback<Video.Response>() {
            @Override
            public void onResponse(Call<Video.Response> call, Response<Video.Response> response) {
                final List<Video> videos = response.body().videos;
                TrailerAdapter trailerAdapter = new TrailerAdapter(getActivity(), videos);
                if (trailerList.getChildCount() > 0)
                    trailerList.removeAllViews();
                for (int i = 0; i < trailerAdapter.getCount(); i++) {
                    View v = trailerAdapter.getView(i, null, null);
                    final int position = i;
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String youtubeUrl = Constants.MOVIESDB_YOUTUBE_VIDEO_BASE_URL_PREFIX +
                                    videos.get(position).getKey();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)));
                        }
                    });
                    trailerList.addView(v);
                }
            }

            @Override
            public void onFailure(Call<Video.Response> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
            }
        });

        Call<Review.Response> reviewCall = movieDbAPI.getReviews(movieData.getId(), BuildConfig.MOVIES_DB_API_KEY);

        reviewCall.enqueue(new Callback<Review.Response>() {
            @Override
            public void onResponse(Call<Review.Response> call, Response<Review.Response> response) {
                List<Review> reviews = response.body().reviews;
                ReviewAdapter reviewAdapter = new ReviewAdapter(getActivity(), reviews);
                if(reviewList.getChildCount() > 0)
                    reviewList.removeAllViews();
                for (int i = 0; i < reviewAdapter.getCount(); i++) {
                    View v = reviewAdapter.getView(i, null, null);
                    reviewList.addView(v);
                }
            }

            @Override
            public void onFailure(Call<Review.Response> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
            }
        });

        favouriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavourite) {
                    favouriteIcon.setImageResource(android.R.drawable.star_big_off);
                    favouriteText.setText("Set as favourite");
                        getActivity().getContentResolver().delete(
                                MovieProvider.Movies.CONTENT_URI,
                                Columns.TITLE + "=?", new String[]{movieData.getTitle()});


                } else {
                    Cursor cursor = null;
                    try {
                        favouriteIcon.setImageResource(android.R.drawable.star_big_on);
                        favouriteText.setText("Favourite");

                        ContentValues values = new ContentValues();
                        values.put(Columns._ID, movieData.getId());
                        values.put(Columns.POSTER_PATH, movieData.getPosterPath());
                        values.put(Columns.OVERVIEW, movieData.getOverview());
                        values.put(Columns.RELEASE_DATE, movieData.getReleaseDate());
                        values.put(Columns.TITLE, movieData.getTitle());
                        values.put(Columns.BACKDROP_PATH, movieData.getBackdropPath());
                        values.put(Columns.VOTE_AVERAGE, movieData.getVoteAverage());
                        values.put(Columns.VOTE_COUNT, movieData.getVoteCount());

                        Uri uri = getActivity().getContentResolver().insert(MovieProvider.Movies.CONTENT_URI, values);

                        Log.d("fav", uri.toString());

                        cursor = getActivity().getContentResolver().query(
                                MovieProvider.Movies.CONTENT_URI,
                                null, null, null, null
                        );

                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            Log.d("Cursor", cursor.getString(cursor.getColumnIndex(Columns.TITLE)));
                            cursor.moveToNext();
                        }


                    }  finally {
                        if (cursor != null)
                            cursor.close();
                    }

                }
                isFavourite = !isFavourite;
            }
        });
    }
}

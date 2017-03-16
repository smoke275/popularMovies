package com.smokescreem.shash.popularmovies.ui.fragment;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.smokescreem.shash.popularmovies.BuildConfig;
import com.smokescreem.shash.popularmovies.R;
import com.smokescreem.shash.popularmovies.data.api.MovieApiClient;
import com.smokescreem.shash.popularmovies.data.api.MovieDbAPI;
import com.smokescreem.shash.popularmovies.data.models.Movie;
import com.smokescreem.shash.popularmovies.data.provider.Columns;
import com.smokescreem.shash.popularmovies.data.provider.MovieProvider;
import com.smokescreem.shash.popularmovies.ui.adapters.RecyclerAdapter;
import com.smokescreem.shash.popularmovies.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    private final String LOG_TAG=DashboardFragment.class.getSimpleName();

    private static final String BUNDLE_RECYCLER_LAYOUT = "DashboardFragment.recycler.layout";
    private List mMovieList;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private static final int LOADER_SEARCH_RESULTS = 1;

    Parcelable mLayoutManagerSavedState = null;

    public DashboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null)
        {
            mLayoutManagerSavedState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard_card, container, false);
        ButterKnife.bind(this,rootView);
        layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        mMovieList = new ArrayList();
        adapter = new RecyclerAdapter(mMovieList,getContext());
        recyclerView.setAdapter(adapter);


        return rootView;
    }

    private void updateMovieList() {
        MovieDbAPI movieDbAPI = MovieApiClient.getClient().create(MovieDbAPI.class);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortKey =  sharedPreferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular_api));

        Log.v(LOG_TAG,"Sort key : "+sortKey);
        if (!sortKey.equalsIgnoreCase(Constants.FAVORITES)) {
            Call<Movie.Response> call = movieDbAPI.getMovies(sortKey, BuildConfig.MOVIES_DB_API_KEY);
            call.enqueue(new Callback<Movie.Response>() {
                @Override
                public void onResponse(Call<Movie.Response> call, Response<Movie.Response> response) {
                    List<Movie> movies = response.body().movies;
                    mMovieList = movies;
                    ((RecyclerAdapter)adapter).updateData(movies);
                    if (mLayoutManagerSavedState != null) {
                        recyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
                    }
                }

                @Override
                public void onFailure(Call<Movie.Response> call, Throwable t) {
                    Log.e(LOG_TAG, t.toString());
                }
            });
        } else {
            Cursor cursor = null;
            List mFavoriteMovieList = new ArrayList<Movie>();
            try {
                cursor = getActivity().getContentResolver().query(
                        MovieProvider.Movies.CONTENT_URI, null, null, null, null
                );

                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Log.d("Cursor", cursor.getString(cursor.getColumnIndex(Columns.TITLE)));

                    Movie favoriteMovie = new Movie();
                    favoriteMovie.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(Columns._ID))));
                    favoriteMovie.setTitle(cursor.getString(cursor.getColumnIndex(Columns.TITLE)));
                    favoriteMovie.setPosterPath(cursor.getString(cursor.getColumnIndex(Columns.POSTER_PATH)));
                    favoriteMovie.setOverview(cursor.getString(cursor.getColumnIndex(Columns.OVERVIEW)));
                    favoriteMovie.setReleaseDate(cursor.getString(cursor.getColumnIndex(Columns.RELEASE_DATE)));
                    favoriteMovie.setBackdropPath(cursor.getString(cursor.getColumnIndex(Columns.BACKDROP_PATH)));
                    favoriteMovie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(Columns.VOTE_AVERAGE))));
                    favoriteMovie.setVoteCount(Long.parseLong(cursor.getString(cursor.getColumnIndex(Columns.VOTE_COUNT))));

                    mFavoriteMovieList.add(favoriteMovie);
                    cursor.moveToNext();
                }
                ((RecyclerAdapter)adapter).updateData(mFavoriteMovieList);

                if (mLayoutManagerSavedState != null) {
                    recyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}

package com.smokescreem.shash.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.smokescreem.shash.popularmovies.models.Movie;
import com.smokescreem.shash.popularmovies.api.MovieApiClient;
import com.smokescreem.shash.popularmovies.models.MovieData;
import com.smokescreem.shash.popularmovies.api.MovieDbAPI;
import com.smokescreem.shash.popularmovies.adapters.MovieInfoArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A Simple Placeholder class for the movie grid
 */
public class DashboardFragment extends Fragment {
    private final String LOG_TAG=DashboardFragment.class.getSimpleName();
    private MovieInfoArrayAdapter mMovieAdapter;

    public DashboardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mMovieAdapter = new MovieInfoArrayAdapter(getActivity(),
                //layout
                R.layout.list_item_movies_images,
                //id of view
                R.id.list_item_movie_image_view,
                new ArrayList<Movie>());
        GridView gridViewForecast = (GridView)rootView.findViewById(R.id.gridview_movies);
        gridViewForecast.setAdapter(mMovieAdapter);
        gridViewForecast.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Movie movieData = mMovieAdapter.getItem(position);
                Intent intent = new Intent(adapterView.getContext(),MovieDetailActivity.class);
                Bundle dataBundle = new Bundle();
                intent.putExtra(Constants.MOVIE_DATA, movieData);
                startActivity(intent);
            }
        });

        return rootView;
    }
    private void updateMovieList() {
        MovieDbAPI movieDbAPI = MovieApiClient.getClient().create(MovieDbAPI.class);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortKey =  sharedPreferences.getString(getString(R.string.pref_sort_key),
                getString(R.string.pref_sort_popular_api));

        Log.v(LOG_TAG,"Sort key : "+sortKey);
        Call<MovieData> call = movieDbAPI.getMovies(sortKey,BuildConfig.MOVIES_DB_API_KEY);
        call.enqueue(new Callback<MovieData>() {
            @Override
            public void onResponse(Call<MovieData> call, Response<MovieData> response) {
                List<Movie> movies = response.body().getResults();
                mMovieAdapter.clear();
                for(Movie movie:movies){
                    mMovieAdapter.add(movie);
                }
            }

            @Override
            public void onFailure(Call<MovieData> call, Throwable t) {
                Log.e(LOG_TAG, t.toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();
    }

}

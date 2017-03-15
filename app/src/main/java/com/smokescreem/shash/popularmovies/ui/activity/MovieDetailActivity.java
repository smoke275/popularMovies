package com.smokescreem.shash.popularmovies.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.smokescreem.shash.popularmovies.R;
import com.smokescreem.shash.popularmovies.ui.fragment.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, new MovieDetailFragment())
                    .commit();
        }
    }

}

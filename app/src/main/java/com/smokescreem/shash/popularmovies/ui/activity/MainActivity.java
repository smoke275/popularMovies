package com.smokescreem.shash.popularmovies.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.smokescreem.shash.popularmovies.R;
import com.smokescreem.shash.popularmovies.ui.adapters.RecyclerAdapter;
import com.smokescreem.shash.popularmovies.ui.fragment.MovieDetailFragment;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.DetailsCallback {

    public static boolean isTwoPane = false;
    public static String DETAILS_TAG = "DETAILS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_card);
        if (findViewById(R.id.fragment_holder) != null) {
            isTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_holder, new MovieDetailFragment(), DETAILS_TAG)
                        .commit();

            }
        } else {
            isTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Bundle bundle) {
        if (isTwoPane) {
            MovieDetailFragment detailsFragment;
            detailsFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentByTag(DETAILS_TAG);
            if (detailsFragment != null)
                detailsFragment.updateMovieDetails(bundle);

        }else{
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}

package com.vn.popcorn.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;
import com.vn.popcorn.R;
import com.vn.popcorn.beans.MovieItem;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        // Retrieve the AppCompact Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set the padding to match the Status Bar height
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);

        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);
        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set the transparent color of the status bar, 20% darker
        tintManager.setTintColor(Color.parseColor(getString(R.string.tintcolor)));


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MovieItem.EXTRA_MOVIE)) {
            MovieItem movie = new MovieItem(intent.getBundleExtra(MovieItem.EXTRA_MOVIE));
            ((TextView) findViewById(R.id.movie_title)).setText(movie.getmTitle());
            ((TextView) findViewById(R.id.movie_average_rating)).setText(movie.getRating());
            ((TextView) findViewById(R.id.movie_plot)).setText(movie.getmOverview());
            ((TextView) findViewById(R.id.movie_release_date)).setText(movie.getmReleaseDate());


            Uri posterUri = movie.buildPosterUri(getString(R.string.api_poster_default_size));
            Picasso.with(this)
                    .load(posterUri)
                    .fit()
                    .placeholder(R.drawable.placeholder)
                    .into((ImageView) findViewById(R.id.imageView));
        }

    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


}

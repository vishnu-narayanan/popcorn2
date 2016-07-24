package com.vn.popcorn.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;
import com.vn.popcorn.R;
import com.vn.popcorn.model.MovieItem;
import com.vn.popcorn.model.Review;
import com.vn.popcorn.model.ReviewsResponse;
import com.vn.popcorn.model.Trailer;
import com.vn.popcorn.model.TrailerResponse;
import com.vn.popcorn.rest.ApiClient;
import com.vn.popcorn.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {


    private static final String TAG = DetailActivity.class.getSimpleName();
    private String movieId;

    public TextView numberOfReviews;
    public LinearLayout reviewDetailContainer;
    private ImageView trailerImage;
    private TextView noTrailerFound;

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

        numberOfReviews = (TextView) findViewById(R.id.number_of_reviews);
        reviewDetailContainer = (LinearLayout) findViewById(R.id.review_detail_container);
        trailerImage = (ImageView) findViewById(R.id.trailer);
        noTrailerFound = (TextView) findViewById(R.id.no_trailers);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(MovieItem.EXTRA_MOVIE)) {
            MovieItem movie = new MovieItem(intent.getBundleExtra(MovieItem.EXTRA_MOVIE));
            ((TextView) findViewById(R.id.movie_title)).setText(movie.getmTitle());
            ((TextView) findViewById(R.id.movie_average_rating)).setText(movie.getRating());
            ((TextView) findViewById(R.id.movie_plot)).setText(movie.getmOverview());
            ((TextView) findViewById(R.id.movie_release_date)).setText(movie.getmReleaseDate());
            movieId = movie.getId();


            Uri posterUri = movie.buildPosterUri(getString(R.string.api_poster_default_size));
            Picasso.with(this)
                    .load(posterUri)
                    .fit()
                    .placeholder(R.drawable.placeholder)
                    .into((ImageView) findViewById(R.id.imageView));
        }

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final String key = getString(R.string.api);

        Call<ReviewsResponse> call = apiService.getMovieDetails(Integer.parseInt(movieId), key);
        call.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                if (response.body() == null) {
                    Log.d(TAG, String.valueOf(call.request().url()));
                    setEmptyView();
                } else {
                    List<Review> reviews = response.body().getResults();
                    Log.d(TAG, "Number of reviews received: " + reviews.size());
                    setReviews(response.body());
                    //recyclerView.setAdapter(new MoviesAdapter(movies,R.layout.list_item_movie,getApplicationContext()));
                }
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                //Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

        Call<TrailerResponse> call1 = apiService.getMovieTrailer(Integer.parseInt(movieId), key);
        call1.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if (response.body() == null) {

                } else {
                    List<Trailer> trailers = response.body().getResults();
                    setTrailer(response.body());
                }
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }

    private void setTrailer(TrailerResponse trailers) {
        if (trailers.getResults().isEmpty()) {
            noTrailerFound.setVisibility(View.VISIBLE);
        } else {
            boolean first = true;
            for (final Trailer trailer : trailers.getResults()) {
                if (first) {
                    Uri trailerUri = buildTrailerUri(trailer.getKey());

                    Picasso.with(getApplicationContext())
                            .load(trailerUri)
                            .fit()
                            .placeholder(R.drawable.placeholder_trailer)
                            .into(trailerImage);

                    trailerImage.setVisibility(View.VISIBLE);
                    trailerImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openYoutube(trailer.getKey());
                        }
                    });
                    first = false;
                }
            }

        }

    }

    private Uri buildTrailerUri(String key) {
        final String BASE_URL = "http://img.youtube.com/vi/";
        String quality = "mqdefault.jpg";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(key)
                .appendEncodedPath(quality)
                .build();
        Log.d(TAG, builtUri.toString());

        return builtUri;

    }

    private void openYoutube(String id) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id));
            startActivity(intent);
        }
    }

    private void setReviews(ReviewsResponse reviews) {

        if (reviews.getResults().isEmpty()) {
            setEmptyView();
        } else {
            reviewDetailContainer.removeAllViews();
            boolean first = true;
            for (Review review : reviews.getResults()) {
                LinearLayout container = (LinearLayout) View.inflate(this, R.layout.review_item, null);
                if (first) {
                    first = false;
                } else {
                    View separator = container.findViewById(R.id.separator);
                    separator.setVisibility(View.VISIBLE);
                }
                TextView reviewAuthor = (TextView) container.findViewById(R.id.review_author);
                final TextView reviewContent = (TextView) container.findViewById(R.id.review_content);
                reviewAuthor.setText(review.getAuthor());
                reviewContent.setText(review.getContent());
                reviewDetailContainer.addView(container);
            }
            numberOfReviews.setText(String.valueOf(reviews.getResults().size()));

        }
    }

    private void setEmptyView() {
        numberOfReviews.setText(String.valueOf(0));
        TextView emptyText = new TextView(this);
        emptyText.setText(R.string.no_reviews_yet);
        emptyText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.grey_500));
        reviewDetailContainer.removeAllViews();
        reviewDetailContainer.addView(emptyText);
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

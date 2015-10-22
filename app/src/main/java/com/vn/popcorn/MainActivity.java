package com.vn.popcorn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<MovieItem> mItems;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        this this = getApplicationthis();
//        ImageView imageView = (ImageView) findViewById(R.id.imageView);
//        Picasso.with(this).load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg").resize(185, 277).into(imageView);


        // Retrieve the AppCompact Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mItems = new ArrayList<>();

        // Set the padding to match the Status Bar height
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);

        // create our manager instance after the content view is set
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // enable status bar tint
        tintManager.setStatusBarTintEnabled(true);

        // enable navigation bar tint
        tintManager.setNavigationBarTintEnabled(true);
        // set the transparent color of the status bar, 20% darker
        tintManager.setTintColor(Color.parseColor("#20000000"));


        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // The number of Columns
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        updatemovies();
//
//        mAdapter = new GridAdapter(this, mItems);
//        mRecyclerView.setAdapter(mAdapter);
//


//        );

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        updatemovies();
//
//    }

    public void updatemovies() {

        FetchMovieTask movieTask = new FetchMovieTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String preference = prefs.getString(this.getString(R.string.pref_sort_order),
                this.getString(R.string.pref_sort_default));
        movieTask.execute(preference);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        private ProgressDialog pDialog;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }



        /**
         * Take the String representing the complete movie data in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[] getMovieDataFromJson(String movieJsonStr, int numMovies)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TMDB_RESULT = "results";
            final String TMDB_POSTER = "poster_path";
//            final String TMDB_TEMPERATURE = "temp";
//            final String TMDB_MAX = "max";
//            final String TMDB_MIN = "min";
//            final String TMDB_DESCRIPTION = "main";
//
//            //code to parse preference data from json for MapView
//            final String TMDB_CITY = "city";
//            final String TMDB_COORDINATES = "coord";
//            final String TMDB_LONG = "lon";
//            final String TMDB_LAT = "lat";


            JSONObject movieJson = new JSONObject(movieJsonStr);
            Log.v(LOG_TAG, "movieJson " + movieJson);
            JSONArray movieArray = movieJson.getJSONArray(TMDB_RESULT);

//            String poster = movieArray.getString(TMDB_POSTER);

            //code to parse preference data from JSON for MapView

//            JSONObject cityObject = movieJson.getJSONObject(TMDB_CITY);
//            JSONObject coordinateObject = cityObject.getJSONObject(TMDB_COORDINATES);
//
//            double lon = coordinateObject.getDouble(TMDB_LONG);
//            double lat = coordinateObject.getDouble(TMDB_LAT);

//            Log.v(LOG_TAG,"CityObject" + cityObject);
//            Log.v(LOG_TAG, "Longitude" + lon);
//            Log.v(LOG_TAG, "Latitude" + lat);

//            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//            SharedPreferences.Editor editor = sharedPref.edit();
//            editor.putLong("Latitude", Double.doubleToLongBits(lat));
//            editor.putLong("Longitude", Double.doubleToLongBits(lon));
//            editor.commit();


            String[] resultStrs = new String[numMovies];


            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String sortType = sharedPrefs.getString(getString(R.string.pref_sort_order),
                    getString(R.string.pref_sort_default));


            for (int i = 0; i < movieArray.length(); i++) {
                String poster;
                // Get the JSON object representing the day
                JSONObject movie = movieArray.getJSONObject(i);
                poster = movie.getString(TMDB_POSTER);
                resultStrs[i] = "http://image.tmdb.org/t/p/w185/"+poster;
            }

            for (String s : resultStrs) {
                Log.v(LOG_TAG, "Movie Poster Paths: " + s);
            }
            return resultStrs;

        }


        @Override
        protected String[] doInBackground(String... params) {


            if (params.length == 0) {
                return null;
            }
            //code for talking to the cloud

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            final String key = getString(R.string.api);

            Log.v(LOG_TAG, "API key --> " + key);

            int numMovies = 20;


            try {
                // Construct the URL for the TMDB Query
                // http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=[YOUR API KEY]


                final String FORECAST_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String API_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
                        .appendQueryParameter(API_PARAM, key)
                        .build();


                URL url = new URL(builtUri.toString());
                Log.v(LOG_TAG, "Built URI -->" + builtUri.toString());


                // Create the request to TMDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Movie Json String : " + movieJsonStr);


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(movieJsonStr, numMovies);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (result != null) {
                // mForecastAdapter.clear();
                for (String movieStr : result) {
                    // GridAdapter.add(movieStr);
                    if(movieStr!=null) {
                        MovieItem movie = new MovieItem();
                        movie.setmImgUrl(movieStr);
                        mItems.add(movie);
                    }

                }
                //new data is back from server woo hoo :P
            }
            setRAdapter();
        }
    }

    private void setRAdapter() {

        mAdapter = new GridAdapter(this, mItems);
        mRecyclerView.setAdapter(mAdapter);



    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

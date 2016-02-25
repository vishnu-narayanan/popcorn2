package com.vn.popcorn.beans;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vn on 7/10/15.
 */
public class MovieItem implements Parcelable{
    private String id;
    private String mImgUrl;
    private String mTitle;
    private String mOverview;
    private String mReleaseDate;
    private String mVoteAverage;

    public static final String EXTRA_MOVIE = "com.vn.popcorn.EXTRA_MOVIE";
    public static final String TMDB_KEY = "id";
    public static final String TMDB_RESULT = "results";
    public static final String TMDB_POSTER = "poster_path";
    public static final String TMDB_TITLE = "original_title";
    public static final String TMDB_OVERVIEW = "overview";
    public static final String TMDB_VOTES = "vote_average";
    public static final String TMDB_RELEASEDATE = "release_date";


    public MovieItem(String id,
                 String title, String overview, String poster_path,
                 String vote_average,String release_date) {
        this.id = id;
        this.mTitle = title;
        this.mOverview = overview;
        this.mImgUrl = poster_path;
        this.mVoteAverage = vote_average;
        this.mReleaseDate = release_date;
    }
    public String getmImgUrl() {
        return mImgUrl;
    }

    public void setmImgUrl(String mImgUrl) {
        this.mImgUrl = mImgUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(String mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    protected MovieItem(Parcel in) {
        mImgUrl = in.readString();
        mTitle = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mVoteAverage = in.readString();
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mImgUrl);
        dest.writeString(mTitle);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeString(mVoteAverage);
    }

    public static MovieItem fromJson(JSONObject jsonObject) throws JSONException{
        return new MovieItem(
                jsonObject.getString(TMDB_KEY),
                jsonObject.getString(TMDB_TITLE),
                jsonObject.getString(TMDB_OVERVIEW),
                jsonObject.getString(TMDB_POSTER),
                jsonObject.getString(TMDB_VOTES),
                jsonObject.getString(TMDB_RELEASEDATE)

        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri buildPosterUri(String size) {
        final String BASE_URL = "http://image.tmdb.org/t/p/";

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(size)
                .appendEncodedPath(mImgUrl)
                .build();

        return builtUri;
    }


    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        bundle.putString(TMDB_KEY, id);
        bundle.putString(TMDB_TITLE, mTitle);
        bundle.putString(TMDB_OVERVIEW, mOverview);
        bundle.putString(TMDB_POSTER, mImgUrl);
        bundle.putString(TMDB_VOTES, mVoteAverage);
        bundle.putString(TMDB_RELEASEDATE, mReleaseDate);


        return bundle;
    }

    public MovieItem(Bundle bundle) {
        this(
                bundle.getString(TMDB_KEY),
                bundle.getString(TMDB_TITLE),
                bundle.getString(TMDB_OVERVIEW),
                bundle.getString(TMDB_POSTER),
                bundle.getString(TMDB_VOTES),
                bundle.getString(TMDB_RELEASEDATE)
        );
    }
    public String getRating() {
        return "" + mVoteAverage + " / 10";
    }
//    public String getmImgUrl() {
//        return mImgUrl;
//    }
//
//    public void setmImgUrl(String mImgUrl) {
//        this.mImgUrl = mImgUrl;
//    }


}

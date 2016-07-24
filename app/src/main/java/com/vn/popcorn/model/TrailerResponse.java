package com.vn.popcorn.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vn on 23/7/16.
 */
public class TrailerResponse {

    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<Trailer> results;

    public int getId() {
        return id;
    }

    public List<Trailer> getResults() {
        return results;
    }
}

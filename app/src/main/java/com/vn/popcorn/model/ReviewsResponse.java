package com.vn.popcorn.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vn on 2/7/16.
 */
public class ReviewsResponse {


    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<Review> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public List<Review> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }
}

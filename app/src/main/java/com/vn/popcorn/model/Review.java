package com.vn.popcorn.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vn on 2/7/16.
 */
public class Review {

    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}

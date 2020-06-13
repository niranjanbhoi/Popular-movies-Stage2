package com.developer.kimy.popularmovies.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviews {

    @SerializedName("results")
    private List<Reviews> reviewList;

    public MovieReviews(List<Reviews> reviewList) {
        this.reviewList = reviewList;
    }

    public List<Reviews> getReviewList() {
        return reviewList;
    }

}
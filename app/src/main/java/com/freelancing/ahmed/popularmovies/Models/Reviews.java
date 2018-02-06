package com.freelancing.ahmed.popularmovies.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 1/16/2018.
 */

public class Reviews {
    @SerializedName("results")
    private List<ReviewsResult> results;

    public Reviews() {
        this.results = new ArrayList<>();
    }

    public List<ReviewsResult> getResults() {
        return results;
    }

    public class ReviewsResult {
        @SerializedName("id")
        private String mID;
        @SerializedName("author")
        private String mAuthor;
        @SerializedName("content")
        private String mContent;

        public ReviewsResult(String ID, String author, String content) {
            mID = ID;
            mAuthor = author;
            mContent = content;
        }

        public String getmID() {
            return mID;
        }

        public String getmAuthor() {
            return mAuthor;
        }

        public String getmContent() {
            return mContent;
        }
    }

}


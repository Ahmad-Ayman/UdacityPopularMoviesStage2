package com.freelancing.ahmed.popularmovies.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 1/16/2018.
 */

public class Videos {
    @SerializedName("results")
    private List<Videos.VideosResult> results;

    public Videos() {
        this.results = new ArrayList<>();
    }

    public List<Videos.VideosResult> getResults() {
        return results;
    }

    public class VideosResult {
        @SerializedName("id")
        private String mID;
        @SerializedName("key")
        private String mKey;
        @SerializedName("name")
        private String mName;

        public VideosResult(String ID, String key, String name) {
            mID = ID;
            mKey = key;
            mName = name;
        }

        public String getmID() {
            return mID;
        }

        public String getmKey() {
            return mKey;
        }

        public String getmName() {
            return mName;
        }
    }
}

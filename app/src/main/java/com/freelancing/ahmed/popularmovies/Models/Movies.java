package com.freelancing.ahmed.popularmovies.Models;

/**
 * Created by ahmed on 1/4/2018.
 */

public class Movies {
    private int mID;
    private int mVoteCounter;
    private double mVoteavg;
    private String mPosterPath;
    private String mTitle;
    private String mOverview;
    private String mReleaseDate;
    private String mBackPoster;

    public Movies( int iD,
                   int voteCounter,
            double voteavg,
            String posterPath,
            String title,
            String overview,
            String releaseDate,
             String BackPoster){
        mID=iD;
        mVoteCounter=voteCounter;
        mVoteavg = voteavg;
        mPosterPath = posterPath;
        mTitle=title;
        mOverview=overview;
        mReleaseDate=releaseDate;
        mBackPoster=BackPoster;
    }

    public int getmID() {
        return mID;
    }

    public int getmVoteCounter() {
        return mVoteCounter;
    }

    public double getmVoteavg() {
        return mVoteavg;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmBackPoster() {return mBackPoster;}
}

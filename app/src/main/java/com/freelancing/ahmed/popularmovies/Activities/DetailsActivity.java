package com.freelancing.ahmed.popularmovies.Activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.freelancing.ahmed.popularmovies.Adapters.MoviesAdapter;
import com.freelancing.ahmed.popularmovies.Adapters.MoviesCursorAdapter;
import com.freelancing.ahmed.popularmovies.Adapters.ReviewsAdapter;
import com.freelancing.ahmed.popularmovies.Adapters.VideosAdapter;
import com.freelancing.ahmed.popularmovies.ApiClients.ApiClient;
import com.freelancing.ahmed.popularmovies.BuildConfig;
import com.freelancing.ahmed.popularmovies.Interfaces.ApiInterface;
import com.freelancing.ahmed.popularmovies.Models.Movies;
import com.freelancing.ahmed.popularmovies.Models.Reviews;
import com.freelancing.ahmed.popularmovies.Models.Videos;
import com.freelancing.ahmed.popularmovies.R;
import com.freelancing.ahmed.popularmovies.Utils.JsonParserUtil;
import com.freelancing.ahmed.popularmovies.Utils.NetworkUtils;
import com.freelancing.ahmed.popularmovies.data.MoviesAppContract;
import com.freelancing.ahmed.popularmovies.data.MoviesAppDbHelper;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {
    private RecyclerView rView,vView;
    private LinearLayoutManager lLayout,l2Layout;
    private ReviewsAdapter rcAdapter;
    private VideosAdapter videosAdapter;
    View Divider;
    ImageView backimg;
    String idpass;
    ScrollView parentview;
    private ProgressBar mLoadingIndicatorReviews;
    private ProgressBar mLoadingIndicatorVideos;
    TextView error;
    // variables for data //////////////
    int id,counter;
    double avg;
    String tit,ov,releaseD,backposter,poster;
    int dbRowID;
    String fromwhere;
    /////////////////////
    final static String API_KEY = BuildConfig.API_KEY;
    private ApiInterface apiInterface;
    TextView title, overview, ratecount, rateavg, releasedate;
    TextView errorVideos;
    MaterialFavoriteButton favorite;
    final static String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w780";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        parentview=(ScrollView) findViewById(R.id.scrollparent);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


            mLoadingIndicatorReviews = (ProgressBar) findViewById(R.id.pb_loading_indicatorReviews);
            mLoadingIndicatorVideos = (ProgressBar) findViewById(R.id.pb_loading_indicatorVideos);
            Intent i = getIntent();
            tit = i.getExtras().getString("TITLE_KEY");
            id = i.getExtras().getInt("ID_KEY");
            Log.e("Id comes from intent",String.valueOf(id));
            idpass = String.valueOf(id);
            Log.e("Movie Id to retrieve",idpass);
            counter = i.getExtras().getInt("VOTECOUNTER_KEY");
            avg = i.getExtras().getDouble("VOTEAVG_KEY");
            //final String pica = i.getExtras().getString("PIC_KEY");
            ov = i.getExtras().getString("OVERVIEW_KEY");
            releaseD = i.getExtras().getString("RELEASE_KEY");
            backposter = i.getExtras().getString("BACKPOSTER_KEY");
            poster = i.getExtras().getString("POSTER_KEY");
            dbRowID = i.getExtras().getInt("ROW_ID_KEY");

            //img = (ImageView) findViewById(R.id.iv_movie_detail);
            title = (TextView) findViewById(R.id.tv_movie_name_details);
            ratecount = (TextView) findViewById(R.id.tv_movie_totalvotes_details);
            rateavg = (TextView) findViewById(R.id.tv_movie_avgrate_details);
            releasedate = (TextView) findViewById(R.id.tv_movie_release_details);
            overview = (TextView) findViewById(R.id.tv_movie_overview_details);
            error = (TextView) findViewById(R.id.errormsgreviews);
            errorVideos = (TextView) findViewById(R.id.errormsgVideos);
            backimg = (ImageView) findViewById(R.id.posterimage);
            Divider = (View) findViewById(R.id.dividerforVideosTitle);
            Typeface font = Typeface.createFromAsset(getAssets(), "PlayfairDisplayBold.ttf");
            TextView text1, text2, text3, text4, text5;
            text1 = findViewById(R.id.text1);
            text2 = findViewById(R.id.text2);
            text3 = findViewById(R.id.text3);
            text4 = findViewById(R.id.text4);
            text5 = findViewById(R.id.text5);
            text1.setTypeface(font);
            text2.setTypeface(font);
            text3.setTypeface(font);
            text4.setTypeface(font);
            text5.setTypeface(font);
            //String urlpic = BASE_POSTER_URL+pica;
            //Picasso.with(this).load(urlpic).into(img);
            String backposterurl = BASE_POSTER_URL + backposter;
            Picasso.with(this).load(backposterurl).fit().into(backimg);
            title.setText(tit);
            ratecount.setText(String.valueOf(counter));
            rateavg.setText(String.valueOf(avg));
            releasedate.setText(releaseD);
            overview.setText(ov);
            this.setTitle("Movie Details");


            favorite = findViewById(R.id.checkfav);
        MoviesAppDbHelper mMoviesAppDbHelper = new MoviesAppDbHelper(getApplicationContext());
        SQLiteDatabase db = mMoviesAppDbHelper.getReadableDatabase();

        Cursor cursorforfv = db.query(MoviesAppContract.MoviesEntry.TABLE_NAME,
                null,
                MoviesAppContract.MoviesEntry.COLUMN_MOVIES_ID+ "=?",
                new String[] { idpass },
                null,null,null,null);
        if(cursorforfv.getCount()>=1){
            favorite.setFavorite(true,false);
        }
        else{
            favorite.setFavorite(false,false);
        }
        favorite.setOnFavoriteChangeListener(
                new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                      //  Uri uriquery = MoviesAppContract.MoviesEntry.CONTENT_URI;
//                    String stringId=idpass ;
//                    Log.e("idpass= ",idpass);
//                    Log.e("stringId =",stringId);
//                    //uriquery = uriquery.buildUpon().appendPath(stringId).build();
//                    Log.e("uriquery = ",uriquery.toString());
//                    Cursor cr=getContentResolver().query(uriquery,
//                            null,
//                            MoviesAppContract.MoviesEntry.COLUMN_MOVIES_ID+"="+idpass,
//                            null,
//                            null);
//
                    MoviesAppDbHelper mMoviesAppDbHelper = new MoviesAppDbHelper(getApplicationContext());
                    SQLiteDatabase db = mMoviesAppDbHelper.getReadableDatabase();

                    Cursor cursor = db.query(MoviesAppContract.MoviesEntry.TABLE_NAME,
                            null,
                            MoviesAppContract.MoviesEntry.COLUMN_MOVIES_ID+ "=?",
                            new String[] { idpass },
                            null,null,null);

                    if(cursor.getCount() < 1 ) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MoviesAppContract.MoviesEntry.COLUMN_MOVIE_NAME, tit);
                        contentValues.put(MoviesAppContract.MoviesEntry.COLUMN_MOVIE_POSTER, poster);
                        contentValues.put(MoviesAppContract.MoviesEntry.COLUMN_MOVIES_ID, Integer.parseInt(idpass));
                        contentValues.put(MoviesAppContract.MoviesEntry.COLUMN_MOVIES_OVERVIEWS, ov);
                        contentValues.put(MoviesAppContract.MoviesEntry.COLUMN_MOVIES_RATE, avg);
                        contentValues.put(MoviesAppContract.MoviesEntry.COLUMN_MOVIES_RELEASE_DATE, releaseD);
                        contentValues.put(MoviesAppContract.MoviesEntry.COLUMN_MOVIES_TOTAL_VOTES, counter);
                        contentValues.put(MoviesAppContract.MoviesEntry.COLUMN_MOVIES_BACK_POSTER, backposter);
                        Uri uri = getContentResolver().insert(MoviesAppContract.MoviesEntry.CONTENT_URI, contentValues);
                        if (uri != null) {

                        }
                    }
                    else {
                        Uri uri = MoviesAppContract.MoviesEntry.CONTENT_URI;
                        String stringIdDelete =idpass ;
                        uri = uri.buildUpon().appendPath(stringIdDelete).build();
                        //String where = MoviesAppContract.MoviesEntry.COLUMN_MOVIES_ID + " = " + idpass;
//                        // COMPLETED (2) Delete a single row of data using a ContentResolver
                        getContentResolver().delete(uri,null , null);

                    }
                }
            });


            rView = (RecyclerView) findViewById(R.id.rv_reviews);
            lLayout = new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.VERTICAL, false);
            rView.setLayoutManager(lLayout);
            vView = (RecyclerView) findViewById(R.id.rv_vidoes);
            l2Layout = new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.VERTICAL, false);
            vView.setLayoutManager(l2Layout);

            if(isConnectedToInternet()) {

                getReviewsDataFromRetrofit();
                getVideosDataFromRetrofit();
            }
            else{
                showNoInternetSnackBar();
            }
        }

    private void showNoInternetSnackBar(){
        error.setVisibility(View.VISIBLE);
        errorVideos.setVisibility(View.VISIBLE);
        Snackbar snackbar = Snackbar
                .make(parentview, "No internet connection!", 10000)
                .setActionTextColor(Color.YELLOW)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(isConnectedToInternet()){
                            getReviewsDataFromRetrofit();
                            getVideosDataFromRetrofit();
                        }
                        else{
                            showNoInternetSnackBar();
                        }
                    }
                });
        snackbar.show();
    }
    private Boolean isConnectedToInternet(){
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }
    //// Reviews Helper Methods /////////////
    private void getReviewsDataFromRetrofit(){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Reviews> call = apiInterface.getReviews(idpass,API_KEY);
        mLoadingIndicatorReviews.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                Log.e("result data ",response.body().getResults().toString());
                List<Reviews.ReviewsResult> rlist= response.body().getResults();
                if( rlist!=null && !rlist.isEmpty()){
                    rcAdapter = new ReviewsAdapter(DetailsActivity.this, rlist);
                    rView.setAdapter(rcAdapter);
                    showReviewsDataView();
                    Log.e("show data","success");
                    Log.e("rlist values",rlist.toString());
                }
                else{
                    showError();
                    Log.e("show error","that is error");
                }
            }
            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {
                Log.d("onFailure", t.toString());

            }
        });
    }
    private void showReviewsDataView() {
        mLoadingIndicatorReviews.setVisibility(View.INVISIBLE);
        error.setVisibility(View.INVISIBLE);
        rView.setVisibility(View.VISIBLE);

    }
    private void showError(){
        mLoadingIndicatorReviews.setVisibility(View.INVISIBLE);
        rView.setVisibility(View.INVISIBLE);
        error.setVisibility(View.VISIBLE);

    }


    //// Videos Helper Methods /////////////
    private void getVideosDataFromRetrofit(){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Videos> callVideos = apiInterface.getVideos(idpass,API_KEY);
        mLoadingIndicatorVideos.setVisibility(View.VISIBLE);
        callVideos.enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(Call<Videos> call, Response<Videos> response) {
                List<Videos.VideosResult> vlist= response.body().getResults();
                if(vlist!=null && !vlist.isEmpty()){
                    videosAdapter = new VideosAdapter(DetailsActivity.this, vlist);
                    vView.setAdapter(videosAdapter);
                    showVideosDataView();
                    Log.e("show data","success");
                    Log.e("vlist values",vlist.toString());
                }
                else{
                    showErrorVideos();
                    Log.e("show error","that is error");
                }
            }
            @Override
            public void onFailure(Call<Videos> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }
    private void showVideosDataView() {
        errorVideos.setVisibility(View.INVISIBLE);
        mLoadingIndicatorVideos.setVisibility(View.INVISIBLE);
        vView.setVisibility(View.VISIBLE);
        Divider.setVisibility(View.INVISIBLE);
    }
    private void showErrorVideos(){
        mLoadingIndicatorVideos.setVisibility(View.INVISIBLE);
        vView.setVisibility(View.INVISIBLE);
        errorVideos.setVisibility(View.VISIBLE);
    }
}
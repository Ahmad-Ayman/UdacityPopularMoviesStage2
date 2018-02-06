package com.freelancing.ahmed.popularmovies.Activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.freelancing.ahmed.popularmovies.Adapters.MoviesAdapter;
import com.freelancing.ahmed.popularmovies.Adapters.MoviesCursorAdapter;
import com.freelancing.ahmed.popularmovies.Models.Movies;
import com.freelancing.ahmed.popularmovies.R;
import com.freelancing.ahmed.popularmovies.Utils.JsonParserUtil;
import com.freelancing.ahmed.popularmovies.Utils.NetworkUtils;
import com.freelancing.ahmed.popularmovies.data.MoviesAppContract;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //    SharedPreferences preferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
//    String channel = (preferences.getString("email", ""));
    private int item_selection = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FAV_LOADER_ID = 0;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMsgDisplay;
    private FrameLayout parentview;
    private GridLayoutManager lLayout;
    private MoviesAdapter rcAdapter;
    int columns;
    private MoviesCursorAdapter favAdapter;
    private RecyclerView rView;
    private SwipeRefreshLayout swiping;
    private final String KEY_RECYCLER_STATE = "mainactivityRecyclerView";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;
    private int cond = 0;
    int lastFirstVisiblePosition;
    private int channel;
    SharedPreferences pref;
    private ArrayList<Movies> mlist = new ArrayList<>();

    // private ArrayList<Movies> rowListItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parentview = (FrameLayout) findViewById(R.id.framingLayout);
        pref = getSharedPreferences("orderby", MODE_PRIVATE);
        channel = (pref.getInt("selection", 1));
        item_selection = channel;

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            lLayout = new GridLayoutManager(MainActivity.this, 2);
        } else {
            lLayout = new GridLayoutManager(MainActivity.this, 4);
        }

        rView = (RecyclerView) findViewById(R.id.rv_movies);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);
        rcAdapter = new MoviesAdapter(MainActivity.this, mlist);
        favAdapter = new MoviesCursorAdapter(this);
        if (channel == 3) {
            rView.setAdapter(favAdapter);
        } else {
            rView.setAdapter(rcAdapter);

        }
        registerForContextMenu(rView);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        swiping = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mErrorMsgDisplay = findViewById(R.id.errormsg);
        if (channel == 1) {
            loadMoviesData(0);
        } else if (channel == 2) {
            loadMoviesData(1);
        } else if (channel == 3) {
            loadFavData();
        }
        swiping.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (isConnectedToInternet()) {
                            rcAdapter.setMoviesData(null);
                            favAdapter.setMoviesFavData(null);
                            if (item_selection == 1) {
                                loadMoviesData(0);
                            } else if (item_selection == 2) {
                                loadMoviesData(1);
                            } else if (item_selection == 3) {
                                reloadFavData();
                            }
                        } else {
                            showNoInternetSnackBar();
                        }
                        // loadMoviesData(conditionforrefresh); // should be according to shared pref value
                        swiping.setRefreshing(false);
                    }
                }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        lastFirstVisiblePosition = ((GridLayoutManager) rView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
//        mBundleRecyclerViewState = new Bundle();
//        mListState = rView.getLayoutManager().onSaveInstanceState();
//        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        columns = 2;
//
//        if (mBundleRecyclerViewState != null) {
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
//                    rView.getLayoutManager().onRestoreInstanceState(mListState);
//
//                }
//            }, 50);
//        }
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//
//            lLayout.setSpanCount(columns);
//
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//
//            lLayout.setSpanCount(columns);
//
//        }
//        rView.setLayoutManager(lLayout);
//    }

    private void reloadFavData() {
        setTitle("Favorited Movies");
        getSupportLoaderManager().restartLoader(FAV_LOADER_ID, null, this);
    }

    private void loadFavData() {
        setTitle("Favorited Movies");
        favAdapter = new MoviesCursorAdapter(this);
        rView.setAdapter(favAdapter);
        getSupportLoaderManager().initLoader(FAV_LOADER_ID, null, this);

    }

    private void loadMoviesData(int condition) {
        if (condition == 0) {
            setTitle("Most Popular Movies");
            cond = 0;
        } else if (condition == 1) {
            setTitle("Top Rated Movies");
            cond = 1;
        }
        rView.setAdapter(rcAdapter);
        showMoviesDataView();
        new MoviesAsyncTask().execute(mlist);

    }

    private void showMoviesDataView() {

        mLoadingIndicator.setVisibility(View.INVISIBLE);
        rView.setVisibility(View.VISIBLE);
        mErrorMsgDisplay.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage() {
        rView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMsgDisplay.setVisibility(View.VISIBLE);
    }

    private Boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

    }

    private void showNoInternetSnackBar() {

        Snackbar snackbar = Snackbar
                .make(parentview, getResources().getString(R.string.nointernet), 10000)
                .setActionTextColor(Color.YELLOW)
                .setAction(getResources().getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (isConnectedToInternet()) {
                            if (item_selection == 1) {
                                loadMoviesData(0);

                            } else if (item_selection == 2) {
                                loadMoviesData(1);

                            } else if (item_selection == 3) {
                                reloadFavData();
                            }
                        } else {
                            showNoInternetSnackBar();
                        }
                    }
                });
        snackbar.show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            // Initialize a Cursor, this will hold all the task data
            Cursor mMoviesFavData = null;

            @Override
            protected void onStartLoading() {
                if (mMoviesFavData != null) {
                    // Delivers any previously loaded data immediately

                    deliverResult(mMoviesFavData);
                } else {
                    // Force a new load

                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MoviesAppContract.MoviesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mMoviesFavData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favAdapter.swapCursor(null);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (item_selection == 3) {
            reloadFavData();
        }
        ((GridLayoutManager) rView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);
        lastFirstVisiblePosition = 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (item_selection == 3) {
            reloadFavData();
        }
        lastFirstVisiblePosition = ((GridLayoutManager) rView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
    }

    public class MoviesAsyncTask extends AsyncTask<ArrayList<Movies>, Void, ArrayList<Movies>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movies> doInBackground(ArrayList<Movies>[] lists) {

            URL MoviesRequestUrl = null;
            if (cond == 0) {
                MoviesRequestUrl = NetworkUtils.buildUrl(0);
            } else if (cond == 1) {
                MoviesRequestUrl = NetworkUtils.buildUrl(1);
            }
            String[] result = null;
            try {
                String JsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(MoviesRequestUrl);
                ArrayList<Movies> JsonMoviesData = JsonParserUtil.getMoviesStringsFromJson(MainActivity.this, JsonMoviesResponse);
                return JsonMoviesData;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null) {
                showMoviesDataView();
                rcAdapter.setMoviesData(movies);
            } else {
                showErrorMessage();
            }
        }
        /** @Override protected void onPostExecute(List strings) {

        } **/
    }


    // Menues

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.main, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.order) {
            ConnectivityManager cm =
                    (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            final boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected == false) {
                // Toast.makeText(this, "Please Check internet Connection", Toast.LENGTH_SHORT).show();
                showNoInternetSnackBar();
            } else {
                registerForContextMenu(getCurrentFocus());
                openContextMenu(getCurrentFocus());
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sortingmenu, menu);
        MenuItem item_popular = menu.findItem(R.id.pop);
        MenuItem item_fav = menu.findItem(R.id.fav);
        MenuItem item_toprated = menu.findItem(R.id.top_rated);
        if (item_selection == 1) {
            item_popular.setChecked(true);
        } else if (item_selection == 2) {
            item_toprated.setChecked(true);
        } else if (item_selection == 3) {
            item_fav.setChecked(true);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.pop:
                rcAdapter.setMoviesData(null);
                favAdapter.swapCursor(null);
                rView.getRecycledViewPool().clear();
                loadMoviesData(0);
                pref = getSharedPreferences("orderby", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                Toast.makeText(this, "Sorted By Popular", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                item_selection = 1;
                editor.putInt("selection", item_selection);
                editor.apply();
                return true;
            case R.id.top_rated:
                rcAdapter.setMoviesData(null);
                favAdapter.swapCursor(null);
                rView.getRecycledViewPool().clear();
                loadMoviesData(1);
                pref = getSharedPreferences("orderby", MODE_PRIVATE);
                editor = pref.edit();
                editor.putInt("sort", 1);
                Toast.makeText(this, "Sorted By Top Rated", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                item_selection = 2;
                editor.putInt("selection", item_selection);
                editor.apply();
                return true;
            case R.id.fav:
                rcAdapter.setMoviesData(null);
                favAdapter.swapCursor(null);
                rView.getRecycledViewPool().clear();
                loadFavData();
                pref = getSharedPreferences("orderby", MODE_PRIVATE);
                editor = pref.edit();
                editor.putInt("sort", 2);
                Toast.makeText(this, "Sorted By Fav", Toast.LENGTH_SHORT).show();
                item.setChecked(true);
                item_selection = 3;
                editor.putInt("selection", item_selection);
                editor.apply();
                return true;

        }


        return super.onContextItemSelected(item);
    }


}

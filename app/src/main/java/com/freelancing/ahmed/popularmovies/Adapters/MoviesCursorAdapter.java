

package com.freelancing.ahmed.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.freelancing.ahmed.popularmovies.Activities.DetailsActivity;
import com.freelancing.ahmed.popularmovies.Interfaces.ItemClickListener;
import com.freelancing.ahmed.popularmovies.Models.Movies;
import com.freelancing.ahmed.popularmovies.R;
import com.freelancing.ahmed.popularmovies.data.MoviesAppContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * This CustomCursorAdapter creates and binds ViewHolders, that hold the description and priority of a task,
 * to a RecyclerView to efficiently display data.
 */
public class MoviesCursorAdapter extends RecyclerView.Adapter<MoviesCursorAdapter.MoviesFavViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;

    private int ID,dbRowID;
    private String pic;
    private int votecounter;
    private double voteavg;
    private String title;
    private String overview;
    private String releaseDate;
    private String backposter;
    public int moviesPosterIndex;
    public int moviesIdIndex;
    public int moviesCounterIndex;
    public int moviesAvgIndex;
    public int titleIndex;
    public int idRowDbIndex;
    public int moviesOvIndex;
    public int moviesReleaseIndex;
    public int moviesBackPosterIndex;
    final static String BASE_POSTER_URL="http://image.tmdb.org/t/p/w185";

    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public MoviesCursorAdapter(Context mContext) {
        this.mContext = mContext;
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new TaskViewHolder that holds the view for each task
     */
    @Override
    public MoviesFavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movies_list_item, parent, false);

        return new MoviesFavViewHolder(view);
    }


    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(MoviesFavViewHolder holder, int position) {

        // Indices for the _id, description, and priority columns
        idRowDbIndex= mCursor.getColumnIndex(MoviesAppContract.MoviesEntry._ID);
        titleIndex = mCursor.getColumnIndex(MoviesAppContract.MoviesEntry.COLUMN_MOVIE_NAME);
        moviesIdIndex = mCursor.getColumnIndex(MoviesAppContract.MoviesEntry.COLUMN_MOVIES_ID);
        moviesOvIndex = mCursor.getColumnIndex(MoviesAppContract.MoviesEntry.COLUMN_MOVIES_OVERVIEWS);
        moviesCounterIndex = mCursor.getColumnIndex(MoviesAppContract.MoviesEntry.COLUMN_MOVIES_TOTAL_VOTES);
        moviesAvgIndex = mCursor.getColumnIndex(MoviesAppContract.MoviesEntry.COLUMN_MOVIES_RATE);
        moviesReleaseIndex = mCursor.getColumnIndex(MoviesAppContract.MoviesEntry.COLUMN_MOVIES_RELEASE_DATE);
        moviesPosterIndex = mCursor.getColumnIndex(MoviesAppContract.MoviesEntry.COLUMN_MOVIE_POSTER);
        moviesBackPosterIndex =mCursor.getColumnIndex(MoviesAppContract.MoviesEntry.COLUMN_MOVIES_BACK_POSTER);


        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idRowDbIndex);
        String poster = mCursor.getString(moviesPosterIndex);
        //Set values
        holder.itemView.setTag(id);
        String urlpic = BASE_POSTER_URL+poster;
        Picasso.with(mContext).load(urlpic).into(holder.mMoviesPoster);
        pic = mCursor.getString(moviesPosterIndex);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {

                mCursor.moveToPosition(pos);
                ID = mCursor.getInt(moviesIdIndex);
                Log.e("Movie Id from cursor",String.valueOf(ID));
                votecounter=mCursor.getInt(moviesCounterIndex);
                voteavg=mCursor.getDouble(moviesAvgIndex);
                title=mCursor.getString(titleIndex);
                overview=mCursor.getString(moviesOvIndex);
                releaseDate = mCursor.getString(moviesReleaseIndex);
                backposter=mCursor.getString(moviesBackPosterIndex);
                pic=mCursor.getString(moviesPosterIndex);
                dbRowID=mCursor.getInt(idRowDbIndex);


                Intent i = new Intent(mContext,DetailsActivity.class);
                i.putExtra("ID_KEY",ID);
                Log.e("Movie Id to sent",String.valueOf(ID));
                i.putExtra("PIC_KEY",pic);
                i.putExtra("VOTECOUNTER_KEY",votecounter);
                i.putExtra("VOTEAVG_KEY",voteavg);
                i.putExtra("TITLE_KEY",title);
                i.putExtra("OVERVIEW_KEY",overview);
                i.putExtra("RELEASE_KEY",releaseDate);
                i.putExtra("BACKPOSTER_KEY",backposter);
                i.putExtra("POSTER_KEY",pic);
                i.putExtra("ROW_ID_KEY",dbRowID);
                i.putExtra("Uniqid","From_CursorAdapter");

                //open activity
                mContext.startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (null==mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
    public void setMoviesFavData(Cursor moviesDataCursor) {

        mCursor = moviesDataCursor; // mMovies is a string Array
        notifyDataSetChanged();
    }
///////////////////////////////////////////////////////
    public class MoviesFavViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public ImageView mMoviesPoster;
        private ItemClickListener itemClickListener;

        public MoviesFavViewHolder(View itemView) {
            super(itemView);
            mMoviesPoster = (ImageView) itemView.findViewById(R.id.iv_movie);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener=ic;
        }
    }
    ///////////////////////////////////////////////////
}
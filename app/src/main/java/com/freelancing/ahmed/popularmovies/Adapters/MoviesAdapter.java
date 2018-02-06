package com.freelancing.ahmed.popularmovies.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.freelancing.ahmed.popularmovies.Activities.DetailsActivity;
import com.freelancing.ahmed.popularmovies.Interfaces.ItemClickListener;
import com.freelancing.ahmed.popularmovies.Models.Movies;
import com.freelancing.ahmed.popularmovies.R;
import com.freelancing.ahmed.popularmovies.data.MoviesAppContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ahmed on 1/3/2018.
 */

/**
 * {@link MoviesAdapter} exposes a list of Movies  to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {
    private Context mContext;
    int ID;
    String pic;
    int votecounter;
    double voteavg;
    String title;
    String overview;
    String releaseDate;
    String backposter;
    //private LayoutInflater inflater;
    final static String BASE_POSTER_URL="http://image.tmdb.org/t/p/w185";
    ArrayList<Movies> movies;




    public MoviesAdapter(Context context, ArrayList<Movies> mList)
    {

        mContext=context;
        movies = mList;
     //   inflater = LayoutInflater.from(context);
    }

    //////////////////////////////////////// View Holder Class //////////////////////////
    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener,View.OnCreateContextMenuListener{
        public ImageView mMovies;
        private ItemClickListener itemClickListener;
        public MoviesAdapterViewHolder(View view) {
            super(view);
           // view.setOnClickListener(this);
            mMovies = (ImageView) view.findViewById(R.id.iv_movie);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener=ic;
        }

        @Override
        public boolean onLongClick(View view) {
        this.itemClickListener.onLongClick(view,getLayoutPosition());
            return true;
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select The Action");
            contextMenu.add(Menu.NONE, view.getId(), Menu.NONE, "Favorite");//groupId, itemId, order, title
            contextMenu.add(Menu.NONE, view.getId(), Menu.NONE, "Unfavorite");
        }
    }

    //////////////////////////////////////////////////
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list_item, null);
        MoviesAdapterViewHolder rcv = new MoviesAdapterViewHolder(layoutView);
        return rcv;
      //  return new MoviesAdapterViewHolder(view);
    }
    /////////////////////////////////////////////////

    @Override
    public void onBindViewHolder(final MoviesAdapterViewHolder moviesAdapterViewHolder, int position) {
        //Movies Movies = movies.get(position);
        pic = movies.get(position).getmPosterPath();

        String urlpic = BASE_POSTER_URL+pic;
        Picasso.with(mContext).load(urlpic).into(moviesAdapterViewHolder.mMovies);
        moviesAdapterViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                pic = movies.get(pos).getmPosterPath();
                ID = movies.get(pos).getmID();
                votecounter=movies.get(pos).getmVoteCounter();
                voteavg=movies.get(pos).getmVoteavg();
                title=movies.get(pos).getmTitle();
                overview=movies.get(pos).getmOverview();
                releaseDate = movies.get(pos).getmReleaseDate();
                backposter=movies.get(pos).getmBackPoster();

                Intent i = new Intent(mContext,DetailsActivity.class);
                i.putExtra("ID_KEY",ID);
                i.putExtra("PIC_KEY",pic);
                i.putExtra("VOTECOUNTER_KEY",votecounter);
                i.putExtra("VOTEAVG_KEY",voteavg);
                i.putExtra("TITLE_KEY",title);
                i.putExtra("OVERVIEW_KEY",overview);
                i.putExtra("RELEASE_KEY",releaseDate);
                i.putExtra("BACKPOSTER_KEY",backposter);
                i.putExtra("POSTER_KEY",pic);
                i.putExtra("Uniqid","From_MoviesAdapter");
                //open activity
                mContext.startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {
              //  setPosition(moviesAdapterViewHolder.getAdapterPosition());
            }
        });

    }


    @Override
    public int getItemCount() {
        if (null == movies) return 0;
        return movies.size();
    }

    public void setMoviesData(ArrayList<Movies> moviesData) {

        movies = moviesData; // mMovies is a string Array
        notifyDataSetChanged();
    }




}

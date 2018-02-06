package com.freelancing.ahmed.popularmovies.Interfaces;

import android.view.View;

/**
 * Created by ahmed on 1/5/2018.
 */

public interface ItemClickListener {
    void onItemClick(View v, int pos);
    public void onLongClick(View view,int position);
}

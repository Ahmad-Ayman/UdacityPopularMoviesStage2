package com.freelancing.ahmed.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.freelancing.ahmed.popularmovies.Interfaces.ItemClickListener;
import com.freelancing.ahmed.popularmovies.Models.Videos;
import com.freelancing.ahmed.popularmovies.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmed on 1/16/2018.
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosAdapterViewHolder> {
    private Context mContext;
    int ID;
    String key;
    String name;
    List<Videos.VideosResult> videos;
    private static final String YOUTUBE_URL ="https://www.youtube.com/watch";
    private static final String YOUTUBE_QUERY_PARAM ="v";

    public VideosAdapter(Context context ,List<Videos.VideosResult> vList){
        mContext=context;
        videos=vList;
    }

    //////////////////////////////////////// View Holder Class //////////////////////////
    public class VideosAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mVideoName;
        private ItemClickListener itemClickListener;

        public VideosAdapterViewHolder(View itemView) {
            super(itemView);
            // view.setOnClickListener(this);
            mVideoName = (TextView) itemView.findViewById(R.id.video_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(view,getLayoutPosition());
        }
        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener=ic;
        }
    }
    //////////////////////////////////////// View Holder Class //////////////////////////

    @Override
    public VideosAdapter.VideosAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_list_item, null);
        VideosAdapter.VideosAdapterViewHolder vcv = new VideosAdapter.VideosAdapterViewHolder(layoutView);
        return vcv;
    }

    @Override
    public void onBindViewHolder(VideosAdapter.VideosAdapterViewHolder holder, int position) {
        name = videos.get(position).getmName();
        key = videos.get(position).getmKey();
        holder.mVideoName.setText(name);
       // holder.mReviewContent.setText(content);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
               key = videos.get(pos).getmKey();
                Toast.makeText(mContext, "Link Pressed", Toast.LENGTH_SHORT).show();
                String url = buildYoutubeURL(key).toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                Intent chooser = Intent.createChooser(intent, "Watch in");
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(chooser);
                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == videos)
            return 0;
        return videos.size();
    }
    public void setVideosData(List<Videos.VideosResult> VideosData) {

        videos = VideosData;
        notifyDataSetChanged();
    }

    private URL buildYoutubeURL (String key){
        Uri uri=null;
        URL url = null;
        uri = Uri.parse(YOUTUBE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_QUERY_PARAM, key)
                .build();
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}

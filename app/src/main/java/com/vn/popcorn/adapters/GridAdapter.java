package com.vn.popcorn.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.vn.popcorn.model.MovieItem;
import com.vn.popcorn.R;
import com.vn.popcorn.activities.DetailActivity;

import java.util.List;

/**
 * Created by vn on 7/10/15.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    private static final String LOG_TAG = GridAdapter.class.getSimpleName();
    public List<MovieItem> mItems;
    private Context context;

    public GridAdapter(Context context, List<MovieItem> mItems) {
        super();
        this.context = context;
        this.mItems = mItems;
        Log.v(LOG_TAG, "GridAdapter: " + mItems);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final MovieItem film = mItems.get(i);


        Uri posterUri = film.buildPosterUri(context.getString(R.string.api_poster_default_size));
        Picasso.with(context)
                .load(posterUri)
                .fit()
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.imgThumbnail);


        viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,DetailActivity.class);
                  intent.putExtra(MovieItem.EXTRA_MOVIE, film.toBundle());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgThumbnail;
        
        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
          }
    }

}

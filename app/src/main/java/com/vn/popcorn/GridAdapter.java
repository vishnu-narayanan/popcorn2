package com.vn.popcorn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by vn on 7/10/15.
 */

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    public List<MovieItem> mItems;
    private Context context;
   // public  String imageUrl[];

//    public GridAdapter(Context context){
//        this.context = context;
//    }

    public GridAdapter(Context context, List<MovieItem> mItems) {
        super();
        this.context = context;
        this.mItems = mItems;
//
//        mItems = new ArrayList<MovieItem>();
//        updatemovies();

//        MovieItem movie = new MovieItem();
//        movie.setmImgUrl("http://image.tmdb.org/t/p/w185//t4PLWexbe4wbNydCOBv18cYexup.jpg");
//        mItems.add(movie);

//        movie.setThumbnail(R.drawable.martain);
//        mItems.add(movie);
//
//        movie = new MovieItem();
//        movie.setThumbnail(R.drawable.hungergames);
//        mItems.add(movie);
//
//        movie = new MovieItem();
//        movie.setThumbnail(R.drawable.kingsman);
//        mItems.add(movie);
//
//        movie = new MovieItem();
//        movie.setThumbnail(R.drawable.interstellar);
//        mItems.add(movie);
//
//        movie = new MovieItem();
//        movie.setThumbnail(R.drawable.avengers);
//        mItems.add(movie);
//
//        movie = new MovieItem();
//        movie.setThumbnail(R.drawable.terminator);
//        mItems.add(movie);
//
//        movie = new MovieItem();
//        movie.setThumbnail(R.drawable.jurassicworld);
//        mItems.add(movie);
//
//        movie = new MovieItem();
//        movie.setThumbnail(R.drawable.mission);
//        mItems.add(movie);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        //updatemovies();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MovieItem film = mItems.get(i);

       // Uri urí = Uri.parse(film.imageUrl);
       // Context context = viewHolder.imgThumbnail.getContext();
       Picasso.with(context)
               .load(film.getmImgUrl())
               .placeholder(R.drawable.oceanbaby)
               .fit()
               .into(viewHolder.imgThumbnail);


       // viewHolder.imgThumbnail.setImageResource(film.getThumbnail());
        viewHolder.imgThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,DetailActivity.class);
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

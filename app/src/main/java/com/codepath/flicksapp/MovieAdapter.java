package com.codepath.flicksapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.flicksapp.models.Config;
import com.codepath.flicksapp.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by sospina on 6/21/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder>{
    //Initialize array of movies
    ArrayList<Movie> movies;

    //Allows us to access Config class
    Config config;

    //Initialize context variable
    Context context;



    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Allows us to process context
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        //Gets values from Config getters
        Movie movie = movies.get(position);
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //Determines if program is in Portrait or Layout mode
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        //Processes imageURL differently depending on orientation
        String imageURL = null;
        if (isPortrait){
            imageURL = config.getImgageURL(config.getPosterSize(), movie.getPosterPath());
        }
        else{
            imageURL = config.getImgageURL(config.getBackdropSize(), movie.getBackdropPath());
        }

        //Again, process info differently depending on orientation
        int placeholder = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageview = isPortrait ? holder.ivPosterImage : holder.ivBackdropImage;

        //Allows us for load up the correct image
        Glide.with(context)
                .load(imageURL)
                .placeholder(placeholder)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .error(R.drawable.flicks_movie_placeholder)
                .into(imageview);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Process app images
        ImageView ivPosterImage;
        ImageView ivBackdropImage;

        //Process app text (title, description)
        TextView tvTitle;
        TextView tvOverview;


        public ViewHolder(View itemView) {
            super(itemView);
            //In charge of displaying items
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // gets item position
            int position = getAdapterPosition();
            // make sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // get the movie at the position, this won't work if the class is static
                Movie movie = movies.get(position);
                // create intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize the movie using parceler, use its short name as a key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show the activity
                context.startActivity(intent);
            }
        }
    }
}

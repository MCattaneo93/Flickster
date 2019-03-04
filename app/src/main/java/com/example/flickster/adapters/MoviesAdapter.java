package com.example.flickster.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.flickster.DetailActivity;
import com.example.flickster.MovieActivity;
import com.example.flickster.R;
import com.example.flickster.models.Movie;

import org.parceler.Parcels;

import java.security.MessageDigest;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import myglide.MyAppGlideModule;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder>{

    Context context;
    List<Movie> movies;

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private Handler handler = new Handler();

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }


    @NonNull
    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d("smile", "onCreateViewHolder");
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapter.ViewHolder viewHolder, int i) {
        Log.d("smile", "onBindViewHolder: " + i);
        Movie movie = movies.get(i);
        //Bind the movie data into the viewHolder
        viewHolder.bind(movie);
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;
        @BindView(R.id.ivPoster) ImageView ivPoster;
        @BindView(R.id.container) RelativeLayout container;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(final Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            //Reference the backdrop path if phone is in landscape

            String imageUrl = movie.getPosterPath();
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                imageUrl = movie.getBackdropPath();
            }

//            progressBar = itemView.findViewById(R.id.progressBar);
//            textView = itemView.findViewById(R.id.textView);
//            // Start long running operation in a background thread
//            new Thread(new Runnable() {
//                public void run() {
//                    while (progressStatus < 100) {
//                        progressStatus += 1;
//                        // Update the progress bar and display the
//                        //current value in the text view
//                        handler.post(new Runnable() {
//                            public void run() {
//                                progressBar.setProgress(progressStatus);
//                                textView.setText(progressStatus+"/"+progressBar.getMax());
//                            }
//                        });
//                        try {
//                            // Sleep for 200 milliseconds.
//                            Thread.sleep(200);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }).start();


            int radius = 30; // corner radius, higher value = more rounded
            int margin = 10;
            //Log.d("smile", movie.getPosterPath());
            Glide.with(context)
                    .load(imageUrl)

//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            progressBar.setVisibility(View.GONE);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            progressBar.setVisibility(View.GONE);
//                            return false;
//                        }
//                    })
                    .fitCenter()
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .into(ivPoster);
            //Add click listener on the whole row

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Navigate to detail activity on tap
                    Intent i = new Intent(context, DetailActivity.class);

                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i);

                }
            });


        }
    }

}

package com.example.flickster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.flickster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class DetailActivity extends YouTubeBaseActivity {

    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.ratingBar) RatingBar ratingbar;
    @BindView(R.id.player) YouTubePlayerView youTubePlayerView;
    @BindView(R.id.genre) TextView genre;
    @BindView(R.id.release_date) TextView releaseDate;

    private static final String YOUTUBE_API_KEY = "AIzaSyDu5HpJqcTOxskuT-JM7V-ia4ykcEbXvqM";
    private static final String TRAILERS_API = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingbar.setRating( (float) movie.getVoteAverage());
        genre.setText(movie.getGenres());
        releaseDate.setText(movie.getReleaseDate());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(TRAILERS_API, movie.getMovieId()), new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONArray results = response.getJSONArray("results");
                    if(results.length() == 0){
                        return;
                    }
                    JSONObject movieTrailer;
                    String youTubeKey = "";

                    for(int i = 0; i < results.length(); i++){
                        movieTrailer = results.getJSONObject(i);
                        if(!movieTrailer.getString("site").equals("YouTube")){
                            continue;
                        }
                        if(movieTrailer.getString("type").equals("Trailer")){
                            youTubeKey = movieTrailer.getString("key");
                        }
                    }

                    initializeYoutube(youTubeKey);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });



    }

    private void initializeYoutube(final String youTubeKey) {

        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("smile", "on init success");
                if(ratingbar.getRating() > 5){
                    youTubePlayer.loadVideo(youTubeKey);
                }else
                    youTubePlayer.cueVideo(youTubeKey);

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("smile", "on init failure");
            }
        });
    }
}

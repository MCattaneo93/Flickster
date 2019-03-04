package com.example.flickster.models;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

@Parcel
public class Movie {

    double voteAverage;
    String posterPath;
    String title;
    String overview;
    String backdropPath;
    String releaseDate;
    String genres;
    private static final String GENRE_API = "https://api.themoviedb.org/3/genre/movie/list?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";
    int movieId;

    //empty constructor for Parceler library
    public Movie(){
    }

    public Movie(final JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        voteAverage = jsonObject.getDouble("vote_average");
        movieId = jsonObject.getInt("id");
        releaseDate = "Release Date: " + jsonObject.getString("release_date");
        final JSONArray genre_ids = jsonObject.getJSONArray("genre_ids");

        final StringBuilder sb = new StringBuilder();
        sb.append("Genres: ");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(GENRE_API, new JsonHttpResponseHandler(){
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray genre_check = response.getJSONArray("genres");
                    for(int i = 0; i < genre_ids.length(); i++){
                        for(int j = 0; j < genre_check.length(); j++){
                            JSONObject genre_object = genre_check.getJSONObject(j);
                            if(genre_ids.get(i).toString().equals(genre_object.get("id").toString())){
                                sb.append(genre_object.get("name"));
                            }
                        }
                        if(i < genre_ids.length() - 1)
                            sb.append(", ");
                    }

                    genres = sb.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static List<Movie> fromJsonArray(JSONArray moviejsonArray) throws JSONException{
        List<Movie> movies = new ArrayList<>();
        for(int i = 0; i < moviejsonArray.length(); i++){
            movies.add(new Movie(moviejsonArray.getJSONObject(i)));
        }

        return movies;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public Integer getMovieId() {
        return movieId;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }
}

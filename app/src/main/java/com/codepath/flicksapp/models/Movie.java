package com.codepath.flicksapp.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
/**
 * Created by sospina on 6/21/17.
 */

@Parcel
public class Movie {
    String title;
    String overview;
    String posterPath;
    String backdropPath;
    Double voteAverage;


    public Movie(JSONObject object) throws JSONException {
        //Extract information from a GET request from a JSON
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Movie() {}

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}

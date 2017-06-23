package com.codepath.flicksapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.codepath.flicksapp.models.Movie;
import com.codepath.flicksapp.models.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {
    //Variables used to formulate URL (necessary for GET request)
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    public final static String API_KEY_PARAM = "api_key"; //api key gotten from secret.xml
    public final static String TAG = "MovieListActivity";

    //Initialize variable types
    AsyncHttpClient client;
    ArrayList<Movie> movies;
    RecyclerView rvMovies;
    MovieAdapter adapter;
    Config config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        client = new AsyncHttpClient();
        movies = new ArrayList<>();
        adapter = new MovieAdapter(movies);
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);
        getConfiguration();
    }
    private void getNowPlaying() {
        //Constructs URL in order to get Now Playing
        String URL = API_BASE_URL + "/movie/now_playing";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));

        //Sends GET request
        client.get(URL, params, new JsonHttpResponseHandler(){
            @Override //onSuccess is when GET request returns successfully
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray results = response.getJSONArray("results");

                    //Adds all the movies in JSON response to Array
                    for (int i = 0; i < results.length(); i++){
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        adapter.notifyItemInserted(movies.size()-1);
                    }

                    //Logs the number of movies processed
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));

                } catch (JSONException e) { // Occurs if failure in parsing JSON file (indexing out of bounds)
                    logError("Failed to parse (Now Playing).", e, true);
                }

            }

            //Occurs if GET request fails to return successfully
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("GET request failed (Now Playing).", throwable, true);
            }
        });
    }
    private void getConfiguration(){
        //starts formulating the URL
        String URL = API_BASE_URL + "/configuration";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //Send the GET request
        client.get(URL, params, new JsonHttpResponseHandler(){
            //Runs if the GET request succeeds
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Try & Catch in case the app fails to parse the JSON response
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseURL %s and posterSize %s",
                            config.getImageBaseURL(),
                            config.getPosterSize()));
                    adapter.setConfig(config);
                    getNowPlaying(); //Ensures that getConfiguration is done before getNowPlaying
                } catch (JSONException e) {
                    logError("Failed to parse.", e, true);
                }
            }
            //Runs if the GET request fails
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("GET request failed (Configuration).", throwable, true);
            }
        });
    }

    //Function which ensures that any error that occurs alerts the user that an error has occured
    private void logError(String message, Throwable error, boolean alertUser){
        Log.e(TAG, message, error);
        if (alertUser){
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}

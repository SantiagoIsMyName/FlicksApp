package com.codepath.flicksapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sospina on 6/22/17.
 */

public class Config {
    String imageBaseURL;
    String posterSize;
    String backdropSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        imageBaseURL = images.getString("secure_base_url");
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        posterSize = posterSizeOptions.optString(3, "w432");
        JSONArray backdropSizeOption = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOption.optString(1, "w780");
    }

    public String getImgageURL(String size, String path) {
        return String.format("%s%s%s", imageBaseURL, size, path);
    }

    public String getImageBaseURL() {
        return imageBaseURL;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}

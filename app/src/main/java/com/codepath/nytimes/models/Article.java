package com.codepath.nytimes.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;


@Parcel
public class Article {
    String headline;
    String webUrl;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    String thumbnail;

    public Article(){

    }
    public Article(JSONObject jsonObject){
    try{
        this.headline = jsonObject.getJSONObject("headline").getString("main");
        this.webUrl = jsonObject.getString("web_url");

        JSONArray multimedia = jsonObject.getJSONArray("multimedia");

        if(multimedia.length() > 0) {
            JSONObject multimediaJson = multimedia.getJSONObject(0);
            this.thumbnail = "http://www.nytimes.com/" + multimediaJson.getString("url");
        } else {
            this.thumbnail = "";
        }

    }catch(JSONException e){
        e.printStackTrace();
    }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array){
        ArrayList<Article> results = new ArrayList<>();
        for(int i = 0 ; i < array.length(); i++){
            try{
                results.add(new Article(array.getJSONObject(i)));

            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }


}

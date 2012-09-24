package com.mangione.mediacenter.model.rottentomatoes;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * User: carminemangione
 * Date: Jul 24, 2010
 * Time: 4:25:31 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class RottenTomatoesSearch {

    private static final String API_KEY = "2ufc48xdsqutqtwrrzpg8rcd";
    private RTSearchResult searchResult;


    public static void main(String[] args) {
        System.out.println(new RottenTomatoesSearch("under one roof").getSearchResult().toString());
    }

    public RottenTomatoesSearch(String movieName) {
        WebResource resource = Client.create()
                .resource("http://api.rottentomatoes.com/api/public/v1.0/movies.json")
                .queryParam("apikey", API_KEY).queryParam("q", movieName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("search_result.json")));
        StringBuffer json = new StringBuffer();
        try {
            while (reader.ready())
                json.append(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
     //   searchResult = RTSearchResult.fromJson(resource.get(String.class));
        searchResult = RTSearchResult.fromJson(json.toString());
    }

    public RTSearchResult getSearchResult() {
        return searchResult;
    }
}

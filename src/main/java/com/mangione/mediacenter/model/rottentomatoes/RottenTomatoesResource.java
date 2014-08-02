package com.mangione.mediacenter.model.rottentomatoes;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

public class RottenTomatoesResource {
    private static final String API_KEY = "2ufc48xdsqutqtwrrzpg8rcd";
    private final WebResource resource;

    public RottenTomatoesResource(String link) {
        resource = Client.create().resource(link).queryParam("apikey", API_KEY);
    }

    public WebResource getResource() {
        return resource;
    }
}

package com.mangione.mediacenter.model.rottentomatoes.moviedetails;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.Serializable;

public class Synopsis implements Serializable {
    private String synopsis = "No synopsis found.";
    private final String posterImageLink;

    public static Synopsis fromWebLink(String link) {
        final WebResource resource = Client.create().resource(link);
        String webpageText = resource.get(String.class);
        return new Synopsis(webpageText);

    }

    public Synopsis(String webpageText) {
        Document doc = Jsoup.parse(webpageText);
        final Element firstPartOfSynopsis = doc.getElementById("movieSynopsis");
        synopsis = firstPartOfSynopsis.childNodes().get(0).toString().trim();
        final Element secondPartOfSynopsis = doc.getElementById("movieSynopsisRemaining");
        if (secondPartOfSynopsis != null) {
            synopsis += " " + secondPartOfSynopsis.childNodes().get(0).toString().trim();
        }

        final Element posterImageElement = doc.getElementsByClass("posterImage").first();
        posterImageLink = posterImageElement.attr("src");
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getPosterImageLink() {
        return posterImageLink;
    }
}

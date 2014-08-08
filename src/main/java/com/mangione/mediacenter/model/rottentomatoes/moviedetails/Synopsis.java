package com.mangione.mediacenter.model.rottentomatoes.moviedetails;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import java.io.Serializable;

public class Synopsis implements Serializable {
    private static final String BEGINNING_OF_SYNOPSIS = "<p id=\"movieSynopsis\" class=\"movie_synopsis\" itemprop=\"description\">";
    private static final String MORE_INFO = "<span id=\"movieSynopsisRemaining\" style=\"display:none;\">";
    private String synopsis = "No synopsis found.";

    public static Synopsis fromWebLink(String link) {
        final WebResource resource = Client.create().resource(link);
        String webpageText = resource.get(String.class);
        return new Synopsis(webpageText);

    }

    public Synopsis(String webpageText) {
        int synopsisLoc = webpageText.indexOf(BEGINNING_OF_SYNOPSIS);
        if (synopsisLoc > 0) {
            synopsisLoc += BEGINNING_OF_SYNOPSIS.length();
            int moreInfoLoc = webpageText.indexOf(MORE_INFO);

            StringBuilder synopsisBuilder = new StringBuilder();
            if (moreInfoLoc > 0) {
                synopsisBuilder.append(webpageText.substring(synopsisLoc, moreInfoLoc));
                moreInfoLoc += MORE_INFO.length();
                String restOfPage = webpageText.substring(moreInfoLoc);
                synopsisBuilder.append(restOfPage.substring(0, restOfPage.indexOf("</span>")));
            } else {
                String restOfPage = webpageText.substring(synopsisLoc);
                synopsisBuilder.append(restOfPage.substring(0, restOfPage.indexOf("<")));
            }

            synopsis = synopsisBuilder.toString();
            synopsis = synopsis.replaceAll("\n", " ");
            synopsis = synopsis.replaceAll("\t", " ");
            synopsis = synopsis.replaceAll(" +", " ");
            synopsis = synopsis.trim();
        }
    }

    public String getSynopsis() {
        return synopsis;
    }
}

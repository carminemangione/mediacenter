package com.mangione.mediacenter.model.rottentomatoes.moviedetails;

public class Synopsis {
    private static final String BEGINNING_OF_SYNOPSIS = "<p id=\"movieSynopsis\" class=\"movie_synopsis\" itemprop=\"description\">";
    private static final String MORE_INFO = "<span id=\"movieSynopsisRemaining\" style=\"display:none;\">";
    private String synopsis = "No synopsis found.";

    public Synopsis(String webpage) {
        int synopsisLoc = webpage.indexOf(BEGINNING_OF_SYNOPSIS);
        if (synopsisLoc > 0) {
            synopsisLoc += BEGINNING_OF_SYNOPSIS.length();
            int moreInfoLoc = webpage.indexOf(MORE_INFO);

            StringBuilder synopsisBuilder = new StringBuilder();
            if (moreInfoLoc > 0) {
                synopsisBuilder.append(webpage.substring(synopsisLoc, moreInfoLoc));
                moreInfoLoc += MORE_INFO.length();
                String restOfPage = webpage.substring(moreInfoLoc);
                synopsisBuilder.append(restOfPage.substring(0, restOfPage.indexOf("</span>")));
            } else {
                String restOfPage = webpage.substring(synopsisLoc);
                synopsisBuilder.append(restOfPage.substring(0, restOfPage.indexOf("<")));
            }

            synopsis = synopsisBuilder.toString();
            synopsis = synopsis.replaceAll("\n", " ");
            synopsis = synopsis.replaceAll(" +", " ");
            synopsis = synopsis.trim();
        }
    }

    public String getSynopsis() {
        return synopsis;
    }
}

package com.mangione.mediacenter.view.rottentomatoes;

import com.mangione.mediacenter.model.rottentomatoes.RTSearchResult;
import com.mangione.mediacenter.model.rottentomatoes.RottenTomatoesSearch;

import javax.swing.*;
import java.net.MalformedURLException;

public class RTDetailController {
    public static void main(String[] args) throws MalformedURLException {
        final RTSearchResult searchResult = new RottenTomatoesSearch("under one roof").getSearchResult();
        JFrame frame = new JFrame();
        frame.add(new RTDetailPanel(searchResult.getMovies()[1]));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}

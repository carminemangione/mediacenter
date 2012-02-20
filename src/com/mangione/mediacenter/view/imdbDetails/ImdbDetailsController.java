package com.mangione.mediacenter.view.imdbDetails;

import com.mangione.mediacenter.model.imdb.IMDBSearchResult;
import com.mangione.mediacenter.model.imdb.IMDBSearchResultListener;
import com.mangione.mediacenter.model.videofile.VideoFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * User: carminemangione
 * Date: Dec 28, 2009
 * Time: 9:34:16 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class ImdbDetailsController implements IMDBSearchResultListener {
    private ImdbDetailsPanel imdbDetailsPanel;

    public  ImdbDetailsController(VideoFile selectedVideoFile, JComponent parent, int xLoc, int yLoc) throws Exception {
        synchronized (this) {
            new IMDBSearchResult(selectedVideoFile, this);
            imdbDetailsPanel = new ImdbDetailsPanel(parent, xLoc, yLoc);
        }
    }

    public void killDetails() {
        imdbDetailsPanel.setVisible(false);
    }

    @Override
    public synchronized void searchFinished(IMDBSearchResult imdbSearchResult) {
        imdbDetailsPanel.imdbResultsRetrieved(imdbSearchResult);
    }
}


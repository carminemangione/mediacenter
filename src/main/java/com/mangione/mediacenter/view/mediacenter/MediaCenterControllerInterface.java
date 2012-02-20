package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.model.videofile.VideoFile;

/**
 * User: carminemangione
 * Date: Dec 5, 2009
 * Time: 2:47:52 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public interface MediaCenterControllerInterface {
    void playerFinishedPlaying();
    void videoSelectionFinished();
    void showMovieDetails(VideoFile selectedVideoFile, int xPos, int yPos);
    void killMovieDetails();
}

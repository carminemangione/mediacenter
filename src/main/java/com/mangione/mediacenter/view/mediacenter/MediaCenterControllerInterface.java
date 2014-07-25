package com.mangione.mediacenter.view.mediacenter;

import com.mangione.mediacenter.model.videofile.VideoFile;

public interface MediaCenterControllerInterface {
    void videoSelectionChanged(VideoFile videoFile);

    void videoSelectionFinished();

    void exitRequested();
}

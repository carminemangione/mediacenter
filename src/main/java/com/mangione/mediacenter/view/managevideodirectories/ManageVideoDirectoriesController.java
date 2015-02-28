package com.mangione.mediacenter.view.managevideodirectories;

import com.mangione.mediacenter.model.VideoDirectories;
import com.mangione.mediacenter.view.mediacenter.MediaCenterControllerInterface;

import javax.swing.*;

/**
 * User: carminemangione
 * Date: Dec 5, 2009
 * Time: 2:26:28 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class ManageVideoDirectoriesController implements ManageVideoDirectoriesControllerInterface {
    private MediaCenterControllerInterface mediaCenterControllerInterface;

    public ManageVideoDirectoriesController(
        MediaCenterControllerInterface mediaCenterControllerInterface) {

        this.mediaCenterControllerInterface = mediaCenterControllerInterface;
    }

    public void launchManageDirectoriesWindow(JFrame parent) {
        new ManageVideoDirectoriesView(VideoDirectories.getInstance().getVideoDirectories(), parent, this);
    }

    @Override
    public void finishedUpdatingDirectories(String[] directories) {
        ManageVideoDirectoriesController.this.mediaCenterControllerInterface.videoSelectionFinished();
    }
}

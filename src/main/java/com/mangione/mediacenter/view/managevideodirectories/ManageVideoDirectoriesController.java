package com.mangione.mediacenter.view.managevideodirectories;

import com.mangione.mediacenter.model.VideoDirectories;
import com.mangione.mediacenter.view.mediacenter.MediaCenterControllerInterface;
import com.mangione.mediacenter.view.mediacenter.MediaCenterView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: carminemangione
 * Date: Dec 5, 2009
 * Time: 2:26:28 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class ManageVideoDirectoriesController implements ManageVideoDirectoriesControllerInterface {
    private final MediaCenterView parent;
    private MediaCenterControllerInterface mediaCenterControllerInterface;

    public ManageVideoDirectoriesController(final MediaCenterView parent, Point location,
        MediaCenterControllerInterface mediaCenterControllerInterface) {
        this.parent = parent;

        this.mediaCenterControllerInterface = mediaCenterControllerInterface;
        
            final JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem menuItem = new JMenuItem("Manage video directories...");
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    launchManageDirectoriesWindow(parent);
                }
            });
            popupMenu.add(menuItem);
            popupMenu.show(parent, location.x, location.y);
//            parent.windowToBack(true);
    }

    private void launchManageDirectoriesWindow(JFrame parent) {
        new ManageVideoDirectoriesView(VideoDirectories.getInstance().getVideoDirectories(), parent, this);
    }

    @Override
    public void finishedUpdatingDirectories(String[] directories) {
        ManageVideoDirectoriesController.this.mediaCenterControllerInterface.videoSelectionFinished();
        parent.windowToBack(false);
    }
}

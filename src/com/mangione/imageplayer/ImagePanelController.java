package com.mangione.imageplayer;

import javax.swing.*;

/**
 * User: carminemangione
 * Date: Feb 23, 2009
 * Time: 2:32:11 AM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class ImagePanelController {
    private final ImagePanel imagePanel;

    public ImagePanelController(String initialImage) {
        imagePanel = new ImagePanel();
        imagePanel.setImage(initialImage);
    }

    public void setImage(String newImage) {
        imagePanel.setImage(newImage);
    }

    public JComponent getImagePanel() {
        return imagePanel;
    }
}

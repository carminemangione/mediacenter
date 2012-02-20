package com.mangione.mediacenter.view.mediacenter;

import javax.swing.*;
import java.awt.*;

/**
 * User: carminemangione
 * Date: Dec 5, 2009
 * Time: 2:27:31 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class MediaCenterView extends JFrame {
    private GraphicsDevice graphicsDevice;

    public MediaCenterView(JComponent mediaImageGrid, GraphicsDevice graphicsDevice) throws HeadlessException {
        this.graphicsDevice = graphicsDevice;
        setLayout(new BorderLayout());
        add(mediaImageGrid, BorderLayout.CENTER);

        setBackground(Color.black);
        setUndecorated(true);
//        graphicsDevice.setFullScreenWindow(this);
        pack();

       setSize(500, 500);
        setVisible(true);
    }



    public void windowToBack(boolean toBack) {
        if (toBack) {
            graphicsDevice.setFullScreenWindow(null);
            pack();
            setVisible(false);
            setState(Frame.ICONIFIED);
        } else {
            restoreWindow();
        }
    }

    public void hideWindow() {
        dispose();
        setVisible(false);
        setState(Frame.ICONIFIED);
    }

    public void restoreWindow() {
        setState(Frame.NORMAL);
        graphicsDevice.setFullScreenWindow(this);
        setVisible(true);
    }


}

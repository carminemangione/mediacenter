package com.mangione.mediacenter.view.mediacenter;

import javax.swing.*;
import java.awt.*;

/**
 * User: carminemangione
 * Date: Dec 5, 2009
 * Time: 2:27:31 PM
 */
public class MediaCenterView extends JFrame {
    private GraphicsDevice graphicsDevice;

    public MediaCenterView(JComponent mediaImageGrid, GraphicsDevice graphicsDevice) throws HeadlessException {
        this.graphicsDevice = graphicsDevice;
        setLayout(new BorderLayout());
        add(mediaImageGrid, BorderLayout.CENTER);

        setBackground(Color.black);
        setUndecorated(true);
        pack();

        setFullScreen();
        setVisible(true);
    }

    private void setFullScreen() {
        if (!"true".equalsIgnoreCase(System.getProperty("debug"))) {
            graphicsDevice.setFullScreenWindow(this);
        } else {
            setSize(500, 500);
        }
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
        setFullScreen();
        setVisible(true);
    }


}

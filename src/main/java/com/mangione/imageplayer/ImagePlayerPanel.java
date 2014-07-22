package com.mangione.imageplayer;

import javax.swing.*;
import java.awt.*;

/**
 * User: carminemangione
 * Date: Feb 22, 2009
 * Time: 1:58:54 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class ImagePlayerPanel extends JFrame {

    public ImagePlayerPanel(JComponent imagePanel, boolean undecorated, Dimension size, Point location) {
        setLayout(new BorderLayout());
        add(imagePanel, BorderLayout.CENTER);
        setUndecorated(undecorated);

        setBackground(Color.lightGray);
        setPreferredSize(size);
        pack();

        setLocation(location.x, location.y);
        setVisible(true);
        setAlwaysOnTop(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}
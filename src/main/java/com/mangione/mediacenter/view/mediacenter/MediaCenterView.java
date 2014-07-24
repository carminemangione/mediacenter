package com.mangione.mediacenter.view.mediacenter;

import javax.swing.*;
import java.awt.*;

/**
 * User: carminemangione
 * Date: Dec 5, 2009
 * Time: 2:27:31 PM
 */
public class MediaCenterView extends JFrame {

    public MediaCenterView(JComponent mediaImageGrid) throws HeadlessException {

        setContentPane(mediaImageGrid);
        setBackground(Color.black);
        setPreferredSize(new Dimension(600, 500));
        validate();
        pack();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

}

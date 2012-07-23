package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;


import com.mangione.mediacenter.model.rottentomatoes.RTMovie;

import javax.swing.*;
import java.awt.*;

/**
 * User: carminemangione
 * Date: Dec 28, 2009
 * Time: 9:33:03 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class RTResolveMoviesPanel extends JPanel {
    public RTResolveMoviesPanel(final JComponent parent, final int xLoc, final int yLoc, RTMovie[] movies) throws Exception {

        parent.add(RTResolveMoviesPanel.this);
        JPanel moviesPanel = new JPanel(new GridLayout(movies.length, 1));
        for (RTMovie movie : movies) {
            final RTResolveMoviePanel panel = new RTResolveMoviePanel(movie);
            JPanel leftAlign = new JPanel(new FlowLayout());
            leftAlign.setAlignmentY(FlowLayout.LEFT);
            leftAlign.add(panel);
            moviesPanel.add(leftAlign);
        }
//        JScrollPane scrollPane = new JScrollPane(moviesPanel);
//        scrollPane.setPreferredSize(new Dimension(moviesPanel.getPreferredSize().width, 300));
        setSize(moviesPanel.getPreferredSize());
        moviesPanel.invalidate();
        add(moviesPanel);
        setLocation(xLoc, yLoc);
        setVisible(true);
        validate();
        parent.repaint();
    }



    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2d = (Graphics2D) graphics;

        Color oldColor = graphics2d.getColor();
        graphics2d.setStroke(new BasicStroke(4f));
        graphics2d.setColor(Color.white);
        Composite oldAlphaComposite = graphics2d.getComposite();

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
        graphics2d.setComposite(ac);
        graphics2d.fillRect(0, 0, getSize().width, getSize().height);
        super.paint(graphics);
        graphics2d.setColor(oldColor);
        graphics2d.setComposite(oldAlphaComposite);


    }
}




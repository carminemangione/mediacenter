package com.mangione.mediacenter.view.rottentomatoes.resolvemovie;


import com.mangione.mediacenter.model.rottentomatoes.RTMovie;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;

/**
 * User: carminemangione
 * Date: Dec 28, 2009
 * Time: 9:33:03 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class RTResolveMoviesPanel extends JPanel {

    private JPanel moviesPanel;

    public RTResolveMoviesPanel(final JComponent parent, final int xLoc, final int yLoc, RTMovie[] movies) throws Exception {

        moviesPanel = createMoviesPanel(movies);


//        JScrollPane scrollPane = new JScrollPane(moviesPanel);
//        scrollPane.setPreferredSize(new Dimension(moviesPanel.getPreferredSize().width, 300));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Insets insets = getInsets();
        Dimension sizeWithInsets = new Dimension(moviesPanel.getPreferredSize().width + insets.left + insets.right,
                moviesPanel.getPreferredSize().height + insets.top + insets.bottom);

        parent.add(RTResolveMoviesPanel.this);
//        DropShadowLayout dropShadowLayout = new DropShadowLayout(4);
//        setLayout(dropShadowLayout);

        setSize(sizeWithInsets);
        add(moviesPanel);
        setLocation(xLoc, yLoc);
        setVisible(true);
        moviesPanel.invalidate();
        validate();
        parent.repaint();
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
    }

    private JPanel createMoviesPanel(RTMovie[] movies) throws MalformedURLException {
        final GridLayout gridLayout = new GridLayout(movies.length, 1, 1, 10);
        JPanel moviesPanel = new JPanel(gridLayout);
        moviesPanel.setOpaque(true);
        moviesPanel.setBackground(Color.LIGHT_GRAY);

        for (RTMovie movie : movies) {
            final RTResolveMoviePanel panel = new RTResolveMoviePanel(movie);
            moviesPanel.add(panel);
        }
        return moviesPanel;
    }


    private class DropShadowLayout implements LayoutManager2 {
        private final int dropShadowThickness;
        private Dimension dimension;
        private Component component;

        public DropShadowLayout(int dropShadowThickness) {
            this.dropShadowThickness = dropShadowThickness;
        }


        @Override
        public void addLayoutComponent(String s, Component component) {
        }

        @Override
        public void removeLayoutComponent(Component component) {
        }

        @Override
        public Dimension preferredLayoutSize(Container container) {
            return dimension;
        }

        @Override
        public Dimension minimumLayoutSize(Container container) {
            return dimension;
        }

        @Override
        public void layoutContainer(Container container) {
            component.setLocation(container.getLocation());

        }

        @Override
        public void addLayoutComponent(Component component, Object o) {
            dimension = new Dimension(component.getPreferredSize().width + dropShadowThickness, component.getPreferredSize().height + dropShadowThickness);
            this.component = component;
        }

        @Override
        public Dimension maximumLayoutSize(Container container) {
            return null;
        }

        @Override
        public float getLayoutAlignmentX(Container container) {
            return 0;
        }

        @Override
        public float getLayoutAlignmentY(Container container) {
            return 0;
        }

        @Override
        public void invalidateLayout(Container container) {

        }
    }

}




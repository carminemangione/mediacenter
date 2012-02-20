package com.mangione.mediacenter.view.imdbDetails;


import javax.swing.*;
import java.awt.*;

/**
 * User: carminemangione
 * Date: Dec 28, 2009
 * Time: 9:33:03 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class ImdbDetailsPanel extends JPanel {
    Font infoFont = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
    JPanel loadingPanel = new JPanel();

    public ImdbDetailsPanel(final JComponent parent, final int xLoc, final int yLoc) throws Exception {
        super(new BorderLayout());
        final JLabel loading = new JLabel("Loading...", JLabel.CENTER);
        loading.setForeground(Color.white);
        loading.setOpaque(false);
        loading.setFont(infoFont);
        add(loading, BorderLayout.NORTH);
        parent.add(ImdbDetailsPanel.this);
        setSize(new Dimension(200, 300));
        setLocation(xLoc, yLoc);
        setVisible(true);
        loading.invalidate();
        validate();
        parent.repaint();
    }

//    public synchronized void imdbResultsRetrieved(final IMDBSearchResult imdbSearchResult) {
//        System.out.println("imdbSearchResult = " + imdbSearchResult);
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                ImdbDetailsPanel.this.remove(loadingPanel);
//                add(new JLabel("FUCKME"), BorderLayout.NORTH);
//                JTextArea description = new JTextArea(imdbSearchResult.getDescription());
//                add(description, BorderLayout.CENTER);
//                invalidate();
//                validate();
//                repaint();
//            }
//        });
//    }

    @Override
    public void paint(Graphics graphics) {
        Graphics2D graphics2d = (Graphics2D) graphics;

        Color oldColor = graphics2d.getColor();
        Stroke oldStroke = graphics2d.getStroke();
        graphics2d.setStroke(new BasicStroke(4f));
        graphics2d.setColor(Color.white);
        Composite oldAlphaComposite = graphics2d.getComposite();

        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
        graphics2d.setComposite(ac);
        graphics2d.fillRect(0, 0, getSize().width, getSize().height);
        super.paint(graphics);
        graphics2d.setComposite(oldAlphaComposite);


    }
}




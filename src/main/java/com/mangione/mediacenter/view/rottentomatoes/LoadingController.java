package com.mangione.mediacenter.view.rottentomatoes;

import javax.swing.*;
import java.awt.*;

public class LoadingController implements RottenTomatoesControllerInterface {

    private static final Font LOADING_FONT = new Font("ITC Garamond", Font.ITALIC, 20);
    private final JPanel jPanel;

    public LoadingController() {
        jPanel = new JPanel(new BorderLayout());
        jPanel.setOpaque(false);
        JLabel loadingLabel = new JLabel("Loading...");
        loadingLabel.setFont(LOADING_FONT);
        loadingLabel.setForeground(Color.LIGHT_GRAY);
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);

        jPanel.add(loadingLabel, BorderLayout.CENTER);
    }

    @Override
    public JPanel getPanel() {
        return jPanel;
    }
}

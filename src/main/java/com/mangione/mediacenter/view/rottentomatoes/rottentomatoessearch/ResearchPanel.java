package com.mangione.mediacenter.view.rottentomatoes.rottentomatoessearch;

import com.google.common.base.Strings;
import com.mangione.mediacenter.view.SharedConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ResearchPanel extends JPanel {

    private static final String DEFAULT_TEXT = "Enter new Rotten Tomatoes Search";
    private static final String CLEAR_MOVIE = "Clear Movie";
    private  JButton researchButton;
    private final ResearchController researchController;
    private JTextField newSearchTitle;

    public ResearchPanel(ResearchController researchController) {
        this.researchController = researchController;
        setOpaque(false);
        setLayout(new BorderLayout());
        JTextField newSearchTitle = setUpSearchEntryBox();
        add(newSearchTitle, BorderLayout.NORTH);

        JPanel buttonPanelNotStretch = setUpButton();
        add(buttonPanelNotStretch, BorderLayout.SOUTH);
    }

    private JPanel setUpButton() {
        researchButton = new JButton(CLEAR_MOVIE);
        researchButton.setOpaque(false);
        JPanel buttonPanelNotStretch = new JPanel();
        buttonPanelNotStretch.setOpaque(false);
        buttonPanelNotStretch.add(researchButton);
        researchButton.addActionListener(e -> {
            researchController.research(normalizeSearchString(newSearchTitle));
            newSearchTitle.setText(DEFAULT_TEXT);
        });
        return buttonPanelNotStretch;
    }

    private JTextField setUpSearchEntryBox() {
        newSearchTitle = new JTextField(DEFAULT_TEXT, DEFAULT_TEXT.length());
        newSearchTitle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (DEFAULT_TEXT.equals(newSearchTitle.getText())) {
                    newSearchTitle.setText("");
                }
                super.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (Strings.isNullOrEmpty(newSearchTitle.getText())) {
                    newSearchTitle.setText(DEFAULT_TEXT);
                    researchButton.setText(CLEAR_MOVIE);
                } else {
                    researchButton.setText("Re-Search");
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String returnText = normalizeSearchString(newSearchTitle);
                    researchController.research(returnText);
                    newSearchTitle.setText(DEFAULT_TEXT);
                }
            }
        });
        newSearchTitle.setFont(SharedConstants.SYNOPSIS_FONT);
        newSearchTitle.setForeground(SharedConstants.LIGHT_TEXT_COLOR);
        newSearchTitle.setBackground(SharedConstants.DEFAULT_BACKGROUND_COLOR);
        return newSearchTitle;
    }

    private String normalizeSearchString(JTextField newSearchTitle) {
        final String text = newSearchTitle.getText();
        return Strings.isNullOrEmpty(text) || ResearchPanel.DEFAULT_TEXT.equals(text)
                ? null : text;
    }
}

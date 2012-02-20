package com.mangione.imageplayer.buttonpanel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * User: carminemangione
 * Date: Feb 22, 2009
 * Time: 10:47:25 PM
 * Copyright Cognigtive Health Sciences, Inc. All rights reserved
 */
public class ButtonPanel extends JPanel {

    public ButtonPanel(final ButtonPanelControllerInterface controller) {

        ImageIcon backIcon = new ImageIcon(ButtonPanel.class.getClassLoader().getResource("back16.gif"));
        JButton backButton = new JButton(backIcon);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                controller.backPressed();

            }
        });
        add(backButton);

        ImageIcon deleteIcon = new ImageIcon(ButtonPanel.class.getClassLoader().getResource("delete16.gif"));
        JButton deleteButton = new JButton(deleteIcon);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                controller.deletePressed();

            }
        });
        add(deleteButton);

        ImageIcon fastForwardIcon = new ImageIcon(ButtonPanel.class.getClassLoader().getResource("fastForward16.gif"));
        JButton fastForwardButton = new JButton(fastForwardIcon);
        fastForwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                controller.fastForwardPressed();

            }
        });
        add(fastForwardButton);

        ImageIcon forwardIcon = new ImageIcon(ButtonPanel.class.getClassLoader().getResource("forward16.gif"));
        JButton forwardButton = new JButton(forwardIcon);
        forwardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                controller.forwardPressed();

            }
        });
        add(forwardButton);

        ImageIcon pauseIcon = new ImageIcon(ButtonPanel.class.getClassLoader().getResource("pause16.gif"));
         JButton pauseButton = new JButton(pauseIcon);
         pauseButton.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent event) {
                 controller.pausePressed();

             }
         });
        add(pauseButton);

        ImageIcon playIcon = new ImageIcon(ButtonPanel.class.getClassLoader().getResource("play16.gif"));
        JButton playButton = new JButton(playIcon);
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                controller.playPressed();

            }
        });
        add(playButton);

        ImageIcon stopImage = new ImageIcon(ButtonPanel.class.getClassLoader().getResource("stop16.gif"));
        JButton stopButton = new JButton(stopImage);
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                controller.stopPressed();

            }
        });
        add(stopButton);

    }
}

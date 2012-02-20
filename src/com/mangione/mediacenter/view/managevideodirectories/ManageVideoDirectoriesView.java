package com.mangione.mediacenter.view.managevideodirectories;

import com.mangione.mediacenter.model.VideoDirectories;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.File;

/**
 * User: carminemangione
 * Date: Dec 5, 2009
 * Time: 3:23:14 PM
 * Copyright Cognitive Health Sciences, Inc. All rights reserved
 */
public class ManageVideoDirectoriesView extends JDialog {
    private JList directoryList = new JList();
    private List<String> currentDirectories = new ArrayList<String>();

    public ManageVideoDirectoriesView(String[] videoDirectories, final JFrame parent,
                                      final ManageVideoDirectoriesControllerInterface controller) {
        super(parent);
        setModal(true);
        setAlwaysOnTop(true);
        directoryList.setModel(new DefaultListModel());
        setLayout(new BorderLayout());
        clearAndFillDirectoryList(videoDirectories);
        JScrollPane scrollPane = new JScrollPane(directoryList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        add(scrollPane, BorderLayout.CENTER);

        ImageIcon icon = new ImageIcon(ManageVideoDirectoriesView.class.getClassLoader().getResource("48px-Crystal_Clear_action_edit_add.png"));
        JButton addButton = new JButton(icon);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FileDialog fileChooser = new FileDialog(ManageVideoDirectoriesView.this);
                ManageVideoDirectoriesView.this.setVisible(false);
                System.setProperty("apple.awt.fileDialogForDirectories", "true");
                fileChooser.setAlwaysOnTop(true);
                fileChooser.setVisible(true);
                currentDirectories.add(fileChooser.getDirectory() + File.separator + fileChooser.getFile());
                String[] videoDirectories1 = currentDirectories.toArray(new String[currentDirectories.size()]);
                clearAndFillDirectoryList(videoDirectories1);
                controller.finishedUpdatingDirectories(videoDirectories1);

            }
        });

        JButton removeButton = new JButton(new ImageIcon(
                ManageVideoDirectoriesView.class.getClassLoader().getResource("48px-Crystal_Clear_action_edit_remove.png")));
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int currentSelected = directoryList.getSelectedIndex();
                if (currentSelected > -1) {
                    currentDirectories.remove(currentSelected);
                    clearAndFillDirectoryList(currentDirectories.toArray(new String[currentDirectories.size()]));
                }
            }
        });

        JButton moviesButton = new JButton(new ImageIcon(
                ManageVideoDirectoriesView.class.getClassLoader().getResource("48px-Crystal_Clear_app_clean.png").getFile()));
        moviesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                controller.finishedUpdatingDirectories(currentDirectories.toArray(new String[currentDirectories.size()]));
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(moviesButton);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    private void clearAndFillDirectoryList(final String[] videoDirectories) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                currentDirectories.clear();
                DefaultListModel listModel = (DefaultListModel) directoryList.getModel();
                listModel.removeAllElements();
                currentDirectories.addAll(Arrays.asList(videoDirectories));
                Collections.sort(currentDirectories);
                for (String videoDirectory : videoDirectories) {
                    listModel.addElement(videoDirectory);
                }
                VideoDirectories.getInstance().setDirectories(videoDirectories);
                directoryList.invalidate();
                setVisible(true);
                repaint();
            }
        });
    }
}

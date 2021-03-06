package com.mangione.imageplayer;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class ScrollTest {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(200,200);
        frame.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                if (event.isShiftDown()) {
                    System.err.println("Horizontal " + event.getWheelRotation());
                } else {
                    System.err.println("Vertical " + event.getWheelRotation());
                }
            }
        });
        frame.setVisible(true);
    }


}

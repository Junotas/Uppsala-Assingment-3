package com.uppsala;

import javax.swing.*;

public class CircleFrame extends JFrame {
    public CircleFrame() {
        setTitle("Cirkelgång");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        CirclePanel panel = new CirclePanel();
        add(panel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CircleFrame::new);
    }
}

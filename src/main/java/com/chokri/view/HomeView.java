package com.chokri.view;

import javax.swing.*;
import java.awt.*;

public class HomeView extends JFrame {

    public HomeView() {
        setTitle("Home Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        setJMenuBar(new MenuBar(this));

        JPanel panel = new JPanel();
        JLabel welcomeLabel = new JLabel("Welcome to the Exam App!");
        panel.add(welcomeLabel);
        add(panel);
    }
}

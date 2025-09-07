package com.chokri.view;

import com.chokri.utils.UITheme;
import javax.swing.*;
import java.awt.*;

public class HomeView extends JFrame {

    public HomeView() {
        UITheme.setupFrame(this, "Accueil");
        setJMenuBar(new MenuBar(this));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        UITheme.setupPanel(mainPanel);

        GridBagConstraints gbc = UITheme.createGridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel welcomeLabel = new JLabel("Bienvenue dans l'application d'examen");
        UITheme.setupTitle(welcomeLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(welcomeLabel, gbc);

        JButton teacherViewBtn = new JButton("Enseignant");
        UITheme.setupButton(teacherViewBtn);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(teacherViewBtn, gbc);

        JButton studentViewBtn = new JButton("Étudiant");
        UITheme.setupButton(studentViewBtn);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(studentViewBtn, gbc);

        teacherViewBtn.addActionListener(e->openTeacherView());

        studentViewBtn.addActionListener(e->openStudentView());

        add(mainPanel);
    }

    public void openTeacherView() {
        SwingUtilities.invokeLater(() -> {
            TeacherView teacherView = new TeacherView();
            teacherView.setVisible(true);
            this.dispose();
        });
    }

    public void openStudentView() {
        SwingUtilities.invokeLater(() -> {
            StudentView studentView = new StudentView();
            studentView.setVisible(true);
            this.dispose();
        });
    }
}

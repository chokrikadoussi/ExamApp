package com.chokri.view;

import com.chokri.utils.UITheme;
import com.chokri.utils.SessionManager;
import com.chokri.model.UserRole;
import javax.swing.*;
import java.awt.*;

public class HomeView extends JFrame {

    public HomeView() {
        UITheme.setupFrame(this, "Accueil");

        JPanel mainPanel = new JPanel(new GridBagLayout());
        UITheme.setupPanel(mainPanel);
        GridBagConstraints gbc = UITheme.createGridBagConstraints();

        // Message de bienvenue
        JLabel welcomeLabel = new JLabel("Bienvenue dans l'application d'examen");
        UITheme.setupTitle(welcomeLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(welcomeLabel, gbc);

        // Label de sélection
        JLabel roleLabel = new JLabel("Veuillez sélectionner votre rôle :");
        UITheme.setupHeader(roleLabel);
        gbc.gridy = 1;
        gbc.insets = new Insets(30, 5, 10, 5);
        mainPanel.add(roleLabel, gbc);

        // Panel pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(UITheme.SECONDARY_COLOR);

        // Bouton Étudiant
        JButton studentButton = new JButton("Étudiant");
        UITheme.setupButton(studentButton);
        studentButton.addActionListener(e -> {
            SessionManager.getInstance().setCurrentRole(UserRole.STUDENT);
            new StudentView().setVisible(true);
            dispose();
        });

        // Bouton Professeur
        JButton teacherButton = new JButton("Professeur");
        UITheme.setupButton(teacherButton);
        teacherButton.addActionListener(e -> {
            SessionManager.getInstance().setCurrentRole(UserRole.TEACHER);
            new TeacherView().setVisible(true);
            dispose();
        });

        buttonPanel.add(studentButton);
        buttonPanel.add(teacherButton);

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 5, 5, 5);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
        // On ne met pas de MenuBar ici car c'est la page de sélection du rôle
    }
}

package com.chokri.utils;

import javax.swing.*;
import java.awt.*;

public class UITheme {
    // Couleurs
    public static final Color PRIMARY_COLOR = new Color(51, 153, 255);    // Bleu principal
    public static final Color SECONDARY_COLOR = new Color(245, 245, 245); // Gris clair pour le fond
    public static final Color TEXT_COLOR = new Color(51, 51, 51);        // Gris foncé pour le texte
    public static final Color ACCENT_COLOR = new Color(255, 102, 102);   // Rouge pour les alertes/erreurs

    // Polices
    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 20);
    public static final Font HEADER_FONT = new Font("Arial", Font.BOLD, 16);
    public static final Font LABEL_FONT = new Font("Arial", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 14);

    // Dimensions
    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 600;
    public static final Insets PADDING = new Insets(5, 5, 5, 5);
    public static final int TEXT_FIELD_SIZE = 20;

    // Méthodes utilitaires pour appliquer les styles
    public static void setupHeader(JLabel label) {
        label.setFont(HEADER_FONT);
        label.setForeground(TEXT_COLOR);
    }

    public static void setupTitle(JLabel label) {
        label.setFont(TITLE_FONT);
        label.setForeground(PRIMARY_COLOR);
    }

    public static void setupLabel(JLabel label) {
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);
    }

    public static void setupButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
    }

    public static void setupTextField(JTextField textField) {
        textField.setFont(LABEL_FONT);
        textField.setColumns(TEXT_FIELD_SIZE);
    }

    public static void setupPanel(JPanel panel) {
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public static GridBagConstraints createGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = PADDING;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    // Configuration générale de la fenêtre
    public static void setupFrame(JFrame frame, String title) {
        frame.setTitle(title);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        ((JPanel)frame.getContentPane()).setBackground(SECONDARY_COLOR);
    }
}

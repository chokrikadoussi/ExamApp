package com.chokri;

import com.chokri.view.HomeView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Configuration du Look and Feel Nimbus pour une apparence moderne
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Si Nimbus n'est pas disponible, on utilise le Look and Feel par défaut du système
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                //TODO: log the exception
                System.err.println("Erreur lors du chargement du Look and Feel : " + ex.getMessage());
            }
        }

        // Lancement de l'interface graphique sur l'EDT
        SwingUtilities.invokeLater(() -> {
            HomeView homeView = new HomeView();
            homeView.setVisible(true);
        });
    }
}
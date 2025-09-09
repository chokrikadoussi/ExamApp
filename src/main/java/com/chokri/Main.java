package com.chokri;

import com.chokri.config.AppConfig;
import com.chokri.controller.AppOrchestrator;
import com.chokri.view.HomeView;

import javax.swing.*;

/**
 * Point d'entrée principal de l'application ExamApp.
 * Initialise le système de dependency injection avant de lancer l'interface.
 */
public class Main {
    public static void main(String[] args) {
        // Initialiser le système de dependency injection
        AppConfig.configure();

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
            try {
                // Créer l'orchestrateur principal
                AppOrchestrator orchestrator = new AppOrchestrator();

                // Lancer la vue principale avec l'orchestrateur
                new HomeView(orchestrator).setVisible(true);

            } catch (Exception e) {
                System.err.println("Erreur lors du démarrage de l'application : " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
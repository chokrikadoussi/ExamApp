package com.chokri.view;

import com.chokri.controller.AppOrchestrator;
import com.chokri.model.Quiz;
import com.chokri.utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TeacherView extends JFrame {
    private final AppOrchestrator orchestrator;
    private final DefaultListModel<Quiz> quizListModel;

    public TeacherView(AppOrchestrator orchestrator) {
        this.orchestrator = orchestrator;

        UITheme.setupFrame(this, "Espace Enseignant");
        setJMenuBar(new MenuBar(this, orchestrator));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        UITheme.setupPanel(mainPanel);
        GridBagConstraints gbc = UITheme.createGridBagConstraints();

        // Titre
        JLabel titleLabel = new JLabel("Gestion des Quiz");
        UITheme.setupTitle(titleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // Bouton Nouveau Quiz
        JButton newQuizButton = new JButton("Créer un nouveau Quiz");
        UITheme.setupButton(newQuizButton);
        newQuizButton.addActionListener(e -> openQuizCreation());
        gbc.gridy = 1;
        mainPanel.add(newQuizButton, gbc);

        // Liste des quiz
        JLabel listLabel = new JLabel("Quiz existants");
        UITheme.setupHeader(listLabel);
        gbc.gridy = 2;
        mainPanel.add(listLabel, gbc);

        quizListModel = new DefaultListModel<>();
        loadQuizzes();
        JList<Quiz> quizList = new JList<>(quizListModel);
        quizList.setCellRenderer(new QuizListRenderer());

        JScrollPane scrollPane = new JScrollPane(quizList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        gbc.gridy = 3;
        mainPanel.add(scrollPane, gbc);

        // Panel des actions
        JPanel actionPanel = new JPanel(new FlowLayout());
        actionPanel.setBackground(UITheme.SECONDARY_COLOR);

        JButton publishButton = new JButton("Publier");
        UITheme.setupButton(publishButton);
        publishButton.addActionListener(e -> publishSelectedQuiz(quizList.getSelectedValue()));

        JButton editButton = new JButton("Modifier");
        UITheme.setupButton(editButton);
        editButton.addActionListener(e -> editSelectedQuiz(quizList.getSelectedValue()));

        JButton deleteButton = new JButton("Supprimer");
        UITheme.setupButton(deleteButton);
        deleteButton.addActionListener(e -> deleteSelectedQuiz(quizList.getSelectedValue()));

        JButton refreshButton = new JButton("Actualiser");
        UITheme.setupButton(refreshButton);
        refreshButton.addActionListener(e -> loadQuizzes());

        actionPanel.add(publishButton);
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        actionPanel.add(refreshButton);

        gbc.gridy = 4;
        mainPanel.add(actionPanel, gbc);

        add(mainPanel);
        pack();
    }

    private void loadQuizzes() {
        quizListModel.clear();
        List<Quiz> quizzes = orchestrator.getAllQuizzes();
        for (Quiz quiz : quizzes) {
            quizListModel.addElement(quiz);
        }
    }

    private void openQuizCreation() {
        new QuizView(orchestrator).setVisible(true);
        dispose();
    }

    private void publishSelectedQuiz(Quiz quiz) {
        if (quiz != null) {
            // Vérifier si le quiz peut être publié via l'orchestrateur
            if (!orchestrator.canPublishQuiz(quiz)) {
                JOptionPane.showMessageDialog(this,
                    "Ce quiz ne peut pas être publié. Vérifiez qu'il contient des questions et un temps limite valide.",
                    "Publication impossible",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            orchestrator.publishQuiz(quiz);
            JOptionPane.showMessageDialog(this,
                "Le quiz a été publié avec succès !",
                "Publication",
                JOptionPane.INFORMATION_MESSAGE);
            loadQuizzes(); // Rafraîchir la liste
        }
    }

    private void editSelectedQuiz(Quiz quiz) {
        if (quiz != null) {
            // TODO: Implémenter l'édition de quiz
            JOptionPane.showMessageDialog(this,
                "Fonctionnalité d'édition à venir",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSelectedQuiz(Quiz quiz) {
        if (quiz != null) {
            // Afficher les statistiques du quiz avant suppression
            AppOrchestrator.QuizStatistics stats = orchestrator.calculateQuizStatistics(quiz);
            String message = String.format(
                "Êtes-vous sûr de vouloir supprimer ce quiz ?\n\n" +
                "Statistiques:\n" +
                "- Soumissions totales: %d\n" +
                "- Score moyen: %.2f\n\n" +
                "Cette action est irréversible.",
                stats.getTotalSubmissions(),
                stats.getAverageScore()
            );

            int response = JOptionPane.showConfirmDialog(this,
                message,
                "Confirmation de suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

            if (response == JOptionPane.YES_OPTION) {
                orchestrator.deleteQuiz(quiz);
                JOptionPane.showMessageDialog(this,
                    "Quiz supprimé avec succès.",
                    "Suppression",
                    JOptionPane.INFORMATION_MESSAGE);
                loadQuizzes(); // Rafraîchir la liste
            }
        }
    }

    // Renderer personnalisé pour afficher les quiz dans la JList
    private static class QuizListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof Quiz quiz) {
                String status = quiz.isPublished() ? "[Publié] " : "[Brouillon] ";
                setText(status + quiz.getTitle() +
                    String.format(" (Coefficient: %.1f, Questions: %d, Temps: %d min)",
                        quiz.getCoefficient(),
                        quiz.getQuestionCount(),
                        quiz.getTimeLimit()));

                // Couleur différente selon le statut
                if (quiz.isPublished()) {
                    setForeground(isSelected ? Color.WHITE : new Color(0, 128, 0));
                } else {
                    setForeground(isSelected ? Color.WHITE : Color.DARK_GRAY);
                }
            }

            return this;
        }
    }
}

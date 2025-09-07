package com.chokri.view;

import com.chokri.controller.QuizController;
import com.chokri.model.Quiz;
import com.chokri.model.Question;
import com.chokri.utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentView extends JFrame {
    private final QuizController quizController;
    private final JPanel mainPanel;
    private final CardLayout cardLayout;
    private final Map<Question, JTextField> answerFields;

    public StudentView() {
        this.quizController = QuizController.getInstance();
        this.answerFields = new HashMap<>();

        UITheme.setupFrame(this, "Espace Étudiant - Quiz disponibles");
        setJMenuBar(new MenuBar(this));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        UITheme.setupPanel(mainPanel);

        // Panel liste des quiz
        JPanel quizListPanel = createQuizListPanel();
        mainPanel.add(quizListPanel, "QUIZ_LIST");

        add(mainPanel);
    }

    private JPanel createQuizListPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        UITheme.setupPanel(panel);
        GridBagConstraints gbc = UITheme.createGridBagConstraints();

        // Titre
        JLabel titleLabel = new JLabel("Quiz disponibles");
        UITheme.setupTitle(titleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // Liste des quiz publiés
        List<Quiz> publishedQuizzes = quizController.getPublishedQuizzes();

        if (publishedQuizzes.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucun quiz disponible pour le moment");
            UITheme.setupLabel(emptyLabel);
            gbc.gridy = 1;
            panel.add(emptyLabel, gbc);
        } else {
            int row = 1;
            for (Quiz quiz : publishedQuizzes) {
                JPanel quizPanel = createQuizEntryPanel(quiz);
                gbc.gridy = row++;
                panel.add(quizPanel, gbc);
            }
        }

        return panel;
    }

    private JPanel createQuizEntryPanel(Quiz quiz) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(UITheme.SECONDARY_COLOR);

        JLabel quizInfo = new JLabel(String.format("%s (Coefficient: %.1f, Temps: %d min)",
            quiz.getTitle(), quiz.getCoefficient(), quiz.getTimeLimit()));
        UITheme.setupLabel(quizInfo);
        panel.add(quizInfo);

        JButton startButton = new JButton("Commencer");
        UITheme.setupButton(startButton);
        startButton.addActionListener(e -> showQuizQuestions(quiz));
        panel.add(startButton);

        return panel;
    }

    private void showQuizQuestions(Quiz quiz) {
        JPanel questionsPanel = new JPanel(new GridBagLayout());
        UITheme.setupPanel(questionsPanel);
        GridBagConstraints gbc = UITheme.createGridBagConstraints();

        // Titre du quiz
        JLabel quizTitle = new JLabel(quiz.getTitle());
        UITheme.setupTitle(quizTitle);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        questionsPanel.add(quizTitle, gbc);

        // Info temps et coefficient
        JLabel quizInfo = new JLabel(String.format("Temps: %d minutes - Coefficient: %.1f",
            quiz.getTimeLimit(), quiz.getCoefficient()));
        UITheme.setupHeader(quizInfo);
        gbc.gridy = 1;
        questionsPanel.add(quizInfo, gbc);

        // Questions
        int row = 2;
        for (Question question : quiz.getQuestions()) {
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 2;

            JLabel questionLabel = new JLabel(question.getTitle());
            UITheme.setupLabel(questionLabel);
            questionsPanel.add(questionLabel, gbc);

            JTextField answerField = new JTextField(20);
            UITheme.setupTextField(answerField);
            gbc.gridy = row + 1;
            questionsPanel.add(answerField, gbc);

            answerFields.put(question, answerField);
            row += 2;
        }

        // Bouton de soumission
        JButton submitButton = new JButton("Soumettre les réponses");
        UITheme.setupButton(submitButton);
        submitButton.addActionListener(e -> submitQuizAnswers(quiz));
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER;
        questionsPanel.add(submitButton, gbc);

        // Ajouter le panel au mainPanel avec scroll
        JScrollPane scrollPane = new JScrollPane(questionsPanel);
        scrollPane.setBorder(null);
        mainPanel.add(scrollPane, "QUIZ_" + quiz.getTitle());
        cardLayout.show(mainPanel, "QUIZ_" + quiz.getTitle());
    }

    private void submitQuizAnswers(Quiz quiz) {
        // TODO: Implémenter la logique de soumission des réponses
        // Pour l'instant, on affiche juste un message
        JOptionPane.showMessageDialog(this,
            "Vos réponses ont été soumises avec succès !",
            "Confirmation",
            JOptionPane.INFORMATION_MESSAGE);

        // Retour à la liste des quiz
        cardLayout.show(mainPanel, "QUIZ_LIST");

        // Nettoyer les réponses
        answerFields.clear();
    }
}

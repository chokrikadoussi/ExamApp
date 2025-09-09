package com.chokri.view;

import com.chokri.controller.AppOrchestrator;
import com.chokri.model.Quiz;
import com.chokri.model.Question;
import com.chokri.utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuizView extends JFrame {
    private final AppOrchestrator orchestrator;
    private final JTextField titleField;
    private final JTextField coefficientField;
    private final JTextField timeLimitField;
    private final JList<Question> questionList;
    private final DefaultListModel<Question> questionListModel;

    public QuizView(AppOrchestrator orchestrator) {
        this.orchestrator = orchestrator;

        UITheme.setupFrame(this, "Créer un Quiz");
        setJMenuBar(new MenuBar(this, orchestrator));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        UITheme.setupPanel(mainPanel);
        GridBagConstraints gbc = UITheme.createGridBagConstraints();

        // Titre
        JLabel titleLabel = new JLabel("Créer un nouveau Quiz");
        UITheme.setupTitle(titleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        // Titre du quiz
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel titleFieldLabel = new JLabel("Titre du quiz :");
        UITheme.setupLabel(titleFieldLabel);
        mainPanel.add(titleFieldLabel, gbc);

        gbc.gridx = 1;
        titleField = new JTextField(20);
        UITheme.setupTextField(titleField);
        mainPanel.add(titleField, gbc);

        // Coefficient
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel coefficientLabel = new JLabel("Coefficient :");
        UITheme.setupLabel(coefficientLabel);
        mainPanel.add(coefficientLabel, gbc);

        gbc.gridx = 1;
        coefficientField = new JTextField(10);
        UITheme.setupTextField(coefficientField);
        coefficientField.setText("1.0");
        mainPanel.add(coefficientField, gbc);

        // Temps limite
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel timeLimitLabel = new JLabel("Temps limite (minutes) :");
        UITheme.setupLabel(timeLimitLabel);
        mainPanel.add(timeLimitLabel, gbc);

        gbc.gridx = 1;
        timeLimitField = new JTextField(10);
        UITheme.setupTextField(timeLimitField);
        timeLimitField.setText("30");
        mainPanel.add(timeLimitField, gbc);

        // Liste des questions disponibles
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel questionsLabel = new JLabel("Questions disponibles :");
        UITheme.setupHeader(questionsLabel);
        mainPanel.add(questionsLabel, gbc);

        // Liste avec modèle
        questionListModel = new DefaultListModel<>();
        questionList = new JList<>(questionListModel);
        questionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(questionList);
        scrollPane.setPreferredSize(new Dimension(400, 200));

        gbc.gridy = 5;
        mainPanel.add(scrollPane, gbc);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(UITheme.SECONDARY_COLOR);

        JButton refreshQuestionsButton = new JButton("Actualiser les questions");
        UITheme.setupButton(refreshQuestionsButton);
        refreshQuestionsButton.addActionListener(e -> loadQuestions());
        buttonPanel.add(refreshQuestionsButton);

        JButton createQuizButton = new JButton("Créer le Quiz");
        UITheme.setupButton(createQuizButton);
        createQuizButton.addActionListener(e -> createQuiz());
        buttonPanel.add(createQuizButton);

        gbc.gridy = 6;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
        pack();

        // Charger les questions au démarrage
        loadQuestions();
    }

    private void loadQuestions() {
        questionListModel.clear();
        List<Question> questions = orchestrator.getAllQuestions();
        for (Question question : questions) {
            questionListModel.addElement(question);
        }
    }

    private void createQuiz() {
        String title = titleField.getText().trim();
        String coefficientText = coefficientField.getText().trim();
        String timeLimitText = timeLimitField.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Le titre du quiz ne peut pas être vide.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        double coefficient;
        int timeLimit;

        try {
            coefficient = Double.parseDouble(coefficientText);
            timeLimit = Integer.parseInt(timeLimitText);

            if (coefficient <= 0 || timeLimit <= 0) {
                throw new NumberFormatException("Les valeurs doivent être positives");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Le coefficient et le temps limite doivent être des nombres positifs.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Créer le quiz via l'orchestrateur
        Quiz quiz = orchestrator.createQuiz(title, coefficient);
        quiz.setTimeLimit(timeLimit);

        // Ajouter les questions sélectionnées
        List<Question> selectedQuestions = questionList.getSelectedValuesList();
        for (Question question : selectedQuestions) {
            orchestrator.addQuestionToQuiz(quiz, question);
        }

        JOptionPane.showMessageDialog(this,
            String.format("Quiz '%s' créé avec succès avec %d question(s)!",
                title, selectedQuestions.size()),
            "Succès",
            JOptionPane.INFORMATION_MESSAGE);

        // Réinitialiser le formulaire
        titleField.setText("");
        coefficientField.setText("1.0");
        timeLimitField.setText("30");
        questionList.clearSelection();
    }
}

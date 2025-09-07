package com.chokri.view;

import com.chokri.controller.QuizController;
import com.chokri.controller.QuestionController;
import com.chokri.model.Quiz;
import com.chokri.model.Question;
import com.chokri.utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuizView extends JFrame {
    private final QuizController quizController;
    private final QuestionController questionController;
    private final JTextField titleField;
    private final JTextField coefficientField;
    private final JTextField timeLimitField;
    private final JList<Question> questionList;
    private final DefaultListModel<Question> questionListModel;

    public QuizView() {
        this.quizController = QuizController.getInstance();
        this.questionController = QuestionController.getInstance();

        UITheme.setupFrame(this, "Créer un Quiz");
        setJMenuBar(new MenuBar(this));

        JPanel mainPanel = new JPanel(new GridBagLayout());
        UITheme.setupPanel(mainPanel);
        GridBagConstraints gbc = UITheme.createGridBagConstraints();

        // Titre
        JLabel titleLabel = new JLabel("Titre du Quiz :");
        UITheme.setupLabel(titleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        titleField = new JTextField();
        UITheme.setupTextField(titleField);
        gbc.gridx = 1;
        mainPanel.add(titleField, gbc);

        // Coefficient
        JLabel coeffLabel = new JLabel("Coefficient :");
        UITheme.setupLabel(coeffLabel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(coeffLabel, gbc);

        coefficientField = new JTextField();
        UITheme.setupTextField(coefficientField);
        gbc.gridx = 1;
        mainPanel.add(coefficientField, gbc);

        // Temps limite
        JLabel timeLabel = new JLabel("Temps limite (minutes) :");
        UITheme.setupLabel(timeLabel);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(timeLabel, gbc);

        timeLimitField = new JTextField();
        UITheme.setupTextField(timeLimitField);
        gbc.gridx = 1;
        mainPanel.add(timeLimitField, gbc);

        // Liste des questions disponibles
        JLabel questionsLabel = new JLabel("Questions disponibles :");
        UITheme.setupHeader(questionsLabel);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(questionsLabel, gbc);

        questionListModel = new DefaultListModel<>();
        loadAvailableQuestions();
        questionList = new JList<>(questionListModel);
        questionList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(questionList);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        gbc.gridy = 4;
        mainPanel.add(scrollPane, gbc);

        // Bouton de création
        JButton createButton = new JButton("Créer le Quiz");
        UITheme.setupButton(createButton);
        createButton.addActionListener(e -> createQuiz());
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(createButton, gbc);

        add(mainPanel);
        pack();
    }

    private void loadAvailableQuestions() {
        List<Question> questions = questionController.getQuestions();
        questionListModel.clear();
        for (Question question : questions) {
            questionListModel.addElement(question);
        }
    }

    private void createQuiz() {
        try {
            String title = titleField.getText();
            double coefficient = Double.parseDouble(coefficientField.getText());
            int timeLimit = Integer.parseInt(timeLimitField.getText());

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez saisir un titre pour le quiz.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Quiz quiz = quizController.createQuiz(title, coefficient);
            quiz.setTimeLimit(timeLimit);

            // Ajouter les questions sélectionnées
            List<Question> selectedQuestions = questionList.getSelectedValuesList();
            for (Question question : selectedQuestions) {
                quizController.addQuestionToQuiz(quiz, question);
            }

            JOptionPane.showMessageDialog(this, "Quiz créé avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            clearFields();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Veuillez saisir des valeurs numériques valides pour le coefficient et le temps limite.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        titleField.setText("");
        coefficientField.setText("");
        timeLimitField.setText("");
        questionList.clearSelection();
    }
}

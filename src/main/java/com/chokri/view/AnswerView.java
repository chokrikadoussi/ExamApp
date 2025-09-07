package com.chokri.view;

import com.chokri.controller.AnswerController;
import com.chokri.controller.QuestionController;
import com.chokri.model.Question;
import com.chokri.utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswerView extends JFrame {

    private final AnswerController answerController;
    private final Map<Question, JTextField> answerFields;
    private final List<Question> questions;

    public AnswerView() {
        this.answerController = new AnswerController();
        this.answerFields = new HashMap<>();

        UITheme.setupFrame(this, "Répondre aux questions");
        setJMenuBar(new MenuBar(this));

        QuestionController questionController = QuestionController.getInstance();
        this.questions = questionController.getQuestions();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        UITheme.setupPanel(mainPanel);
        GridBagConstraints gbc = UITheme.createGridBagConstraints();

        // Titre de la page
        JLabel pageTitle = new JLabel("Répondez aux questions");
        UITheme.setupTitle(pageTitle);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(pageTitle, gbc);
        gbc.gridwidth = 1;

        if (questions.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucune question à afficher pour le moment.");
            UITheme.setupLabel(emptyLabel);
            gbc.gridy = 1;
            mainPanel.add(emptyLabel, gbc);
        } else {
            int gridy = 1;
            for (Question question : questions) {
                // Question title with bold font
                JLabel titleLabel = new JLabel(question.getTitle());
                UITheme.setupHeader(titleLabel);
                gbc.gridx = 0;
                gbc.gridy = gridy;
                gbc.gridwidth = 2;
                mainPanel.add(titleLabel, gbc);

                // Answer label and field
                JLabel answerLabel = new JLabel("Votre réponse :");
                UITheme.setupLabel(answerLabel);
                gbc.gridx = 0;
                gbc.gridy = gridy + 1;
                gbc.gridwidth = 1;
                mainPanel.add(answerLabel, gbc);

                JTextField answerField = new JTextField();
                UITheme.setupTextField(answerField);
                gbc.gridx = 1;
                gbc.gridy = gridy + 1;
                mainPanel.add(answerField, gbc);

                answerFields.put(question, answerField);
                gridy += 3; // Spacing between questions
            }

            // Validate button
            JButton validateButton = new JButton("Valider les réponses");
            UITheme.setupButton(validateButton);
            validateButton.addActionListener(e -> validateAndShowScore());

            gbc.gridx = 0;
            gbc.gridy = gridy;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            mainPanel.add(validateButton, gbc);
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null); // Remove border
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
        add(scrollPane);
    }

    private void validateAndShowScore() {
        Map<Question, String> userAnswers = new HashMap<>();
        for (Map.Entry<Question, JTextField> entry : answerFields.entrySet()) {
            userAnswers.put(entry.getKey(), entry.getValue().getText());
        }

        int score = answerController.validateAnswers(userAnswers);
        int total = questions.size();
        JOptionPane.showMessageDialog(this,
                String.format("Votre score est de : %d/%d", score, total),
                "Résultat",
                JOptionPane.INFORMATION_MESSAGE);
    }
}

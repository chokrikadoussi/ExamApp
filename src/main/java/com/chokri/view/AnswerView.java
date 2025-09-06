package com.chokri.view;

import com.chokri.controller.AnswerController;
import com.chokri.controller.QuestionController;
import com.chokri.model.Question;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswerView extends JFrame {

    private final AnswerController answerController;
    // Utilisation d'une Map pour associer directement chaque question à son champ de réponse.
    // C'est une approche robuste pour éviter les erreurs liées à l'ordre des listes.
    private final Map<Question, JTextField> answerFields;
    private final List<Question> questions;

    public AnswerView() {
        this.answerController = new AnswerController();
        this.answerFields = new HashMap<>();

        setTitle("Répondre aux questions");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setJMenuBar(new MenuBar(this));

        QuestionController questionController = QuestionController.getInstance();
        this.questions = questionController.getQuestions();

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        if (questions.isEmpty()) {
            mainPanel.add(new JLabel("Aucune question à afficher pour le moment."));
        } else {
            int gridy = 0;
            for (Question question : questions) {
                JLabel titleLabel = new JLabel(question.getTitle());
                Font boldFont = new Font(titleLabel.getFont().getName(), Font.BOLD, titleLabel.getFont().getSize());
                titleLabel.setFont(boldFont);

                gbc.gridx = 0;
                gbc.gridy = gridy;
                gbc.gridwidth = 1;
                mainPanel.add(titleLabel, gbc);

                gbc.gridx = 0;
                gbc.gridy = gridy + 1;
                mainPanel.add(new JLabel("Votre réponse :"), gbc);

                JTextField answerField = new JTextField(20);
                gbc.gridx = 1;
                gbc.gridy = gridy + 1;
                mainPanel.add(answerField, gbc);

                // On lie la question à son champ de texte pour une récupération facile et fiable.
                answerFields.put(question, answerField);

                gridy += 2; // Move to the next pair of rows for the next question
            }
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton validateAnswersBtn = new JButton("Valider les réponses");
        validateAnswersBtn.addActionListener(e -> validateAndShowScore());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(validateAnswersBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void validateAndShowScore() {
        // On collecte les réponses de l'utilisateur depuis la Map.
        Map<Question, String> userAnswers = new HashMap<>();
        for (Map.Entry<Question, JTextField> entry : answerFields.entrySet()) {
            userAnswers.put(entry.getKey(), entry.getValue().getText());
        }

        int score = answerController.validateAnswers(userAnswers);
        int total = questions.size();
        JOptionPane.showMessageDialog(this, "Votre score est de : " + score + "/" + total);

    }
}

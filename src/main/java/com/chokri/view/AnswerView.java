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
    private final Map<Question, JTextField> answerFields;

    public AnswerView() {
        this.answerController = new AnswerController();
        this.answerFields = new HashMap<>();

        setTitle("Répondre aux questions");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setJMenuBar(new MenuBar(this));

        QuestionController questionController = QuestionController.getInstance();
        List<Question> questions = questionController.getQuestions();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        if (questions.isEmpty()) {
            mainPanel.add(new JLabel("Aucune question à afficher pour le moment."));
        } else {
            for (Question question : questions) {
                JPanel questionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                questionPanel.add(new JLabel(question.getTitle()));
                questionPanel.add(new JLabel("Votre réponse :"));
                JTextField answerField = new JTextField(20);
                questionPanel.add(answerField);
                mainPanel.add(questionPanel);
                answerFields.put(question, answerField);
            }
        }

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        add(scrollPane, BorderLayout.CENTER);

        JButton validateAnswersBtn = new JButton("Valider les réponses");
        validateAnswersBtn.addActionListener(e -> {
            Map<Question, String> userAnswers = new HashMap<>();
            for (Map.Entry<Question, JTextField> entry : answerFields.entrySet()) {
                userAnswers.put(entry.getKey(), entry.getValue().getText());
            }

            int score = answerController.validateAnswers(userAnswers);
            int total = questions.size();
            JOptionPane.showMessageDialog(this, "Votre score est de : " + score + "/" + total);
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(validateAnswersBtn);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}

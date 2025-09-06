package com.chokri.view;

import com.chokri.controller.QuestionController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuestionView extends JFrame {
    private final QuestionController questionController;
    private final JTextField questionTitleField;
    private final JTextField questionAnswerField;

    public QuestionView() {
        questionController = QuestionController.getInstance();

        setTitle("Create Question");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        setJMenuBar(new MenuBar(this));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        // Creating components
        JLabel welcomeMessage = new JLabel("Créer une question");
        JLabel questionTitleLabel = new JLabel("Titre :");
        questionTitleField = new JTextField(20);
        JLabel questionAnswerLabel = new JLabel("Réponse :");
        questionAnswerField = new JTextField(20);
        JButton createButton = new JButton("Créer");

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createQuestion();
            }
        });

        // Adding to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(welcomeMessage, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(questionTitleLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(questionTitleField, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(questionAnswerLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(questionAnswerField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createButton, gbc);

        add(panel);

    }

    public void createQuestion() {
        String title = questionTitleField.getText();
        String answer = questionAnswerField.getText();
        questionController.createQuestion(title, answer);
        questionTitleField.setText("");
        questionAnswerField.setText("");
        JOptionPane.showMessageDialog(this, "Question créée avec succès !");
    }
}

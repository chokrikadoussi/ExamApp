package com.chokri.view;

import com.chokri.controller.QuestionController;

import javax.swing.*;
import java.awt.*;

public class QuestionView extends JFrame {
    private final QuestionController questionController;
    private final JTextField questionTitleField;
    private final JTextField questionAnswerField;
    private final JTextField errorMarginField;
    private final JLabel errorMarginLabel;
    private final JRadioButton textRadioButton;
    private final JRadioButton numericRadioButton;

    public QuestionView() {
        questionController = QuestionController.getInstance();

        setTitle("Créer Question");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        setJMenuBar(new MenuBar(this));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Header
        JLabel welcomeMessage = new JLabel("Créer une question");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(welcomeMessage, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

        // Question Type
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Type :"), gbc);

        textRadioButton = new JRadioButton("Texte", true);
        numericRadioButton = new JRadioButton("Numérique");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(textRadioButton);
        typeGroup.add(numericRadioButton);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioPanel.add(textRadioButton);
        radioPanel.add(numericRadioButton);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(radioPanel, gbc);

        // Title
        JLabel questionTitleLabel = new JLabel("Titre :");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(questionTitleLabel, gbc);

        questionTitleField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(questionTitleField, gbc);

        // Answer
        JLabel questionAnswerLabel = new JLabel("Réponse :");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(questionAnswerLabel, gbc);

        questionAnswerField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(questionAnswerField, gbc);

        // Error Margin (initially hidden)
        errorMarginLabel = new JLabel("Marge d'erreur :");
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(errorMarginLabel, gbc);

        errorMarginField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(errorMarginField, gbc);

        errorMarginLabel.setVisible(false);
        errorMarginField.setVisible(false);

        // Listeners to show/hide the error margin field
        textRadioButton.addActionListener(e -> {
            errorMarginLabel.setVisible(false);
            errorMarginField.setVisible(false);
        });
        numericRadioButton.addActionListener(e -> {
            errorMarginLabel.setVisible(true);
            errorMarginField.setVisible(true);
        });

        // Create Button
        JButton createButton = new JButton("Créer");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createButton, gbc);

        createButton.addActionListener(e -> createQuestion());

        add(panel);
    }

    public void createQuestion() {
        String title = questionTitleField.getText();
        String answer = questionAnswerField.getText();

        if (textRadioButton.isSelected()) {
            questionController.createTextQuestion(title, answer);
        } else if (numericRadioButton.isSelected()) {
            String errorMargin = errorMarginField.getText();
            questionController.createNumericQuestion(title, answer, errorMargin);
        }

        // Clear all fields
        questionTitleField.setText("");
        questionAnswerField.setText("");
        errorMarginField.setText("");
        JOptionPane.showMessageDialog(this, "Question créée avec succès !");
    }
}

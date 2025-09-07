package com.chokri.view;

import com.chokri.controller.QuestionController;
import com.chokri.utils.UITheme;

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
        UITheme.setupFrame(this, "Créer une question");
        setJMenuBar(new MenuBar(this));

        JPanel panel = new JPanel(new GridBagLayout());
        UITheme.setupPanel(panel);

        GridBagConstraints gbc = UITheme.createGridBagConstraints();

        // Header
        JLabel welcomeMessage = new JLabel("Créer une question");
        UITheme.setupTitle(welcomeMessage);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(welcomeMessage, gbc);

        // Question Type
        JLabel typeLabel = new JLabel("Type :");
        UITheme.setupLabel(typeLabel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(typeLabel, gbc);

        textRadioButton = new JRadioButton("Texte", true);
        numericRadioButton = new JRadioButton("Numérique");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(textRadioButton);
        typeGroup.add(numericRadioButton);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioPanel.setBackground(UITheme.SECONDARY_COLOR);
        radioPanel.add(textRadioButton);
        radioPanel.add(numericRadioButton);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(radioPanel, gbc);

        // Title
        JLabel questionTitleLabel = new JLabel("Titre :");
        UITheme.setupLabel(questionTitleLabel);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(questionTitleLabel, gbc);

        questionTitleField = new JTextField();
        UITheme.setupTextField(questionTitleField);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(questionTitleField, gbc);

        // Answer
        JLabel questionAnswerLabel = new JLabel("Réponse :");
        UITheme.setupLabel(questionAnswerLabel);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(questionAnswerLabel, gbc);

        questionAnswerField = new JTextField();
        UITheme.setupTextField(questionAnswerField);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(questionAnswerField, gbc);

        // Error Margin
        errorMarginLabel = new JLabel("Marge d'erreur :");
        UITheme.setupLabel(errorMarginLabel);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(errorMarginLabel, gbc);

        errorMarginField = new JTextField();
        UITheme.setupTextField(errorMarginField);
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(errorMarginField, gbc);

        errorMarginLabel.setVisible(false);
        errorMarginField.setVisible(false);

        // Radio button listeners
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
        UITheme.setupButton(createButton);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createButton, gbc);

        createButton.addActionListener(e -> createQuestion());

        add(panel);
    }

    private void createQuestion() {
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

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
        JLabel questionAnswerLabel = new JLabel("Réponse(s) :");
        UITheme.setupLabel(questionAnswerLabel);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(questionAnswerLabel, gbc);

        questionAnswerField = new JTextField();
        UITheme.setupTextField(questionAnswerField);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(questionAnswerField, gbc);

        // Aide format CSV pour les réponses textuelles
        JLabel csvHelpLabel = new JLabel("<html>Format: séparé par des virgules<br>Exemple: Paris, Londres, Madrid</html>");
        csvHelpLabel.setForeground(UITheme.TEXT_COLOR);
        csvHelpLabel.setFont(csvHelpLabel.getFont().deriveFont(Font.ITALIC, 11f));
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(csvHelpLabel, gbc);

        // Error Margin (initially hidden)
        errorMarginLabel = new JLabel("Marge d'erreur :");
        UITheme.setupLabel(errorMarginLabel);
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(errorMarginLabel, gbc);

        errorMarginField = new JTextField();
        UITheme.setupTextField(errorMarginField);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(errorMarginField, gbc);

        errorMarginLabel.setVisible(false);
        errorMarginField.setVisible(false);
        csvHelpLabel.setVisible(true);

        // Listeners pour afficher/masquer les champs appropriés
        textRadioButton.addActionListener(e -> {
            errorMarginLabel.setVisible(false);
            errorMarginField.setVisible(false);
            csvHelpLabel.setVisible(true);
            questionAnswerLabel.setText("Réponse(s) :");
        });
        numericRadioButton.addActionListener(e -> {
            errorMarginLabel.setVisible(true);
            errorMarginField.setVisible(true);
            csvHelpLabel.setVisible(false);
            questionAnswerLabel.setText("Réponse :");
        });

        // Create Button
        JButton createButton = new JButton("Créer");
        UITheme.setupButton(createButton);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createButton, gbc);

        createButton.addActionListener(e -> createQuestion());

        add(panel);
    }

    private void createQuestion() {
        String title = questionTitleField.getText();
        String answer = questionAnswerField.getText();

        if (title.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Le titre de la question ne peut pas être vide.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (answer.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "La réponse ne peut pas être vide.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (textRadioButton.isSelected()) {
            questionController.createTextQuestion(title, answer);
        } else if (numericRadioButton.isSelected()) {
            String errorMargin = errorMarginField.getText();
            if (errorMargin.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "La marge d'erreur ne peut pas être vide pour une question numérique.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            questionController.createNumericQuestion(title, answer, errorMargin);
        }

        // Clear all fields
        questionTitleField.setText("");
        questionAnswerField.setText("");
        errorMarginField.setText("");
        JOptionPane.showMessageDialog(this, "Question créée avec succès !");
    }
}

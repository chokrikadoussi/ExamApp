package com.chokri.view;

import com.chokri.controller.QuestionController;
import com.chokri.utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionView extends JFrame {
    private final QuestionController questionController;
    private final JTextField questionTitleField;
    private final JTextField questionAnswerField;
    private final JTextField errorMarginField;
    private final JTextField pointsField; // Nouveau champ pour les points
    private final JLabel errorMarginLabel;
    private final JRadioButton textRadioButton;
    private final JRadioButton numericRadioButton;
    private final JRadioButton qcmRadioButton;

    // Composants pour le QCM
    private final JPanel qcmPanel;
    private final List<JTextField> qcmOptions;
    private final ButtonGroup qcmAnswerGroup;
    private final JPanel qcmOptionsPanel;

    public QuestionView() {
        questionController = QuestionController.getInstance();
        qcmOptions = new ArrayList<>();
        qcmAnswerGroup = new ButtonGroup();

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
        qcmRadioButton = new JRadioButton("QCM");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(textRadioButton);
        typeGroup.add(numericRadioButton);
        typeGroup.add(qcmRadioButton);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        radioPanel.setBackground(UITheme.SECONDARY_COLOR);
        radioPanel.add(textRadioButton);
        radioPanel.add(numericRadioButton);
        radioPanel.add(qcmRadioButton);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(radioPanel, gbc);

        // Points field
        JLabel pointsLabel = new JLabel("Points :");
        UITheme.setupLabel(pointsLabel);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        panel.add(pointsLabel, gbc);

        pointsField = new JTextField("1"); // Valeur par défaut : 1 point
        UITheme.setupTextField(pointsField);
        gbc.gridx = 1;
        panel.add(pointsField, gbc);

        // Title field (décalé d'une ligne)
        JLabel questionTitleLabel = new JLabel("Titre :");
        UITheme.setupLabel(questionTitleLabel);
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(questionTitleLabel, gbc);

        questionTitleField = new JTextField();
        UITheme.setupTextField(questionTitleField);
        gbc.gridx = 1;
        panel.add(questionTitleField, gbc);

        // Panneau pour réponse textuelle/numérique
        JPanel standardAnswerPanel = new JPanel(new GridBagLayout());
        standardAnswerPanel.setBackground(UITheme.SECONDARY_COLOR);

        // Answer
        JLabel questionAnswerLabel = new JLabel("Réponse(s) :");
        UITheme.setupLabel(questionAnswerLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        standardAnswerPanel.add(questionAnswerLabel, gbc);

        questionAnswerField = new JTextField();
        UITheme.setupTextField(questionAnswerField);
        gbc.gridx = 1;
        standardAnswerPanel.add(questionAnswerField, gbc);

        // Error Margin
        errorMarginLabel = new JLabel("Marge d'erreur :");
        UITheme.setupLabel(errorMarginLabel);
        gbc.gridx = 0;
        gbc.gridy = 1;
        standardAnswerPanel.add(errorMarginLabel, gbc);

        errorMarginField = new JTextField();
        UITheme.setupTextField(errorMarginField);
        gbc.gridx = 1;
        standardAnswerPanel.add(errorMarginField, gbc);

        // Aide format CSV
        JLabel csvHelpLabel = new JLabel("<html>Format: séparé par des virgules<br>Exemple: Paris, Londres, Madrid</html>");
        csvHelpLabel.setForeground(UITheme.TEXT_COLOR);
        csvHelpLabel.setFont(csvHelpLabel.getFont().deriveFont(Font.ITALIC, 11f));
        gbc.gridx = 1;
        gbc.gridy = 2;
        standardAnswerPanel.add(csvHelpLabel, gbc);

        // Panneau QCM
        qcmPanel = new JPanel(new GridBagLayout());
        qcmPanel.setBackground(UITheme.SECONDARY_COLOR);

        qcmOptionsPanel = new JPanel(new GridBagLayout());
        qcmOptionsPanel.setBackground(UITheme.SECONDARY_COLOR);

        JButton addOptionButton = new JButton("Ajouter une option");
        UITheme.setupButton(addOptionButton);
        addOptionButton.addActionListener(e -> addQCMOption());

        gbc.gridx = 0;
        gbc.gridy = 0;
        qcmPanel.add(new JLabel("Options du QCM :"), gbc);

        gbc.gridy = 1;
        qcmPanel.add(qcmOptionsPanel, gbc);

        gbc.gridy = 2;
        qcmPanel.add(addOptionButton, gbc);

        // Ajouter les panneaux au panel principal
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(standardAnswerPanel, gbc);
        panel.add(qcmPanel, gbc);
        qcmPanel.setVisible(false);

        // Create Button
        JButton createButton = new JButton("Créer");
        UITheme.setupButton(createButton);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createButton, gbc);

        createButton.addActionListener(e -> createQuestion());

        // Listeners pour gérer la visibilité des panneaux
        textRadioButton.addActionListener(e -> {
            standardAnswerPanel.setVisible(true);
            qcmPanel.setVisible(false);
            errorMarginLabel.setVisible(false);
            errorMarginField.setVisible(false);
            csvHelpLabel.setVisible(true);
            questionAnswerLabel.setText("Réponse(s) :");
            pack();
        });

        numericRadioButton.addActionListener(e -> {
            standardAnswerPanel.setVisible(true);
            qcmPanel.setVisible(false);
            errorMarginLabel.setVisible(true);
            errorMarginField.setVisible(true);
            csvHelpLabel.setVisible(false);
            questionAnswerLabel.setText("Réponse :");
            pack();
        });

        qcmRadioButton.addActionListener(e -> {
            standardAnswerPanel.setVisible(false);
            qcmPanel.setVisible(true);
            pack();
        });

        add(panel);
        pack();
    }

    private void addQCMOption() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = qcmOptions.size();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.WEST;

        JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionPanel.setBackground(UITheme.SECONDARY_COLOR);

        JTextField optionField = new JTextField(20);
        UITheme.setupTextField(optionField);
        qcmOptions.add(optionField);

        JRadioButton correctAnswer = new JRadioButton("Bonne réponse");
        qcmAnswerGroup.add(correctAnswer);

        optionPanel.add(optionField);
        optionPanel.add(correctAnswer);

        qcmOptionsPanel.add(optionPanel, gbc);
        pack();
        revalidate();
    }

    private void createQuestion() {
        String title = questionTitleField.getText().trim();
        String pointsStr = pointsField.getText().trim();
        int points;

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Le titre de la question ne peut pas être vide.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            points = Integer.parseInt(pointsStr);
            if (points <= 0) {
                throw new NumberFormatException("Les points doivent être positifs");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Le nombre de points doit être un entier positif.",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (textRadioButton.isSelected()) {
                String answer = questionAnswerField.getText();
                if (answer.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La réponse ne peut pas être vide.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                questionController.createTextQuestion(title, answer, points);
            }
            else if (numericRadioButton.isSelected()) {
                String answer = questionAnswerField.getText();
                String errorMargin = errorMarginField.getText();
                if (answer.trim().isEmpty() || errorMargin.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "La réponse et la marge d'erreur ne peuvent pas être vides.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                questionController.createNumericQuestion(title, answer, errorMargin, points);
            }
            else if (qcmRadioButton.isSelected()) {
                if (qcmOptions.size() < 2) {
                    JOptionPane.showMessageDialog(this,
                        "Un QCM doit avoir au moins 2 options.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String[] options = new String[qcmOptions.size()];
                int selectedIndex = -1;
                int currentIndex = 0;

                Component[] components = qcmOptionsPanel.getComponents();
                for (Component comp : components) {
                    if (comp instanceof JPanel) {
                        JPanel optionPanel = (JPanel) comp;
                        Component[] optionComponents = optionPanel.getComponents();

                        for (Component c : optionComponents) {
                            if (c instanceof JTextField) {
                                options[currentIndex] = ((JTextField) c).getText().trim();
                            } else if (c instanceof JRadioButton && ((JRadioButton) c).isSelected()) {
                                selectedIndex = currentIndex;
                            }
                        }
                        currentIndex++;
                    }
                }

                if (selectedIndex == -1) {
                    JOptionPane.showMessageDialog(this,
                        "Veuillez sélectionner la bonne réponse.",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                questionController.createQCMQuestion(title, options, selectedIndex, points);
            }

            // Clear all fields
            questionTitleField.setText("");
            questionAnswerField.setText("");
            errorMarginField.setText("");
            pointsField.setText("1"); // Réinitialiser à 1 point
            clearQCMOptions();

            JOptionPane.showMessageDialog(this, "Question créée avec succès !");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la création de la question : " + e.getMessage(),
                "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearQCMOptions() {
        qcmOptionsPanel.removeAll();
        qcmOptions.clear();
        qcmAnswerGroup.clearSelection();
        pack();
        revalidate();
    }
}

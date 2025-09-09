package com.chokri.view;

import com.chokri.controller.AppOrchestrator;
import com.chokri.utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionView extends JFrame {
    private final AppOrchestrator orchestrator;
    private final JTextField questionTitleField;
    private final JTextField questionAnswerField;
    private final JTextField errorMarginField;
    private final JTextField pointsField;
    private final JLabel errorMarginLabel;
    private final JRadioButton textRadioButton;
    private final JRadioButton numericRadioButton;
    private final JRadioButton qcmRadioButton;

    // Composants pour le QCM
    private final JPanel qcmPanel;
    private final List<JTextField> qcmOptions;
    private final ButtonGroup qcmAnswerGroup;
    private final JPanel qcmOptionsPanel;

    public QuestionView(AppOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
        qcmOptions = new ArrayList<>();
        qcmAnswerGroup = new ButtonGroup();

        UITheme.setupFrame(this, "Créer une question");
        setJMenuBar(new MenuBar(this, orchestrator));

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
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(typeLabel, gbc);

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.setBackground(UITheme.SECONDARY_COLOR);

        textRadioButton = new JRadioButton("Textuelle");
        numericRadioButton = new JRadioButton("Numérique");
        qcmRadioButton = new JRadioButton("QCM");

        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(textRadioButton);
        typeGroup.add(numericRadioButton);
        typeGroup.add(qcmRadioButton);
        textRadioButton.setSelected(true);

        typePanel.add(textRadioButton);
        typePanel.add(numericRadioButton);
        typePanel.add(qcmRadioButton);

        gbc.gridx = 1;
        panel.add(typePanel, gbc);

        // Titre de la question
        JLabel titleLabel = new JLabel("Titre :");
        UITheme.setupLabel(titleLabel);
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(titleLabel, gbc);

        questionTitleField = new JTextField(20);
        UITheme.setupTextField(questionTitleField);
        gbc.gridx = 1;
        panel.add(questionTitleField, gbc);

        // Points
        JLabel pointsLabel = new JLabel("Points :");
        UITheme.setupLabel(pointsLabel);
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(pointsLabel, gbc);

        pointsField = new JTextField(5);
        UITheme.setupTextField(pointsField);
        pointsField.setText("1");
        gbc.gridx = 1;
        panel.add(pointsField, gbc);

        // Réponse/Marge d'erreur (pour textuelle et numérique)
        JLabel answerLabel = new JLabel("Réponse :");
        UITheme.setupLabel(answerLabel);
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(answerLabel, gbc);

        questionAnswerField = new JTextField(20);
        UITheme.setupTextField(questionAnswerField);
        gbc.gridx = 1;
        panel.add(questionAnswerField, gbc);

        // Marge d'erreur (seulement pour numérique)
        errorMarginLabel = new JLabel("Marge d'erreur :");
        UITheme.setupLabel(errorMarginLabel);
        gbc.gridy = 5;
        gbc.gridx = 0;
        panel.add(errorMarginLabel, gbc);

        errorMarginField = new JTextField(10);
        UITheme.setupTextField(errorMarginField);
        errorMarginField.setText("0.1");
        gbc.gridx = 1;
        panel.add(errorMarginField, gbc);

        // Panel QCM
        qcmPanel = new JPanel(new GridBagLayout());
        qcmPanel.setBackground(UITheme.SECONDARY_COLOR);
        qcmPanel.setBorder(BorderFactory.createTitledBorder("Options QCM"));

        qcmOptionsPanel = new JPanel(new GridBagLayout());
        qcmOptionsPanel.setBackground(UITheme.SECONDARY_COLOR);

        JButton addOptionButton = new JButton("Ajouter option");
        UITheme.setupButton(addOptionButton);
        addOptionButton.addActionListener(e -> addQCMOption());

        GridBagConstraints qcmGbc = new GridBagConstraints();
        qcmGbc.gridx = 0;
        qcmGbc.gridy = 0;
        qcmGbc.gridwidth = 2;
        qcmGbc.fill = GridBagConstraints.HORIZONTAL;
        qcmPanel.add(qcmOptionsPanel, qcmGbc);

        qcmGbc.gridy = 1;
        qcmGbc.gridwidth = 1;
        qcmPanel.add(addOptionButton, qcmGbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(qcmPanel, gbc);

        // Ajouter les deux premières options par défaut
        addQCMOption();
        addQCMOption();

        // Action listeners pour gérer l'affichage selon le type
        textRadioButton.addActionListener(e -> updateFieldsVisibility());
        numericRadioButton.addActionListener(e -> updateFieldsVisibility());
        qcmRadioButton.addActionListener(e -> updateFieldsVisibility());

        // Bouton créer
        JButton createButton = new JButton("Créer la question");
        UITheme.setupButton(createButton);
        createButton.addActionListener(e -> createQuestion());
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(createButton, gbc);

        add(panel);
        pack();

        // Initialiser l'affichage
        updateFieldsVisibility();
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

    private void updateFieldsVisibility() {
        boolean isNumeric = numericRadioButton.isSelected();
        boolean isQCM = qcmRadioButton.isSelected();

        errorMarginLabel.setVisible(isNumeric);
        errorMarginField.setVisible(isNumeric);
        qcmPanel.setVisible(isQCM);
        questionAnswerField.setVisible(!isQCM);

        pack();
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
                orchestrator.createTextQuestion(title, answer, points);
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
                // Valider les valeurs numériques
                Double.parseDouble(answer);
                Double.parseDouble(errorMargin);
                orchestrator.createNumericQuestion(title, answer, errorMargin, points);
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
                                String optionText = ((JTextField) c).getText().trim();
                                if (optionText.isEmpty()) {
                                    JOptionPane.showMessageDialog(this,
                                        "Toutes les options doivent être remplies.",
                                        "Erreur", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                options[currentIndex] = optionText;
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

                orchestrator.createQCMQuestion(title, options, selectedIndex, points);
            }

            // Clear all fields
            questionTitleField.setText("");
            questionAnswerField.setText("");
            errorMarginField.setText("");
            pointsField.setText("1");
            clearQCMOptions();

            JOptionPane.showMessageDialog(this, "Question créée avec succès !");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Format numérique invalide.",
                "Erreur", JOptionPane.ERROR_MESSAGE);
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
        addQCMOption();
        addQCMOption();
        pack();
        revalidate();
    }
}

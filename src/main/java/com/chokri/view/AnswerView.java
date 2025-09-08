package com.chokri.view;

import com.chokri.controller.AnswerController;
import com.chokri.model.Question;
import com.chokri.model.QuestionQCM;
import com.chokri.utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswerView extends JFrame {
    private final AnswerController answerController;
    private final Map<Question, Object> answerComponents; // Peut contenir JTextField ou ButtonGroup
    private final List<Question> questions;

    // Constructeur pour une fenêtre standalone
    public AnswerView(List<Question> questions) {
        this.questions = questions;
        this.answerController = new AnswerController();
        this.answerComponents = new HashMap<>();
        initialize();
    }

    // Méthode pour créer un panel à intégrer dans une autre vue
    public static JPanel createPanel(List<Question> questions, Map<Question, Object> answerComponents) {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        UITheme.setupPanel(mainPanel);
        GridBagConstraints gbc = UITheme.createGridBagConstraints();

        if (questions.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucune question à afficher pour le moment.");
            UITheme.setupLabel(emptyLabel);
            gbc.gridy = 1;
            mainPanel.add(emptyLabel, gbc);
        } else {
            int gridy = 0;
            for (Question question : questions) {
                // Question title with bold font
                JLabel titleLabel = new JLabel(question.getTitle());
                UITheme.setupHeader(titleLabel);
                gbc.gridx = 0;
                gbc.gridy = gridy;
                gbc.gridwidth = 2;
                mainPanel.add(titleLabel, gbc);
                gridy++;

                if (question instanceof QuestionQCM) {
                    QuestionQCM qcm = (QuestionQCM) question;
                    ButtonGroup group = new ButtonGroup();
                    JPanel optionsPanel = createQCMOptionsPanel(qcm, group);

                    gbc.gridx = 0;
                    gbc.gridy = gridy;
                    gbc.gridwidth = 2;
                    mainPanel.add(optionsPanel, gbc);
                    answerComponents.put(question, group);
                } else {
                    JLabel answerLabel = new JLabel("Votre réponse :");
                    UITheme.setupLabel(answerLabel);
                    gbc.gridx = 0;
                    gbc.gridy = gridy;
                    gbc.gridwidth = 1;
                    mainPanel.add(answerLabel, gbc);

                    JTextField answerField = new JTextField();
                    UITheme.setupTextField(answerField);
                    gbc.gridx = 1;
                    mainPanel.add(answerField, gbc);
                    answerComponents.put(question, answerField);
                }

                gridy++;
                // Ajouter un espace entre les questions
                gbc.gridy = gridy++;
                mainPanel.add(Box.createVerticalStrut(20), gbc);
            }
        }

        return mainPanel;
    }

    private void initialize() {
        UITheme.setupFrame(this, "Répondre aux questions");
        setJMenuBar(new MenuBar(this));

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
                System.out.println("Traitement de la question: " + question.getTitle());
                System.out.println("Type de la question: " + question.getClass().getName());

                // Question title with bold font
                JLabel titleLabel = new JLabel(question.getTitle());
                UITheme.setupHeader(titleLabel);
                gbc.gridx = 0;
                gbc.gridy = gridy;
                gbc.gridwidth = 2;
                mainPanel.add(titleLabel, gbc);
                gridy++;

                // Vérifier explicitement si c'est une instance de QuestionQCM
                if (question instanceof QuestionQCM) {
                    System.out.println("Question détectée comme QCM");
                    QuestionQCM qcm = (QuestionQCM) question;
                    // Création du groupe de boutons radio pour le QCM
                    ButtonGroup group = new ButtonGroup();
                    JPanel optionsPanel = createQCMOptionsPanel(qcm, group);

                    gbc.gridx = 0;
                    gbc.gridy = gridy;
                    gbc.gridwidth = 2;
                    mainPanel.add(optionsPanel, gbc);
                    answerComponents.put(question, group);
                } else {
                    // Question textuelle ou numérique
                    JLabel answerLabel = new JLabel("Votre réponse :");
                    UITheme.setupLabel(answerLabel);
                    gbc.gridx = 0;
                    gbc.gridy = gridy;
                    gbc.gridwidth = 1;
                    mainPanel.add(answerLabel, gbc);

                    JTextField answerField = new JTextField();
                    UITheme.setupTextField(answerField);
                    gbc.gridx = 1;
                    mainPanel.add(answerField, gbc);
                    answerComponents.put(question, answerField);
                }

                gridy++;
                // Ajouter un espace entre les questions
                gbc.gridy = gridy++;
                mainPanel.add(Box.createVerticalStrut(20), gbc);
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

        for (Map.Entry<Question, Object> entry : answerComponents.entrySet()) {
            Question question = entry.getKey();
            Object component = entry.getValue();

            if (component instanceof JTextField) {
                userAnswers.put(question, ((JTextField) component).getText());
            } else if (component instanceof ButtonGroup) {
                ButtonGroup group = (ButtonGroup) component;
                if (group.getSelection() != null) {
                    ButtonModel selectedButton = group.getSelection();
                    // Trouver l'index du bouton sélectionné
                    int selectedIndex = -1;
                    int currentIndex = 0;
                    for (java.util.Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements(); ) {
                        AbstractButton button = buttons.nextElement();
                        if (button.getModel() == selectedButton) {
                            selectedIndex = currentIndex;
                            break;
                        }
                        currentIndex++;
                    }
                    userAnswers.put(question, String.valueOf(selectedIndex));
                } else {
                    userAnswers.put(question, ""); // Aucune réponse sélectionnée
                }
            }
        }

        int score = answerController.validateAnswers(userAnswers);
        int total = questions.size();
        JOptionPane.showMessageDialog(this,
                String.format("Votre score est de : %d/%d", score, total),
                "Résultat",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private static JPanel createQCMOptionsPanel(QuestionQCM qcm, ButtonGroup group) {
        JPanel optionsPanel = new JPanel(new GridBagLayout());
        optionsPanel.setBackground(UITheme.SECONDARY_COLOR);
        GridBagConstraints optGbc = new GridBagConstraints();
        optGbc.anchor = GridBagConstraints.WEST;
        optGbc.insets = new Insets(2, 20, 2, 2);

        List<String> options = qcm.getOptions();
        for (int i = 0; i < options.size(); i++) {
            JRadioButton radioButton = new JRadioButton(options.get(i));
            radioButton.setBackground(UITheme.SECONDARY_COLOR);
            group.add(radioButton);
            optGbc.gridy = i;
            optionsPanel.add(radioButton, optGbc);
        }

        return optionsPanel;
    }
}

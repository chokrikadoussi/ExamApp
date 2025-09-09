package com.chokri.view;

import com.chokri.component.AnswerPanel;
import com.chokri.controller.AppOrchestrator;
import com.chokri.model.Question;
import com.chokri.model.Quiz;
import com.chokri.model.Submission;
import com.chokri.utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StudentView extends JFrame {
    private final AppOrchestrator orchestrator;
    private final JPanel mainPanel;
    private final CardLayout cardLayout;
    private long quizStartTime;

    public StudentView(AppOrchestrator orchestrator) {
        this.orchestrator = orchestrator;

        UITheme.setupFrame(this, "Espace Étudiant - Quiz disponibles");
        setJMenuBar(new MenuBar(this, orchestrator));

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        UITheme.setupPanel(mainPanel);

        // Panel liste des quiz
        JPanel quizListPanel = createQuizListPanel();
        mainPanel.add(quizListPanel, "QUIZ_LIST");

        add(mainPanel);
    }

    private JPanel createQuizListPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        UITheme.setupPanel(panel);
        GridBagConstraints gbc = UITheme.createGridBagConstraints();

        // Titre
        JLabel titleLabel = new JLabel("Quiz disponibles");
        UITheme.setupTitle(titleLabel);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // Liste des quiz publiés via l'orchestrateur
        List<Quiz> publishedQuizzes = orchestrator.getPublishedQuizzes();

        if (publishedQuizzes.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucun quiz disponible pour le moment");
            UITheme.setupLabel(emptyLabel);
            gbc.gridy = 1;
            panel.add(emptyLabel, gbc);
        } else {
            int row = 1;
            for (Quiz quiz : publishedQuizzes) {
                JPanel quizPanel = createQuizEntryPanel(quiz);
                gbc.gridy = row++;
                panel.add(quizPanel, gbc);
            }
        }

        return panel;
    }

    private JPanel createQuizEntryPanel(Quiz quiz) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(UITheme.SECONDARY_COLOR);

        // Vérifier si le quiz a déjà été complété via l'orchestrateur
        boolean isCompleted = orchestrator.isQuizCompleted(quiz);

        // Créer une étiquette avec une icône pour indiquer l'état
        String statusIcon = isCompleted ? "✓ " : "";
        JLabel quizInfo = new JLabel(String.format("%s%s (Coefficient: %.1f, Temps: %d min)",
            statusIcon, quiz.getTitle(), quiz.getCoefficient(), quiz.getTimeLimit()));
        UITheme.setupLabel(quizInfo);
        if (isCompleted) {
            quizInfo.setForeground(new Color(0, 128, 0)); // Vert pour les quiz complétés
        }
        panel.add(quizInfo);

        // Changer le texte du bouton selon l'état
        JButton actionButton = new JButton(isCompleted ? "Voir résultats" : "Commencer");
        UITheme.setupButton(actionButton);
        actionButton.addActionListener(e -> showQuizQuestions(quiz, isCompleted));
        panel.add(actionButton);

        return panel;
    }

    private void showQuizQuestions(Quiz quiz, boolean readOnly) {
        if (readOnly) {
            // Afficher les résultats précédents via l'orchestrateur
            Optional<Submission> latestSubmission = orchestrator.getLatestSubmission(quiz);
            latestSubmission.ifPresent(submission -> {
                String resultMessage = orchestrator.generateDetailedResults(submission);

                JTextArea textArea = new JTextArea(resultMessage);
                textArea.setEditable(false);
                textArea.setBackground(UITheme.SECONDARY_COLOR);
                textArea.setFont(UITheme.LABEL_FONT);

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(400, 300));

                JOptionPane.showMessageDialog(this,
                    scrollPane,
                    "Résultats du Quiz",
                    JOptionPane.INFORMATION_MESSAGE);
            });
            return;
        }

        // Enregistrer le temps de début pour un nouveau quiz
        quizStartTime = System.currentTimeMillis();

        JPanel quizPanel = new JPanel(new BorderLayout());
        UITheme.setupPanel(quizPanel);

        // En-tête du quiz
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(UITheme.SECONDARY_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel quizTitle = new JLabel(quiz.getTitle());
        UITheme.setupTitle(quizTitle);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        headerPanel.add(quizTitle, gbc);

        // Afficher le total des points disponibles
        int totalPoints = quiz.getQuestions().stream()
                .mapToInt(Question::getPoints)
                .sum();
        JLabel quizInfo = new JLabel(String.format("Temps: %d minutes - Coefficient: %.1f - Total: %d points",
            quiz.getTimeLimit(), quiz.getCoefficient(), totalPoints));
        UITheme.setupHeader(quizInfo);
        gbc.gridy = 1;
        headerPanel.add(quizInfo, gbc);

        quizPanel.add(headerPanel, BorderLayout.NORTH);

        // Utilisation du composant AnswerPanel
        AnswerPanel answerPanel = new AnswerPanel(quiz.getQuestions());
        JScrollPane scrollPane = new JScrollPane(answerPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        quizPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(UITheme.SECONDARY_COLOR);

        if (!readOnly) {
            JButton validateButton = new JButton("Valider les réponses");
            UITheme.setupButton(validateButton);
            validateButton.addActionListener(e -> validateQuiz(quiz, answerPanel.getAnswerComponents()));
            buttonPanel.add(validateButton);
        }

        // Bouton de retour
        JButton backButton = new JButton("Retour à la liste des quiz");
        UITheme.setupButton(backButton);
        backButton.addActionListener(e -> {
            answerPanel.getAnswerComponents().clear();
            mainPanel.remove(quizPanel);
            cardLayout.show(mainPanel, "QUIZ_LIST");
        });
        buttonPanel.add(backButton);

        quizPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(quizPanel, "QUIZ_" + quiz.getTitle());
        cardLayout.show(mainPanel, "QUIZ_" + quiz.getTitle());
    }


    private void disableAllComponents(Container container) {
        Component[] components = container.getComponents();
        for (Component component : components) {
            component.setEnabled(false);
            if (component instanceof Container) {
                disableAllComponents((Container) component);
            }
        }
    }

    private void validateQuiz(Quiz quiz, Map<Question, Object> answerComponents) {
        // Calculer le temps passé
        long endTime = System.currentTimeMillis();
        int timeSpentMinutes = (int) ((endTime - quizStartTime) / (1000 * 60));

        Map<Question, String> userAnswers = new HashMap<>();

        // Collecter les réponses
        for (Map.Entry<Question, Object> entry : answerComponents.entrySet()) {
            Question question = entry.getKey();
            Object component = entry.getValue();

            if (component instanceof JTextField) {
                userAnswers.put(question, ((JTextField) component).getText().trim());
            } else if (component instanceof ButtonGroup) {
                ButtonGroup group = (ButtonGroup) component;
                if (group.getSelection() != null) {
                    ButtonModel selectedButton = group.getSelection();
                    // Trouver l'index du bouton sélectionné
                    int selectedIndex = -1;
                    int currentIndex = 0;
                    for (java.util.Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
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

        // Soumettre le quiz via l'orchestrateur
        Submission submission = orchestrator.submitQuiz(quiz, userAnswers, timeSpentMinutes);
        String resultMessage = orchestrator.generateDetailedResults(submission);

        // Afficher les résultats dans une boîte de dialogue personnalisée avec défilement
        JTextArea textArea = new JTextArea(resultMessage);
        textArea.setEditable(false);
        textArea.setBackground(UITheme.SECONDARY_COLOR);
        textArea.setFont(UITheme.LABEL_FONT);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this,
            scrollPane,
            "Résultats du Quiz",
            JOptionPane.INFORMATION_MESSAGE);

        // Retour à la liste des quiz
        cardLayout.show(mainPanel, "QUIZ_LIST");
    }
}

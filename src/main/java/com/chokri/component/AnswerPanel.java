package com.chokri.component;

import com.chokri.model.Question;
import com.chokri.model.QuestionQCM;
import com.chokri.utils.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswerPanel extends JPanel {
    private final Map<Question, Object> answerComponents;
    private final List<Question> questions;

    public AnswerPanel(List<Question> questions) {
        this.questions = questions;
        this.answerComponents = new HashMap<>();
        setLayout(new GridBagLayout());
        UITheme.setupPanel(this);
        GridBagConstraints gbc = UITheme.createGridBagConstraints();
        if (questions.isEmpty()) {
            JLabel emptyLabel = new JLabel("Aucune question à afficher pour le moment.");
            UITheme.setupLabel(emptyLabel);
            gbc.gridy = 1;
            add(emptyLabel, gbc);
        } else {
            int gridy = 0;
            for (Question question : questions) {
                JLabel titleLabel = new JLabel(question.getTitle());
                UITheme.setupHeader(titleLabel);
                gbc.gridx = 0;
                gbc.gridy = gridy;
                gbc.gridwidth = 2;
                add(titleLabel, gbc);
                gridy++;
                if (question instanceof QuestionQCM) {
                    QuestionQCM qcm = (QuestionQCM) question;
                    ButtonGroup group = new ButtonGroup();
                    JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    for (String option : qcm.getOptions()) {
                        JRadioButton radioButton = new JRadioButton(option);
                        group.add(radioButton);
                        optionsPanel.add(radioButton);
                    }
                    gbc.gridx = 0;
                    gbc.gridy = gridy;
                    gbc.gridwidth = 2;
                    add(optionsPanel, gbc);
                    answerComponents.put(question, group);
                } else {
                    JTextField answerField = new JTextField(30);
                    gbc.gridx = 0;
                    gbc.gridy = gridy;
                    gbc.gridwidth = 2;
                    add(answerField, gbc);
                    answerComponents.put(question, answerField);
                }
                gridy++;
            }
        }
    }

    public Map<Question, Object> getAnswerComponents() {
        return answerComponents;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}


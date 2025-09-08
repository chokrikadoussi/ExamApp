package com.chokri.controller;

import com.chokri.model.Question;
import com.chokri.model.QuestionNum;
import com.chokri.model.QuestionText;

import java.util.ArrayList;
import java.util.List;

public class QuestionController {

    private static QuestionController instance;
    private final List<Question> questions;

    private QuestionController() {
        this.questions = new ArrayList<>();
    }

    public static QuestionController getInstance() {
        if (instance == null) {
            instance = new QuestionController();
        }
        return instance;
    }

    public void createTextQuestion(String title, String answersCSV) {
        Question question = new QuestionText(title, answersCSV);
        this.questions.add(question);
    }

    public void createNumericQuestion(String title, String answer, String errorMargin) {
        try {
            double answerValue = Double.parseDouble(answer);
            double marginValue = Double.parseDouble(errorMargin);
            Question question = new QuestionNum(title, answerValue, marginValue);
            this.questions.add(question);
        } catch (NumberFormatException e) {
            //TODO: log this exception properly
            // Handle the case where the answer or margin is not a valid number.
            // For now, we can show an error or log it.
            System.err.println("Erreur de format numérique : " + e.getMessage());
            // In a real app, you'd show a JOptionPane to the user.
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }
}

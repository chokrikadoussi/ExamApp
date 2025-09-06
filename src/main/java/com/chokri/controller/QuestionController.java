package com.chokri.controller;

import com.chokri.model.Question;

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

    public void createQuestion(String title, String answer) {
        // Logic to add a question
        Question question = new Question(title, answer);
        this.questions.add(question);
    }

    public List<Question> getQuestions() {
        return questions;
    }
}

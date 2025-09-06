package com.chokri.controller;

import com.chokri.model.Question;

import java.util.Map;

public class AnswerController {

    private final QuestionController questionController;

    public AnswerController() {
        this.questionController = QuestionController.getInstance();
    }

    public int validateAnswers(Map<Question, String> userAnswers) {
        int score = 0;
        for (Map.Entry<Question, String> entry : userAnswers.entrySet()) {
            Question question = entry.getKey();
            String userAnswer = entry.getValue();
            if (question.checkAnswer(userAnswer)) {
                score++;
            }
        }
        return score;
    }
}

package com.chokri.controller;

import com.chokri.model.Question;
import com.chokri.model.Quiz;
import com.chokri.service.GradingService;

import java.util.Map;

public class AnswerController {
    private static AnswerController instance;
    private final GradingService gradingService;

    public AnswerController() {
        this.gradingService = GradingService.getInstance();
    }

    public static AnswerController getInstance() {
        if (instance == null) {
            instance = new AnswerController();
        }
        return instance;
    }

    public int validateAnswers(Map<Question, String> userAnswers) {
        return gradingService.calculateRawScore(userAnswers);
    }

    public double calculateQuizScore(Quiz quiz, Map<Question, String> userAnswers) {
        return gradingService.calculateQuizScore(quiz, userAnswers);
    }

    public String getFormattedQuizResult(Quiz quiz, Map<Question, String> userAnswers) {
        return gradingService.formatQuizResult(quiz, userAnswers);
    }
}

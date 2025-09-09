package com.chokri.controller;

import com.chokri.model.Question;
import com.chokri.model.Quiz;
import com.chokri.service.GradingService;

import java.util.Map;

/**
 * Contrôleur gérant les réponses et l'évaluation.
 * Utilise l'injection de dépendances au lieu du pattern singleton.
 */
public class AnswerController {
    private final GradingService gradingService;

    // Injection de dépendances via constructeur
    public AnswerController(GradingService gradingService) {
        this.gradingService = gradingService;
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

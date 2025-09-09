package com.chokri.controller;

import com.chokri.model.Quiz;
import com.chokri.model.Question;
import com.chokri.service.QuizService;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur gérant les quiz.
 * Utilise l'injection de dépendances au lieu du pattern singleton.
 */
public class QuizController {
    private final QuizService quizService;

    // Injection de dépendances via constructeur
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    public Quiz createQuiz(String title, double coefficient) {
        return quizService.createQuiz(title, coefficient);
    }

    public Optional<Quiz> getQuizById(String id) {
        return quizService.getQuizById(id);
    }

    public boolean quizExists(String id) {
        return quizService.quizExists(id);
    }

    public void addQuestionToQuiz(Quiz quiz, Question question) {
        quizService.addQuestionToQuiz(quiz, question);
    }

    public void addQuestionToQuizById(String quizId, String questionId) {
        quizService.addQuestionToQuizById(quizId, questionId);
    }

    public void removeQuestionFromQuiz(Quiz quiz, Question question) {
        quizService.removeQuestionFromQuiz(quiz, question);
    }

    public void removeQuestionFromQuizById(String quizId, String questionId) {
        quizService.removeQuestionFromQuizById(quizId, questionId);
    }

    public void publishQuiz(Quiz quiz) {
        quizService.publishQuiz(quiz);
    }

    public void publishQuizById(String id) {
        quizService.publishQuizById(id);
    }

    public void deleteQuiz(Quiz quiz) {
        quizService.deleteQuiz(quiz);
    }

    public void deleteQuizById(String id) {
        quizService.deleteQuizById(id);
    }

    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    public List<Quiz> getPublishedQuizzes() {
        return quizService.getPublishedQuizzes();
    }
}

package com.chokri.controller;

import com.chokri.model.Quiz;
import com.chokri.model.Question;
import com.chokri.service.QuizService;
import java.util.List;

public class QuizController {
    private static QuizController instance;
    private final QuizService quizService;

    private QuizController() {
        this.quizService = QuizService.getInstance();
    }

    public static QuizController getInstance() {
        if (instance == null) {
            instance = new QuizController();
        }
        return instance;
    }

    public Quiz createQuiz(String title, double coefficient) {
        return quizService.createQuiz(title, coefficient);
    }

    public void addQuestionToQuiz(Quiz quiz, Question question) {
        quizService.addQuestionToQuiz(quiz, question);
    }

    public void publishQuiz(Quiz quiz) {
        quizService.publishQuiz(quiz);
    }

    public void deleteQuiz(Quiz quiz) {
        quizService.deleteQuiz(quiz);
    }

    public List<Quiz> getAllQuizzes() {
        return quizService.getAllQuizzes();
    }

    public List<Quiz> getPublishedQuizzes() {
        return quizService.getPublishedQuizzes();
    }
}

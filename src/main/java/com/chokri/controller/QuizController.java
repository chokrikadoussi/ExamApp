package com.chokri.controller;

import com.chokri.model.Quiz;
import com.chokri.model.Question;
import java.util.ArrayList;
import java.util.List;

public class QuizController {
    private static QuizController instance;
    private final List<Quiz> quizzes;

    private QuizController() {
        this.quizzes = new ArrayList<>();
    }

    public static QuizController getInstance() {
        if (instance == null) {
            instance = new QuizController();
        }
        return instance;
    }

    public Quiz createQuiz(String title, double coefficient) {
        Quiz quiz = new Quiz(title, coefficient);
        quizzes.add(quiz);
        return quiz;
    }

    public void addQuestionToQuiz(Quiz quiz, Question question) {
        quiz.addQuestion(question);
    }

    public void removeQuestionFromQuiz(Quiz quiz, Question question) {
        quiz.removeQuestion(question);
    }

    public void setQuizTimeLimit(Quiz quiz, int minutes) {
        quiz.setTimeLimit(minutes);
    }

    public void publishQuiz(Quiz quiz) {
        quiz.setPublished(true);
    }

    public List<Quiz> getAllQuizzes() {
        return new ArrayList<>(quizzes);
    }

    public List<Quiz> getPublishedQuizzes() {
        return quizzes.stream()
                .filter(Quiz::isPublished)
                .toList();
    }

    public void deleteQuiz(Quiz quiz) {
        quizzes.remove(quiz);
    }
}

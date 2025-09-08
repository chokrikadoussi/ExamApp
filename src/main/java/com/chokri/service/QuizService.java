package com.chokri.service;

import com.chokri.model.Quiz;
import com.chokri.model.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuizService {
    private static QuizService instance;
    private final List<Quiz> quizzes;

    private QuizService() {
        this.quizzes = new ArrayList<>();
    }

    public static QuizService getInstance() {
        if (instance == null) {
            instance = new QuizService();
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

    public void publishQuiz(Quiz quiz) {
        quiz.setPublished(true);
    }

    public void deleteQuiz(Quiz quiz) {
        if (quiz != null) {
            quizzes.remove(quiz);
        }
    }

    public List<Quiz> getAllQuizzes() {
        return new ArrayList<>(quizzes);
    }

    public List<Quiz> getPublishedQuizzes() {
        return quizzes.stream()
                .filter(Quiz::isPublished)
                .toList();
    }

    public double calculateQuizScore(Quiz quiz, Map<Question, String> userAnswers) {
        double score = 0;
        for (Question question : quiz.getQuestions()) {
            String userAnswer = userAnswers.get(question);
            if (userAnswer != null && question.checkAnswer(userAnswer)) {
                score++;
            }
        }
        return score * quiz.getCoefficient();
    }
}

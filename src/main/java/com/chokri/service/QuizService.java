package com.chokri.service;

import com.chokri.model.Quiz;
import com.chokri.model.Question;
import com.chokri.repository.IRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service gérant les quiz dans l'application.
 * Utilise l'injection de dépendances au lieu du pattern singleton.
 */
public class QuizService {
    private final IRepository<Quiz> quizRepository;
    private final QuestionService questionService;

    // Injection de dépendances via constructeur
    public QuizService(IRepository<Quiz> quizRepository, QuestionService questionService) {
        this.quizRepository = quizRepository;
        this.questionService = questionService;
    }

    public Quiz createQuiz(String title, double coefficient) {
        Quiz quiz = new Quiz(title, coefficient);
        return quizRepository.save(quiz);
    }

    public Optional<Quiz> getQuizById(String id) {
        return quizRepository.findById(id);
    }

    public boolean quizExists(String id) {
        return quizRepository.existsById(id);
    }

    public void addQuestionToQuiz(Quiz quiz, Question question) {
        if (quiz != null && question != null) {
            quiz.addQuestion(question);
            quizRepository.save(quiz);
        }
    }

    public void addQuestionToQuizById(String quizId, String questionId) {
        Optional<Quiz> quiz = getQuizById(quizId);
        Optional<Question> question = questionService.getQuestionById(questionId);

        if (quiz.isPresent() && question.isPresent()) {
            quiz.get().addQuestion(question.get());
            quizRepository.save(quiz.get());
        }
    }

    public void removeQuestionFromQuiz(Quiz quiz, Question question) {
        if (quiz != null && question != null) {
            quiz.removeQuestion(question);
            quizRepository.save(quiz);
        }
    }

    public void removeQuestionFromQuizById(String quizId, String questionId) {
        Optional<Quiz> quiz = getQuizById(quizId);
        Optional<Question> question = questionService.getQuestionById(questionId);

        if (quiz.isPresent() && question.isPresent()) {
            quiz.get().removeQuestion(question.get());
            quizRepository.save(quiz.get());
        }
    }

    public void publishQuiz(Quiz quiz) {
        if (quiz != null) {
            quiz.setPublished(true);
            quizRepository.save(quiz);
        }
    }

    public void publishQuizById(String id) {
        Optional<Quiz> quiz = getQuizById(id);
        if (quiz.isPresent()) {
            quiz.get().setPublished(true);
            quizRepository.save(quiz.get());
        }
    }

    public void deleteQuiz(Quiz quiz) {
        if (quiz != null) {
            quizRepository.delete(quiz);
        }
    }

    public void deleteQuizById(String id) {
        quizRepository.deleteById(id);
    }

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public List<Quiz> getPublishedQuizzes() {
        return quizRepository.findAll().stream()
                .filter(Quiz::isPublished)
                .toList();
    }

    public double calculateQuizScore(Quiz quiz, Map<Question, String> userAnswers) {
        double score = 0;
        for (Question question : quiz.getQuestions()) {
            String userAnswer = userAnswers.get(question);
            if (userAnswer != null && question.checkAnswer(userAnswer)) {
                score += question.getPoints();
            }
        }
        return score * quiz.getCoefficient();
    }
}

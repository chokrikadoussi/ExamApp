package com.chokri.controller;

import com.chokri.di.DIContainer;
import com.chokri.model.Question;
import com.chokri.model.Quiz;
import com.chokri.model.Submission;
import com.chokri.service.QuestionService;
import com.chokri.service.QuizService;
import com.chokri.service.SubmissionService;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Orchestrateur principal de l'application.
 * Coordonne les interactions entre les différents services pour les vues.
 * Sépare la logique métier de l'interface utilisateur.
 */
public class AppOrchestrator {
    private final QuestionController questionController;
    private final QuizController quizController;
    private final QuestionService questionService;
    private final QuizService quizService;
    private final SubmissionService submissionService;

    public AppOrchestrator() {
        DIContainer container = DIContainer.getInstance();
        this.questionController = container.resolve(QuestionController.class);
        this.quizController = container.resolve(QuizController.class);
        this.questionService = container.resolve(QuestionService.class);
        this.quizService = container.resolve(QuizService.class);
        this.submissionService = container.resolve(SubmissionService.class);
    }

    // === Orchestration des Questions ===

    public List<Question> getAllQuestions() {
        return questionController.getAllQuestions();
    }

    public Question createTextQuestion(String title, String answers, int points) {
        return questionController.createTextQuestion(title, answers, points);
    }

    public Question createNumericQuestion(String title, String answer, String errorMargin, int points) {
        return questionController.createNumericQuestion(title, answer, errorMargin, points);
    }

    public Question createQCMQuestion(String title, String[] options, int correctIndex, int points) {
        return questionController.createQCMQuestion(title, options, correctIndex, points);
    }

    public void deleteQuestion(Question question) {
        questionController.deleteQuestion(question);
    }

    public int validateAnswers(Map<Question, String> userAnswers) {
        int correctAnswers = 0;
        for (Map.Entry<Question, String> entry : userAnswers.entrySet()) {
            Question question = entry.getKey();
            String userAnswer = entry.getValue();
            if (questionController.validateAnswer(question, userAnswer)) {
                correctAnswers += question.getPoints();
            }
        }
        return correctAnswers;
    }

    // === Orchestration des Quiz ===

    public List<Quiz> getAllQuizzes() {
        return quizController.getAllQuizzes();
    }

    public List<Quiz> getPublishedQuizzes() {
        return quizController.getPublishedQuizzes();
    }

    public Quiz createQuiz(String title, double coefficient) {
        return quizController.createQuiz(title, coefficient);
    }

    public void addQuestionToQuiz(Quiz quiz, Question question) {
        quizController.addQuestionToQuiz(quiz, question);
    }

    public void publishQuiz(Quiz quiz) {
        quizController.publishQuiz(quiz);
    }

    public void deleteQuiz(Quiz quiz) {
        quizController.deleteQuiz(quiz);
    }

    // === Orchestration des Soumissions ===

    public Submission submitQuiz(Quiz quiz, Map<Question, String> answers, int timeSpent) {
        return submissionService.submitQuiz(quiz, answers, timeSpent);
    }

    public boolean isQuizCompleted(Quiz quiz) {
        return submissionService.isQuizCompleted(quiz);
    }

    public Optional<Submission> getLatestSubmission(Quiz quiz) {
        return submissionService.getLatestSubmission(quiz);
    }

    public String generateDetailedResults(Submission submission) {
        return submissionService.generateDetailedResults(submission);
    }

    // === Logique métier complexe (orchestration de plusieurs services) ===

    /**
     * Valide qu'un quiz peut être publié (contient des questions, etc.)
     */
    public boolean canPublishQuiz(Quiz quiz) {
        if (quiz == null || quiz.getQuestions().isEmpty()) {
            return false;
        }

        // Vérifier que toutes les questions existent encore
        for (Question question : quiz.getQuestions()) {
            if (!questionService.questionExists(question.getId())) {
                return false;
            }
        }

        return quiz.getTimeLimit() > 0;
    }

    /**
     * Calcule les statistiques d'un quiz
     */
    public QuizStatistics calculateQuizStatistics(Quiz quiz) {
        List<Submission> submissions = submissionService.getSubmissionsForQuiz(quiz);

        if (submissions.isEmpty()) {
            return new QuizStatistics(0, 0, 0, 0);
        }

        double totalScore = submissions.stream().mapToDouble(Submission::getScore).sum();
        double averageScore = totalScore / submissions.size();
        double maxScore = submissions.stream().mapToDouble(Submission::getScore).max().orElse(0);
        double minScore = submissions.stream().mapToDouble(Submission::getScore).min().orElse(0);

        return new QuizStatistics(submissions.size(), averageScore, maxScore, minScore);
    }

    /**
     * Classe interne pour encapsuler les statistiques d'un quiz
     */
    public static class QuizStatistics {
        private final int totalSubmissions;
        private final double averageScore;
        private final double maxScore;
        private final double minScore;

        public QuizStatistics(int totalSubmissions, double averageScore, double maxScore, double minScore) {
            this.totalSubmissions = totalSubmissions;
            this.averageScore = averageScore;
            this.maxScore = maxScore;
            this.minScore = minScore;
        }

        // Getters
        public int getTotalSubmissions() { return totalSubmissions; }
        public double getAverageScore() { return averageScore; }
        public double getMaxScore() { return maxScore; }
        public double getMinScore() { return minScore; }
    }
}

package com.chokri.service;

import com.chokri.model.Question;
import com.chokri.model.Quiz;
import com.chokri.model.Submission;
import java.util.*;

public class SubmissionService {
    private static SubmissionService instance;
    private final List<Submission> submissions;
    private final GradingService gradingService;
    private final Set<String> completedQuizzes; // Stocke les IDs des quiz complétés (format: quizTitle)

    private SubmissionService() {
        this.submissions = new ArrayList<>();
        this.gradingService = GradingService.getInstance();
        this.completedQuizzes = new HashSet<>();
    }

    public static SubmissionService getInstance() {
        if (instance == null) {
            instance = new SubmissionService();
        }
        return instance;
    }

    public Submission submitQuiz(Quiz quiz, Map<Question, String> answers, int timeSpentMinutes) {
        Submission submission = new Submission(quiz);
        submission.setAnswers(answers);
        submission.setTimeSpentMinutes(timeSpentMinutes);

        // Calculer le score
        double score = gradingService.calculateQuizScore(quiz, answers);
        submission.setScore(score);

        // Si le temps est dépassé, appliquer une pénalité
        if (submission.isTimeExceeded()) {
            score *= 0.8; // 20% de pénalité
            submission.setScore(score);
        }

        // Sauvegarder la soumission
        submissions.add(submission);

        // Marquer le quiz comme complété
        completedQuizzes.add(quiz.getTitle());

        return submission;
    }

    public boolean isQuizCompleted(Quiz quiz) {
        return completedQuizzes.contains(quiz.getTitle());
    }

    public Optional<Submission> getLatestSubmission(Quiz quiz) {
        return submissions.stream()
                .filter(s -> s.getQuiz().equals(quiz))
                .max(Comparator.comparing(Submission::getSubmissionTime));
    }

    public List<Submission> getSubmissionsForQuiz(Quiz quiz) {
        return submissions.stream()
                .filter(s -> s.getQuiz().equals(quiz))
                .toList();
    }

    public String generateDetailedResults(Submission submission) {
        StringBuilder result = new StringBuilder();
        Quiz quiz = submission.getQuiz();
        Map<Question, String> answers = submission.getAnswers();

        result.append(String.format("Quiz: %s\n", quiz.getTitle()));
        result.append(String.format("Score final: %.2f / %.2f\n",
            submission.getScore(), quiz.getMaxScore()));
        result.append(String.format("Temps utilisé: %d / %d minutes\n",
            submission.getTimeSpentMinutes(), quiz.getTimeLimit()));

        if (submission.isTimeExceeded()) {
            result.append("⚠️ Pénalité de 20% appliquée pour dépassement de temps\n");
        }

        result.append("\nDétail des réponses:\n");
        for (Question question : quiz.getQuestions()) {
            String userAnswer = answers.getOrDefault(question, "Non répondu");
            result.append(String.format("\nQuestion: %s\n", question.getTitle()));
            result.append(String.format("Votre réponse: %s\n", userAnswer));
        }

        return result.toString();
    }
}

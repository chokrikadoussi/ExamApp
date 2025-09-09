package com.chokri.service;

import com.chokri.model.Question;
import com.chokri.model.Quiz;
import com.chokri.model.Submission;
import com.chokri.repository.SubmissionJacksonRepository;
import java.util.*;

/**
 * Service gérant les soumissions dans l'application.
 * Utilise l'injection de dépendances au lieu du pattern singleton.
 */
public class SubmissionService {
    private final SubmissionJacksonRepository submissionRepository;
    private final GradingService gradingService;
    private final Set<String> completedQuizzes; // Cache pour les quiz complétés

    // Injection de dépendances via constructeur
    public SubmissionService(SubmissionJacksonRepository submissionRepository, GradingService gradingService) {
        this.submissionRepository = submissionRepository;
        this.gradingService = gradingService;
        this.completedQuizzes = new HashSet<>();

        // Charger les quiz complétés depuis les soumissions existantes
        loadCompletedQuizzesFromRepository();
    }

    private void loadCompletedQuizzesFromRepository() {
        List<Submission> submissions = submissionRepository.findAll();
        for (Submission submission : submissions) {
            if (submission.getQuiz().getId() != null) {
                completedQuizzes.add(submission.getQuiz().getId());
            }
        }
    }

    public Submission submitQuiz(Quiz quiz, Map<Question, String> answers, int timeSpentMinutes) {
        Submission submission = new Submission(quiz);
        submission.setAnswers(answers);
        submission.setTimeSpentMinutes(timeSpentMinutes);

        // Calculer le score brut (sans coefficient)
        double score = gradingService.calculateRawScore(answers);
        // Si le temps est dépassé, appliquer une pénalité
        if (submission.isTimeExceeded()) {
            score *= 0.8; // 20% de pénalité
        }
        submission.setScore(score);

        // Sauvegarder la soumission
        submissionRepository.save(submission);

        // Marquer le quiz comme complété en utilisant l'ID
        if (quiz.getId() != null) {
            completedQuizzes.add(quiz.getId());
        }

        return submission;
    }

    public boolean isQuizCompleted(Quiz quiz) {
        return quiz.getId() != null && completedQuizzes.contains(quiz.getId());
    }

    public boolean isQuizCompletedById(String quizId) {
        return quizId != null && completedQuizzes.contains(quizId);
    }

    public Optional<Submission> getLatestSubmission(Quiz quiz) {
        if (quiz.getId() == null) {
            return Optional.empty();
        }

        return submissionRepository.findByQuizId(quiz.getId()).stream()
                .max(Comparator.comparing(Submission::getSubmissionTime));
    }

    public Optional<Submission> getLatestSubmissionByQuizId(String quizId) {
        if (quizId == null) {
            return Optional.empty();
        }

        return submissionRepository.findByQuizId(quizId).stream()
                .max(Comparator.comparing(Submission::getSubmissionTime));
    }

    public List<Submission> getSubmissionsForQuiz(Quiz quiz) {
        if (quiz.getId() == null) {
            return new ArrayList<>();
        }

        return submissionRepository.findByQuizId(quiz.getId());
    }

    public List<Submission> getSubmissionsForQuizById(String quizId) {
        if (quizId == null) {
            return new ArrayList<>();
        }

        return submissionRepository.findByQuizId(quizId);
    }

    public List<Submission> getAllSubmissions() {
        return submissionRepository.findAll();
    }

    public void deleteSubmission(Submission submission) {
        if (submission != null) {
            submissionRepository.delete(submission);
        }
    }

    public void deleteSubmissionsByQuizId(String quizId) {
        if (quizId != null) {
            List<Submission> submissions = submissionRepository.findByQuizId(quizId);
            for (Submission submission : submissions) {
                submissionRepository.delete(submission);
            }
            completedQuizzes.remove(quizId);
        }
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
            result.append("⚠ Pénalité de 20% appliquée pour dépassement de temps\n");
        }

        result.append("\nDétail des réponses:\n");
        for (Question question : quiz.getQuestions()) {
            String userAnswer = answers.getOrDefault(question, "Non répondu");
            boolean isCorrect = question.checkAnswer(userAnswer);
            result.append(String.format("\nQuestion: %s [%d pts]\n", question.getTitle(), question.getPoints()));
            result.append(String.format("Votre réponse: %s (%s)\n", userAnswer, isCorrect ? "Correct" : "Incorrect"));
        }

        return result.toString();
    }
}

package com.chokri.service;

import com.chokri.model.Question;
import com.chokri.model.Quiz;
import java.util.Map;

/**
 * Service gérant le calcul des scores et l'évaluation des réponses.
 * Plus de singleton - peut être injecté comme dépendance.
 */
public class GradingService {

    public int calculateRawScore(Map<Question, String> userAnswers) {
        return userAnswers.entrySet().stream()
                .mapToInt(entry -> entry.getKey().checkAnswer(entry.getValue()) ? entry.getKey().getPoints() : 0)
                .sum();
    }

    public double calculateQuizScore(Quiz quiz, Map<Question, String> userAnswers) {
        double rawScore = calculateRawScore(userAnswers);
        return rawScore * quiz.getCoefficient();
    }

    public String formatQuizResult(Quiz quiz, Map<Question, String> userAnswers) {
        double rawScore = calculateRawScore(userAnswers);
        int maxPossiblePoints = quiz.getQuestions().stream()
                .mapToInt(Question::getPoints)
                .sum();
        int answeredQuestions = (int) userAnswers.values().stream()
                .filter(answer -> answer != null && !answer.trim().isEmpty())
                .count();

        StringBuilder result = new StringBuilder();
        result.append(String.format("Score : %d/%d points\n", (int)rawScore, maxPossiblePoints));
        result.append(String.format("Questions répondues : %d/%d\n\n",
                answeredQuestions, quiz.getQuestions().size()));

        result.append("Détail par question :\n");
        for (Question question : quiz.getQuestions()) {
            String userAnswer = userAnswers.getOrDefault(question, "Non répondu");
            boolean isCorrect = question.checkAnswer(userAnswer);
            result.append(String.format("\nQuestion : %s (%d pts)\n",
                    question.getTitle(), question.getPoints()));
            result.append(String.format("Votre réponse : %s (%s)\n",
                    userAnswer, isCorrect ? "Correct" : "Incorrect"));
        }

        return result.toString();
    }
}

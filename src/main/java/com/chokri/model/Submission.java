package com.chokri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Submission {
    private Quiz quiz;
    private Map<String, String> answersByQuestionId; // Clé = ID de question, Valeur = réponse
    private LocalDateTime submissionTime;
    private double score;
    private int timeSpentMinutes;

    // Constructeur par défaut pour Jackson
    public Submission() {
        this.answersByQuestionId = new HashMap<>();
        this.submissionTime = LocalDateTime.now();
    }

    public Submission(Quiz quiz) {
        this.quiz = quiz;
        this.answersByQuestionId = new HashMap<>();
        this.submissionTime = LocalDateTime.now();
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    // Nouvelle méthode pour Jackson - utilise les IDs des questions
    public Map<String, String> getAnswersByQuestionId() {
        return new HashMap<>(answersByQuestionId);
    }

    public void setAnswersByQuestionId(Map<String, String> answersByQuestionId) {
        this.answersByQuestionId = new HashMap<>(answersByQuestionId);
    }

    // Méthodes de compatibilité avec l'ancien code - ignorées par Jackson
    @JsonIgnore
    public void setAnswers(Map<Question, String> answers) {
        this.answersByQuestionId.clear();
        for (Map.Entry<Question, String> entry : answers.entrySet()) {
            if (entry.getKey().getId() != null) {
                this.answersByQuestionId.put(entry.getKey().getId(), entry.getValue());
            }
        }
    }

    @JsonIgnore
    public Map<Question, String> getAnswers() {
        Map<Question, String> result = new HashMap<>();
        if (quiz != null && quiz.getQuestions() != null) {
            for (Question question : quiz.getQuestions()) {
                String answer = answersByQuestionId.get(question.getId());
                if (answer != null) {
                    result.put(question, answer);
                }
            }
        }
        return result;
    }

    // Méthode utilitaire pour obtenir la réponse d'une question spécifique
    @JsonIgnore
    public String getAnswerForQuestion(Question question) {
        return question.getId() != null ? answersByQuestionId.get(question.getId()) : null;
    }

    // Méthode utilitaire pour définir la réponse d'une question spécifique
    @JsonIgnore
    public void setAnswerForQuestion(Question question, String answer) {
        if (question.getId() != null) {
            answersByQuestionId.put(question.getId(), answer);
        }
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getTimeSpentMinutes() {
        return timeSpentMinutes;
    }

    public void setTimeSpentMinutes(int timeSpentMinutes) {
        this.timeSpentMinutes = timeSpentMinutes;
    }

    @JsonIgnore
    public boolean isTimeExceeded() {
        return quiz != null && timeSpentMinutes > quiz.getTimeLimit();
    }
}

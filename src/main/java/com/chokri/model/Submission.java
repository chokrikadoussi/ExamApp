package com.chokri.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Submission {
    private Quiz quiz;
    private Map<Question, String> answers;
    private LocalDateTime submissionTime;
    private double score;
    private int timeSpentMinutes;

    public Submission(Quiz quiz) {
        this.quiz = quiz;
        this.answers = new HashMap<>();
        this.submissionTime = LocalDateTime.now();
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setAnswers(Map<Question, String> answers) {
        this.answers = new HashMap<>(answers);
    }

    public Map<Question, String> getAnswers() {
        return new HashMap<>(answers);
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
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

    public boolean isTimeExceeded() {
        return timeSpentMinutes > quiz.getTimeLimit();
    }
}

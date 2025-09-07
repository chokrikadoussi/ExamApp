package com.chokri.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private String title;
    private double coefficient;
    private List<Question> questions;
    private int timeLimit; // en minutes
    private boolean isPublished;

    public Quiz(String title, double coefficient) {
        this.title = title;
        this.coefficient = coefficient;
        this.questions = new ArrayList<>();
        this.isPublished = false;
    }

    // Getters et Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    // Méthodes de gestion des questions
    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void removeQuestion(Question question) {
        questions.remove(question);
    }

    public int getQuestionCount() {
        return questions.size();
    }

    // Méthode pour calculer le score total possible
    public double getMaxScore() {
        return questions.size() * coefficient;
    }
}

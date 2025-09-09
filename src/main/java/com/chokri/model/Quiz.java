package com.chokri.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quiz {
    private String id;
    private String title;
    private double coefficient;
    private List<Question> questions;
    private int timeLimit; // en minutes
    private boolean isPublished;

    // Constructeur vide pour désérialisation
    public Quiz() {
        this.id = UUID.randomUUID().toString();
        this.questions = new ArrayList<>();
        this.isPublished = false;
    }

    public Quiz(String title, double coefficient) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.coefficient = coefficient;
        this.questions = new ArrayList<>();
        this.isPublished = false;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public void setQuestions(List<Question> questions) {
        this.questions = questions != null ? questions : new ArrayList<>();
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
        if (question != null) {
            questions.add(question);
        }
    }

    public void removeQuestion(Question question) {
        if (question != null) {
            questions.remove(question);
        }
    }

    public int getQuestionCount() {
        return questions.size();
    }

    // Méthode pour calculer le score total possible
    public double getMaxScore() {
        return questions.stream()
            .mapToInt(Question::getPoints)
            .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quiz quiz = (Quiz) o;
        return Objects.equals(id, quiz.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Quiz{id='%s', title='%s', coefficient=%.1f, questions=%d, published=%b}",
                           id, title, coefficient, questions.size(), isPublished);
    }
}

package com.chokri.model;

public abstract class Question {

    private String title;
    private int points; // Points pour cette question

    // Constructeurs vide pour désérialisation
    public Question() {}

    public Question(String title, int points) {
        this.title = title;
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public abstract boolean checkAnswer(String userAnswer);

    @Override
    public abstract String toString();

}

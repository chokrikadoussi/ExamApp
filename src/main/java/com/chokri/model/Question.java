package com.chokri.model;

public abstract class Question {

    private String title;

    public Question(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public abstract boolean checkAnswer(String userAnswer);

}

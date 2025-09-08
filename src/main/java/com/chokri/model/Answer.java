package com.chokri.model;

public class Answer {

    private Question question;
    private String reponse;

    public Answer(Question question, String reponse) {
        this.question = question;
        this.reponse = reponse;
    }
}

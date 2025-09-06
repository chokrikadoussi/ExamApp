package com.chokri.model;

public class Question {

    private String title;
    private String answer;

    public Question(String title, String answer) {
        this.title = title;
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean checkAnswer(String userAnswer) {
        return this.answer.equalsIgnoreCase(userAnswer.trim());
    }

    @Override
    public String toString() {
        return "Question{" +
                "titre='" + title + '\'' +
                ", réponse='" + answer + '\'' +
                '}';
    }
}

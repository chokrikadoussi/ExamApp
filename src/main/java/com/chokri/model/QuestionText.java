package com.chokri.model;

public class QuestionText extends Question {

    private String answer;

    public QuestionText(String title, String answer) {
        super(title);
        this.answer = answer;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        if (userAnswer == null) {
            return false;
        }
        return this.answer.trim().equalsIgnoreCase(userAnswer.trim());
    }

    @Override
    public String toString() {
        return "Titre Question : " + this.getTitle() + '\'' +
                " | Bonne(s) Réponse(s) : '" + this.getAnswer();
    }
}


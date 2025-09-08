package com.chokri.model;

public class QuestionNum extends Question {

    private double answer;
    private double marginOfError;

    public QuestionNum() {}

    public QuestionNum(String title, double answer, double marginOfError, int points) {
        super(title, points);
        this.answer = answer;
        this.marginOfError = marginOfError;
    }

    public double getMarginOfError() {
        return this.marginOfError;
    }

    public void setMarginOfError(double marginOfError) {
        //TODO: ajouter une validation pour s'assurer que la marge d'erreur est positive
        this.marginOfError = marginOfError;
    }

    public double getAnswer() {
        return this.answer;
    }

    public void setAnswer(double answer) {
        this.answer = answer;
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        try {
            double userValue = Double.parseDouble(userAnswer.trim());
            return Math.abs(userValue - answer) <= marginOfError;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Question numérique: " + getTitle() +
               " (Points: " + getPoints() +
               ", Réponse: " + answer +
               ", Marge d'erreur: ±" + marginOfError + ")";
    }
}

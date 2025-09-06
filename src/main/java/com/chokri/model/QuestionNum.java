package com.chokri.model;

public class QuestionNum extends Question {

    private double answer;
    private double marginOfError;

    public QuestionNum(String title, double answer) {
        super(title);
        this.answer = answer;
        this.marginOfError = 0; // Marge d'erreur par défaut
    }

    public QuestionNum(String title, double answer, double marginOfError) {
        super(title);
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
        if (userAnswer == null) {
            return false;
        }
        try {
            double userProposition = Double.parseDouble(userAnswer);
            //TODO: revoir la logique de comparaison avec la marge d'erreur
            return (userProposition - this.marginOfError) <= this.answer;
        } catch (Exception e) {
            //TODO: log the exception
            System.out.println("Erreur de format de nombre : " + e.getMessage());
            return false;
        }
    }

    // TODO: revoir méthode toString
    @Override
    public String toString() {
        return "QuestionNum{" +
                "title='" + getTitle() + '\'' +
                ", answer=" + answer +
                ", marginOfError=" + marginOfError +
                '}';
    }
}

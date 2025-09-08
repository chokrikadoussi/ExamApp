package com.chokri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

public class QuestionText extends Question {

    private List<String> answers;

    public QuestionText() {
        this.answers = new ArrayList<>();
    }

    public QuestionText(String title, String answersCSV, int points) {
        super(title, points);
        this.answers = new ArrayList<>();
        setAnswersFromCSV(answersCSV);
    }

    public List<String> getAnswers() {
        return new ArrayList<>(answers);
    }

    public void setAnswers(List<String> answers) {
        this.answers = new ArrayList<>(answers);
    }

    public void setAnswersFromCSV(String answersCSV) {
        if (this.answers == null) {
            this.answers = new ArrayList<>();
        } else {
            answers.clear();
        }
        if (answersCSV != null && !answersCSV.trim().isEmpty()) {
            String[] answerArray = answersCSV.split(",");
            for (String answer : answerArray) {
                String trimmedAnswer = answer.trim();
                if (!trimmedAnswer.isEmpty()) {
                    answers.add(trimmedAnswer);
                }
            }
        }
    }

    @JsonIgnore
    public String getAnswersAsCSV() {
        if (answers == null || answers.isEmpty()) {
            return "";
        }
        return String.join(", ", answers);
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        if (userAnswer == null || userAnswer.trim().isEmpty()) {
            return false;
        }
        String trimmedUserAnswer = userAnswer.trim();
        return answers.stream()
                .anyMatch(answer -> answer.equalsIgnoreCase(trimmedUserAnswer));
    }

    @Override
    public String toString() {
        return "Titre Question : " + this.getTitle() + '\'' +
                " | Bonne(s) Réponse(s) : '" + getAnswersAsCSV() + "'";
    }
}

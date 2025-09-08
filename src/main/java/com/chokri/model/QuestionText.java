package com.chokri.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionText extends Question {

    private final List<String> answers;

    public QuestionText(String title, String answersCSV) {
        super(title);
        this.answers = new ArrayList<>();
        setAnswersFromCSV(answersCSV);
    }

    public List<String> getAnswers() {
        return new ArrayList<>(answers);
    }

    public void setAnswersFromCSV(String answersCSV) {
        answers.clear();
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

    public String getAnswersAsCSV() {
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

package com.chokri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionQCM extends Question {
    private List<String> options;
    private int correctOptionIndex;

    public QuestionQCM() {}

    public QuestionQCM(String title, String[] options, int correctOptionIndex, int points) {
        super(title, points);
        this.options = new ArrayList<>();
        if (options != null) {
            this.options.addAll(Arrays.asList(options));
        }
        if (correctOptionIndex >= 0 && correctOptionIndex < options.length) {
            this.correctOptionIndex = correctOptionIndex;
        } else {
            throw new IllegalArgumentException("L'index de la bonne réponse doit être compris entre 0 et " + (options.length - 1));
        }
    }

    public List<String> getOptions() {
        return new ArrayList<>(options); // Retourne une copie pour préserver l'encapsulation
    }

    public void setOptions(List<String> options) {
        this.options = new ArrayList<>(options);
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public void setCorrectOptionIndex(int correctOptionIndex) {
        if (correctOptionIndex >= 0 && correctOptionIndex < options.size()) {
            this.correctOptionIndex = correctOptionIndex;
        } else {
            throw new IllegalArgumentException("L'index de la bonne réponse doit être compris entre 0 et " + (options.size() - 1));
        }
    }

    @JsonIgnore
    public String getCorrectAnswer() {
        return options.get(correctOptionIndex);
    }

    @JsonIgnore
    public String getFormattedOptions() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < options.size(); i++) {
            sb.append(i).append(". ").append(options.get(i));
            if (i < options.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean checkAnswer(String userAnswer) {
        try {
            int selectedIndex = Integer.parseInt(userAnswer.trim());
            return selectedIndex == correctOptionIndex;
        } catch (NumberFormatException e) {
            return false; // Si ce n'est pas un nombre valide, la réponse est incorrecte
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Question QCM: ").append(getTitle())
          .append(" (Points: ").append(getPoints()).append(")\n");
        for (int i = 0; i < options.size(); i++) {
            sb.append(i).append(". ").append(options.get(i)).append("\n");
        }
        return sb.toString();
    }
}

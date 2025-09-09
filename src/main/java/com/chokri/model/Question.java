package com.chokri.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Objects;
import java.util.UUID;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = QuestionText.class, name = "text"),
    @JsonSubTypes.Type(value = QuestionQCM.class, name = "qcm"),
    @JsonSubTypes.Type(value = QuestionNum.class, name = "numeric")
})
public abstract class Question {

    private String id;
    private String title;
    private int points; // Points pour cette question

    // Constructeurs vide pour désérialisation
    public Question() {
        this.id = UUID.randomUUID().toString();
    }

    public Question(String title, int points) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public abstract boolean checkAnswer(String userAnswer);

    @Override
    public abstract String toString();

}

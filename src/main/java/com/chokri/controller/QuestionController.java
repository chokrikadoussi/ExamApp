package com.chokri.controller;

import com.chokri.model.Question;
import com.chokri.service.IQuestionService;
import com.chokri.service.QuestionService;
import java.util.List;

/**
 * Contrôleur gérant les interactions avec les questions.
 * Utilise l'interface IQuestionService pour respecter le principe d'inversion de dépendance.
 */
public class QuestionController {
    private static QuestionController instance;
    private final IQuestionService questionService;

    private QuestionController() {
        this.questionService = QuestionService.getInstance();
    }

    public static QuestionController getInstance() {
        if (instance == null) {
            instance = new QuestionController();
        }
        return instance;
    }

    /**
     * Crée une question à réponse textuelle.
     *
     * @param title Le titre de la question
     * @param answersCSV Les réponses acceptables séparées par des virgules
     * @param points Le nombre de points attribués à la question
     * @return La question créée
     */
    public Question createTextQuestion(String title, String answersCSV, int points) {
        return questionService.createTextQuestion(title, answersCSV, points);
    }

    /**
     * Crée une question à réponse numérique.
     *
     * @param title Le titre de la question
     * @param answer La réponse numérique
     * @param errorMargin La marge d'erreur acceptable
     * @param points Le nombre de points attribués à la question
     * @return La question créée
     * @throws IllegalArgumentException si les valeurs numériques sont invalides
     */
    public Question createNumericQuestion(String title, String answer, String errorMargin, int points) {
        try {
            double answerValue = Double.parseDouble(answer);
            double marginValue = Double.parseDouble(errorMargin);
            return questionService.createNumericQuestion(title, answerValue, marginValue, points);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Les valeurs numériques sont invalides", e);
        }
    }

    /**
     * Crée une question à choix multiples.
     *
     * @param title Le titre de la question
     * @param options Les options de réponse
     * @param correctOptionIndex L'index de la bonne réponse
     * @param points Le nombre de points attribués à la question
     * @return La question créée
     */
    public Question createQCMQuestion(String title, String[] options, int correctOptionIndex, int points) {
        return questionService.createQCMQuestion(title, options, correctOptionIndex, points);
    }

    /**
     * Récupère toutes les questions.
     *
     * @return La liste de toutes les questions
     */
    public List<Question> getQuestions() {
        return questionService.getAllQuestions();
    }

    /**
     * Supprime une question.
     *
     * @param question La question à supprimer
     */
    public void deleteQuestion(Question question) {
        questionService.deleteQuestion(question);
    }
}

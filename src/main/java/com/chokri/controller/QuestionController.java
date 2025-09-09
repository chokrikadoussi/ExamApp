package com.chokri.controller;

import com.chokri.model.Question;
import com.chokri.service.QuestionService;
import java.util.List;
import java.util.Optional;

/**
 * Contrôleur gérant les questions.
 * Utilise l'injection de dépendances au lieu du pattern singleton.
 */
public class QuestionController {
    private final QuestionService questionService;

    // Injection de dépendances via constructeur
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
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
            double numericAnswer = Double.parseDouble(answer);
            double numericErrorMargin = Double.parseDouble(errorMargin);
            return questionService.createNumericQuestion(title, numericAnswer, numericErrorMargin, points);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Format numérique invalide", e);
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
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    /**
     * Récupère une question par son identifiant.
     *
     * @param id L'identifiant de la question
     * @return La question correspondante, s'elle existe
     */
    public Optional<Question> getQuestionById(String id) {
        return questionService.getQuestionById(id);
    }

    /**
     * Vérifie si une question existe.
     *
     * @param id L'identifiant de la question
     * @return true si la question existe, false sinon
     */
    public boolean questionExists(String id) {
        return questionService.questionExists(id);
    }

    /**
     * Met à jour une question.
     *
     * @param question La question à mettre à jour
     * @return La question mise à jour
     */
    public Question updateQuestion(Question question) {
        return questionService.updateQuestion(question);
    }

    /**
     * Supprime une question.
     *
     * @param question La question à supprimer
     */
    public void deleteQuestion(Question question) {
        questionService.deleteQuestion(question);
    }

    /**
     * Supprime une question par son identifiant.
     *
     * @param id L'identifiant de la question à supprimer
     */
    public void deleteQuestionById(String id) {
        questionService.deleteQuestionById(id);
    }

    /**
     * Valide la réponse d'un utilisateur à une question.
     *
     * @param question La question posée
     * @param userAnswer La réponse de l'utilisateur
     * @return true si la réponse est correcte, false sinon
     */
    public boolean validateAnswer(Question question, String userAnswer) {
        return questionService.validateAnswer(question, userAnswer);
    }
}

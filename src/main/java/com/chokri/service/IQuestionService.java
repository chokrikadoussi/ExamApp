package com.chokri.service;

import com.chokri.model.Question;
import java.util.List;
import java.util.Optional;

/**
 * Interface définissant les opérations de service pour la gestion des questions.
 */
public interface IQuestionService {
    /**
     * Crée une question à réponse textuelle.
     * @param title Le titre de la question
     * @param answer La réponse correcte
     * @param points Le nombre de points pour cette question
     * @return La question créée
     */
    Question createTextQuestion(String title, String answer, int points);

    /**
     * Crée une question à réponse numérique.
     * @param title Le titre de la question
     * @param answer La réponse correcte
     * @param errorMargin La marge d'erreur acceptable
     * @param points Le nombre de points pour cette question
     * @return La question créée
     */
    Question createNumericQuestion(String title, double answer, double errorMargin, int points);

    /**
     * Crée une question à choix multiples.
     * @param title Le titre de la question
     * @param options Les options de réponse
     * @param correctOptionIndex L'index de la bonne réponse
     * @param points Le nombre de points pour cette question
     * @return La question créée
     */
    Question createQCMQuestion(String title, String[] options, int correctOptionIndex, int points);

    /**
     * Récupère toutes les questions.
     * @return La liste de toutes les questions
     */
    List<Question> getAllQuestions();

    /**
     * Trouve une question par son ID.
     * @param id L'identifiant de la question
     * @return Un Optional contenant la question si trouvée, Optional.empty() sinon
     */
    Optional<Question> getQuestionById(String id);

    /**
     * Vérifie si une question existe avec l'ID donné.
     * @param id L'identifiant à vérifier
     * @return true si la question existe, false sinon
     */
    boolean questionExists(String id);

    /**
     * Met à jour une question existante.
     * @param question La question à mettre à jour
     * @return La question mise à jour
     */
    Question updateQuestion(Question question);

    /**
     * Supprime une question.
     * @param question La question à supprimer
     */
    void deleteQuestion(Question question);

    /**
     * Supprime une question par son ID.
     * @param id L'identifiant de la question à supprimer
     */
    void deleteQuestionById(String id);
}

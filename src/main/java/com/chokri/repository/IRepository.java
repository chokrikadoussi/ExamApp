package com.chokri.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour les opérations de persistance.
 * @param <T> Le type d'entité à persister
 */
public interface IRepository<T> {
    /**
     * Sauvegarde une entité.
     * @param entity L'entité à sauvegarder
     * @return L'entité sauvegardée
     */
    T save(T entity);

    /**
     * Sauvegarde une collection d'entités.
     * @param entities Les entités à sauvegarder
     */
    void saveAll(List<T> entities);

    /**
     * Récupère toutes les entités.
     * @return La liste de toutes les entités
     */
    List<T> findAll();

    /**
     * Trouve une entité par son ID.
     * @param id L'identifiant de l'entité
     * @return Un Optional contenant l'entité si trouvée, Optional.empty() sinon
     */
    Optional<T> findById(String id);

    /**
     * Vérifie si une entité existe avec l'ID donné.
     * @param id L'identifiant à vérifier
     * @return true si l'entité existe, false sinon
     */
    boolean existsById(String id);

    /**
     * Supprime une entité.
     * @param entity L'entité à supprimer
     */
    void delete(T entity);

    /**
     * Supprime une entité par son ID.
     * @param id L'identifiant de l'entité à supprimer
     */
    void deleteById(String id);

    /**
     * Vide le repository.
     */
    void deleteAll();
}

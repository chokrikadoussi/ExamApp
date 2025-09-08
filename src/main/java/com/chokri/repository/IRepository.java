package com.chokri.repository;

import java.util.List;

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
     * Supprime une entité.
     * @param entity L'entité à supprimer
     */
    void delete(T entity);

    /**
     * Vide le repository.
     */
    void deleteAll();
}

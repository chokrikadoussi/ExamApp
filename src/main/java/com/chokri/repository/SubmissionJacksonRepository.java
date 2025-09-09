package com.chokri.repository;

import com.chokri.config.DataPathConfig;
import com.chokri.model.Submission;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation de IRepository pour les soumissions utilisant Jackson pour la persistance JSON.
 */
public class SubmissionJacksonRepository implements IRepository<Submission> {
    private final ObjectMapper objectMapper;
    private final Path submissionsFilePath;

    public SubmissionJacksonRepository() {
        // Configuration de l'ObjectMapper avec support des dates Java 8+
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.submissionsFilePath = DataPathConfig.getSubmissionsFilePath();

        DataPathConfig.ensureDataDirectoryExists();

        File file = submissionsFilePath.toFile();
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (created) {
                    objectMapper.writeValue(file, new ArrayList<Submission>());
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la création du fichier submissions.json : " + e.getMessage());
            }
        }
    }

    @Override
    public Submission save(Submission entity) {
        List<Submission> submissions = findAll();
        boolean updated = false;

        // Pour les soumissions, on peut vouloir garder un historique
        // donc on n'update pas, on ajoute toujours
        submissions.add(entity);

        saveAll(submissions);
        return entity;
    }

    @Override
    public void saveAll(List<Submission> entities) {
        try {
            objectMapper.writeValue(submissionsFilePath.toFile(), entities);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des soumissions : " + e.getMessage());
        }
    }

    @Override
    public List<Submission> findAll() {
        List<Submission> submissions = new ArrayList<>();
        File file = submissionsFilePath.toFile();

        if (file.exists() && file.length() > 0) {
            try {
                JavaType submissionListType = objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, Submission.class);

                submissions = objectMapper.readValue(file, submissionListType);

                if (submissions == null) {
                    submissions = new ArrayList<>();
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la lecture des soumissions : " + e.getMessage());
            }
        }

        return submissions;
    }

    @Override
    public void delete(Submission entity) {
        List<Submission> submissions = findAll();
        submissions.remove(entity);
        saveAll(submissions);
    }

    @Override
    public void deleteAll() {
        try {
            objectMapper.writeValue(submissionsFilePath.toFile(), new ArrayList<Submission>());
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression de toutes les soumissions : " + e.getMessage());
        }
    }

    @Override
    public Optional<Submission> findById(String id) {
        // Les soumissions n'ont pas d'ID pour le moment, donc on retourne empty
        return Optional.empty();
    }

    @Override
    public boolean existsById(String id) {
        return false;
    }

    @Override
    public void deleteById(String id) {
        // Non implémenté car les soumissions n'ont pas d'ID
    }

    /**
     * Recherche les soumissions par ID de quiz
     */
    public List<Submission> findByQuizId(String quizId) {
        return findAll().stream()
                .filter(s -> quizId.equals(s.getQuiz().getId()))
                .toList();
    }
}

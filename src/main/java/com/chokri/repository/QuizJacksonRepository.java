package com.chokri.repository;

import com.chokri.config.DataPathConfig;
import com.chokri.model.Quiz;
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
 * Implémentation de IRepository pour les quiz utilisant Jackson pour la persistance JSON.
 */
public class QuizJacksonRepository implements IRepository<Quiz> {
    private final ObjectMapper objectMapper;
    private final Path quizFilePath;

    public QuizJacksonRepository() {
        // Configuration de l'ObjectMapper avec support des dates Java 8+
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Utilisation de la configuration centralisée des chemins
        this.quizFilePath = DataPathConfig.getQuizFilePath();

        // Assurer que le répertoire de données existe
        DataPathConfig.ensureDataDirectoryExists();

        // Créer le fichier s'il n'existe pas
        File file = quizFilePath.toFile();
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (created) {
                    // Écrire un tableau vide si le fichier est nouveau
                    objectMapper.writeValue(file, new ArrayList<Quiz>());
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la création du fichier quiz.json : " + e.getMessage());
            }
        }
    }

    @Override
    public Quiz save(Quiz entity) {
        List<Quiz> quizzes = findAll();
        boolean updated = false;
        for (int i = 0; i < quizzes.size(); i++) {
            if (quizzes.get(i).getId().equals(entity.getId())) {
                quizzes.set(i, entity);
                updated = true;
                break;
            }
        }

        if (!updated) {
            quizzes.add(entity);
        }

        saveAll(quizzes);
        return entity;
    }

    @Override
    public void saveAll(List<Quiz> entities) {
        try {
            objectMapper.writeValue(quizFilePath.toFile(), entities);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des quiz : " + e.getMessage());
        }
    }

    @Override
    public List<Quiz> findAll() {
        List<Quiz> quizzes = new ArrayList<>();
        File file = quizFilePath.toFile();

        if (file.exists() && file.length() > 0) {
            try {
                JavaType quizListType = objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, Quiz.class);

                quizzes = objectMapper.readValue(file, quizListType);

                if (quizzes == null) {
                    quizzes = new ArrayList<>();
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la lecture des quiz : " + e.getMessage());
            }
        }

        return quizzes;
    }

    @Override
    public void delete(Quiz entity) {
        List<Quiz> quizzes = findAll();
        quizzes.removeIf(q -> q.getId().equals(entity.getId()));
        saveAll(quizzes);
    }

    @Override
    public void deleteAll() {
        try {
            objectMapper.writeValue(quizFilePath.toFile(), new ArrayList<Quiz>());
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression de tous les quiz : " + e.getMessage());
        }
    }

    @Override
    public Optional<Quiz> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }

        List<Quiz> quizzes = findAll();
        return quizzes.stream()
                .filter(q -> id.equals(q.getId()))
                .findFirst();
    }

    @Override
    public boolean existsById(String id) {
        return findById(id).isPresent();
    }

    @Override
    public void deleteById(String id) {
        if (id == null) {
            return;
        }

        List<Quiz> quizzes = findAll();
        quizzes.removeIf(q -> id.equals(q.getId()));
        saveAll(quizzes);
    }
}

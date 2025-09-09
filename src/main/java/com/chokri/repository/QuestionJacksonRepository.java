package com.chokri.repository;

import com.chokri.config.DataPathConfig;
import com.chokri.model.Question;
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
 * Implémentation de IRepository pour les questions utilisant Jackson pour la persistance JSON.
 * Cette classe offre des fonctionnalités de persistance optimisées grâce à Jackson.
 */
public class QuestionJacksonRepository implements IRepository<Question> {
    private final ObjectMapper objectMapper;
    private final Path questionsFilePath;

    public QuestionJacksonRepository() {
        // Configuration de l'ObjectMapper avec support des dates Java 8+
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Utilisation de la configuration centralisée des chemins
        this.questionsFilePath = DataPathConfig.getQuestionsFilePath();

        // Assurer que le répertoire de données existe
        DataPathConfig.ensureDataDirectoryExists();

        // Créer le fichier s'il n'existe pas
        File file = questionsFilePath.toFile();
        if (!file.exists()) {
            try {
                boolean created = file.createNewFile();
                if (created) {
                    // Écrire un tableau vide si le fichier est nouveau
                    objectMapper.writeValue(file, new ArrayList<Question>());
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la création du fichier questions.json : " + e.getMessage());
            }
        }
    }

    @Override
    public Question save(Question entity) {
        List<Question> questions = findAll();
        // Vérifie si la question existe déjà par ID (mise à jour)
        boolean updated = false;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getId().equals(entity.getId())) {
                questions.set(i, entity);
                updated = true;
                break;
            }
        }

        // Si la question n'existe pas, l'ajouter
        if (!updated) {
            questions.add(entity);
        }

        saveAll(questions);
        return entity;
    }

    @Override
    public void saveAll(List<Question> entities) {
        try {
            objectMapper.writeValue(questionsFilePath.toFile(), entities);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des questions : " + e.getMessage());
        }
    }

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();
        File file = questionsFilePath.toFile();

        if (file.exists() && file.length() > 0) {
            try {
                // Création d'un type pour la liste de questions
                JavaType questionListType = objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, Question.class);

                // Lecture du fichier JSON et conversion en liste de questions
                questions = objectMapper.readValue(file, questionListType);

                if (questions == null) {
                    questions = new ArrayList<>();
                }
            } catch (IOException e) {
                System.err.println("Erreur lors de la lecture des questions : " + e.getMessage());
            }
        }

        return questions;
    }

    @Override
    public void delete(Question entity) {
        List<Question> questions = findAll();
        questions.removeIf(q -> q.getId().equals(entity.getId()));
        saveAll(questions);
    }

    @Override
    public void deleteAll() {
        try {
            objectMapper.writeValue(questionsFilePath.toFile(), new ArrayList<Question>());
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression de toutes les questions : " + e.getMessage());
        }
    }

    @Override
    public Optional<Question> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }

        List<Question> questions = findAll();
        return questions.stream()
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

        List<Question> questions = findAll();
        questions.removeIf(q -> id.equals(q.getId()));
        saveAll(questions);
    }
}

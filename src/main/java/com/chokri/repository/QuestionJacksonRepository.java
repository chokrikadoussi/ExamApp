package com.chokri.repository;

import com.chokri.model.Question;
import com.chokri.model.QuestionNum;
import com.chokri.model.QuestionQCM;
import com.chokri.model.QuestionText;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation de IRepository pour les questions utilisant Jackson pour la persistance JSON.
 * Cette classe offre des fonctionnalités de persistance optimisées grâce à Jackson.
 */
public class QuestionJacksonRepository implements IRepository<Question> {
    private static final String QUESTIONS_FILE = "src/main/java/com/chokri/data/questions.json";
    private final ObjectMapper objectMapper;

    public QuestionJacksonRepository() {
        // Configuration de l'ObjectMapper pour gérer le polymorphisme
        this.objectMapper = new ObjectMapper();

        // Activer l'indentation pour un JSON plus lisible
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // Configuration du polymorphisme avec la méthode moderne (non dépréciée)
        this.objectMapper.activateDefaultTyping(
                this.objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        // Enregistrement des sous-types pour le polymorphisme
        this.objectMapper.registerSubtypes(
                new NamedType(QuestionText.class, "text"),
                new NamedType(QuestionQCM.class, "qcm"),
                new NamedType(QuestionNum.class, "numeric")
        );

        // Créer le fichier s'il n'existe pas
        File file = new File(QUESTIONS_FILE);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                // Écrire un tableau vide si le fichier est nouveau
                objectMapper.writeValue(file, new ArrayList<Question>());
            } catch (IOException e) {
                System.err.println("Erreur lors de la création du fichier questions.json : " + e.getMessage());
            }
        }
    }

    @Override
    public Question save(Question entity) {
        List<Question> questions = findAll();
        // Vérifie si la question existe déjà (mise à jour)
        boolean updated = false;
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getTitle().equals(entity.getTitle())) {
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
            objectMapper.writeValue(new File(QUESTIONS_FILE), entities);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des questions : " + e.getMessage());
        }
    }

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();
        File file = new File(QUESTIONS_FILE);

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
        questions.removeIf(q -> q.getTitle().equals(entity.getTitle()));
        saveAll(questions);
    }

    @Override
    public void deleteAll() {
        try {
            objectMapper.writeValue(new File(QUESTIONS_FILE), new ArrayList<Question>());
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression de toutes les questions : " + e.getMessage());
        }
    }
}

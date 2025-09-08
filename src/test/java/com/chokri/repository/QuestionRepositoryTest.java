package com.chokri.repository;

import com.chokri.model.Question;
import com.chokri.model.QuestionText;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de validation de l'implémentation du repository de questions avec Jackson.
 */
public class QuestionRepositoryTest {

    private IRepository<Question> repository;

    @BeforeEach
    public void setUp() {
        repository = new QuestionJacksonRepository();

        // Nettoyage du repository avant chaque test
        repository.deleteAll();
    }

    @AfterEach
    public void tearDown() {
        // Nettoyage du repository après chaque test
        repository.deleteAll();
    }

    @Test
    public void testSaveAndFind() {
        // Créer une question de test
        Question question = new QuestionText("Question de test", "réponse1, réponse2", 10);

        // Sauvegarder la question
        repository.save(question);

        // Récupérer toutes les questions
        List<Question> questions = repository.findAll();

        // Vérifier que la question a été correctement sauvegardée
        assertEquals(1, questions.size(), "Le repository devrait contenir une question");
        assertEquals("Question de test", questions.get(0).getTitle(), "Le titre de la question est incorrect");
        assertEquals(10, questions.get(0).getPoints(), "Le nombre de points est incorrect");
    }

    @Test
    public void testDeleteQuestion() {
        // Créer et sauvegarder deux questions
        Question question1 = new QuestionText("Question 1", "réponse1", 10);
        Question question2 = new QuestionText("Question 2", "réponse2", 15);

        repository.save(question1);
        repository.save(question2);

        // Vérifier que les deux questions ont été sauvegardées
        assertEquals(2, repository.findAll().size(), "Le repository devrait contenir deux questions");

        // Supprimer une question
        repository.delete(question1);

        // Vérifier qu'il ne reste qu'une question
        List<Question> questions = repository.findAll();
        assertEquals(1, questions.size(), "Le repository devrait contenir une seule question après suppression");
        assertEquals("Question 2", questions.get(0).getTitle(), "La question restante devrait être 'Question 2'");
    }

    @Test
    public void testUpdateQuestion() {
        // Créer et sauvegarder une question
        Question question = new QuestionText("Question à modifier", "réponse initiale", 10);
        repository.save(question);

        // Modifier la question (même titre pour être identifiée comme la même)
        question.setPoints(20);
        repository.save(question);

        // Vérifier que la question a été mise à jour
        List<Question> questions = repository.findAll();
        assertEquals(1, questions.size(), "Le repository devrait contenir une seule question");
        assertEquals("Question à modifier", questions.get(0).getTitle(), "Le titre de la question est incorrect");
        assertEquals(20, questions.get(0).getPoints(), "Le nombre de points modifié est incorrect");
    }

    @Test
    public void testDeleteAll() {
        // Créer et sauvegarder plusieurs questions
        repository.save(new QuestionText("Question 1", "réponse1", 10));
        repository.save(new QuestionText("Question 2", "réponse2", 15));
        repository.save(new QuestionText("Question 3", "réponse3", 20));

        // Vérifier que les questions ont été sauvegardées
        assertEquals(3, repository.findAll().size(), "Le repository devrait contenir trois questions");

        // Supprimer toutes les questions
        repository.deleteAll();

        // Vérifier que le repository est vide
        List<Question> questions = repository.findAll();
        assertEquals(0, questions.size(), "Le repository devrait être vide après deleteAll()");
    }
}

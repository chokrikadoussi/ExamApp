package com.chokri.service;

import com.chokri.model.Question;
import com.chokri.model.QuestionNum;
import com.chokri.model.QuestionQCM;
import com.chokri.model.QuestionText;
import com.chokri.repository.IRepository;
import com.chokri.repository.QuestionJacksonRepository;

import java.util.List;

/**
 * Service gérant les questions dans l'application.
 * Implémente l'interface IQuestionService pour respecter le principe d'inversion de dépendance.
 * Utilise un repository Jackson pour la persistance des données.
 */
public class QuestionService implements IQuestionService {
    private static QuestionService instance;
    private final IRepository<Question> questionRepository;

    private QuestionService() {
        this.questionRepository = new QuestionJacksonRepository();
    }

    public static QuestionService getInstance() {
        if (instance == null) {
            instance = new QuestionService();
        }
        return instance;
    }

    @Override
    public Question createTextQuestion(String title, String answersCSV, int points) {
        Question question = new QuestionText(title, answersCSV, points);
        return questionRepository.save(question);
    }

    @Override
    public Question createNumericQuestion(String title, double answer, double errorMargin, int points) {
        Question question = new QuestionNum(title, answer, errorMargin, points);
        return questionRepository.save(question);
    }

    @Override
    public Question createQCMQuestion(String title, String[] options, int correctOptionIndex, int points) {
        Question question = new QuestionQCM(title, options, correctOptionIndex, points);
        return questionRepository.save(question);
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @Override
    public void deleteQuestion(Question question) {
        if (question != null) {
            questionRepository.delete(question);
        }
    }

    /**
     * Valide la réponse fournie pour une question donnée.
     * @param question La question à vérifier
     * @param userAnswer La réponse de l'utilisateur
     * @return true si la réponse est correcte, false sinon
     */
    public boolean validateAnswer(Question question, String userAnswer) {
        return question.checkAnswer(userAnswer);
    }
}

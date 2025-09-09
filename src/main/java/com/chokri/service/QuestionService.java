package com.chokri.service;

import com.chokri.model.Question;
import com.chokri.model.QuestionNum;
import com.chokri.model.QuestionQCM;
import com.chokri.model.QuestionText;
import com.chokri.repository.IRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service gérant les questions dans l'application.
 * Utilise l'injection de dépendances au lieu du pattern singleton.
 */
public class QuestionService implements IQuestionService {
    private final IRepository<Question> questionRepository;

    // Injection de dépendances via constructeur
    public QuestionService(IRepository<Question> questionRepository) {
        this.questionRepository = questionRepository;
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
    public Optional<Question> getQuestionById(String id) {
        return questionRepository.findById(id);
    }

    @Override
    public boolean questionExists(String id) {
        return questionRepository.existsById(id);
    }

    @Override
    public Question updateQuestion(Question question) {
        if (question == null || question.getId() == null) {
            throw new IllegalArgumentException("La question et son ID ne peuvent pas être null");
        }
        return questionRepository.save(question);
    }

    @Override
    public void deleteQuestion(Question question) {
        if (question != null && question.getId() != null) {
            questionRepository.deleteById(question.getId());
        }
    }

    @Override
    public void deleteQuestionById(String id) {
        if (id != null) {
            questionRepository.deleteById(id);
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

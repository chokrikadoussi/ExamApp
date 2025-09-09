package com.chokri.config;

import com.chokri.di.DIContainer;
import com.chokri.repository.*;
import com.chokri.service.*;
import com.chokri.controller.*;

/**
 * Configuration des dépendances de l'application.
 * Point central pour enregistrer toutes les dépendances dans le container DI.
 */
public class AppConfig {

    public static void configure() {
        DIContainer container = DIContainer.getInstance();

        // Configuration des repositories
        container.registerSingletonFactory(IRepository.class, QuestionJacksonRepository::new);
        container.registerSingletonFactory(QuestionJacksonRepository.class, QuestionJacksonRepository::new);
        container.registerSingletonFactory(QuizJacksonRepository.class, QuizJacksonRepository::new);
        container.registerSingletonFactory(SubmissionJacksonRepository.class, SubmissionJacksonRepository::new);

        // Configuration des services avec injection des dépendances
        container.registerSingletonFactory(IQuestionService.class, () ->
            new QuestionService(container.resolve(QuestionJacksonRepository.class)));

        container.registerSingletonFactory(QuestionService.class, () ->
            new QuestionService(container.resolve(QuestionJacksonRepository.class)));

        container.registerSingletonFactory(QuizService.class, () ->
            new QuizService(
                container.resolve(QuizJacksonRepository.class),
                container.resolve(QuestionService.class)
            ));

        container.registerSingletonFactory(GradingService.class, GradingService::new);

        container.registerSingletonFactory(SubmissionService.class, () ->
            new SubmissionService(
                container.resolve(SubmissionJacksonRepository.class),
                container.resolve(GradingService.class)
            ));

        // Configuration des contrôleurs
        container.registerSingletonFactory(QuestionController.class, () ->
            new QuestionController(container.resolve(QuestionService.class)));

        container.registerSingletonFactory(QuizController.class, () ->
            new QuizController(container.resolve(QuizService.class)));

        container.registerSingletonFactory(AnswerController.class, () ->
            new AnswerController(container.resolve(GradingService.class)));
    }

    /**
     * Configuration pour les tests (permet de mocker les dépendances)
     */
    public static void configureForTesting() {
        DIContainer.getInstance().clear();
        // Les tests peuvent enregistrer leurs propres mocks
    }
}

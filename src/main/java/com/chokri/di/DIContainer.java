package com.chokri.di;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Container de dependency injection simple pour l'application ExamApp.
 * Permet de découpler les dépendances et améliorer la testabilité.
 */
public class DIContainer {
    private static DIContainer instance;
    private final Map<Class<?>, Object> singletons = new HashMap<>();
    private final Map<Class<?>, Supplier<?>> factories = new HashMap<>();

    private DIContainer() {}

    public static DIContainer getInstance() {
        if (instance == null) {
            instance = new DIContainer();
        }
        return instance;
    }

    /**
     * Enregistre un singleton dans le container
     */
    public <T> void registerSingleton(Class<T> type, T instance) {
        singletons.put(type, instance);
    }

    /**
     * Enregistre une factory pour créer des instances
     */
    public <T> void registerFactory(Class<T> type, Supplier<T> factory) {
        factories.put(type, factory);
    }

    /**
     * Enregistre une factory singleton (créée une seule fois)
     */
    public <T> void registerSingletonFactory(Class<T> type, Supplier<T> factory) {
        factories.put(type, () -> {
            if (!singletons.containsKey(type)) {
                singletons.put(type, factory.get());
            }
            return type.cast(singletons.get(type));
        });
    }

    /**
     * Résout une dépendance du container
     */
    @SuppressWarnings("unchecked")
    public <T> T resolve(Class<T> type) {
        // D'abord, chercher dans les singletons
        if (singletons.containsKey(type)) {
            return (T) singletons.get(type);
        }

        // Ensuite, utiliser une factory si disponible
        if (factories.containsKey(type)) {
            return (T) factories.get(type).get();
        }

        throw new IllegalArgumentException("Type non enregistré dans le container DI: " + type.getName());
    }

    /**
     * Vérifie si un type est enregistré
     */
    public boolean isRegistered(Class<?> type) {
        return singletons.containsKey(type) || factories.containsKey(type);
    }

    /**
     * Nettoie le container (utile pour les tests)
     */
    public void clear() {
        singletons.clear();
        factories.clear();
    }
}

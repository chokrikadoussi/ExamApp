package com.chokri.config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration des chemins de données de l'application.
 * Gère la localisation des fichiers de données selon l'environnement.
 */
public class DataPathConfig {

    // Répertoire de données par défaut (racine du projet)
    private static final String DEFAULT_DATA_DIR = "data";

    // Variables d'environnement pour override
    private static final String ENV_DATA_DIR = "EXAMAPP_DATA_DIR";

    /**
     * Obtient le répertoire de données configuré.
     * Priorité : variable d'environnement > répertoire par défaut
     */
    public static Path getDataDirectory() {
        String dataDir = System.getenv(ENV_DATA_DIR);
        if (dataDir != null && !dataDir.trim().isEmpty()) {
            return Paths.get(dataDir);
        }
        return Paths.get(DEFAULT_DATA_DIR);
    }

    /**
     * Obtient le chemin du fichier questions.json
     */
    public static Path getQuestionsFilePath() {
        return getDataDirectory().resolve("questions.json");
    }

    /**
     * Obtient le chemin du fichier quiz.json
     */
    public static Path getQuizFilePath() {
        return getDataDirectory().resolve("quiz.json");
    }

    /**
     * Obtient le chemin du fichier submissions.json
     */
    public static Path getSubmissionsFilePath() {
        return getDataDirectory().resolve("submissions.json");
    }

    /**
     * Assure que le répertoire de données existe
     */
    public static void ensureDataDirectoryExists() {
        Path dataDir = getDataDirectory();
        File dir = dataDir.toFile();
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new RuntimeException("Impossible de créer le répertoire de données : " + dataDir);
            }
        }
    }

    /**
     * Retourne le chemin absolu du répertoire de données
     */
    public static String getAbsoluteDataDirectory() {
        return getDataDirectory().toAbsolutePath().toString();
    }
}

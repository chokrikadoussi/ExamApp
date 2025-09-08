package com.chokri.view;

import com.chokri.utils.SessionManager;
import javax.swing.*;

public class MenuBar extends JMenuBar {

    public MenuBar(JFrame currentFrame) {
        JMenu menu = new JMenu("Navigation");
        add(menu);

        JMenuItem homeItem = new JMenuItem("Accueil");
        menu.add(homeItem);

        // Items spécifiques au rôle
        if (SessionManager.getInstance().isTeacher()) {
            // Menu du professeur
            JMenuItem questionItem = new JMenuItem("Gérer les Questions");
            JMenuItem quizItem = new JMenuItem("Gérer les Quiz");
            menu.add(questionItem);
            menu.add(quizItem);

            questionItem.addActionListener(e -> {
                if (!(currentFrame instanceof QuestionView)) {
                    currentFrame.dispose();
                    new QuestionView().setVisible(true);
                }
            });

            quizItem.addActionListener(e -> {
                if (!(currentFrame instanceof TeacherView)) {
                    currentFrame.dispose();
                    new TeacherView().setVisible(true);
                }
            });
        } else {
            // Menu de l'étudiant
            JMenuItem quizItem = new JMenuItem("Quiz disponibles");
            menu.add(quizItem);

            quizItem.addActionListener(e -> {
                if (!(currentFrame instanceof StudentView)) {
                    currentFrame.dispose();
                    new StudentView().setVisible(true);
                }
            });
        }

        homeItem.addActionListener(e -> {
            if (!(currentFrame instanceof HomeView)) {
                currentFrame.dispose();
                new HomeView().setVisible(true);
            }
        });
    }
}

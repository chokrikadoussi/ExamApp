package com.chokri.view;

import javax.swing.*;

public class MenuBar extends JMenuBar {

    public MenuBar(JFrame currentFrame) {
        JMenu menu = new JMenu("Navigation");
        add(menu);

        JMenuItem homeItem = new JMenuItem("Accueil");
        JMenuItem questionItem = new JMenuItem("Questions");
        JMenuItem quizItem = new JMenuItem("Quiz");
        JMenuItem answerItem = new JMenuItem("Réponses");

        menu.add(homeItem);
        menu.add(questionItem);
        menu.add(quizItem);
        menu.add(answerItem);

        homeItem.addActionListener(e -> {
            if (!(currentFrame instanceof HomeView)) {
                currentFrame.dispose();
                new HomeView().setVisible(true);
            }
        });

        questionItem.addActionListener(e -> {
            if (!(currentFrame instanceof QuestionView)) {
                currentFrame.dispose();
                new QuestionView().setVisible(true);
            }
        });

        quizItem.addActionListener(e -> {
            if (!(currentFrame instanceof QuizView)) {
                currentFrame.dispose();
                new QuizView().setVisible(true);
            }
        });

        answerItem.addActionListener(e -> {
            if (!(currentFrame instanceof AnswerView)) {
                currentFrame.dispose();
                new AnswerView().setVisible(true);
            }
        });
    }
}

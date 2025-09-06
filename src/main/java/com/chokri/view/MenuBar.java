package com.chokri.view;

import javax.swing.*;

public class MenuBar extends JMenuBar {

    public MenuBar(JFrame currentFrame) {
        JMenu menu = new JMenu("Navigation");
        add(menu);

        JMenuItem homeItem = new JMenuItem("Home");
        JMenuItem questionItem = new JMenuItem("Question");
        JMenuItem answerItem = new JMenuItem("Réponse");

        menu.add(homeItem);
        menu.add(questionItem);
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

        answerItem.addActionListener(e -> {
            if (!(currentFrame instanceof AnswerView)) {
                currentFrame.dispose();
                new AnswerView().setVisible(true);
            }
        });
    }
}


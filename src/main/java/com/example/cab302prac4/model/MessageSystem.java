package com.example.cab302prac4.model;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Objects;

public class MessageSystem {
    public void displayMessage(String text, Boolean error, Label messageLabel){
        if (error) {
            messageLabel.setTextFill(Color.color(0.75, 0, 0));
            messageLabel.setText(text);
        }
        else {
            messageLabel.setTextFill(Color.color(0, 0.75, 0));
            messageLabel.setText(text);
        }
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(b -> messageLabel.setText(null));
        pause.play();
    }
}

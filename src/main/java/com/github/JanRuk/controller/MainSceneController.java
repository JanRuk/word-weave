package com.github.JanRuk.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class MainSceneController {

    public AnchorPane anchrpnTitle;
    public Button btnClear;
    public Button btnExit;
    public Button btnSubmit;
    public Label lblA, lblB, lblC, lblD, lblE, lblF;
    public Label lblG, lblH, lblI, lblJ, lblK, lblL;
    public Label lblM, lblN, lblO, lblP, lblQ, lblR, lblS, lblT, lblU, lblV, lblW, lblX, lblY, lblZ;
    public Label lblTitle;
    public TextField txtR1C1, txtR1C2, txtR1C3, txtR1C4, txtR1C5;
    public TextField txtR2C1, txtR2C2, txtR2C3, txtR2C4, txtR2C5;
    public TextField txtR3C1, txtR3C2, txtR3C3, txtR3C4, txtR3C5;
    public TextField txtR4C1, txtR4C2, txtR4C3, txtR4C4, txtR4C5;
    public TextField txtR5C1, txtR5C2, txtR5C3, txtR5C4, txtR5C5;
    public TextField txtR6C1, txtR6C2, txtR6C3, txtR6C4, txtR6C5;
    public HBox hbxRow1;
    public HBox hbxRow3;
    public HBox hbxRow2;
    public HBox hbxRow4;
    public HBox hbxRow5;
    public HBox hbxRow6;
    public Label lblWinStreak;

    public final String wordToGuess = generateWord();
    public StringBuilder currentWord = new StringBuilder();
    public AnchorPane root;
    public GridPane grdRows;
    HBox activeHbox;

    public void initialize() {
        disableRows(hbxRow2, hbxRow3, hbxRow4, hbxRow5, hbxRow6);
        setTextFieldsToSingleLetters(hbxRow1,hbxRow2, hbxRow3, hbxRow4, hbxRow5);
        System.out.println(wordToGuess);
    }

    public void btnSubmitOnAction(ActionEvent event) {
        for (Node node : grdRows.getChildren()) {
            if (node instanceof HBox hbox) {
                if (isActiveHBox(hbox)) {
                    activeHbox = hbox;
                    break;
                }
            }
        }
        for (Node node : activeHbox.getChildren()) {
            if (node instanceof TextField) currentWord.append(((TextField) node).getText());
        }
        System.out.println(currentWord.toString());

        String guessedWord = currentWord.toString().toUpperCase();


        if (guessedWord.equals(wordToGuess)) {
            new Alert(Alert.AlertType.INFORMATION, "You guessed correctly").show();
        } else {
            moveToNextHBox(activeHbox);
        }
    }

    private void moveToNextHBox(HBox activeHbox) {
        boolean foundCurrent = false;
        for (Node node : grdRows.getChildren()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                if (foundCurrent) {
                    setHBoxDisabled(hbox, false);
                    return;
                }
                if (hbox == activeHbox) {
                    foundCurrent = true;
                    setHBoxDisabled(hbox, true);
                }
            }
        }
    }

    public void btnClearOnAction(ActionEvent event) {

    }

    public void btnExitOnAction(ActionEvent event) {

    }

    private void disableRows(HBox... hbxRow) {
        for (HBox hbox : hbxRow) {
            for (Node node : hbox.getChildren()) {
                if (node instanceof TextField) {
                    node.setDisable(true);
                }
            }
        }
    }

    private String generateWord() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("/home/janinda/Documents/dep-13/projects/word-weave/src/main/resources/word/word-list.txt")));
            String[] words = content.split(",");
            return words[new Random().nextInt(words.length)].trim().toUpperCase();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setTextFieldsToSingleLetters(HBox... hbxRow) {
        for (HBox hbox : hbxRow) {
            for (int i = 0; i < hbox.getChildren().size(); i++) {
                Node node = hbox.getChildren().get(i);
                final int index = i;
                if (node instanceof TextField textField) {
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue.isEmpty()) {
                            textField.setText(newValue.substring(0, 1));
                            if (index < hbox.getChildren().size() - 1) {
                                Node next = hbox.getChildren().get(index + 1);
                                if (next instanceof TextField) next.requestFocus();
                            }
                        }
                    });
                    textField.setOnKeyReleased(event -> {
                        if (event.getCode() == KeyCode.BACK_SPACE) {
                            if (index > 0) {
                                Node previous = hbox.getChildren().get(index - 1);
                                if (previous instanceof TextField) previous.requestFocus();
                            }
                        }
                    });
                }
            }
        }
    }
    // Enable or disable all text fields in a HBox
    private void setHBoxDisabled(HBox hbox, boolean disabled) {
        for (Node node : hbox.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setDisable(disabled);
                if (!disabled) ((TextField) node).requestFocus();
            }
        }
    }

    private boolean isActiveHBox(HBox hbox) {
        for (Node node : hbox.getChildren()) {
            if (node instanceof TextField && !((TextField) node).isDisable()) return true;
        }
        return false;
    }

}


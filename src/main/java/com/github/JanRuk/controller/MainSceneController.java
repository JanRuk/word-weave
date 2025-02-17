package com.github.JanRuk.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;

public class MainSceneController {

    public static String wordToGuess = generateWord();
    public AnchorPane anchrpnTitle;
    public Button btnClear;
    public Button btnExit;
    public Button btnSubmit;
    public Label lblA, lblB, lblC, lblD, lblE, lblF;
    public Label lblG, lblH, lblI, lblJ, lblK, lblL;
    public Label lblM, lblN, lblO, lblP, lblQ, lblR, lblS, lblT, lblU, lblV, lblW, lblX, lblY, lblZ;
    public Label lblTitle;
    public HBox hbxRow1;
    public HBox hbxRow3;
    public HBox hbxRow2;
    public HBox hbxRow4;
    public HBox hbxRow5;
    public HBox hbxRow6;
    public Label lblWinStreak;
    public StringBuilder currentWord = new StringBuilder();
    public AnchorPane root;
    public GridPane grdRows;
    public TextField txtR1C1;
    HBox activeHbox;

    public void initialize() {
        disableRows(hbxRow2, hbxRow3, hbxRow4, hbxRow5, hbxRow6);
        setTextFieldsToSingleLetters(hbxRow1, hbxRow2, hbxRow3, hbxRow4, hbxRow5, hbxRow6);
        System.out.println(wordToGuess);
    }

    public void btnSubmitOnAction(ActionEvent event) {
        playGame();
    }

    private void playGame() {
        for (Node node : grdRows.getChildren()) {
            if (node instanceof HBox hbox) {
                if (isActiveHBox(hbox)) {
                    activeHbox = hbox;
                    break;
                }
            }
        }
        for (Node node : activeHbox.getChildren()) {
            if (node instanceof TextField textField) currentWord.append(textField.getText());
        }
        System.out.println(currentWord.toString());
        String guessedWord = currentWord.toString().toUpperCase();
        if (guessedWord.equals(wordToGuess)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your guess is Correct.Do you want to continue?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.isPresent() && buttonType.get() == ButtonType.YES) {
                disableRows(hbxRow2, hbxRow3, hbxRow4, hbxRow5, hbxRow6);
                setTextFieldsToSingleLetters(hbxRow1, hbxRow2, hbxRow3, hbxRow4, hbxRow5, hbxRow6);
                cleanText();
                setHBoxDisabled(hbxRow1, false);
                txtR1C1.requestFocus();
            } else if (buttonType.get() == ButtonType.NO) {
                btnExit.fire();
            }

        } else {
            moveToNextHBox(activeHbox);
            currentWord = new StringBuilder();
        }
    }

    private void moveToNextHBox(HBox activeHbox) {
        boolean foundCurrent = false;
        for (Node node : grdRows.getChildren()) {
            if (node instanceof HBox hbox) {
                if (foundCurrent) {
                    setHBoxDisabled(hbox, false);

                    for (Node child : hbox.getChildren()) {
                        if (child instanceof TextField) {
                            ((TextField) child).requestFocus();
                            return;
                        }
                    }
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

    private static String generateWord() {
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

    private void cleanText() {
        for (Node node : grdRows.getChildren()) {
            if (node instanceof HBox hbox) {
                for (Node child : hbox.getChildren()) {
                    if (child instanceof TextField) ((TextField) child).clear();
                }
            }
        }
    }

}


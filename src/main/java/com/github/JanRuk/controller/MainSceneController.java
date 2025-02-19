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
    private static String[] wordList;

    public void initialize() {
        disableRows(hbxRow2, hbxRow3, hbxRow4, hbxRow5, hbxRow6);
        setTextFieldsToSingleLetters(hbxRow1, hbxRow2, hbxRow3, hbxRow4, hbxRow5, hbxRow6);
        System.out.println(wordToGuess);

    }

    public void btnSubmitOnAction(ActionEvent event) {
        playGame();
    }

    private void playGame() {
        activeHbox = null;
        currentWord.setLength(0);
        for (Node node : grdRows.getChildren()) {
            if (node instanceof HBox hbox) {
                if (isActiveHBox(hbox)) {
                    activeHbox = hbox;
                    break;
                }
            }
        }
        if (activeHbox == null) {
            System.out.println("No active hbox");
            return;
        }
        for (Node node : activeHbox.getChildren()) {
            if (node instanceof TextField textField) currentWord.append(textField.getText());
        }
        String guessedWord = currentWord.toString().toUpperCase();

        System.out.println("Guessed word: " + guessedWord);
        System.out.println("Correct word: " + wordToGuess);


        if (guessedWord.equals(wordToGuess)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your guess is Correct.Do you want to continue?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.isPresent() && buttonType.get() == ButtonType.YES) {

                wordToGuess = generateWord();
                System.out.println("New word: " + wordToGuess);
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
            currentWord.setLength(0);
        }
    }

    private void moveToNextHBox(HBox activeHbox) {
        boolean foundCurrent = false;

        for (Node node : grdRows.getChildren()) {
            if (node instanceof HBox hbox) {
                if (foundCurrent) {
                    setHBoxDisabled(hbox, false);

                    for (Node child : hbox.getChildren()) {
                        if (child instanceof TextField textField) {
                            textField.requestFocus();
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

    private static void loadWords() {
        try {
            String content = Files.readString(Paths.get("src/main/resources/word/word-list.txt"));
            wordList = content.split(",");
        } catch (IOException e) {
            e.printStackTrace();
            wordList =  new String[]{"Error"};
        }
    }

    private static String generateWord() {
       if (wordList == null) loadWords();
       return wordList[new Random().nextInt(wordList.length)].trim().toUpperCase();
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


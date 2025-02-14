package com.github.JanRuk.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;

public class MainSceneController {

    public AnchorPane anchrpnTitle;
    public Button btnClear;
    public Button btnExit;
    public Button btnSubmit;
    public Label lblA, lblB, lblC, lblD, lblE, lblF;
    public Label lblG, lblH, lblI, lblJ, lblK, lblL;
    public Label lblM, lblN, lblO, lblP, lblQ, lblR, lblS, lblT, lblU, lblV, lblW, lblX, lblY, lblZ;
    public Label lblTitle;
    public TextField txtR1C1,txtR1C2,txtR1C3,txtR1C4,txtR1C5;
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

    public void initialize() {
        disableRows(hbxRow2, hbxRow3, hbxRow4, hbxRow5, hbxRow6);
    }

    public void btnClearOnAction(ActionEvent event) {

    }

    public void btnExitOnAction(ActionEvent event) {

    }

    public void btnSubmitOnAction(ActionEvent event) {

    }

    public void disableRows(HBox... hbxRow) {
        for (HBox hbox: hbxRow) {
            for (Node node: hbox.getChildren()) {
                if (node instanceof TextField) {
                    node.setDisable(true);
                }
            }
        }
    }

}


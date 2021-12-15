package sample.controllers;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import sample.Main;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class UserController {

    public Label moneyLabel, accountLabel;
    public Button auth, btnMakeDeposit, btnShowHistoryDeposit, btnMoneyTransfer, btnShowHistoryTransferMoney;

    public void initialize(){
        accountLabel.setText(Main.sendToServerString("getUserAccNumber"));
    }

    public void showHistoryTransferMoney(ActionEvent actionEvent){
        new Main().openNewWindow((Stage)btnShowHistoryTransferMoney.getScene().getWindow(), false, "views/historyMoneyTransfer.fxml", "История денежных переводов", true);
    }

    public void moneyTransferAction(ActionEvent actionEvent){
        new Main().openNewWindow((Stage)btnMoneyTransfer.getScene().getWindow(), false, "views/moneyTransfer.fxml", "Денежный перевод", true);
    }

    public void makeDepositAction(ActionEvent actionEvent){
        new Main().openNewWindow((Stage)btnMakeDeposit.getScene().getWindow(), false, "views/loan.fxml", "Оформление кредита", true);
    }

    public void showHistoryDeposit(ActionEvent actionEvent){
        new Main().openNewWindow((Stage)btnShowHistoryDeposit.getScene().getWindow(),false,"views/historyLoan.fxml","История оформленных кредитов",true);
    }

    public void authAction(ActionEvent actionEvent){
        new Main().openNewWindow((Stage)auth.getScene().getWindow(), true, "views/start.fxml", "Вход", true);
    }

    public void showMoney(MouseEvent mouseEvent){
        moneyLabel.setText(Main.sendToServerString("getUserMoney") + "$");
    }

    public void showMoneyWithDeposit(MouseEvent mouseEvent){
        moneyLabel.setText(Main.sendToServerString("getUserMoneyWithDeposit") + "$");
    }

    public void hideMoney(MouseEvent mouseEvent){
        moneyLabel.setText("");
    }
}

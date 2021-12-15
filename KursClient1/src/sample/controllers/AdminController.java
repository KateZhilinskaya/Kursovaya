package sample.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.Main;

public class AdminController {
    public Button btnShowAllUsers, btnShowAllHistoryDeposit, btnShowAllHistoryTransferMoney, auth;

    public void showAllUsers(ActionEvent actionEvent){
        new Main().openNewWindow((Stage)btnShowAllUsers.getScene().getWindow(), false, "views/showAllUsers.fxml", "Все пользователи", true);
    }

    public void showAllHistoryDeposit(ActionEvent actionEvent){
        new Main().openNewWindow((Stage)btnShowAllHistoryDeposit.getScene().getWindow(), false, "views/showAllLoan.fxml", "Все оформленные кредиты", true);
    }

    public void showAllHistoryTransferMoney(ActionEvent actionEvent){
        new Main().openNewWindow((Stage)btnShowAllHistoryTransferMoney.getScene().getWindow(), false, "views/showAllTransferMoney.fxml", "Все сделанные переводы", true);
    }

    public void authAction(ActionEvent actionEvent){
        new Main().openNewWindow((Stage)auth.getScene().getWindow(), true, "views/start.fxml", "Вход", true);
    }
}

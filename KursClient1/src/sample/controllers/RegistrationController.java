package sample.controllers;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sample.Main;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;


public class RegistrationController {
    public int interval;
    public Button btnRegistration, back;
    public TextField fieldLogin;
    public PasswordField fieldPassword,repeatPassword;
    public Label errorLabel;

    public void BackAction(ActionEvent actionEvent) {
        new Main().openNewWindow((Stage)btnRegistration.getScene().getWindow(),true,"views/start.fxml","Вход",false);
    }

    public void registrationAction(ActionEvent actionEvent){
        String login = fieldLogin.getText();
        String password = fieldPassword.getText();
        String repeat = repeatPassword.getText();

        if(login.equals("") || password.equals("") || repeat.equals("")) {
            errorLabel.setText("Заполните все поля.");
            setTimer();
        }
        else if(!password.equals(repeat)) errorLabel.setText("Пароли не совпадают.");
        else if(password.length() <= 5) errorLabel.setText("Пароль слишком короткий.");
        else{
            Main.sendToServerString("registration " + login + " " + password);
            new Main().openNewWindow((Stage)btnRegistration.getScene().getWindow(),true,"views/user.fxml","Меню пользователя",false);
        }
    }

    public void setTimer() {
       Timer timer = new Timer();
       interval = 3;
       timer.scheduleAtFixedRate(new TimerTask() {
           public void run() {
               if(interval < 0) {
                   Platform.runLater(() -> errorLabel.setText(""));
                   timer.cancel();
               }
               else interval--;
           }
        }, 1000,1000);
    }
}

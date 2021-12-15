package sample.controllers;

import javafx.application.Platform;
import sample.Main;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class StartController{

    public int interval;
    public Button signUpBtn, regBtn;
    public TextField fieldLogin, fieldPassword;
    public Label errorLabel;

    public void regButton(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        new Main().openNewWindow((Stage) regBtn.getScene().getWindow(), true, "views/registration.fxml", "Регистрация", false);
    }

    public void signUpButton(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Stage stage = (Stage) signUpBtn.getScene().getWindow();
        String login = fieldLogin.getText();
        String password = fieldPassword.getText();

        if (!(login.equals("") || password.equals(""))) {
            String answer = Main.sendToServerString("signUp " + fieldLogin.getText() + " " + fieldPassword.getText());
            if (answer.equals("User"))
                new Main().openNewWindow(stage, true, "views/user.fxml", "Меню пользователя", false);
            else if (answer.equals("Admin"))
                new Main().openNewWindow(stage, true, "views/admin.fxml", "Меню администратора", false);
            else {
                fieldPassword.setText("");
                fieldLogin.setText("");
                errorLabel.setText("Данные введены не верно.");
                setTimer();
            }
        } else {
            errorLabel.setText("Заполните все поля.");
            setTimer();
        }
    }

    public void setTimer() {
        Timer timer = new Timer();
        interval = 2;
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

package sample.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import sample.Main;

import java.util.Timer;
import java.util.TimerTask;

public class MoneyTransferController {
    public CheckBox checkSaveReport;
    public TextField fieldNumberAccount, fieldSum;
    public Label errorLabel;
    private String numberAccount;
    private String resultOperation;
    private double sum;
    private int interval;

    public void transferMoneyAction(ActionEvent actionEvent){
        errorLabel.setText("");
        numberAccount = fieldNumberAccount.getText();
        try{
            String checkSum = fieldSum.getText();
            if (!checkSum.matches("^[+]?\\d+(\\.{0,1}(\\d+?))?$")) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Некорректно ведена сумма.");
                setTimer(3);
            }else{
                sum = Double.parseDouble(fieldSum.getText());
                if (sum < 5) {
                    errorLabel.setText("Слишком маленькая сумма.");
                    setTimer(3);
                }
                else if (numberAccount.equals("") || fieldSum.equals("")) {
                    errorLabel.setText("Некорректные данные.");
                    setTimer(3);
                }
                else {
                    errorLabel.setTextFill(Color.GREEN);
                    errorLabel.setText("Перевод выполнен.");
                    setTimer(4);
                    fieldNumberAccount.setText("");
                    fieldSum.setText("");
                    if (checkSaveReport.isSelected())
                        resultOperation = Main.sendToServerString("makeTransferMoney " + numberAccount + " " + sum + " file");
                    else resultOperation = Main.sendToServerString("makeTransferMoney " + numberAccount + " " + sum);
                }
            }
        }catch (Exception e) {
                fieldNumberAccount.setText("");
                fieldSum.setText("");
                errorLabel.setText(resultOperation);
            }
    }

    public void setTimer(int k) {
        Timer timer = new Timer();
        interval = k;
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

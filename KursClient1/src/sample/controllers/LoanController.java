package sample.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import sample.Main;
import sample.classes.TypeDeposit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class LoanController {
    public Label errorLabel, labelPercentDeposit, labelNewSumDeposit, labelTermDeposit, labelStartDateDeposit, labelEndDateDeposit;
    public TextField fieldSumDeposit;
    public Button btnMakeDeposit;
    public ComboBox comboBoxDeposit;
    public CheckBox checkSaveReport;
    private ArrayList<TypeDeposit> depositList = new ArrayList<>();
    private int startMonth, startYear, term, interval;
    private Calendar calendar;
    private double sum, percent = 9.2;
    private String nameDeposit = "Семейный-классик";

    public void initialize(){
        btnMakeDeposit.setDisable(true);
        ArrayList<String> list = new ArrayList<>();
        list = (ArrayList<String>) Main.getObjectFromServer("getTypeDeposit");
        String[] depositString;
        ObservableList<String> observableList = FXCollections.observableArrayList();
        for(String tmp : list){
            depositString = tmp.split(" ");
            observableList.add(depositString[0]);
            depositList.add(new TypeDeposit(depositString[0], Double.parseDouble(depositString[1]), Integer.parseInt(depositString[2])));
        }

        comboBoxDeposit.setItems(observableList);
        comboBoxDeposit.getSelectionModel().selectFirst();
        comboBoxDeposit.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                nameDeposit = comboBoxDeposit.getSelectionModel().getSelectedItem().toString();
                calendar = calendar.getInstance();

                percent = depositList.get(newValue.intValue()).getPercent();
                labelPercentDeposit.setText(Double.toString(percent) + "%");

                labelNewSumDeposit.setText(String.format ("%.2f", (sum + (sum * percent / 100))));

                term = depositList.get(newValue.intValue()).getTerm();
                labelTermDeposit.setText(Integer.toString(term) + " месяцев");

                startMonth = calendar.get(Calendar.MONTH) + 1;
                startYear = calendar.get(Calendar.YEAR);

                labelStartDateDeposit.setText(Integer.toString(startMonth) + "." + Integer.toString(startYear));

                if (startMonth + term > 12)
                    labelEndDateDeposit.setText(Integer.toString(startMonth - 12 + term) + "." + Integer.toString(startYear + 1));
                else
                    labelEndDateDeposit.setText(Integer.toString(startMonth + term) + "." + Integer.toString(startYear));
            }
        });
    }

    public void keyEvent(KeyEvent keyEvent){
        try {
            String checkSum = fieldSumDeposit.getText();
            if (!checkSum.matches("^[+]?\\d+(\\.{0,1}(\\d+?))?$")) {
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Некорректно ведена сумма.");
                labelNewSumDeposit.setText("0.00");
                btnMakeDeposit.setDisable(true);
            }else{
                sum = Double.parseDouble(fieldSumDeposit.getText());
                if (sum < 50) {
                    btnMakeDeposit.setDisable(true);
                    errorLabel.setTextFill(Color.RED);
                    errorLabel.setText("Маленькая сумма.");
                    labelNewSumDeposit.setText("0.00");
                } else {
                    labelNewSumDeposit.setText(String.format("%.2f", (sum + (sum * percent / 100))));
                    errorLabel.setText("");
                    btnMakeDeposit.setDisable(false);
                }
            }
        }catch (Exception e){
            labelNewSumDeposit.setText("0.00");
        }
    }

    public void makeDepositAction(ActionEvent actionEvent){
        String answer = "";
        try{
            sum = Double.parseDouble(fieldSumDeposit.getText());
            if (sum >= 50){
                if(checkSaveReport.isSelected())
                    answer = Main.sendToServerString("makeDeposit " + nameDeposit + " " + sum + " " + term + " saveFile");
                else
                    answer = Main.sendToServerString("makeDeposit " + nameDeposit + " " + sum);
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setText("Успешно оформлено.");
                labelNewSumDeposit.setText("0.00");
                fieldSumDeposit.setText("0.00");
                setTimer(6);
            }else{
                errorLabel.setTextFill(Color.RED);
                errorLabel.setText("Маленькая сумма");
                labelNewSumDeposit.setText("0.00");
                setTimer(3);
            }
        }catch (Exception e){
            e.printStackTrace();
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

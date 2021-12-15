package sample.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Main;
import sample.classes.ShowUsers;
import sample.classes.TableMoney;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShowAllUsersController {
    public ChoiceBox choiceBox;
    ShowUsers tmp;
    String choiceSearchField = "name";
    public Button btnAdd, btnDelete, btnEdit, searchBtn,btnShowAllRecord;
    public TextField fieldName, fieldPassword, fieldRole, fieldMoney, fieldAccountNum, searchField;
    public TableView<ShowUsers> dataTable;
    public TableColumn<ShowUsers, String> idColumn, nameColumn, passwordColumn, roleColumn, moneyColumn, accountNumColumn, regDateColumn;

    public void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<ShowUsers, String>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<ShowUsers, String>("name"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<ShowUsers, String>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<ShowUsers, String>("role"));
        moneyColumn.setCellValueFactory(new PropertyValueFactory<ShowUsers, String>("money"));
        accountNumColumn.setCellValueFactory(new PropertyValueFactory<ShowUsers, String>("accountNum"));
        regDateColumn.setCellValueFactory(new PropertyValueFactory<ShowUsers, String>("regDate"));

        ObservableList<ShowUsers> ars = FXCollections.observableArrayList();

        ObservableList<String> availableChoices = FXCollections.observableArrayList("name", "password","role","money","accountNum");
        choiceBox.setItems(availableChoices);
        choiceBox.getSelectionModel().selectFirst();
        final String[] arrayChoice = new String[]{"name", "password","role","money","account_num"};
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                choiceSearchField = arrayChoice[newValue.intValue()];
            }
        });
        ArrayList<String> list = new ArrayList<>();
        String[] userString = {""};
        list = (ArrayList<String>) Main.getObjectFromServer("showAllUsers");

        for(String tmp : list){
            userString = tmp.split(" ");
            ars.add(new ShowUsers(Integer.parseInt(userString[0]), userString[1], userString[2], Integer.parseInt(userString[3]), Double.parseDouble(userString[4]), userString[5], userString[6]));
        }
        dataTable.setItems(ars);

        setRecord();
    }

    public void addRecord(ActionEvent actionEvent){

        boolean validation = true;

        String login = fieldName.getText();
        String password = fieldPassword.getText();
        String role = fieldRole.getText();
        String money = fieldMoney.getText();
        String accountNum = fieldAccountNum.getText();

        if (login.equals("") || password.equals("") || role.equals("") || money.equals("") || accountNum.equals("")){
            validation = false;
            wrongData("Заполните все поля");
        } else if (!money.matches("[\\d]+") || !role.matches("[\\d]+"))  {
            validation = false;
            wrongData("Проверьте корректность данных");
        }
        if (validation) {
            Main.sendToServerString("registration " + login + " " + password + " " + role + " " + money + " " + accountNum);

            fieldName.setText("");
            fieldPassword.setText("");
            fieldRole.setText("");
            fieldMoney.setText("");
            fieldAccountNum.setText("");
        }

        initialize();
    }

    public void deleteRecord(ActionEvent actionEvent){
        try {
            String deleteId = "";
            deleteId = Integer.toString(tmp.getId());
            Main.sendToServerString("deleteUser " + deleteId);

            fieldName.setText("");
            fieldPassword.setText("");
            fieldRole.setText("");
            fieldMoney.setText("");
            fieldAccountNum.setText("");
            initialize();
        }catch (NullPointerException e) {
            wrongData("Выберите поле");
        }
    }

    public void editRecord(ActionEvent actionEvent){
        try {
            boolean validation = true;
            int id = Integer.parseInt(Integer.toString(tmp.getId()));
            String login = fieldName.getText();
            String password = fieldPassword.getText();
            String role = fieldRole.getText();
            String money = fieldMoney.getText();
            String accountNum = fieldAccountNum.getText();

            System.out.println(id);
            if (login.equals("") || password.equals("") || role.equals("") || money.equals("") || accountNum.equals("")) {
                validation = false;
                wrongData("Заполните все поля");
            } else if (!role.matches("[\\d]+") || !money.matches("^[+]?\\d+(\\.{0,1}(\\d+?))?$")) {
                validation = false;
                wrongData("Проверьте корректност данных");
            }

            if (validation) {
                Main.getObjectFromServer("editUser " + id + " " + login + " " + password + " " + role + " " + money + " " + accountNum);

                fieldName.setText("");
                fieldPassword.setText("");
                fieldRole.setText("");
                fieldMoney.setText("");
                fieldAccountNum.setText("");
            }

            initialize();
        }catch (NullPointerException e) {
            wrongData("Выберите поле");

        }
    }

    public void search(ActionEvent actionEvent){

        String searchText = searchField.getText();
        if(!searchText.equals("")) {
            ObservableList<ShowUsers> searchArs = FXCollections.observableArrayList();
            ArrayList<String> list = new ArrayList();
            String[] foundUsers;
            list = (ArrayList<String>) Main.getObjectFromServer("searchUser " + choiceSearchField + " " + searchText);
            for (String tmp : list){
                foundUsers = tmp.split(" ");
                searchArs.add(new ShowUsers(Integer.parseInt(foundUsers[0]), foundUsers[1], foundUsers[2], Integer.parseInt(foundUsers[3]), Double.parseDouble(foundUsers[4]), foundUsers[5], foundUsers[6]));
            }
            dataTable.setItems(searchArs);
            btnShowAllRecord.setDisable(false);
            setRecord();
            searchField.setText("");
        }else{
            wrongData("Заполните поле поиска");
        }
    }

    private void setRecord() {
        try {
            dataTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ShowUsers>() {
                @Override
                public void changed(ObservableValue<? extends ShowUsers> observable, ShowUsers oldValue, ShowUsers newValue) {
                    tmp = dataTable.getSelectionModel().getSelectedItem();
                    fieldName.setText(tmp.getName());
                    fieldPassword.setText(tmp.getPassword());
                    fieldRole.setText(Integer.toString(tmp.getRole()));
                    fieldMoney.setText(Double.toString(tmp.getMoney()));
                    fieldAccountNum.setText(tmp.getAccountNum());
                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void showAllRecord(ActionEvent actionEvent){
        btnShowAllRecord.setDisable(true);
        initialize();
    }

    private void wrongData(String text){
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Wrong data!");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }
}


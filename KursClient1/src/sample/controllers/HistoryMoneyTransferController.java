package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Main;
import sample.classes.TableMoney;

import java.util.ArrayList;

public class HistoryMoneyTransferController {
    public TableView<TableMoney> dataTable;
    public TableColumn<TableMoney, String> accountNumColumn;
    public TableColumn<TableMoney, String> sumColumn;
    public TableColumn<TableMoney, String> dateColumn;
    public Button showAll, btnSearch;
    public TextField searchField;

    public void initialize(){
        accountNumColumn.setCellValueFactory(new PropertyValueFactory<TableMoney, String>("accountNum"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<TableMoney, String>("sum"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<TableMoney, String>("date"));

        ObservableList<TableMoney> ars = FXCollections.observableArrayList();
        ArrayList<String> list = new ArrayList<>();
        String[] transferMoneyString = {""};
        list = (ArrayList<String>) Main.getObjectFromServer("showTransferMoney");

        for(String tmp : list){
            transferMoneyString = tmp.split(" ");
            ars.add(new TableMoney(transferMoneyString[0], Double.parseDouble(transferMoneyString[1]), transferMoneyString[2]));
        }

        dataTable.setItems(ars);
        showAll.setDisable(true);
    }

    public void showAllAction(ActionEvent actionEvent){
        initialize();
        searchField.setText("");
        showAll.setDisable(true);
    }

    public void searchAction(ActionEvent actionEvent){
        ObservableList<TableMoney> searchArs = FXCollections.observableArrayList();
        ArrayList<String> list = new ArrayList();
        String searchAcc = searchField.getText();
        String[] foundTransferMoneyString;
        if(!searchAcc.equals(""))
            list = (ArrayList<String>) Main.getObjectFromServer("showTransferMoney " + searchAcc);
        for (String tmp : list){
            foundTransferMoneyString = tmp.split(" ");
            searchArs.add(new TableMoney(foundTransferMoneyString[0], Double.parseDouble(foundTransferMoneyString[1]), foundTransferMoneyString[2]));
        }
        searchField.setText("");
        dataTable.setItems(searchArs);
        showAll.setDisable(false);
    }

}

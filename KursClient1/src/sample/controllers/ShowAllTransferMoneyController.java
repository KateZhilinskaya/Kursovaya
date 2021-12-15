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

public class ShowAllTransferMoneyController {
    public TableView<TableMoney> dataTable;
    public TableColumn<TableMoney, String> nameColumn;
    public TableColumn<TableMoney, String> accountNumColumn;
    public TableColumn<TableMoney, String> sumColumn;
    public TableColumn<TableMoney, String> dateColumn;
    public Button showAll, btnSearch;
    public TextField searchField;

    public void initialize(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<TableMoney, String>("name"));
        accountNumColumn.setCellValueFactory(new PropertyValueFactory<TableMoney, String>("accountNum"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<TableMoney, String>("sum"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<TableMoney, String>("date"));

        ObservableList<TableMoney> ars = FXCollections.observableArrayList();
        ArrayList<String> list = new ArrayList<>();
        String[] transferMoneyString = {""};
        list = (ArrayList<String>) Main.getObjectFromServer("showAllTransferMoney");

        for(String tmp : list){
            transferMoneyString = tmp.split(" ");
            ars.add(new TableMoney(transferMoneyString[0], transferMoneyString[1], transferMoneyString[2], Double.parseDouble(transferMoneyString[3])));
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
            list = (ArrayList<String>) Main.getObjectFromServer("showAllTransferMoney " + searchAcc);
        for (String tmp : list){
            foundTransferMoneyString = tmp.split(" ");
            searchArs.add(new TableMoney(foundTransferMoneyString[0], foundTransferMoneyString[1], foundTransferMoneyString[2], Double.parseDouble(foundTransferMoneyString[3])));
        }
        searchField.setText("");
        dataTable.setItems(searchArs);
        showAll.setDisable(false);
    }


}

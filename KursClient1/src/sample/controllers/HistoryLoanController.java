package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Main;
import sample.classes.ShowMyLoan;

import java.util.ArrayList;

public class HistoryLoanController {
    public TableView<ShowMyLoan> dataTable;
    public TableColumn<ShowMyLoan, String> depositColumn;
    public TableColumn<ShowMyLoan, String> sumColumn;
    public TableColumn<ShowMyLoan, String> termColumn;
    public TableColumn<ShowMyLoan, String> percentColumn;
    public TableColumn<ShowMyLoan, String> dateColumn;

    public void initialize(){
        depositColumn.setCellValueFactory(new PropertyValueFactory<ShowMyLoan, String>("name"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<ShowMyLoan, String>("sum"));
        percentColumn.setCellValueFactory(new PropertyValueFactory<ShowMyLoan, String>("percent"));
        termColumn.setCellValueFactory(new PropertyValueFactory<ShowMyLoan, String>("term"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<ShowMyLoan, String>("date"));

        ObservableList<ShowMyLoan> ars = FXCollections.observableArrayList();
        ArrayList<String> list = new ArrayList<>();
        String[] myDeposit = {""};
        list = (ArrayList<String>) Main.getObjectFromServer("showMyDeposit");

        for(String tmp : list){
            myDeposit = tmp.split(" ");
            ars.add(new ShowMyLoan(myDeposit[0], Double.parseDouble(myDeposit[1]),
                    Integer.parseInt(myDeposit[2]), Double.parseDouble(myDeposit[3]), myDeposit[4]));
        }

        dataTable.setItems(ars);
    }
}

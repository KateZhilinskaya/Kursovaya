package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Main;
import sample.classes.ShowMyLoan;

import java.util.ArrayList;

public class ShowAllLoanController {
    public TableView<ShowMyLoan> dataTable;
    public TableColumn<ShowMyLoan, String> depositColumn;
    public TableColumn<ShowMyLoan, String> nameColumn;
    public TableColumn<ShowMyLoan, String> sumColumn;
    public TableColumn<ShowMyLoan, String> termColumn;
    public TableColumn<ShowMyLoan, String> percentColumn;
    public TableColumn<ShowMyLoan, String> dateColumn;
    public Button showAll, btnSearch;
    public TextField searchField;
    public PieChart pieChartDeposit;
    public BarChart barChartDeposit;
    public ArrayList<String> listPie, listBar;
    public String[] arrayStr = {""};
    XYChart.Series series;

    public void initialize(){
        depositColumn.setCellValueFactory(new PropertyValueFactory<ShowMyLoan, String>("name"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<ShowMyLoan, String>("userName"));
        sumColumn.setCellValueFactory(new PropertyValueFactory<ShowMyLoan, String>("sum"));
        percentColumn.setCellValueFactory(new PropertyValueFactory<ShowMyLoan, String>("percent"));
        termColumn.setCellValueFactory(new PropertyValueFactory<ShowMyLoan, String>("term"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<ShowMyLoan, String>("date"));

        series = new XYChart.Series();
        series.setName("Депозиты");

        listBar = new ArrayList();
        listPie = new ArrayList();

        listPie = (ArrayList<String>) Main.getObjectFromServer("showAllStatisticsDeposit");
        listBar = listPie;
        ObservableList<PieChart.Data> pieChartObs = FXCollections.observableArrayList();
        for (String str : listPie){
            arrayStr = str.split(" ");
            series.getData().add(new XYChart.Data(arrayStr[0], Integer.parseInt(arrayStr[1])));
            pieChartObs.add(new PieChart.Data(arrayStr[0], Integer.parseInt(arrayStr[1])));
        }
        barChartDeposit.getData().add(series);
        pieChartDeposit.setData(pieChartObs);
        loadData();
    }

    public void loadData(){
        ObservableList<ShowMyLoan> ars = FXCollections.observableArrayList();
        ArrayList<String> list = new ArrayList<>();
        String[] myAllDeposit = {""};
        list = (ArrayList<String>) Main.getObjectFromServer("showAllDeposit");

        for(String tmp : list){
            myAllDeposit = tmp.split(" ");
            ars.add(new ShowMyLoan(myAllDeposit[0], Double.parseDouble(myAllDeposit[1]), myAllDeposit[2], Double.parseDouble(myAllDeposit[3]), Integer.parseInt(myAllDeposit[4]), myAllDeposit[5]));
        }

        showAll.setDisable(true);
        dataTable.setItems(ars);
    }

    public void showAllAction(ActionEvent actionEvent){
        loadData();
        searchField.setText("");
        showAll.setDisable(true);
    }

    public void searchAction(ActionEvent actionEvent){
        ObservableList<ShowMyLoan> searchArs = FXCollections.observableArrayList();
        ArrayList<String> list = new ArrayList();
        String searchSum = searchField.getText();
        String[] foundDeposit;
        if(!searchSum.equals(""))
            list = (ArrayList<String>) Main.getObjectFromServer("showAllDeposit " + searchSum);
        for (String tmp : list){
            foundDeposit = tmp.split(" ");
            searchArs.add(new ShowMyLoan(foundDeposit[0], Double.parseDouble(foundDeposit[1]), foundDeposit[2], Double.parseDouble(foundDeposit[3]), Integer.parseInt(foundDeposit[4]), foundDeposit[5]));
        }
        searchField.setText("");
        dataTable.setItems(searchArs);
        showAll.setDisable(false);
    }

}


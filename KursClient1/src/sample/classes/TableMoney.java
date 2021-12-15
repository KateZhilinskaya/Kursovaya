package sample.classes;

public class TableMoney {
    private String accountNum, date, name;
    private double sum;

    public TableMoney(String name, String accountNum, String date, double sum) {
        this.accountNum = accountNum;
        this.date = date;
        this.name = name;
        this.sum = sum;
    }

    public TableMoney(String accountNum, double sum, String date) {
        this.accountNum = accountNum;
        this.date = date;
        this.sum = sum;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public double getSum() {
        return sum;
    }
}

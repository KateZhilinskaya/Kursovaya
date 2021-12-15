package sample.classes;

public class ShowMyLoan {
    private String name, date, userName;
    private double percent, sum;
    private int term;

    public ShowMyLoan(String name, double percent, int term, double sum, String date) {
        this.name = name;
        this.date = date;
        this.percent = percent;
        this.sum = sum;
        this.term = term;
    }

    public ShowMyLoan(String userName, double sum, String name, double percent, int term, String date) {
        this.name = name;
        this.date = date;
        this.userName = userName;
        this.percent = percent;
        this.sum = sum;
        this.term = term;
    }

    public String getUserName() {
        return userName;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public double getPercent() {
        return percent;
    }

    public double getSum() {
        return sum;
    }

    public int getTerm() {
        return term;
    }
}

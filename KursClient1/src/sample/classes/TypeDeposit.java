package sample.classes;

public class TypeDeposit {
    private String name;
    private int term;
    private double percent;

    public TypeDeposit(String name, double percent, int term){
        this.name = name;
        this.term = term;
        this.percent = percent;
    }

    public String getName() {
        return name;
    }

    public int getTerm() {
        return term;
    }

    public double getPercent() {
        return percent;
    }
}

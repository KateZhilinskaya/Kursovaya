package sample.classes;

public class ShowUsers {
    private int id, role;
    private String name, accountNum, password, regDate;
    private double money;

    public ShowUsers(int id, String name, String password, int role, double money, String accountNum, String regDate) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.money = money;
        this.accountNum = accountNum;
        this.password = password;
        this.regDate = regDate;
    }

    public int getId() {
        return id;
    }

    public int getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public String getPassword() {
        return password;
    }

    public String getRegDate() {
        return regDate;
    }

    public double getMoney() {
        return money;
    }
}

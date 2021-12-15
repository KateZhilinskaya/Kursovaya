import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class DatabaseWork {
    Connection dbConnection;
    private double userCurrentSum = 0;
    private double userAfterDepositSum = 0;
    private int userId = 0;
    private Statement statement;
    String insertStr = "";
    double percent = 0;
    String userAccNumber = "";

    public DatabaseWork() {
        dbConnection();
    }

    public void dbConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost/course?serverTimezone=GMT%2B8", "root", "root");

            statement = dbConnection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void getUserAccNumber(ObjectOutputStream soos){
        try {
            soos.writeObject(userAccNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getUserMoney(ObjectOutputStream soos){
        try {
            soos.writeObject(Double.toString(userCurrentSum));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getUserMoneyWithDeposit(ObjectOutputStream soos){
        try {
            soos.writeObject(Double.toString(userCurrentSum + userAfterDepositSum));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void signUp(String[] action, ObjectOutputStream soos) {

        ResultSet users = null;
        try {
            users = statement.executeQuery("SELECT * FROM users");


            String answer = "Error";
            while (users.next()) {
                if (users.getString("name").equals(action[1]) && users.getString("password").equals(action[2])) {
                    userId = Integer.parseInt(users.getString("id"));
                    userCurrentSum = Double.parseDouble(users.getString("money"));
                    userAccNumber = users.getString("account_num");
                    if (users.getString("role").equals("0")) answer = "User";
                    else answer = "Admin";
                }
            }

            users.close();
            soos.writeObject(answer);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void showAllStatisticsDeposit(String[] arr, ObjectOutputStream soos) {

        ArrayList<String> typeDepositList = new ArrayList<>();
        ResultSet typeDeposit = null;
        ArrayList<String> listForClient = new ArrayList<>();
        int count = 0, tmp = 1;
        try {

            typeDeposit = getTypeDepositTable();
            while (typeDeposit.next()) typeDepositList.add(typeDeposit.getString("name"));
            typeDeposit.close();
            ResultSet allDeposit = null;

            for (String str : typeDepositList) {
                allDeposit = statement.executeQuery("SELECT * FROM deposit WHERE deposit_id = " + quotate(Integer.toString(tmp)));
                while (allDeposit.next()) count++;
                listForClient.add(str + " " + Integer.toString(count));
                count = 0;
                tmp++;
            }

            allDeposit.close();
            soos.writeObject(listForClient);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void showAllTransferMoney(String[] tmp, ObjectOutputStream soos) {
        ArrayList<String> list = new ArrayList<>();
        ResultSet allTransferMoney = null;
        try {
            ArrayList<String> listUsers = new ArrayList<>();
            ResultSet users = getUserFromTable();
            while (users.next()) listUsers.add(users.getString("name"));
            users.close();

            if (tmp.length == 2)
                allTransferMoney = statement.executeQuery(Query.TRANSFER_MONEY_WHERE_ACOOUNT_NUM + quotate(tmp[1]));
            else allTransferMoney = getTransferMoneyFromTable();
            while (allTransferMoney.next()) {
                list.add(listUsers.get(Integer.parseInt(allTransferMoney.getString("sender_id")) - 1) + " " +
                        allTransferMoney.getString("receive_num") + " " +
                        allTransferMoney.getString("date") + " " +
                        allTransferMoney.getString("sum"));
            }
            allTransferMoney.close();

            soos.writeObject(list);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void showAllUsers(String[] tmp, ObjectOutputStream soos) {
        try {
            ArrayList<String> listUsers = new ArrayList<>();
            ResultSet allUsers = getUserFromTable();
            while (allUsers.next()) listUsers.add(allUsers.getString("id") + " " + allUsers.getString("name") + " " + allUsers.getString("password") + " " + allUsers.getString("role") + " " +
                            allUsers.getString("money") + " " + allUsers.getString("account_num") + " " + allUsers.getString("reg_date"));
            allUsers.close();
            soos.writeObject(listUsers);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String[] tmp, ObjectOutputStream soos) {
        try {
            insertStr = "DELETE FROM `deposit` WHERE `user_id` = (" + quotate(tmp[1]) + ")";
            statement.executeUpdate(insertStr);
            insertStr = "DELETE FROM `transfer_money` WHERE `sender_id` = (" + quotate(tmp[1]) + ")";
            statement.executeUpdate(insertStr);
            insertStr = "DELETE FROM `users` WHERE `id` = (" + quotate(tmp[1]) + ")";
            statement.executeUpdate(insertStr);

            soos.writeObject("Success");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void editUser(String[] tmp, ObjectOutputStream soos) {
        try {
            insertStr = "UPDATE `users` SET "
                    + " name = " + quotate(tmp[2]) + ","
                    + " password = " + quotate(tmp[3]) + ","
                    + " role = " + quotate(tmp[4]) + ","
                    + " money = " + quotate(tmp[5]) + ","
                    + " account_num = " + quotate(tmp[6])
                    + " WHERE id = " + quotate((tmp[1]));
            statement.executeUpdate(insertStr);
            soos.writeObject("Success");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void searchUser(String[] tmp, ObjectOutputStream soos) {
        try {
            ArrayList<String> listUsers = new ArrayList<>();
            ResultSet allUsers = getUserFromTable();
            while (allUsers.next()) {
                if (allUsers.getString(tmp[1]).equals(tmp[2]))
                    listUsers.add(allUsers.getString("id") + " " + allUsers.getString("name") + " " + allUsers.getString("password") + " " + allUsers.getString("role") + " " +
                            allUsers.getString("money") + " " + allUsers.getString("account_num") + " " + allUsers.getString("reg_date"));
                System.out.println(allUsers.getString(tmp[1]) + " " + tmp[2]);
            }
                allUsers.close();

            soos.writeObject(listUsers);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void showAllDeposit(String[] tmp, ObjectOutputStream soos) {
        ArrayList<String> list = new ArrayList<>();

        ArrayList<String> listDepositType = new ArrayList<>();
        try {
            ResultSet allDepositType = getTypeDepositTable();
            while (allDepositType.next())
                listDepositType.add(allDepositType.getString("name") + " " + allDepositType.getString("percent") + " " + allDepositType.getString("term"));
            allDepositType.close();

            ArrayList<String> listDepositUsers = new ArrayList<>();
            ResultSet allUsers = getUserFromTable();
            while (allUsers.next()) listDepositUsers.add(allUsers.getString("name"));
            allUsers.close();

            ResultSet allDeposit = null;
            if (tmp.length == 2) allDeposit = statement.executeQuery(Query.DEPOSIT_WHERE_SUM_LESS + tmp[1]);
            else allDeposit = getDepositFromTable();

            String[] strTmp = {""};
            while (allDeposit.next()) {
                strTmp = listDepositType.get(Integer.parseInt(allDeposit.getString("deposit_id")) - 1).split(" ");
                list.add(listDepositUsers.get(Integer.parseInt(allDeposit.getString("user_id")) - 1) + " " +
                        allDeposit.getString("sum") + " " +
                        strTmp[0] + " " +
                        strTmp[1] + " " +
                        strTmp[2] + " " +
                        allDeposit.getString("date"));
            }

            allDeposit.close();
            soos.writeObject(list);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void showMyDeposit(String[] tmp, ObjectOutputStream soos) {
        ArrayList<String> typeDepositList = new ArrayList<>();
        try {
            ResultSet depositType = getTypeDepositTable();

            while (depositType.next())
                typeDepositList.add(depositType.getString("name") +
                        " " + depositType.getString("percent") +
                        " " + depositType.getString("term"));
            depositType.close();

            ResultSet myDeposit = statement.executeQuery(Query.DEPOSIT_WHERE_USER_ID + quotate(Integer.toString(userId)));
            ArrayList<String> myDepositList = new ArrayList<>();
            while (myDeposit.next())
                myDepositList.add(typeDepositList.get(Integer.parseInt(myDeposit.getString("deposit_id")) - 1) +
                        " " + myDeposit.getString("sum") + " " + myDeposit.getString("date"));

            myDeposit.close();
            soos.writeObject(myDepositList);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void registration(String[] tmp, ObjectOutputStream soos) {
        int countId = 1;
        try {
            ResultSet users = statement.executeQuery("SELECT * FROM users ");
            while (users.next()) countId++;
            users.close();
            userId = countId;
            Random rnd = new Random();
            char smallLet = (char) (rnd.nextInt(26) + 'a');
            char bigLet = (char) (rnd.nextInt(26) + 'A');
            userAccNumber = "BY" + (rnd.nextInt((10000 - 0) + 1) + 0) + bigLet + smallLet + (rnd.nextInt((10000 - 0) + 1) + 0);
            if (tmp.length == 3) {
                insertStr = "INSERT INTO users VALUES ("
                        + quotate(Integer.toString(countId)) + ","
                        + quotate(tmp[1]) + ","
                        + quotate(tmp[2]) + ","
                        + quotate("0") + ","
                        + quotate(Double.toString(1000)) + ","
                        + quotate(userAccNumber) + ","
                        + quotate(getDate()) + ")";
                statement.executeUpdate(insertStr);
                userCurrentSum = 1000;
            }else{
                insertStr = "INSERT INTO users VALUES ("
                        + quotate(Integer.toString(countId)) + ","
                        + quotate(tmp[1]) + ","
                        + quotate(tmp[2]) + ","
                        + quotate(tmp[3]) + ","
                        + quotate(tmp[4]) + ","
                        + quotate(tmp[5]) + ","
                        + quotate(getDate()) + ")";
                statement.executeUpdate(insertStr);
                userCurrentSum = Double.parseDouble(tmp[4]);
            }
            soos.writeObject("SUCCESS");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void showTransferMoney(String[] stringFromClient, ObjectOutputStream soos) {
        ArrayList<String> list = new ArrayList<>();
        try {
            ResultSet users = getUserFromTable();
            ArrayList<String> usersList = new ArrayList();
            while (users.next())
                usersList.add(users.getString("account_num"));
            users.close();

            ResultSet transferMoney = statement.executeQuery(Query.TRANSFER_MONEY_WHERE_SENDER_ID + quotate(Integer.toString(userId)));
            if (stringFromClient.length == 1)
                while (transferMoney.next())
                    list.add(transferMoney.getString("receive_num") + " " + transferMoney.getString("sum") + " " +
                            transferMoney.getString("date"));
            else
                while (transferMoney.next())
                    if (stringFromClient[1].equals(transferMoney.getString("receive_num")))
                        list.add(transferMoney.getString("receive_num") + " " + transferMoney.getString("sum") + " " +
                                transferMoney.getString("date"));
            transferMoney.close();

            soos.writeObject(list);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void makeDeposit(String[] stringFromClient, ObjectOutputStream soos) {
        try {
            ResultSet tmp = getDepositFromTable();

            String deposit_id = "";
            int countId = 1;
            while (tmp.next()) countId++;
            tmp.close();

            ResultSet deposit_type = getTypeDepositTable();
            while (deposit_type.next()) {
                if (deposit_type.getString("name").equals(stringFromClient[1])) {
                    deposit_id = deposit_type.getString("id");
                    percent = Double.parseDouble(deposit_type.getString("percent"));
                }
            }
            deposit_type.close();

            insertStr = "INSERT INTO deposit VALUES ("
                    + quotate(Integer.toString(countId)) + ","
                    + quotate(Integer.toString(userId)) + ","
                    + quotate(deposit_id) + ","
                    + quotate(getDate()) + ","
                    + quotate(stringFromClient[2]) + ")";

            statement.executeUpdate(insertStr);

            insertStr = "UPDATE `users` SET "
                    + " money = " + quotate(Double.toString(userCurrentSum - Double.parseDouble(stringFromClient[2])))
                    + " WHERE  (" + "id = " + quotate(Integer.toString(userId)) + ")";
            userCurrentSum -= Double.parseDouble(stringFromClient[2]);
            statement.executeUpdate(insertStr);
            userAfterDepositSum = (Double.parseDouble(stringFromClient[2]) + (Double.parseDouble(stringFromClient[2]) * percent / 100));

            if (stringFromClient.length == 5)
                saveToFile("\nВы оформили депозит " + stringFromClient[1] + " на " + stringFromClient[3] + " месяцев " +
                        "сумой " + stringFromClient[2] + "$",soos);

            soos.writeObject("");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void getTypeDeposit(ObjectOutputStream soos) {
        ArrayList<String> list = new ArrayList<>();
        try {
            ResultSet tmp = getTypeDepositTable();
            while (tmp.next()) {
                list.add(tmp.getString("name") + " " + tmp.getString("percent") + " " +
                        tmp.getString("term"));
            }
            tmp.close();
            soos.writeObject(list);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile(String string,ObjectOutputStream soos){
        try (FileOutputStream fos = new FileOutputStream("otchyot.txt", true)) {
            byte[] buffer = string.getBytes();
            fos.write(buffer, 0, buffer.length);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        int countId = 1;
        try {
            ResultSet saveFile = statement.executeQuery("SELECT * FROM save_to_file ");
            while (saveFile.next()) countId++;
            saveFile.close();
            userId = countId;
            insertStr = "INSERT INTO save_to_file VALUES ("
                    + quotate(Integer.toString(countId)) + ","
                    + quotate(Integer.toString(userId)) + ","
                    + quotate(string) + ","
                    + quotate(getDate()) + ")";
            statement.executeUpdate(insertStr);
            soos.writeObject("SUCCESS");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void makeTransferMoney(String[] action, ObjectOutputStream soos){
        double sum = Double.parseDouble(action[2]);
        String numberAcc = action[1];
        try {
            String receiver_num = getReceiverUserId(numberAcc);
            int idNewCountry = getIdNewCountryTransferMoney();
            double otherCurrentSum = 0;
            ResultSet tmp = getUserFromTable();


            if (!receiver_num.equals("NOT FOUND"))
                while (tmp.next()) {
                    if (tmp.getString("id").equals(receiver_num))
                        otherCurrentSum = Integer.parseInt(tmp.getString("money"));
                }

            insertStr = "INSERT INTO transfer_money VALUES ("
                    + quotate(Integer.toString(idNewCountry)) + ","
                    + quotate(Integer.toString(userId)) + ","
                    + quotate(numberAcc) + ","
                    + quotate(Double.toString(sum)) + ","
                    + quotate(getDate()) + ")";

            statement.executeUpdate(insertStr);

            insertStr = "UPDATE `users` SET "
                    + " money = " + quotate(Double.toString(userCurrentSum - sum))
                    + " WHERE  (" + "id = " + quotate(Integer.toString(userId)) + ")";
            statement.executeUpdate(insertStr);
            userCurrentSum -= sum;
            if (!receiver_num.equals("NOT FOUND")) {
                insertStr = "UPDATE `users` SET "
                        + " money = " + quotate(Double.toString(sum + otherCurrentSum))
                        + " WHERE  (" + "id = " + quotate(receiver_num) + ")";
                statement.executeUpdate(insertStr);
            }
            if (action.length == 4) saveToFile("\nВы перевели на счёт " + numberAcc + " " + sum + "$ " + getDate(),soos);
            soos.writeObject(receiver_num);
        }
        catch (SQLException | IOException e){
            e.printStackTrace();
        }
    }

    private int getIdNewCountryTransferMoney(){
        int count = 1;
        try {
            ResultSet transferMoney = getTransferMoneyFromTable();
            while (transferMoney.next()) count++;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return count;
    }

    private String getReceiverUserId(String acc){
        String answer = "";
        try {
            ResultSet users = getUserFromTable();
            while (users.next()) {
                if (users.getString("account_num").equals(acc)) {
                    answer = users.getString("id");
                }
            }
            users.close();
            if (answer.equals("")) answer = "NOT FOUND";
        }catch (SQLException e){
            e.printStackTrace();
        }
        return answer;
    }

    private ResultSet getTypeDepositTable() throws SQLException {
        return statement.executeQuery(Query.SHOW_TYPE_DEPOSIT);
    }

    private ResultSet getDepositFromTable() throws SQLException {
        return statement.executeQuery(Query.SHOW_ALL_DEPOSIT);
    }

    private ResultSet getTransferMoneyFromTable() throws SQLException {
        return statement.executeQuery(Query.SHOW_TRANSFER_MONEY);
    }

    private ResultSet getUserFromTable() throws SQLException {
        return statement.executeQuery(Query.SHOW_ALL_USERS);
    }

    private static String quotate(String content) {
        return "'" + content + "'";
    }

    private static String getDate() {
        return new SimpleDateFormat("dd.MM.yyyy-HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}

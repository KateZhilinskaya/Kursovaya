import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private DatabaseWork myDB;
    private Socket clientSocket;
    private ObjectInputStream sois;
    private ObjectOutputStream soos;
    private String[] tmp;
    private String action;
    private int countClients = 0;

    public Server(Socket socket) throws IOException {
        clientSocket = socket;
        sois = new ObjectInputStream(clientSocket.getInputStream());
        soos = new ObjectOutputStream(clientSocket.getOutputStream());

        start();
    }

    public void run() {
        myDB = new DatabaseWork();
        try {
            System.out.println("Соединение установлено..");

            while (true) {
                action = (String) sois.readObject();
                tmp = action.split(" ");

                switch (tmp[0]) {
                    case "signUp":
                        myDB.signUp(tmp,soos);
                        break;
                    case "getUserAccNumber":
                        myDB.getUserAccNumber(soos);
                        break;
                    case "getUserMoney":
                        myDB.getUserMoney(soos);
                        break;
                    case "getUserMoneyWithDeposit":
                        myDB.getUserMoneyWithDeposit(soos);
                        break;
                    case "makeTransferMoney":
                        myDB.makeTransferMoney(tmp,soos);
                        break;
                    case "getTypeDeposit":
                        myDB.getTypeDeposit(soos);
                        break;
                    case "makeDeposit":
                        myDB.makeDeposit(tmp,soos);
                        break;
                    case "showAllTransferMoney":
                        myDB.showAllTransferMoney(tmp,soos);
                        break;
                    case "showTransferMoney":
                        myDB.showTransferMoney(tmp,soos);
                        break;
                    case "registration":
                        myDB.registration(tmp,soos);
                        break;
                    case "deleteUser":
                        myDB.deleteUser(tmp, soos);
                        break;
                    case "editUser":
                        myDB.editUser(tmp, soos);
                        break;
                    case "searchUser":
                        myDB.searchUser(tmp, soos);
                        break;
                    case "showMyDeposit":
                        myDB.showMyDeposit(tmp,soos);
                        break;
                    case "showAllDeposit":
                        myDB.showAllDeposit(tmp,soos);
                        break;
                    case "showAllUsers":
                        myDB.showAllUsers(tmp,soos);
                        break;
                    case "showAllStatisticsDeposit":
                        myDB.showAllStatisticsDeposit(tmp,soos);
                        break;
                    default:
                        soos.writeObject("false");
                        break;
                }
            }

        } catch (Exception e) {
            System.err.println("Клиент отключился.");
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                System.err.println("Сокет не закрыт");
            }
        }
    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(2528);
        System.out.println("Сервер запущен...");

        try {
            while (true) {
                Socket newSocket = serverSocket.accept();
                System.out.println("Новый клиент подключился");

                try {
                    new Server(newSocket);
                } catch (Exception e) {
                    newSocket.close();
                }
            }

        } finally {
            serverSocket.close();
        }
    }
}

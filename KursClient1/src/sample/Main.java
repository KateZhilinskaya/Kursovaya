package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Main extends Application {

    static private ObjectOutputStream coos;
    static private ObjectInputStream cois;
    static private Socket clientSocket;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample/views/start.fxml"));
        primaryStage.setTitle("Вход");
        primaryStage.setResizable(false);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        root.requestFocus();
    }


    public static void main(String[] args) {
        try {
            System.out.println("Соединение с сервером..");
            clientSocket = new Socket("127.0.0.1", 2528);
            System.out.println("Соединение установлено....");
            coos = new ObjectOutputStream(clientSocket.getOutputStream());
            cois = new ObjectInputStream(clientSocket.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
        launch(args);
    }

    public void openNewWindow(Stage stage,boolean isCurrentClose,String path,String title,boolean isModal){
        try {
            if(isCurrentClose) stage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Parent root1 = (Parent) fxmlLoader.load();
            stage = new Stage();
            if(isModal) stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle(title);
            stage.setScene(new Scene(root1));
            stage.show();
            root1.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String sendToServerString(String sendMessages) {
        String receive = "";
        try{
            coos.writeObject(sendMessages);
            receive = (String)cois.readObject();
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        return receive;
    }

    public static Object getObjectFromServer(String sendMessage){
        Object obj = null;
        try{
            coos.writeObject(sendMessage);
            obj = cois.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return obj;
    }

}

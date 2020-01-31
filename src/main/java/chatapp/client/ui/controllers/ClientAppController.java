package chatapp.client.ui.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import chatapp.client.Client;
import chatapp.client.ReceiveMessageListener;
import chatapp.message.Message;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ClientAppController implements Initializable, ReceiveMessageListener {

    @FXML
    AnchorPane rootWindow;

    // AnchorPane1
    @FXML
    AnchorPane anchorPane1;
    @FXML
    Button btnStart;
    @FXML
    TextField textUsername;

    // AnchorPane2
    @FXML
    AnchorPane anchorPane2;
    @FXML
    Button btnStartChat;
    @FXML
    TextField textUsernameToChat;

    private Client client;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        client = new Client("localhost", 3050);
        client.attachReceiveMessageListener(this);
    }

    public void startClientHandler(MouseEvent event) {
        final String username = textUsername.getText();
        client.setUsername(username);
        client.start();
    }

    public void startChatHandler(MouseEvent event) {

        String userString = textUsernameToChat.getText();
        StringTokenizer users = new StringTokenizer(userString);
        List<String> chatUsernames = new ArrayList<String>();
        while (users.hasMoreTokens()) {
            chatUsernames.add(users.nextToken());
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/clientChat.fxml"));
            Parent root =(Parent)loader.load();
            ClientChatController controller = loader.getController();
            controller.initReceiverList(chatUsernames, userString);
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        textUsernameToChat.setText("");
    }

 

    @Override
    public void onComplete(Message message) {

        // Check for Hello Message
        if (message.getSender().equals("server") && message.getData().equals("hello")) {
            System.out.println("Handshake Done");
            anchorPane1.setVisible(false);
            anchorPane2.setVisible(true);
        }else{
            
        }
    }
}
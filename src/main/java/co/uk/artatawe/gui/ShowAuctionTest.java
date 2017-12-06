package co.uk.artatawe.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ShowAuctionTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../gui/ShowAuction.fxml"));

        Scene scene = new Scene(root);

        primaryStage.setTitle("Show Auction Test");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);

    }

}
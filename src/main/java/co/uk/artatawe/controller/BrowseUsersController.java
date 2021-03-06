package co.uk.artatawe.controller;

import co.uk.artatawe.database.FavouriteUserDatabaseManager;
import co.uk.artatawe.database.UserDatabaseManager;
import co.uk.artatawe.main.FavouriteUsers;
import co.uk.artatawe.main.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Handles browse user fxml file.
 * Displays all users and the ability to un/favourite them.
 *
 * @author 914937 - Plamena
 * @author 908928 - Susmita
 */
public class BrowseUsersController implements Initializable {

    private final int IMAGE_WIDTH = 150;
    private final int GAP = 10;
    private String username; //logged in user.

    @FXML
    private TilePane tilePane;

    @FXML
    private ScrollPane scrollPane;

    /**
     * Empty constructor
     */
    public BrowseUsersController() {

    }

    /**
     * Sets username.
     *
     * @param username username of logged in user.
     */
    public BrowseUsersController(String username) {
        this.username = username;
    }

    /**
     * Gets all favourite users of logged in user.
     *
     * @return arraylist of favourited users of logged in user.
     */
    public ArrayList<User> getAllFavouriteUsers() {

        FavouriteUserDatabaseManager favouriteUserDatabaseManager = new FavouriteUserDatabaseManager();
        String sqlSelect = "select * from favouriteuser;";
        ArrayList<User> favs = new ArrayList<>();

        for (FavouriteUsers favouriteUsers : favouriteUserDatabaseManager.getFavouriteUsers(sqlSelect)) {
            if (favouriteUsers.getUser1().getUserName().equals(this.username)) {
                favs.add(favouriteUsers.getUser2());
            }
        }

        return favs;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.username = username;
        getUserProfiles();
    }

    /**
     * Checks to see if user is a favourite.
     *
     * @param user user
     * @return true or false.
     */
    private boolean isFavouriteOf(User user) {
        for (User fav : getAllFavouriteUsers()) {
            if (fav.getUserName().equals(user.getUserName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Displays all users' profile image and their username.
     * Also displays heart button to favourite/unfavourite users.
     */
    public void getUserProfiles() {
        Stage stage = new Stage();

        UserDatabaseManager userDatabaseManager = new UserDatabaseManager();

        String sqlSelect = "Select * from user where username <> '" + this.username + "';";

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToHeight(true);
        scrollPane.setContent(tilePane);
        tilePane.setHgap(GAP);
        tilePane.setVgap(GAP);

        //Gets all usernames and profile images
        for (User user : userDatabaseManager.getAllUsers(sqlSelect)) {

            ImageView imageView = new ImageView();
            user.getProfileImage().displayProfileImage(imageView);
            imageView.setFitWidth(IMAGE_WIDTH);
            imageView.setFitHeight(stage.getHeight() - 10);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);

            Button heartButton = new Button();

            Image heartIcon = new Image(("co/uk/artatawe/gui/Icons/icons8-heart-48.png"));

            if (isFavouriteOf(user)) {
                heartIcon = new Image("co/uk/artatawe/gui/Icons/icons8-love-50.png");
            }

            heartButton.setGraphic(new ImageView(heartIcon));
            heartButton.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-border-color: transparent");

            VBox vBox = new VBox();

            vBox.getChildren().addAll(imageView);
            heartButton.setText(user.getUserName());

            vBox.getChildren().add(heartButton);
            tilePane.getChildren().add(vBox); //add image to gridpane.


            //Add event handler.
            //If user presses heart, check if already favourite, if not remove from favourite.
            //Write changes to database.
            heartButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {

                    FavouriteUserDatabaseManager favouriteUserDatabaseManager = new FavouriteUserDatabaseManager();

                    if (isFavouriteOf(user)) {
                        String sqlDelete = "delete from favouriteuser where username1 = '" + username
                                + "' and username2 = '" + user.getUserName() + "';";

                        favouriteUserDatabaseManager.executeStatement(sqlDelete);

                        Image emptyHeart = new Image(("co/uk/artatawe/gui/Icons/icons8-heart-48.png"));
                        heartButton.setGraphic(new ImageView(emptyHeart));
                    } else {
                        String sqlInsert = "insert into favouriteuser(username1,username2) values ('" +
                                username + "', '" + user.getUserName() + "');";

                        favouriteUserDatabaseManager.executeStatement(sqlInsert);

                        Image fullHeart = new Image(("co/uk/artatawe/gui/Icons/icons8-love-50.png"));
                        heartButton.setGraphic(new ImageView(fullHeart));
                    }
                }
            });
        }
    }


    /**
     * Gets logged in user's username.
     *
     * @return username of logged in user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets logged in user's username.
     *
     * @param username username of logged in user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

}


package co.uk.artatawe.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import co.uk.artatawe.artwork.Artwork;
import co.uk.artatawe.database.ArtworkDatabaseManager;
import co.uk.artatawe.sample.Main;

/**
 * Controller class for browse auction.
 *
 * @author 908928
 * @author Plamena Tseneva
 */
public class BrowseAuctionController implements Initializable {

    private String username; //logged in user.
    private final int WIDTH = 800; //size of window.
    private final int HEIGHT = 600; //size of window.
    private Stage stage = new Stage();


    @FXML
    private TilePane artworkTilePane;

    @FXML
    private ScrollPane artworkScrollPane;

    @FXML
    private ImageView imv;


    /**
     * Empty constructor.
     */
    public BrowseAuctionController() {
    }

    /**
     * Sets username.
     * @param username username of logged in user.
     */
    public BrowseAuctionController(String username) {
        this.username = username;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        getImages();
    }


    /**
     * Gets all artworks currently in auction. Displays them.
     */
    public void getImages() {

        ArtworkDatabaseManager artworkDatabaseManager = new ArtworkDatabaseManager();

        String sqlSelect = "Select * from artwork, auction where auction.auctionid = artwork.artworkid and auctioncomp = 0;";

        ArrayList<Artwork> artworkArrayList = artworkDatabaseManager.getAllArtworks(sqlSelect); //get ongoing artworks.

        ArrayList<String> artworkPhoto = new ArrayList<>();

        Image[] images = new Image[artworkArrayList.size()];
        ImageView[] imageViews = new ImageView[artworkArrayList.size()];
        VBox[] vBoxes = new VBox[artworkArrayList.size()]; //vboxs to add in grid pane.


        //Get location of artwork photos.
        for (Artwork artwork : artworkArrayList) {
            artworkPhoto.add(artwork.getPhoto());
        }
        Rectangle2D viewportRect = new Rectangle2D(40, 35, 110, 110);

        String[] imageLocation = artworkPhoto.toArray(new String[artworkArrayList.size()]); //convert array list to array.

        for (int i = 0; i < imageLocation.length; i++) {
            images[i] = new Image(imageLocation[i], 200, 0, true, true); //get image.
            imageViews[i] = new ImageView(images[i]); //add image to image view.
            imageViews[i].setFitWidth(150);
            imageViews[i].setFitHeight(stage.getHeight() - 10);
            imageViews[i].setPreserveRatio(true);
            imageViews[i].setSmooth(true);
            imageViews[i].setCache(true);
            imageViews[i].setViewport(viewportRect);
            vBoxes[i] = new VBox();
            vBoxes[i].getChildren().add(imageViews[i]); //add vbox inside gridpane.
            artworkTilePane.getChildren().add(vBoxes[i]); //add image to gridpane.

        }

    }


    /**
     * Displays profile page when clicked.
     * @param event event.
     */
    @FXML
    void handleProfileAction(ActionEvent event) {
        Parent root;
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("co/uk/artatawe/gui/ProfilePage.fxml"));

            //manually set controller.
            ProfilePageController profilePageController = new ProfilePageController();
            profilePageController.setUsername(this.username);
            fxmlLoader.setController(profilePageController);

            root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("My profile");
            stage.setScene(new Scene(root, WIDTH, HEIGHT));

            profilePageController.setUsername(this.username); //parse username.

            stage.show(); //display profile page.

            //hides current window.
            ((Node) (event.getSource())).getScene().getWindow().hide();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Displays create auction when clicked.
     * @param event event.
     */
    @FXML
    void handleAuctionAction(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("co/uk/artatawe/gui/CreateAuction.fxml"));
            root = fxmlLoader.load();

            stage.setTitle("Create new auction");
            stage.setScene(new Scene(root, WIDTH, HEIGHT));

            CreateAuctionController createAuctionController = fxmlLoader.getController();

            createAuctionController.changeSellerUsername(this.username);

            stage.show(); //display create auctions.

            //hides current window.
            ((Node) (event.getSource())).getScene().getWindow().hide();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

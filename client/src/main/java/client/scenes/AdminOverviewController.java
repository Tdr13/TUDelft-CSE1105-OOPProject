package client.scenes;

import client.MyFXML;
import client.MyModule;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Board;
import commons.Card;
import commons.Listing;
import jakarta.ws.rs.WebApplicationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.inject.Guice.createInjector;

public class AdminOverviewController {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    public Button disconnectButton;
    public Button openButton;
    private ServerUtils server;
    private Scene overview;
    private Stage primaryStage;
    private Map<Button, Board> buttonBoardMap = new HashMap<>();
    private Board selectedBoard;
    @FXML
    Button securityButton;
    @FXML
    private javafx.scene.control.ScrollPane previewPane;
    @FXML
    private VBox boardBox;
    @FXML
    private Button deleteButton;

    /**
     * Constructor which initializes the server.
     *
     * @param server the server instance used for communication
     */

    @Inject
    public AdminOverviewController(ServerUtils server) {
        this.server = server;
    }

    /**
     * Initializes the controller.
     */
    public void initialize() {
        refresh();
        openButton.setVisible(false);
        deleteButton.setVisible(false);
        securityButton.setVisible(false);
    }

    /**
     * refreshes the overview.
     */
    public void refresh() {
        boardBox.getChildren().clear();
        buttonBoardMap = new HashMap<>();
        List<Board> boards = server.getBoardsFromDB();
        for (Board board : boards) {
            Button button = new Button(board.getTitle());
            setupButton(button, board);
        }
    }

    /**
     * Finds a board by a given title.
     *
     * @param keyEvent the event that triggered the method
     */
    public void findByText(KeyEvent keyEvent) {
        search(((javafx.scene.control.TextField) keyEvent.getSource()).getText().trim());
    }

    /**
     * Searches for a board with a given title.
     *
     * @param searchCriteria the title to search for
     */
    private void search(String searchCriteria) {
        boardBox.getChildren().clear();
        buttonBoardMap = new HashMap<>();
        List<Board> boards = server.getBoardsFromDB();
        for (Board board : boards) {
            if (board.getTitle().contains(searchCriteria)) {
                Button button = new Button(board.getTitle());
                setupButton(button, board);
            }
        }
    }

    /**
     * Sets up a button for a board.
     *
     * @param button the button to set up
     * @param board  the board to set up the button for
     */
    private void setupButton(Button button, Board board) {
        button.setOnAction(this::seePreview);
        button.setStyle("-fx-background-color: #FFFFFF;");
        button.setPrefSize(133, 60);
        button.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderWidths.DEFAULT)));
        buttonBoardMap.put(button, board);
        boardBox.getChildren().add(button);
    }

    /**
     * Shows a preview of the selected board.
     *
     * @param actionEvent the event that triggered the method
     */
    private void seePreview(ActionEvent actionEvent) {
        var root = FXML.load(BoardOverviewController.class, "client", "scenes", "BoardOverview.fxml");
        root.getKey().setBoard(buttonBoardMap.get(actionEvent.getSource()));
        root.getKey().setAdminControl(true);
        root.getKey().refresh();
        SubScene subScene = new SubScene(root.getValue(), 635, 427);
        double scaleFactor = Math.min(350 / subScene.getWidth(), 260 / subScene.getHeight());
        Scale scale = new Scale(scaleFactor, scaleFactor);
        subScene.getTransforms().clear();
        subScene.getTransforms().add(scale);
        previewPane.setContent(subScene);
        deleteButton.setVisible(true);
        securityButton.setVisible(true);
        openButton.setVisible(true);
        selectedBoard = buttonBoardMap.get(actionEvent.getSource());
    }

    /**
     * Deletes the selected board from the database.
     * If a user is using the board, the user will be shown a message that the board has been deleted.
     *
     * @param actionEvent the event that triggered the method
     */
    public void delete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete board");
        alert.setHeaderText("Are you sure you want to delete this board?");
        alert.setContentText("This action cannot be undone.\n Board name: " + selectedBoard.getTitle());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            for (Listing list : selectedBoard.getLists()) {
                for (int i = 0; i < list.getCards().size(); i++) {
                    Card card = list.getCards().get(i);
                    try {
                        server.deleteCard(card.getCardId(), true);
                    } catch (WebApplicationException e) {
                        var alertError = new Alert(Alert.AlertType.ERROR);
                        alertError.initModality(Modality.APPLICATION_MODAL);
                        alertError.setContentText(e.getMessage());
                        alertError.showAndWait();
                        return;
                    }
                }

                try {
                    server.deleteList(list.getListId());
                } catch (WebApplicationException e) {
                    var alertError = new Alert(Alert.AlertType.ERROR);
                    alertError.initModality(Modality.APPLICATION_MODAL);
                    alertError.setContentText(e.getMessage());
                    alertError.showAndWait();
                }
            }

            try {
                SubScene subScene = (SubScene) previewPane.getContent();
                subScene.setRoot(new Label("When selecting a board, a preview will be shown here."));
                server.deleteBoard(selectedBoard.getBoardId());
            } catch (WebApplicationException e) {
                var alertError = new Alert(Alert.AlertType.ERROR);
                alertError.initModality(Modality.APPLICATION_MODAL);
                alertError.setContentText(e.getMessage());
                alertError.showAndWait();
            }
            refresh();
        }
    }

    /**
     * Changes the password of the selected board.
     */
    public void changePassword() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Please enter a new password for the board");
        dialog.showAndWait().ifPresent(password -> {
            selectedBoard.setPassword(password);
            selectedBoard = server.addBoard(selectedBoard);
            refresh();
        });
    }

    /**
     * Disconnects the admin.
     * @param actionEvent - the button pressed
     */
    public void switchToHomePageScene(ActionEvent actionEvent) {
        var homePageOverview = FXML.load(HomePageOverviewController.class, "client", "scenes", "HomePageOverview.fxml");
        primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        overview = new Scene(homePageOverview.getValue());
        primaryStage.setScene(overview);
        primaryStage.setTitle("Connection");
    }

    /**
     * Switches to the board so the admin can use the client app.
     * @param actionEvent - the button clicked
     */
    public void switchToBoard(ActionEvent actionEvent) {
        var boardOverview = FXML.load(BoardOverviewController.class, "client", "scenes", "BoardOverview.fxml");
        boardOverview.getKey().setBoard(selectedBoard);
        boardOverview.getKey().setHasAccess(true);
        boardOverview.getKey().setIsAdmin(true);
        boardOverview.getKey().refresh();
        primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        overview = new Scene(boardOverview.getValue());
        primaryStage.setScene(overview);
    }

}

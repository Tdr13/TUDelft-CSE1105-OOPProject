package client.scenes;

import client.MyFXML;
import client.MyModule;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.messaging.simp.stomp.StompSession;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

import static com.google.inject.Guice.createInjector;

public class HomePageOverviewController {
    private Stage primaryStage;
    private Scene overview;
    private String adminPassword;
    private ServerUtils server;
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    @javafx.fxml.FXML
    private TextField username;
    @javafx.fxml.FXML
    private TextField serverAddress;
    @javafx.fxml.FXML
    private Button connect;

    private BoardOverviewController boardOverviewController;
    private InitialOverviewController initialOverviewController;

    /**
     * Constructor which initialize the server.
     *
     * @param server the server instance used for communication
     * @param boardOverviewController instance for board overview controller
     * @param initialOverviewController instance for the initial controller
     */
    @Inject
    public HomePageOverviewController (ServerUtils server, BoardOverviewController boardOverviewController, InitialOverviewController initialOverviewController){
        this.server = server;
        this.initialOverviewController = initialOverviewController;
        this.boardOverviewController = boardOverviewController;
    }


    /**
     * Initializes the controller and makes Enter input the data.
     */
    public void initialize() {
        username.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                connect.fire();
            }
        });
        serverAddress.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                connect.fire();
            }
        });
        setAdminPassword();
    }

    /**
     * Fetches and sets the admin password from the server files.
     * <p>NOTE: the pathing of the files changes depending on where the project is run from.</p>
     */
    private void setAdminPassword() {
        adminPassword = "";
        try {
            File test = new File("build.gradle");
            File file = new File("server/src/adminPass.txt");
            if (test.getAbsolutePath().contains("client")) {
                file = new File("../server/src/adminPass.txt");
            }
            Scanner scanner = new Scanner(file);
            adminPassword = scanner.nextLine().trim();
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function that goes from the homepage back to the board.
     *
     * @param actionEvent the action event on the button
     * @throws IOException the exception which might be caused
     */
    public void switchToBoard(javafx.event.ActionEvent actionEvent) throws IOException {

        // before switching to the board scene, we need to validate the URL

        AnchorPane anchorPane = (AnchorPane) ((Button)actionEvent.getSource()).getParent();
        String userUrl = serverAddress.getText().trim();
        String http  = "http://";

        if (userUrl.contains(http))
            userUrl = userUrl.substring(7);

        if (checkConnection(userUrl) && username.getText().trim().length() > 0) {

            StompSession session = server.startWebSockets(userUrl);

            String fileName = "user_files/" + username.getText().trim() + userUrl.substring(userUrl.lastIndexOf(":") + 1) + ".txt";
            File test = new File("build.gradle");
            if (!test.getAbsolutePath().contains("client"))
                fileName = "client/" + fileName;
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }

            //initializeWebSockets(session);

            var initialOverview = FXML.load(InitialOverviewController.class, "client", "scenes", "InitialOverview.fxml");
            initialOverview.getKey().setFileName(fileName);
            initialOverview.getKey().refresh();
            primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            primaryStage.setResizable(false);
            overview = new Scene(initialOverview.getValue());
            primaryStage.setScene(overview);
            primaryStage.show();
        } else {
            // put a message in the text area
            if (username.getText().trim().length() > 0) {
                serverAddress.setText("Invalid url");
                serverAddress.setStyle("-fx-border-color: red; -fx-border-radius: 5px;");
            }
            else {
                username.setText("Invalid username");
                username.setStyle("-fx-border-color: red; -fx-border-radius: 5px;");
            }
        }
    }

    /**
     * This method checks if the url entered by the user is a valid one.
     *
     * @param userUrl the string representing the url
     //* @param port the port of the application
     * @return true if the url is valid, or false otherwise
     */
    public boolean checkConnection(String userUrl){
        try {
            server.checkServer(userUrl);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method sets the admin password.
     *
     * @param adminPassword the password
     */
    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    /**
     * This method opens the admin login dialog.
     *
     * @param actionEvent the action event on the button
     */
    public void adminLogin(ActionEvent actionEvent) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Admin login");
        dialog.setHeaderText("Please enter the necessary information:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField serverField = new TextField();
        serverField.setPromptText("Server");
        TextField passwordField = new TextField();
        passwordField.setPromptText("Admin Password");

        grid.add(new Label("Server:"), 0, 0);
        grid.add(serverField, 1, 0);
        grid.add(new Label("Admin Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String server = serverField.getText();
            String password = passwordField.getText();

            String http  = "http://";

            if (server.contains(http))
                server = server.substring(7);
            if (password.equals(adminPassword) && checkConnection(server)) {
                var adminOverview = FXML.load(AdminOverviewController.class, "client", "scenes", "AdminOverview.fxml");
                primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                overview = new Scene(adminOverview.getValue());
                primaryStage.setScene(overview);
                primaryStage.show();
            }
        }

    }

    public void connectHover(){

        connect.setStyle("-fx-background-color: green"
                + "; -fx-text-fill: #ffffff; -fx-border-color: #ffffff;" +
                "-fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;" +
                "-fx-background-insets: 2px");
    }

    public void connectNormal( ){
        connect.setStyle("-fx-background-color: #ffffff"
                + "; -fx-text-fill: green; -fx-border-color: green;"+
                "-fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;"+
                "-fx-background-insets: 2px");
    }


}

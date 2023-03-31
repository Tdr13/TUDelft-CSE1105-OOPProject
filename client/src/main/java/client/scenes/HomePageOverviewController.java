package client.scenes;

import client.MyFXML;
import client.MyModule;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.messaging.simp.stomp.StompSession;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

import static com.google.inject.Guice.createInjector;

public class HomePageOverviewController {
    private Stage primaryStage;
    private Scene overview;

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
            userUrl = userUrl.substring(6);


       // String userUrl = "http://localhost:" + userPort;
//        if (!userUrl.startsWith("http://")) {
//            userUrl = "http://" + userUrl;
//        }

        if (checkConnection(userUrl) && username.getText().trim().length() > 0) {

            StompSession session = server.startWebSockets(userUrl);

            String fileName = "user_files/"+username.getText().trim()+userUrl.substring(userUrl.lastIndexOf(":")+1)+".txt";
            File test = new File("build.gradle");
            if(!test.getAbsolutePath().contains("client"))
                fileName = "client/" + fileName;
            File file = new File(fileName);
            if(!file.exists()){
                file.createNewFile();
            }

            //initializeWebSockets(session);

            var initialOverview = FXML.load(InitialOverviewController.class, "client", "scenes", "InitialOverview.fxml");
            initialOverview.getKey().setFileName(fileName);
            initialOverview.getKey().refresh();
            primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            overview = new Scene(initialOverview.getValue());
            primaryStage.setScene(overview);
            primaryStage.show();
        } else {
            // put a message in the text area
            if(username.getText().trim().length() > 0)
                serverAddress.setText("Invalid url");
            else
                username.setText("Invalid username");
        }
    }

//    public void initializeWebSockets(StompSession session)
//    {
//        boardOverviewController.init(session);
//        initialOverviewController.init(session);
//    }
    /**
     * This method checks if the url entered by the user is a valid one.
     * @param userUrl the string representing the url
     //* @param port the port of the application
     * @return true if the url is valid, or false otherwise
     */
    public boolean checkConnection(String userUrl){
        try {
            server.checkServer(userUrl);
            return true;
        } catch(Exception e){
            return false;
        }
    }
}

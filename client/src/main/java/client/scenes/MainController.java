package client.scenes;


import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;


public class MainController {

    private Stage primaryStage;
    private HomePageOverviewController homePageOverviewController;
    private Scene homePageOverview;

    private BoardOverviewController boardOverviewController;
    private Scene boardOverview;

    private CardOverviewController cardOverviewController;
    private Scene cardOverview;


    /**
     * Initializes the application.
     *
     * @param primaryStage     The main stage of the application
     * @param homePageOverview Homepage
//     * @param boardOverview    Board overview
//     * @param cardOverview     Card overview
//     * @param initialOverview Initial overview
     */
    public void initialize(Stage primaryStage,
                           Pair<HomePageOverviewController, Parent> homePageOverview) {
        this.primaryStage = primaryStage;

        this.homePageOverviewController = homePageOverview.getKey();
        this.homePageOverview = new Scene(homePageOverview.getValue());

//        this.boardOverviewController = boardOverview.getKey();
//        this.boardOverview = new Scene(boardOverview.getValue());
//
//        this.cardOverviewController = cardOverview.getKey();
//        this.cardOverview = new Scene(cardOverview.getValue());

        showOverview();
        primaryStage.show();
    }

    public void showBoardOverview() {

        primaryStage.setTitle("Talio");
        primaryStage.setScene(boardOverview);
    }

    public void showOverview() {
        primaryStage.setTitle("Talio");
        primaryStage.setResizable(false);
        primaryStage.setScene(homePageOverview);

        // to be implemented: refresh
        //overviewController.refresh();
    }

    public void showCardOverview() {
        primaryStage.setTitle("Talio");
        primaryStage.setScene(cardOverview);
    }



}
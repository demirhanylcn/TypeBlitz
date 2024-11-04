import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Run extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MonkeyType");

        Scene scene = new Scene(Utilities.createUtilities(primaryStage), 1200, 800);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, Utilities::handleKeyPressed);


        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.getScene().getStylesheets().add("styles.css");
        primaryStage.show();

        Utilities.gameFinishedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                primaryStage.hide();
                GraphCreator.showTheGraph(primaryStage);
            }
        });
    }

}


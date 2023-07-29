package filehandler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;

public class FileHandlerStartup extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FileHandlerStartup.class.getResource("startPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 984, 620);

        stage.initStyle(StageStyle.UNDECORATED);
        Pane headerPane = (Pane) scene.lookup("#headerPane");
        windowDraggingFunctionality(headerPane,stage);

        stage.setTitle("FileHandler");

        String cssPath = getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(cssPath);


        Parameters params = getParameters();
        List<String> rawParams = params.getRaw();
        if (rawParams.size() > 0) {
            String folderPath = rawParams.get(0);
            MainController mainController = fxmlLoader.getController();
            mainController.setPathTextField(folderPath);
        }

        stage.setScene(scene);
        stage.show();

    }

    private void windowDraggingFunctionality(Pane headerPane, Stage stage) {
        FileHandlerStartup fileHandlerStartup = new FileHandlerStartup();

        headerPane.setOnMousePressed(event ->{
            xOffset= event.getSceneX();
            yOffset=event.getSceneY();
        });
        headerPane.setOnMouseDragged(event ->{
            stage.setX(event.getScreenX() -xOffset);
            stage.setY(event.getScreenY() -yOffset);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
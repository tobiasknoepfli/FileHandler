package filehandler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class FileHandlerStartup extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FileHandlerStartup.class.getResource("startPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 835, 600);
        stage.setTitle("FileHandler");

        Parameters params = getParameters();
        if (params.getRaw().size() > 0) {
            String folderPath = params.getRaw().get(0);
            MainController mainController = fxmlLoader.getController();
            mainController.setPathTextField(folderPath);
        }

        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
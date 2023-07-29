package filehandler;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public static TableColumn<File, String> nameColumn;
    public static TableColumn<File, String> pathColumn;
    public static TableColumn<File, String> typeColumn = new TableColumn<>("Type"); // New column for file type
    public static TableColumn<File, String> moveColumn;

    public static TableColumn<File, String> copyColumn;


    public static void populateContentsTable(
            TableView<File> contentsTable,
            TableColumn<File, String> nameColumn,
            TableColumn<File, String> pathColumn,
            TableColumn<File, String> typeColumn,
            File directory
    ) {
        List<File> fileList = getAllFilesInDirectory(directory);
        ObservableList<File> files = FXCollections.observableArrayList(fileList);

        // Set the cell value factories for the nameColumn and pathColumn
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("absolutePath"));

        // Set the cell factory for the typeColumn to handle FileType.getFileExtension(file)
        typeColumn.setCellValueFactory(param -> {
            File file = param.getValue();
            String fileType = FileType.getFileExtension(file);
            return new SimpleStringProperty(fileType);
        });

        contentsTable.setItems(files);
        contentsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        contentsTable.setItems(files);
    }

    private static List<File> getAllFilesInDirectory(File directory) {
        List<File> fileList = new ArrayList<>();

        if (directory != null && directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        fileList.addAll(getAllFilesInDirectory(file));
                    } else {
                        fileList.add(file);
                    }
                }
            }
        }

        return fileList;
    }
}

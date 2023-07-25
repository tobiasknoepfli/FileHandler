package filehandler;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;


public class MainController {
    @FXML
    private TextField pathTextField;

    @FXML
    private TextField targetTextField;

    @FXML
    private TableView<File> contentsTable;

    @FXML
    private TableColumn<File, String> nameColumn;

    @FXML
    private TableColumn<File, String> pathColumn;

    @FXML
    private TableColumn<File, String> typeColumn;

    @FXML
    private TableView<File> moveTable;

    @FXML
    private TableColumn<File, String> moveColumn;

    @FXML
    private TableView<File> copyTable;

    @FXML
    private TableColumn<File, String> copyColumn;

    @FXML
    private Button OKButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ToggleButton rootShortcutButton;

    @FXML
    private ToggleButton targetShortcutButton;

    @FXML
    private ToggleButton targetFolderButton;

    @FXML
    private CheckBox returnShortcut;

    @FXML
    private void openPathChooser(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            pathTextField.setText(selectedDirectory.getAbsolutePath());

            // Initialize the typeColumn
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("fileType"));

            FileHandler.populateContentsTable(contentsTable, nameColumn, pathColumn, typeColumn, selectedDirectory);
        }
    }

    @FXML
    private void targetPathChooser() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            targetTextField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void moveButtonAction(ActionEvent event) {
        ObservableList<File> selectedFiles = contentsTable.getSelectionModel().getSelectedItems();
        moveColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        moveTable.getItems().addAll(selectedFiles);
        contentsTable.getItems().removeAll(selectedFiles);
        contentsTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void copyButtonAction(ActionEvent event) {
        ObservableList<File> selectedFiles = contentsTable.getSelectionModel().getSelectedItems();
        copyColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        copyTable.getItems().addAll(selectedFiles);
        contentsTable.getItems().removeAll(selectedFiles);
        contentsTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void OKButtonAction(ActionEvent event) {
        String targetPath = targetTextField.getText();
        String sourcePath = pathTextField.getText();

        List<File> moveFiles = moveTable.getItems();
        for (File file : moveFiles) {
            File destination = new File(targetPath, file.getName());
            try {
                Files.move(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<File> copyFiles = copyTable.getItems();
        for (File file : copyFiles) {
            File destination = new File(targetPath, file.getName());
            try {
                Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (rootShortcutButton.isSelected()) {
            String name = showInputDialog("Shortcut Name", "enter the shortcut name","ShortcutInRoot");
            createShortcut(targetPath, sourcePath, name);
            if(returnShortcut.isSelected()){
                createShortcut(sourcePath,targetPath,"return " + name);
            }
        }
        if (targetShortcutButton.isSelected()) {
            String name = showInputDialog("Shortcut Name", "enter the shortcut name","ShortcutInTarget");
            createShortcut(sourcePath,targetPath,name);
            if(returnShortcut.isSelected()){
                createShortcut(targetPath,sourcePath,"return " + name);
            }
        }
        if(targetFolderButton.isSelected()){
            String name = showInputDialog("Folder Name", "enter the folder name","new Folder");
            File newFolder = new File(targetPath, name);
            newFolder.mkdir();

            String nameShortcut = showInputDialog("Shortcut Name", "enter the shortcut name","ShortcutInTargetToFolder");
            createShortcut(targetPath + "/" + name,sourcePath,nameShortcut);
            if(returnShortcut.isSelected()){
                createShortcut(sourcePath,targetPath + "/" + name,"return " + nameShortcut);
            }
        }

        moveTable.getItems().clear();
        copyTable.getItems().clear();

        Platform.exit();
    }

    private void createShortcut(String targetFolder, String sourceFolder, String shortcutName) {
        try {
            ShortcutFactory.createShortcut(targetFolder,sourceFolder + "/" +shortcutName + ".lnk");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private String showInputDialog(String title,String header, String sampleText) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.getEditor().setText(sampleText);

        // Show the input dialog and wait for the user response
        Optional<String> result = dialog.showAndWait();

        return result.orElse(null);
    }
    public void setPathTextField(String folderPath) {
        pathTextField.setText(folderPath);
    }
}
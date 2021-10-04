package NZ251.texteditor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class rtfController implements Initializable {

    @FXML
    private AnchorPane root;

    @FXML
    private MenuItem newB;

    @FXML
    private MenuItem openB;

    @FXML
    private MenuItem saveB;

    @FXML
    private MenuItem saveasB;

    @FXML
    private MenuItem printB;

    @FXML
    private MenuItem o2pdf;

    @FXML
    private MenuItem searchB;

    @FXML
    private MenuItem copyB;

    @FXML
    private MenuItem pasteB;

    @FXML
    private MenuItem cutB;

    @FXML
    private MenuItem restore;

    @FXML
    private MenuItem undo;

    @FXML
    private MenuItem tdB;

    @FXML
    private MenuItem aboutB;

    @FXML
    private WebView text;

    private String chosen="";

    public static String extention = "";

    private String abPath = "";

    private Stage stage;

    public static Font font;
    public static String t = "";
    WebEngine webEngine;

    @FXML
    void aboutF(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.headerTextProperty().set("The statement:Prohibit infringement\n" +
                "Name:         Libin and Clifford\n" +
                "Date：         2021-9-15\n" +
                "Version:      1.0\n");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeCancel);
        alert.showAndWait();
    }

    @FXML
    void copyF(ActionEvent event) {

    }

    @FXML
    void cutF(ActionEvent event) {

    }

    @FXML
    void newF(ActionEvent event) throws IOException {
        Scene scene = new Scene(App.loadFXML("primary"));
        Stage second = new Stage();
        second.setScene(scene);
        second.setTitle("Text Editor");
        second.getIcons().add(new Image("file:src\\main\\resources\\NZ251\\texteditor\\tu.jpg"));
        second.show();
    }

    @FXML
    void openF(ActionEvent event) throws Exception {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("All files (*.*)", "*.*"),
                new FileChooser.ExtensionFilter(".py files (*.py)", "*.py"),
                new FileChooser.ExtensionFilter("*.java files (*.java)", "*.java"),
                new FileChooser.ExtensionFilter("*.cpp files (*.cpp)", "*.cpp")
        );
        File file = chooser.showOpenDialog(null);
        if (file!=null){
            abPath = file.getAbsolutePath();
            extention = FileOp.getFileExtension(file);
            if (extention.equals(".rtf")) {
                rtfArea.rtf2Html(file);
                String hName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".html";
                String url = Objects.requireNonNull(App.class.getResource("src/main/resources/rtftohtml/" + hName)).toExternalForm();
                webEngine.load(url);
            } else  if (extention.equals(".java") || extention.equals(".py") || extention.equals(".cpp")) {
                SecondaryController.t = FileOp.readTXT(file);
                SecondaryController.extention = extention;
                App.setRoot("secondary");
            } else if (extention.equals(".txt")) {
                App.setRoot("primary");
            }
        }
    }

    @FXML
    void pasteF(ActionEvent event) {

    }

    @FXML
    void pdfB(ActionEvent event) {

    }

    @FXML
    void printB(ActionEvent event) {

    }

    @FXML
    void restoreaction(ActionEvent event) {

    }

    @FXML
    void saveF(ActionEvent event) {

    }

    @FXML
    void saveasF(ActionEvent event) {

    }

    @FXML
    void searchF(ActionEvent event) {

    }

    @FXML
    void tdF(ActionEvent event) {

    }

    @FXML
    void undoaction(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        saveB.setDisable(true);
        abPath = System.getProperty("user.dir");
        undo.setDisable(true);
        restore.setDisable(true);
        copyB.setDisable(true);
        pasteB.setDisable(true);
        cutB.setDisable(true);

        webEngine = text.getEngine();
        if (! t.equals("")) {
            System.out.println(t);
            String url = App.class.getResource(t).toExternalForm();
            webEngine.load(url);
        }
    }
}


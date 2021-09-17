package NZ251.texteditor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class PrimaryController implements Initializable {
    @FXML
    private MenuItem newB;

    @FXML
    private MenuItem openB;

    @FXML
    private MenuItem saveB;

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
    private MenuItem tdB;

    @FXML
    private MenuItem aboutB;

    @FXML
    private MenuItem saveasB;

    @FXML
    private TextArea text;

    @FXML
    private ListView<String> listview;
    @FXML
    private AnchorPane root;

    private String chosen="";

    private String extention;

    private String abPath;

    private Stage stage;




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
        chosen = text.getSelectedText();

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();

        clipboardContent.putString(chosen);
        clipboard.setContent(clipboardContent);
    }

    @FXML
    void cutF(ActionEvent event) {
        chosen = text.getSelectedText();
        int a = text.getCaretPosition();
        StringBuilder stringBuilder1 = new StringBuilder(text.getText());
        stringBuilder1.delete(a, a + chosen.length());
        text.setText(stringBuilder1.toString());

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();

        clipboardContent.putString(chosen);
        clipboard.setContent(clipboardContent);
    }

    @FXML
    void pasteF(ActionEvent event) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (!clipboard.getString().equals("")) {
            chosen = clipboard.getString();
        }
        int a = text.getCaretPosition();
        StringBuilder stringBuilder1 = new StringBuilder(text.getText());
        stringBuilder1.insert(a, chosen);
        text.setText(stringBuilder1.toString());
    }

    @FXML
    void cpc(KeyEvent event) {
        String str = text.getText();
        String[] split = str.split("\n");
        int len = split.length + 1;

        ObservableList<String> s = FXCollections.observableArrayList();
        for (int i = 1; i <= len; i++) {
            String c=String.valueOf(i);
            s.add(c);
        }
        listview.setItems(s);

    }
    @FXML
    void apc(MouseEvent event) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        pasteB.setDisable(clipboard.getString().equals("") && chosen.equals(""));
        if (text.getSelectedText().equals("")){
            copyB.setDisable(true);
            cutB.setDisable(true);
        }else {
            copyB.setDisable(false);
            cutB.setDisable(false);
        }
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
    void openF(ActionEvent event) throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("All files (*.*)", "*.*"),
                new FileChooser.ExtensionFilter(".py files (*.py)", "*.py"),
                new FileChooser.ExtensionFilter("*.java files (*.java)", "*.java"),
                new FileChooser.ExtensionFilter("*.cpp files (*.cpp)", "*.cpp")
        );
        File file = chooser.showOpenDialog(null);
        abPath = file.getAbsolutePath();
        extention = FileOp.getFileExtension(file);
        if (extention.equals(".txt")) {
            text.setText(FileOp.readTXT(file));
        }
        saveB.setDisable(false);
        saveasB.setDisable(false);
        o2pdf.setDisable(false);
        printB.setDisable(true);
        stage = getStage();
        stage.setTitle("Text Editor-" + file.getName());
    }

    @FXML
    void pdfB(ActionEvent event) {

    }

    @FXML
    void printB(ActionEvent event) {
        
    }

    @FXML
    void saveF(ActionEvent event) throws IOException {
        File f = new File(abPath);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
        String txt = text.getText();
        bufferedWriter.write(txt);
        bufferedWriter.close();
    }

    @FXML
    void searchF(ActionEvent event) {
        Stage secondStage = new Stage();
        TextField find_tf = new TextField();
        Button find_bu = new Button("Search");
        HBox hbox = new HBox();
        hbox.getChildren().addAll(find_tf, find_bu);
        StackPane secondPane = new StackPane(hbox);
        Scene secondScene = new Scene(secondPane, 250, 50);
        secondStage.getIcons().add(new Image("file:src\\main\\resources\\NZ251\\texteditor\\tu.jpg"));
        secondStage.setScene(secondScene);
        secondStage.show();
        AtomicReference<String> tt = new AtomicReference<>(text.getText());
        find_bu.setOnAction(item -> {
            if (!tt.get().contains(find_tf.getText())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.headerTextProperty().set("String does not exist");
                alert.setTitle("Mistake");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(buttonTypeCancel);
                alert.setWidth(50);
                alert.setHeight(50);
                alert.showAndWait();
            }
            String find_value = find_tf.getText();
            int len = find_value.length();
            int first = tt.get().indexOf(find_value);
            String s = tt.get().replaceFirst(find_value, "" + "*".repeat(len));
            tt.set(s);
            text.selectRange(first, first + len);

        });

    }

    @FXML
    void tdF(ActionEvent event) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s=text.getText()+'\n'+dateFormat.format(date);
        text.setText(s);

    }

    @FXML
    void saveasF(ActionEvent event) throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save file");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("All files (*.*)", "*.*"),
                new FileChooser.ExtensionFilter(".py files (*.py)", "*.py"),
                new FileChooser.ExtensionFilter("*.java files (*.java)", "*.java"),
                new FileChooser.ExtensionFilter("*.cpp files (*.cpp)", "*.cpp")
        );
        File file = chooser.showSaveDialog(null);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        String txt = text.getText();
        bufferedWriter.write(txt);
        bufferedWriter.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        printB.setDisable(true);
        o2pdf.setDisable(true);
        copyB.setDisable(true);
        pasteB.setDisable(true);
        cutB.setDisable(true);
        text.setWrapText(false);
    }

    private Stage getStage() {
        stage = (Stage) root.getScene().getWindow();
        return stage;
    }
}

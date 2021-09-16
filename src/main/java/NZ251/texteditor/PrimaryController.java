package NZ251.texteditor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private String chosen;

    private String extention;

    private String abPath;

    @FXML
    void aboutF(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.headerTextProperty().set("Prohibit infringement\n" +
                "Name:     Libin and Clifford\n" +
                "Date：   2021-9-15\n" +
                "Version:      1.0\n");
        alert.showAndWait();
    }

    @FXML
    void copyF(ActionEvent event) {
        chosen=text.getSelectedText();
    }

    @FXML
    void cutF(ActionEvent event) {
        chosen=text.getSelectedText();
        int a=text.getCaretPosition();
        StringBuffer stringBuilder1 = new StringBuffer(text.getText());
        stringBuilder1.delete(a,a+chosen.length());
        text.setText(stringBuilder1.toString());
    }

    @FXML
    void pasteF(ActionEvent event) {
        int a=text.getCaretPosition();
        StringBuffer stringBuilder1 = new StringBuffer(text.getText());
        stringBuilder1.insert(a,chosen);
        text.setText(stringBuilder1.toString());
    }

    @FXML
    void newF(ActionEvent event) {

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
        TextField find_tf =new TextField();
        Button find_bu = new Button("Search");
        HBox hbox = new HBox();
        hbox.getChildren().addAll(find_tf,find_bu);
        StackPane secondPane = new StackPane(hbox);
        Scene secondScene = new Scene(secondPane, 250, 50);
        secondStage.getIcons().add(new Image("file:src\\main\\resources\\NZ251\\texteditor\\tu.jpg"));
        secondStage.setScene(secondScene);
        secondStage.show();
        AtomicReference<String> tt= new AtomicReference<>(text.getText());
        find_bu.setOnAction(item->{
            String find_value = find_tf.getText();
            int len=find_value.length();
            int first= tt.get().indexOf(find_value);
            StringBuilder a = new StringBuilder(new String());
            a.append("*".repeat(len));
            String s= tt.get().replaceFirst(find_value, a.toString());
            tt.set(s);
            text.selectRange(first,first+len);
        });
    }

    @FXML
    void tdF(ActionEvent event) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        text.setText("Date: " + dateFormat.format(date));

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
        saveasB.setDisable(true);
        saveB.setDisable(true);
        printB.setDisable(true);
        o2pdf.setDisable(true);
        searchB.setDisable(true);
        cutB.setDisable(true);
        pasteB.setDisable(true);
        copyB.setDisable(true);
    }
}

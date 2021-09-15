package NZ251.texteditor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class PrimaryController {

    @FXML
    private MenuItem newF;

    @FXML
    private MenuItem openF;

    @FXML
    private MenuItem saveF;

    @FXML
    private MenuItem pringtB;

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
    private TextArea text;
    private String chosen;
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
        StringBuffer stringBuilder1=new StringBuffer(text.getText());
        stringBuilder1.delete(a,a+chosen.length());
        text.setText(stringBuilder1.toString());
    }

    @FXML
    void pasteF(ActionEvent event) {
        int a=text.getCaretPosition();
        StringBuffer stringBuilder1=new StringBuffer(text.getText());
        stringBuilder1.insert(a,chosen);
        text.setText(stringBuilder1.toString());
    }

    @FXML
    void newF(ActionEvent event) {

    }

    @FXML
    void openF(ActionEvent event) {

    }



    @FXML
    void pdfB(ActionEvent event) {

    }

    @FXML
    void pringtB(ActionEvent event) {

    }

    @FXML
    void saveF(ActionEvent event) {

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
            String a = new String();
            for (int i = 0; i < len; i++) {
                a=a+'*';
            }
            String s= tt.get().replaceFirst(find_value,a);
            tt.set(s);
            text.selectRange(first,first+len);
        });

    }

    @FXML
    void tdF(ActionEvent event) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        text.setText("Date: " + dateFormat.format(date.getTime()));
    }

}

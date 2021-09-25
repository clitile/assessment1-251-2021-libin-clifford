package NZ251.texteditor;

import com.itextpdf.text.DocumentException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static NZ251.texteditor.FileOp.pdf2images;


public class PrimaryController implements Initializable {
    @FXML
    private MenuItem newB;

    @FXML
    private MenuItem openB;

    @FXML
    private MenuItem saveB;

    @FXML
    private MenuItem undo;

    @FXML
    private MenuItem restore;

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
    private AnchorPane root;

    private String chosen="";

    private String extention;

    private String abPath = "";

    private Stage stage;

    public static Font font;
    public static String t = "";

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

    //按键判断没做
    int nowposition=0;
    ArrayList<String> alltxt=new ArrayList<>();
    @FXML
    void spc(KeyEvent event) {
        if (nowposition==0){
            alltxt.add(text.getText());
            nowposition=nowposition+1;
        }
        if (text.getText()!=""){
            if (alltxt.get(nowposition-1).equals(text.getText())==false){
                alltxt.add(text.getText());
                nowposition=nowposition+1;
            }
        }
    }

    @FXML
    void undoaction(ActionEvent actionEvent) {
        nowposition=nowposition-1;
        text.setText(alltxt.get(nowposition));
    }

    @FXML
    void  restoreaction(ActionEvent actionEvent) {
        nowposition=nowposition+1;
        text.setText(alltxt.get(nowposition));
    }

    @FXML
    void apc(MouseEvent event) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (text.getText()!="" && alltxt.get(nowposition-1).equals(text.getText())==false){
            alltxt.add(text.getText());
            nowposition=nowposition+1;
        }
        if (alltxt.size()!=0){
            undo.setDisable(false);
        }
        if (alltxt.size()>nowposition){
            restore.setDisable(false);
        }else{
            restore.setDisable(true);
        }
        if (clipboard.getString() != null) {
            pasteB.setDisable(clipboard.getString().equals("") && chosen.equals(""));
            if (text.getSelectedText().equals("")){
                copyB.setDisable(true);
                cutB.setDisable(true);
            }else {
                copyB.setDisable(false);
                cutB.setDisable(false);
            }
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
        if (file!=null){
            abPath = file.getAbsolutePath();
            extention = FileOp.getFileExtension(file);

            if (extention.equals(".txt")) {
                text.setText(FileOp.readTXT(file));

                saveB.setDisable(false);
                saveasB.setDisable(false);
                o2pdf.setDisable(false);
                printB.setDisable(false);
                stage = getStage();
                stage.setTitle("Text Editor-" + file.getName());

                text.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        System.out.println("changed");
                    }
                });

                font = text.getFont();
                saveB.setDisable(false);
            } else if (extention.equals(".java") || extention.equals(".py") || extention.equals(".cpp")) {
                SecondaryController.t = FileOp.readTXT(file);
                SecondaryController.extention = extention;
                App.setRoot("secondary");
            }
        }
    }

    @FXML
    void pdfB(ActionEvent event) throws  IOException, DocumentException {
        if (!Objects.equals(text.getText(), "")){
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Convert PDF");
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
            );
            File file = chooser.showSaveDialog(null);
            if (file != null) {
                try {
                    FileOp.O2PDF(text.getText(), file, font);
                } catch (Exception e) {
                    file.deleteOnExit();
                }
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.headerTextProperty().set("There is no input");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeCancel);
            alert.showAndWait();
        }
    }

    @FXML
    void printB(ActionEvent event) throws Exception {
        if (!Objects.equals(text.getText(), "")){
            FileOp.O2PDF(text.getText(), new File("src\\main\\java\\NZ251\\texteditor\\a.pdf"), font);
            testPdf2images();
            InputStream inputStream = new FileInputStream("src\\main\\java\\NZ251\\texteditor\\a.png");;
            PrintDemo pd=new PrintDemo();
            pd.printQRCode(inputStream);
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.headerTextProperty().set("There is no input");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeCancel);
            alert.showAndWait();
        }
    }
    @FXML
    void saveF(ActionEvent event) throws IOException,NullPointerException{
        String txt = text.getText();

        if (txt!=null && !abPath.equals("")) {
            File f = new File(abPath);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
            bufferedWriter.write(txt);
            bufferedWriter.close();
        }

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
            if (file != null) {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                String txt = text.getText();
                bufferedWriter.write(txt);
                bufferedWriter.close();
            }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        saveB.setDisable(true);
        abPath = System.getProperty("user.dir");
        undo.setDisable(true);
        restore.setDisable(true);
        copyB.setDisable(true);
        pasteB.setDisable(true);
        cutB.setDisable(true);
        text.setWrapText(false);

        font = text.getFont();
        System.out.println(font);
    }

    private Stage getStage() {
        stage = (Stage) root.getScene().getWindow();
        return stage;
    }

    private void testPdf2images() throws Exception {
        List<byte[]> images = pdf2images(new File("src\\main\\java\\NZ251\\texteditor\\a.pdf"));
        AtomicInteger fileNameIndex = new AtomicInteger(1);
        for (byte[] image : images) {
            new ByteArrayInputStream(image).transferTo(new FileOutputStream("src\\main\\java\\NZ251\\texteditor\\" + "a.png"));
        }
    }
}

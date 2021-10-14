package NZ251.texteditor;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import java.util.concurrent.atomic.AtomicReference;

import static NZ251.texteditor.FileOp.pdf2images;


public class PrimaryController implements Initializable {
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
    private MenuItem copyB;

    @FXML
    private MenuItem pasteB;

    @FXML
    private MenuItem cutB;

    @FXML
    private MenuItem saveasB;

    @FXML
    public  TextArea text;

    @FXML
    private AnchorPane root;

    private String chosen="";

    private String extention;

    private String abPath = "";

    public Stage stage;

    public static Font font;
    public static String t = "";

    @FXML
    // Display basic information
    void aboutF() {
        // Create a new window to display basic information when the event is triggered
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.headerTextProperty().set("The statement:Prohibit infringement\n" +
                                       "Name:         Libin and Clifford\n" +
                                       "Date：         2021-9-15\n" +
                                       "Version:      1.0\n");
        // Contains a close button
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeCancel);
        alert.showAndWait();
    }

    @FXML
    // Copy function
    void copyF() {
        chosen = text.getSelectedText();
        // Copy the content to the clipboard
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(chosen);
        clipboard.setContent(clipboardContent);
    }

    @FXML
    void cutF() {
        // Get the selected text
        chosen = text.getSelectedText();
        int a = text.getCaretPosition();
        StringBuilder stringBuilder1 = new StringBuilder(text.getText());
        stringBuilder1.delete(a, a + chosen.length());
        // Deletes the selection from the text area
        text.setText(stringBuilder1.toString());

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        // Set to clipboard
        clipboardContent.putString(chosen);
        clipboard.setContent(clipboardContent);
    }

    @FXML
    void pasteF() {
        // Get content from the clipboard
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (!clipboard.getString().equals("")) {
            chosen = clipboard.getString();
        }
        int a = text.getCaretPosition();
        StringBuilder stringBuilder1 = new StringBuilder(text.getText());
        stringBuilder1.insert(a, chosen);
        // Copy the obtained text into the text area
        text.setText(stringBuilder1.toString());
    }

    // Determine whether the keystroke event occurred
    int nowposition=0;
    ArrayList<String> alltxt=new ArrayList<>();
    @FXML
    void spc() {
        // Set an initial position, incrementing each time to judge
        if (nowposition==0){
            alltxt.add(text.getText());
            nowposition=nowposition+1;
        }
        if (!Objects.equals(text.getText(), "")){
            // If the text changes, increment 1
            if (!alltxt.get(nowposition - 1).equals(text.getText())){
                alltxt.add(text.getText());
                nowposition=nowposition+1;
            }
        }
    }

    @FXML
    void undoaction() {
        // Returns the last string in the list
        nowposition=nowposition-1;
        text.setText(alltxt.get(nowposition));
    }

    @FXML
    void  restoreaction() {
        nowposition=nowposition+1;
        text.setText(alltxt.get(nowposition));
    }
    // Whether the key is optional. The default value is no
    @FXML
    void apc() {
        // Get the clipboard content
        Clipboard clipboard = Clipboard.getSystemClipboard();
        // The completion condition is + 1
        if (!Objects.equals(text.getText(), "") && !alltxt.get(nowposition - 1).equals(text.getText())){
            alltxt.add(text.getText());
            nowposition=nowposition+1;
        }
        if (alltxt.size()!=0){
            undo.setDisable(false);
        }
        restore.setDisable(alltxt.size() <= nowposition);
        // Settings cannot be selected when there is no content on the clipboard
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
    // Open a new window
    @FXML
    void newF() throws IOException {
        Scene scene = new Scene(App.loadFXML("primary"));
        // Create a new page to open
        Stage second = new Stage();
        second.setScene(scene);
        second.setTitle("Text Editor");
        second.getIcons().add(new Image("file:src\\main\\resources\\NZ251\\texteditor\\tu.jpg"));
        second.show();
    }
    // Open it from the file manager
    @FXML
    void openF() throws Exception {
        FileChooser chooser = new FileChooser();
        // Add an open format
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("All files (*.*)", "*.*"),
                new FileChooser.ExtensionFilter(".py files (*.py)", "*.py"),
                new FileChooser.ExtensionFilter("*.java files (*.java)", "*.java"),
                new FileChooser.ExtensionFilter("*.cpp files (*.cpp)", "*.cpp"),
                new FileChooser.ExtensionFilter("*.rtf files (*.rtf)", "*.rtf")
        );
        File file = chooser.showOpenDialog(null);
        // If the open file is not empty
        if (file!=null){
            // Place the TXT content in the text area
            abPath = file.getAbsolutePath();
            extention = FileOp.getFileExtension(file);
            // When it is a text type
            // when it is a code type
            switch (extention) {// Place the TXT content in the text area
                case ".txt":
                    text.setText(FileOp.readTXT(file));
                    saveB.setDisable(false);
                    saveasB.setDisable(false);
                    o2pdf.setDisable(false);
                    printB.setDisable(false);
                    stage = getStage();
                    stage.setTitle("Text Editor-" + file.getName());
                    text.textProperty().addListener((observableValue, s, t1) -> System.out.println("changed"));
                    font = text.getFont();
                    saveB.setDisable(false);
                    break;
// Open the TXT text in the second control and set the code style
                case ".java":
                case ".py":
                case ".cpp":
                    SecondaryController.t = FileOp.readTXT(file);
                    SecondaryController.extention = extention;
                    App.setRoot("secondary");
                    break;
                case ".rtf":
                    rtfArea.rtf2Html(file);
                    String hName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".html";
                    rtfController.t = "rtftohtml/" + hName;
                    App.setRoot("rtfController");
                    break;
            }
        }
    }
    // When output is PDF
    @FXML
    void pdfB() {

        if (!Objects.equals(text.getText(), "")){
            // Initializes a file selector
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Convert PDF");
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
            );// The selected file is not empty
            File file = chooser.showSaveDialog(null);
            if (file != null) {
                try {
                    FileOp.O2PDF(text.getText(), file, font);
                } catch (Exception e) {
                    file.deleteOnExit();
                }
            }
        } else {
            // Popup window when file is empty
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.headerTextProperty().set("There is no input");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeCancel);
            alert.showAndWait();
        }
    }
    private String pdfpath="src\\main\\java\\NZ251\\texteditor\\a.pdf";
    // Use a printer to print
    @FXML
    void printB() throws Exception {
        if (!Objects.equals(text.getText(), "")){
            // Convert to PDF printing
            FileOp.O2PDF(text.getText(), new File(pdfpath), font);
            testPdf2images();
            InputStream inputStream = new FileInputStream("src\\main\\java\\NZ251\\texteditor\\a.png");
            PrintDemo pd=new PrintDemo();
            // Convert to stream for printing
            pd.printQRCode(inputStream);
        }else {
            // Popup window when file is empty
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.headerTextProperty().set("There is no input");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeCancel);
            alert.showAndWait();
        }
    }
    // Save function
    @FXML
    void saveF() throws IOException,NullPointerException{
        String txt = text.getText();
        if (txt!=null && !abPath.equals("")) {
            File f = new File(abPath);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
            // Save as a new file path
            bufferedWriter.write(txt);
            bufferedWriter.close();
        }

    }
    // Search function
    @FXML
    void searchF() {
        Stage secondStage = new Stage();
        TextField find_tf = new TextField();
        // First pop up a new window for query
        Button find_bu = new Button("Search");
        HBox hbox = new HBox();
        hbox.getChildren().addAll(find_tf, find_bu);
        StackPane secondPane = new StackPane(hbox);
        Scene secondScene = new Scene(secondPane, 250, 50);
        secondStage.getIcons().add(new Image("file:src\\main\\resources\\NZ251\\texteditor\\tu.jpg"));
        secondStage.setScene(secondScene);
        secondStage.show();
        // Add a function to the new window
        AtomicReference<String> tt = new AtomicReference<>(text.getText());
        find_bu.setOnAction(item -> {
            // Find whether to protect the selected content
            if (!tt.get().contains(find_tf.getText())) {
                // If the query does not contain the desired selection, an error window is displayed
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.headerTextProperty().set("String does not exist");
                alert.setTitle("Mistake");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(buttonTypeCancel);
                alert.setWidth(50);
                alert.setHeight(50);
                alert.showAndWait();
            }//The selected content is saved virtually
            String find_value = find_tf.getText();
            int len = find_value.length();
            int first = tt.get().indexOf(find_value);
            String s = tt.get().replaceFirst(find_value, "" + "*".repeat(len));
            tt.set(s);
            text.selectRange(first, first + len);
        });

    }
    // Add the current date and time to the end of the text
    @FXML
    void tdF() {
        Date date = new Date();
        // Set the initial format of the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s=text.getText()+'\n'+dateFormat.format(date);
        text.setText(s);

    }
    // Save the file
    @FXML
    void saveasF() throws IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save file");
        // File selector
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("All files (*.*)", "*.*"),
                new FileChooser.ExtensionFilter(".py files (*.py)", "*.py"),
                new FileChooser.ExtensionFilter("*.java files (*.java)", "*.java"),
                new FileChooser.ExtensionFilter("*.cpp files (*.cpp)", "*.cpp")
        );
        File file = chooser.showSaveDialog(null);
        if (file != null) {
            // Close after writing the file
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            String txt = text.getText();
            bufferedWriter.write(txt);
            bufferedWriter.close();
        }
    }
    // Window initialization
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Make the buttons below optional
        saveB.setDisable(true);
        abPath = System.getProperty("user.dir");
        undo.setDisable(true);
        restore.setDisable(true);
        copyB.setDisable(true);
        pasteB.setDisable(true);
        cutB.setDisable(true);
        text.setWrapText(false);

        font = text.getFont();
        text.setFont(Font.font(20));

    }

    // Open a new page
    private Stage getStage() {
        stage = (Stage) root.getScene().getWindow();
        return stage;
    }
    // PDF to images
    private void testPdf2images() throws Exception {
        List<byte[]> images = pdf2images(new File(pdfpath));
        for (byte[] image : images) {
            // Use bitstream mode conversion
            new ByteArrayInputStream(image).transferTo(new FileOutputStream("src\\main\\java\\NZ251\\texteditor\\" + "a.png"));
        }
    }
    // Initial file configuration
    public void settings() {
        // Pop up a window selection
        ChoiceBox fsize = new ChoiceBox();
        TextField path = new TextField();

        ReadYAML readYAML = new ReadYAML("src/main/resources/conf/key.yaml");
        // Read from the YAMl file
        fsize.setItems(FXCollections.observableArrayList(
                15,16, 17,18,19,20,21,22,23,24,25)
        );
        fsize.setValue(readYAML.properties.get("size"));
        path.setText("src\\main\\java\\NZ251\\texteditor\\a.pdf");
        // Initialize button
        Label a=new Label("Font size:");
        Label d=new Label("Path:");
        Button bb=new Button("Application");
        VBox hbox = new VBox();
        hbox.getChildren().addAll(a,fsize);
        hbox.getChildren().addAll(d,path);
        hbox.getChildren().addAll(bb);
        Scene scene = new Scene(hbox);
        Stage second = new Stage();
        second.setScene(scene);
        second.setTitle("Setting");
        second.getIcons().add(new Image("file:src\\main\\resources\\NZ251\\texteditor\\tu.jpg"));
        second.show();
        // Button setting event to set font size
        bb.setOnAction(item->{
            int aaa=fsize.getSelectionModel().getSelectedIndex();
            text.setFont(Font.font((Integer) fsize.getItems().get(aaa)));
            pdfpath=path.getText();
            System.out.println(path.getText());
        });
    }
}

package NZ251.texteditor;

import com.itextpdf.text.DocumentException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;

import static NZ251.texteditor.CodeKeyWord.PATTERN;

public class SecondaryController implements Initializable {
    @FXML
    private AnchorPane root;

    @FXML
    private MenuItem saveB;

    @FXML
    private MenuItem saveasB;

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
    private StyleClassedTextArea text;

    private String chosen="";

    public static String extention = "";

    private String abPath = "";

    private Stage stage;

    public static Font font;
    public static String t = "";
    private ExecutorService executor;
    public static String[] KEYWORDS;
    // Set the highlight
    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        // Set different colors to match the selected words
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                            matcher.group("PAREN") != null ? "paren" :
                                    matcher.group("BRACE") != null ? "brace" :
                                            matcher.group("BRACKET") != null ? "bracket" :
                                                    matcher.group("SEMICOLON") != null ? "semicolon" :
                                                            matcher.group("STRING") != null ? "string" :
                                                                    matcher.group("COMMENT") != null ? "comment" :
                                                                            null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        // Returns the created text field
        return spansBuilder.create();
    }
    // Search function, similar to initialization window
    int nowposition=0;
    ArrayList<String> alltxt=new ArrayList<>();
    @FXML
        // Criteria for the search function
    void spc() {
        if (nowposition==0){
            alltxt.add(text.getText());
            nowposition=nowposition+1;
        }
        if (!Objects.equals(text.getText(), "")){
            if (!alltxt.get(nowposition - 1).equals(text.getText())){
                alltxt.add(text.getText());
                nowposition = nowposition+1;
            }
        }
    }
    // Clipping function,Similar to the function in primaryController
    @FXML
    void undoaction() {
        nowposition = nowposition-1;
        text.appendText(alltxt.get(nowposition));
    }
    // Undo function, Similar to the function in primaryController
    @FXML
    void  restoreaction() {
        nowposition=nowposition+1;
        text.appendText(alltxt.get(nowposition));
    }
    // Set menu options
    private static class DefaultContextMenu extends ContextMenu {
        public DefaultContextMenu() {
            MenuItem fold = new MenuItem("Fold selected text");
            fold.setOnAction(AE -> { hide(); fold(); } );

            MenuItem unfold = new MenuItem("Unfold from cursor");
            unfold.setOnAction(AE -> { hide(); unfold(); } );

            MenuItem print = new MenuItem("Print");
            print.setOnAction(AE -> { hide(); print(); } );

            getItems().addAll(fold, unfold, print);
        }

        private void fold() {
            ((CodeArea) getOwnerNode()).foldSelectedParagraphs();
        }

        private void unfold() {
            CodeArea area = (CodeArea) getOwnerNode();
            area.unfoldParagraphs( area.getCurrentParagraph() );
        }

        private void print() {
            System.out.println( ((CodeArea) getOwnerNode()).getText() );
        }
    }
    // Similar to the function in primaryController
    @FXML
    void aboutF() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.headerTextProperty().set("The statement:Prohibit infringement\n" +
                "Name:         Libin and Clifford\n" +
                "Date???         2021-9-15\n" +
                "Version:      1.0\n");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeCancel);
        alert.showAndWait();
    }
    // Similar to the function in primaryController
    @FXML
    void apc() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
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
    // Similar to the function in primaryController
    @FXML
    void copyF() {
        chosen = text.getSelectedText();

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();

        clipboardContent.putString(chosen);
        clipboard.setContent(clipboardContent);
    }
    // Similar to the function in primaryController
    @FXML
    void cutF() {
        chosen = text.getSelectedText();
        int a = text.getCaretPosition();
        StringBuilder stringBuilder1 = new StringBuilder(text.getText());
        stringBuilder1.delete(a, a + chosen.length());
        text.appendText(stringBuilder1.toString());

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();

        clipboardContent.putString(chosen);
        clipboard.setContent(clipboardContent);
    }
    // Similar to the function in primaryController
    @FXML
    void newF() throws IOException {
        Scene scene = new Scene(App.loadFXML("primary"));
        Stage second = new Stage();
        second.setScene(scene);
        second.setTitle("Text Editor");
        second.getIcons().add(new Image("file:src\\main\\resources\\NZ251\\texteditor\\tu.jpg"));
        second.show();
    }
    // Similar to the function in primaryController
    @FXML
    void openF() throws Exception {
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
                PrimaryController.t = FileOp.readTXT(file);
                App.setRoot("primary");
            } else if (extention.equals(".java") || extention.equals(".py") || extention.equals(".cpp")) {
                text.appendText(FileOp.readTXT(file));

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
                saveB.setDisable(false);
            } else if (extention.equals(".rtf")) {
                rtfArea.rtf2Html(file);
                String hName = file.getName().substring(0, file.getName().lastIndexOf(".")) + ".html";
                rtfController.t = "rtftohtml/" + hName;
                App.setRoot("rtfController");
            }
        }
    }
    // Similar to the function in primaryController
    @FXML
    void pasteF() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (!clipboard.getString().equals("")) {
            chosen = clipboard.getString();
        }
        int a = text.getCaretPosition();
        StringBuilder stringBuilder1 = new StringBuilder(text.getText());
        stringBuilder1.insert(a, chosen);
        text.appendText(stringBuilder1.toString());
    }
    // Similar to the function in primaryController
    @FXML
    void pdfB() throws DocumentException, IOException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save file");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
        );
        File file = chooser.showSaveDialog(null);
        if (file != null) {
            FileOp.O2PDF(text.getText(), file, font);
        }
    }
    // Similar to the function in primaryController
    @FXML
    void printB() {
        InputStream inputStream=new ByteArrayInputStream(text.getText().getBytes(StandardCharsets.UTF_8));
        PrintDemo printDemo=new PrintDemo();
        printDemo.printQRCode(inputStream);
    }
    // Similar to the function in primaryController
    @FXML
    void saveF() throws IOException {
        String txt = text.getText();

        if (txt!=null && !abPath.equals("")) {
            File f = new File(abPath);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
            bufferedWriter.write(txt);
            bufferedWriter.close();
        }
    }
    // Similar to the function in primaryController
    @FXML
    void saveasF() throws IOException {
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
    // Similar to the function in primaryController
    @FXML
    void searchF() {
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
    // Similar to the function in primaryController
    @FXML
    void tdF() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s=text.getText()+'\n'+dateFormat.format(date);
        text.appendText(s);
    }

    private Stage getStage() {
        stage = (Stage) root.getScene().getWindow();
        return stage;
    }
    // Set the highlight, call the text
    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String txt = text.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<>() {
            @Override
            protected StyleSpans<Collection<String>> call() {
                return computeHighlighting(txt);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        text.setStyleSpans(0, highlighting);
    }
    // Initialize the Settings
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ReadYAML readYAML = new ReadYAML("src/main/resources/conf/key.yaml");
        switch (extention) {
            case ".java":
                KEYWORDS = readYAML.properties.get("JAVA_KEYWORD").toArray(new String[0]);
                break;
            case ".py":
                KEYWORDS = readYAML.properties.get("PYTHON_KEYWORD").toArray(new String[0]);
                break;
            case ".cpp":
                KEYWORDS = readYAML.properties.get("CPP_KEYWORD").toArray(new String[0]);
                break;
        }
        // The default key is not optional
        saveB.setDisable(true);

        copyB.setDisable(true);
        pasteB.setDisable(true);
        cutB.setDisable(true);
        text.setWrapText(false);
        executor = Executors.newSingleThreadExecutor();

        text.setParagraphGraphicFactory(LineNumberFactory.get(text));
        text.setContextMenu(new DefaultContextMenu());
        // Set the rich text area,Walk through the string, judging all the words
        Subscription cleanupWhenDone = text.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(text.multiPlainChanges())
                .filterMap(t -> {
                    if(t.isSuccess()) {
                        return Optional.of(t.get());
                    } else {
                        t.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);

        text.replaceText(0, 0, t);
        text.getCaretPosition();
        text.textProperty().addListener((observableValue, s, t1) -> saveB.setDisable(false));
    }
}

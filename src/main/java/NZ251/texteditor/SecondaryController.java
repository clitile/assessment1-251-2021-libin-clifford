package NZ251.texteditor;

import com.itextpdf.text.DocumentException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.input.*;
import org.reactfx.Subscription;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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

import java.io.*;
import java.lang.reflect.InvocationTargetException;
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
    private MenuItem tdB;

    @FXML
    private MenuItem aboutB;

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

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
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
        return spansBuilder.create();
    }

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
    void apc(MouseEvent event) {
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
        text.appendText(stringBuilder1.toString());

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();

        clipboardContent.putString(chosen);
        clipboard.setContent(clipboardContent);
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
                PrimaryController.t = FileOp.readTXT(file);
                App.setRoot("primary");
            } else if (extention.equals(".java")) {
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
            }
        }
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
        text.appendText(stringBuilder1.toString());
    }

    @FXML
    void pdfB(ActionEvent event) throws DocumentException, IOException {
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

    @FXML
    void printB(ActionEvent event) throws InvocationTargetException {
        InputStream inputStream=new ByteArrayInputStream(text.getText().getBytes(StandardCharsets.UTF_8));
        PrintDemo printDemo=new PrintDemo();
        printDemo.printQRCode(inputStream);
    }

    @FXML
    void saveF(ActionEvent event) throws IOException {
        String txt = text.getText();

        if (txt!=null && !abPath.equals("")) {
            File f = new File(abPath);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
            bufferedWriter.write(txt);
            bufferedWriter.close();
        }
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
        text.appendText(s);
    }

    private Stage getStage() {
        stage = (Stage) root.getScene().getWindow();
        return stage;
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String txt = text.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(txt);
            }
        };
        executor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        text.setStyleSpans(0, highlighting);
    }

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
        saveB.setDisable(true);

        copyB.setDisable(true);
        pasteB.setDisable(true);
        cutB.setDisable(true);
        text.setWrapText(false);
        executor = Executors.newSingleThreadExecutor();

        text.setParagraphGraphicFactory(LineNumberFactory.get(text));
        text.setContextMenu(new DefaultContextMenu());

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
    }
}

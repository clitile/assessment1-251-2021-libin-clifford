module NZ.texteditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires java.desktop;
    requires org.apache.pdfbox;
    requires org.fxmisc.richtext;
    requires reactfx;
    requires org.yaml.snakeyaml;
    requires itext.asian;

    opens NZ251.texteditor to javafx.fxml;
    exports NZ251.texteditor;
}
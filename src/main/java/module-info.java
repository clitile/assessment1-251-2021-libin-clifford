module NZ.texteditor {
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires java.desktop;
    requires org.fxmisc.richtext;
    requires reactfx;

    opens NZ251.texteditor to javafx.fxml;
    exports NZ251.texteditor;
}
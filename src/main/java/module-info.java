// Some module description information
module NZ.texteditor {
    // Other modules that this module needs to associate and depend on
    requires javafx.controls;
    requires javafx.fxml;
    requires itextpdf;
    requires java.desktop;
    requires org.apache.pdfbox;
    requires org.fxmisc.richtext;
    requires reactfx;
    requires org.yaml.snakeyaml;
    requires itext.asian;
    requires org.apache.commons.io;
    requires poi.scratchpad;
    requires flowless;
    requires com.aspose.words;
    requires javafx.web;
    // Want to be accessed by other modules
    opens NZ251.texteditor to javafx.fxml;
    exports NZ251.texteditor;
}
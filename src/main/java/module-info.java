module NZ.texteditor {
    requires javafx.controls;
    requires javafx.fxml;

    opens NZ251.texteditor to javafx.fxml;
    exports NZ251.texteditor;
}
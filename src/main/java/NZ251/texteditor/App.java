package NZ251.texteditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/*
 * JavaFX App
 */
//Run the initialization of the window
public class App extends Application {

    private static Scene scene;
    // Set the title and icon
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.setTitle("TextEditor");
        stage.getIcons().add(new Image("file:src\\main\\resources\\NZ251\\texteditor\\tu.jpg"));
        stage.show();
    }
    // Close the window as needed
    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
    // Set the FXML file
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    // Read the FXML file
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
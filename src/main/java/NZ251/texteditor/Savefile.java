package NZ251.texteditor;

import com.itextpdf.text.DocumentException;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//Written to test the save function
public class Savefile {
    public static String Save(String openFile, String content) throws IOException {
        String txt = content;
        //Writes a string to a file
        if (txt!=null && !openFile.equals("")) {
            File f = new File(openFile);
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(f));
            // Save as a new file path
            bufferedWriter.write(txt);
            bufferedWriter.close();
        }
        return content;
    }

}

package NZ251.texteditor;

//import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

//import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PrimaryControllerTest {
    //Test hold function
    @Test
    public void savetest() throws IOException {
        String fileName = "src/test/java/NZ251/texteditor/ss.txt";
        Savefile.Save(fileName,"abcd");
        try (Scanner sc = new Scanner(new FileReader(fileName))) {
            while (sc.hasNextLine()) {  //按行读取字符串
                String line = sc.nextLine();
                assertEquals("abcd",line);
            }
        }
    }
    //Test open function
    @Test
    public void opentest() throws IOException {
        String fileName = "src/test/java/NZ251/texteditor/ss.txt";
        assertEquals("abcd", FileOp.readTXT(new File(fileName)));
    }
    //Test the search function
    @Test
    public void searchtest() throws IOException {
        assertEquals(Searchfile.Search("tar", "star,sad asd, tarss"), 2);
    }
}
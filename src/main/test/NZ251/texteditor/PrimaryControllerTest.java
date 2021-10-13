package NZ251.texteditor;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PrimaryControllerTest {
    //Test hold function
    @Test
    void savetest() throws IOException {
        String fileName = "src\\main\\test\\NZ251\\texteditor\\ss.txt";
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
    void opentest() throws IOException {
        String fileName = "src\\main\\test\\NZ251\\texteditor\\ss.txt";
        assertEquals("abcd",FileOp.readTXT(new File(fileName)));
    }
    //Test the search function
    @Test
    void searchtest() throws IOException {
        assertEquals(Searchfile.Search("tar","star,sad asd, tarss"),2);
    }
}
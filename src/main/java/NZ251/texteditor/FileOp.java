package NZ251.texteditor;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.text.Font;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileOp {
    public static boolean changeFlag = false;

    public static String getFileExtension (File file) {
        String extention = "";
        if (file != null && file.exists()) {
            String name = file.getName();
            extention = name.substring(name.lastIndexOf("."));
        }
        return extention;
    }

    public static String readTXT(File file) throws IOException {
        byte[] bytes = new byte[1024];
        StringBuilder stringBuffer = new StringBuilder();
        FileInputStream in = new FileInputStream(file);
        int len;
        while ((len = in.read(bytes)) != -1) {
            stringBuffer.append(new String(bytes, 0, len));
        }
        return stringBuffer.toString();
    }

    public static void printFile(String path) {
        //TODO
    }

    public static void O2PDF (String content, File outfile, Font font) throws IOException, DocumentException {
        Document document = new Document();
        OutputStream os = new FileOutputStream(outfile);
        PdfWriter.getInstance(document, os);
        document.open();
        document.add(new Paragraph(content, FontFactory.getFont(String.valueOf(font))));
        document.close();
    }
}

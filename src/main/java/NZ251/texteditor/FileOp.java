package NZ251.texteditor;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.text.Font;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileOp {

    // Reads the file and returns extention upon checking that the file 'extention'
    public static String getFileExtension (File file) {
        String extention = "";
        if (file != null && file.exists()) {
            String name = file.getName();
            extention = name.substring(name.lastIndexOf("."));
        }
        return extention;
    }
    //Use bitstream to read TXT
    public static String readTXT(File file) throws IOException {
        byte[] bytes = new byte[1024];
        StringBuilder stringBuffer = new StringBuilder();
        FileInputStream in = new FileInputStream(file);
        int len;
        while ((len = in.read(bytes)) != -1) {
            stringBuffer.append(new String(bytes, 0, len));
        }
        // Finally, returns as a string
        return stringBuffer.toString();
    }
    //read the pdf
    public static void O2PDF (String content, File outfile, Font font) throws IOException, DocumentException {
        Document document = new Document();
        OutputStream os = new FileOutputStream(outfile);
        PdfWriter.getInstance(document, os);
        document.open();
        // Read in the font format
        BaseFont bf = BaseFont.createFont("src/main/resources/font/simhei.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        com.itextpdf.text.Font font1 = new com.itextpdf.text.Font(bf);
        document.add(new Paragraph(content, font1));
        document.close();
    }

    public static List<byte[]> pdf2images(File pdfFile) throws Exception {
        //load the PDF
        PDDocument pdDocument = PDDocument.load(pdfFile);
        //Create PDF renderer
        PDFRenderer renderer = new PDFRenderer(pdDocument);
        int pages = pdDocument.getNumberOfPages();
        List<byte[]> images = new ArrayList<>();
        for (int i = 0; i < pages; i++) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            //Render each page of the PDF into an image
            BufferedImage image = renderer.renderImage(i);
            ImageIO.write(image, "png", output);
            images.add(output.toByteArray());
        }
        pdDocument.close();
        return images;
    }
}

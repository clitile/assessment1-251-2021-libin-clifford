package NZ251.texteditor;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
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

    public static void O2PDF (String content, File outfile, Font font) throws IOException, DocumentException {
        Document document = new Document();
        OutputStream os = new FileOutputStream(outfile);
        PdfWriter.getInstance(document, os);
        document.open();
        document.add(new Paragraph(content, FontFactory.getFont(String.valueOf(font))));
        document.close();
    }

    public static List<byte[]> pdf2images(File pdfFile) throws Exception {
        //加载PDF
        PDDocument pdDocument = PDDocument.load(pdfFile);
        //创建PDF渲染器
        PDFRenderer renderer = new PDFRenderer(pdDocument);
        int pages = pdDocument.getNumberOfPages();
        List<byte[]> images = new ArrayList<>();
        for (int i = 0; i < pages; i++) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            //将PDF的每一页渲染成一张图片
            BufferedImage image = renderer.renderImage(i);
            ImageIO.write(image, "png", output);
            images.add(output.toByteArray());
        }
        pdDocument.close();
        return images;
    }
}

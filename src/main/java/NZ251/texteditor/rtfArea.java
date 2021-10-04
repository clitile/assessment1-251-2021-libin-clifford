package NZ251.texteditor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.aspose.words.SaveFormat;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;


public class rtfArea {
    public static void main(String[] args) throws Exception {
        rtf2Html(new File("C:\\Users\\xiang\\Desktop\\rtf\\my.rtf"));
        System.out.println(rtfArea.class.getResource("rtftohtml/my.html"));
    }

    public static void rtf2Html(File in) throws Exception {
        String name = in.getName();
        com.aspose.words.Document document = new com.aspose.words.Document(in.getAbsolutePath());
        String destinationFolder = "target/classes/NZ251/texteditor/rtftohtml/";
        File folder = new File(destinationFolder);
        boolean re;
        if (! folder.exists()) {
            re = folder.mkdirs();
        } else re = true;
        if (re) {
            String caselsh = name.substring(0, name.lastIndexOf("."));
            String outFileName = destinationFolder + caselsh + ".doc";
            document.save(outFileName, SaveFormat.DOC);
            String hpath = destinationFolder + caselsh + ".html";
            convert2Html(outFileName, hpath);
            String content = FileUtils.readFileToString(new File(hpath), "utf-8");
            content = content.replaceFirst("<p class=\"p1\">", "");
            content = content.replaceFirst("<span class=\"s1\">Evaluation Only. Created with Aspose.Words. Copyright 2003-2021 Aspose Pty Ltd.</span>", "");
            content = content.replaceFirst("</p>", "");
            OutputStream fos = new FileOutputStream(hpath);
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.flush();
            fos.close();
        }
    }

    private static void write2File(byte[] content, String path) {
        FileOutputStream fos = null;
        BufferedWriter bw = null;
        try {
            File file = new File(path);
            fos = new FileOutputStream(file);
            fos.write(content);
            fos.close();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fos != null)
                    fos.close();
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }
    }

    private static void convert2Html(String fileName, String outPutFile)
            throws TransformerException, IOException, ParserConfigurationException {

        Base64.Encoder encoder = Base64.getEncoder();

        HWPFDocument wordDocument = new HWPFDocument(new FileInputStream(fileName));

        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches,
                                      float heightInches) {
                String encodedText = new String(encoder.encode(content));
                String imgSrc = "data:" + pictureType.getMime() + ";" + "base64," + encodedText;
                return imgSrc;
            }
        });
        wordToHtmlConverter.processDocument(wordDocument);

        Document htmlDocument = wordToHtmlConverter.getDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        out.close();
        write2File(out.toByteArray(), outPutFile);
    }
}
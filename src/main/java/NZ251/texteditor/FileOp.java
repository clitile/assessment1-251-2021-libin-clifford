package NZ251.texteditor;

import java.io.*;

public class FileOp {
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

    }
}

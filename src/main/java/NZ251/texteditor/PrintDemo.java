package NZ251.texteditor;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

public class PrintDemo {
    void printQRCode(InputStream inputStream) throws InvocationTargetException,IllegalAccessError ,IllegalArgumentException{
        DocFlavor flavor=DocFlavor.INPUT_STREAM.PNG;
        Doc doc=new SimpleDoc(inputStream, flavor, null);
        PrintRequestAttributeSet attributeSet=new HashPrintRequestAttributeSet();
        attributeSet.add(new Copies(1));//设置打印份数
        PrintService TscTtp244Pro=null;
        TscTtp244Pro = ServiceUI.printDialog(null, 200, 200, PrintServiceLookup.lookupPrintServices(flavor, null), TscTtp244Pro, flavor, attributeSet);
        if(TscTtp244Pro == null) return;
        DocPrintJob job = TscTtp244Pro.createPrintJob();
        try {
            job.print(doc, attributeSet);
        } catch (PrintException e) {
            e.printStackTrace();
        }

    }
}
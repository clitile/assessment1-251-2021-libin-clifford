package NZ251.texteditor;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.io.InputStream;

public class PrintDemo {
    void printQRCode(InputStream inputStream) throws IllegalAccessError ,IllegalArgumentException{
        DocFlavor flavor=DocFlavor.INPUT_STREAM.PNG;
        Doc doc=new SimpleDoc(inputStream, flavor, null);
        PrintRequestAttributeSet attributeSet=new HashPrintRequestAttributeSet();
        // Set the number of copies to print
        attributeSet.add(new Copies(1));
        //// Create and select a new service
        PrintService TscTtp244Pro=null;
        TscTtp244Pro = ServiceUI.printDialog(null, 200, 200, PrintServiceLookup.lookupPrintServices(flavor, null), TscTtp244Pro, flavor, attributeSet);
        if(TscTtp244Pro == null) return;
        DocPrintJob job = TscTtp244Pro.createPrintJob();
        // Try the operation and catch the exception
        try {
            job.print(doc, attributeSet);
        } catch (PrintException e) {
            e.printStackTrace();
        }

    }
}
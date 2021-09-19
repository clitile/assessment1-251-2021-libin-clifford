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
    void printQRCode(InputStream inputStream) throws InvocationTargetException {
        //设置文档类型当前是png图片
        DocFlavor flavor=DocFlavor.INPUT_STREAM.AUTOSENSE;
        assert inputStream != null;
        //创建一个文档
        Doc doc=new SimpleDoc(inputStream, flavor, null);
        //创建属性设置对象
        PrintRequestAttributeSet attributeSet=new HashPrintRequestAttributeSet();
        attributeSet.add(new Copies(1));//设置打印份数
        //设置打印方向
        //attributeSet.add(OrientationRequested.PORTRAIT);
        // 设置纸张大小,也可以新建MediaSize类来自定义大小
        //发现可以根据属性设置指令打印格式的打印机
        PrintService[] services=PrintServiceLookup.lookupPrintServices(flavor, null);
        //重其中一个打印服务中创建一个打印作业
        PrintService TscTtp244Pro=null;
        if (services.length > 0) {
            for (PrintService service : services) {
                if(service.getName().equals("TSC TTP-244 Pro"))//选择二维码打印机
                    TscTtp244Pro=service;
            }
        }
        // 显示打印对话框
        TscTtp244Pro = ServiceUI.printDialog(null, 200, 200, services, TscTtp244Pro, flavor, attributeSet);
        if(TscTtp244Pro == null) return;
        DocPrintJob job = TscTtp244Pro.createPrintJob();
        try {
            job.print(doc, attributeSet);
        } catch (PrintException e) {
            e.printStackTrace();
        }

    }
}
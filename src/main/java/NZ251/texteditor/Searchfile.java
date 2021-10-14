package NZ251.texteditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//Written to test search methods
public class Searchfile {
    public static int Search(String value,String content) throws IOException {
        int a=0;
        //This function should find all the lookups in the string
        for(int i=0;i<content.length();i++){
            if (content.indexOf(value)!=-1){
                a=a+1;
                content=content.replaceFirst(value,"**");
            }else {
                break;
            }
        }
        return a;
    }
}

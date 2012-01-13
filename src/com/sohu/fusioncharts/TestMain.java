package com.sohu.fusioncharts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class TestMain {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        fis = new FileInputStream("E:\\NetMonitorDatas\\new\\com.sohu.test\\com.sohu.test\\sina\\1326608092788.txt");
//        File file = new File("e:/abc.txt");
//        if(!file.exists())
//        {
//            file.createNewFile();
//        }
        fos = new FileOutputStream("e:/abc.txt");
        int x = 0;
        while((x = fis.read()) != -1)
        {
            fos.write(x);
        }
        fos.flush();
        fis.close();
        fos.close();
    }

}

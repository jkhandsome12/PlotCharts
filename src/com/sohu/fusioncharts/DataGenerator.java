package com.sohu.fusioncharts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class DataGenerator {
    /**
     * 根据文件夹不同获取相应对象集合
     * 
     * @param dirPath
     * @return
     * @throws Exception
     */
    public static Map<String, List<NetSpeed>> getNetSpeedList(String dirPath) throws Exception
    {
        Map<String, List<NetSpeed>> map = new HashMap<String, List<NetSpeed>>();
        File file = new File(dirPath);
        if(file.isDirectory())
        {
            File[] fs = file.listFiles();
            for(File f : fs)
            {
                List<File> list = new ArrayList<File>();
                listFiles(list, f.getPath());
                List<NetSpeed> nss = changeToNetSpeedList(list);
                map.put(f.getName(), nss);
            }
        }
        return map;
    }

    /**
     * 获取某个文件夹下的所有文件,递归遍历
     * 
     * @param list
     * @param dirPath
     * @throws Exception
     */
    public static void listFiles(List<File> list, String dirPath) throws Exception
    {
        File file = new File(dirPath);
        if(file.isDirectory())
        {
            for(File f : file.listFiles())
            {
                listFiles(list, f.getPath());
            }
        }
        else
        {
            list.add(file);
        }
    }

    /**
     * 将文件集合转换成对象集合
     * 
     * @param files
     * @return
     * @throws Exception
     */
    public static List<NetSpeed> changeToNetSpeedList(List<File> files) throws Exception
    {
        List<NetSpeed> nss = new ArrayList<NetSpeed>();
        for(File file : files)
        {
            BufferedReader br=null;
            try
            {
                br = new BufferedReader(new FileReader(file));
                String line = null;
                while((line = br.readLine()) != null)
                {
                    if(!"".equals(line.trim())&&!line.split(",")[7].equals("null"))
                    {
                        NetSpeed ne = parseLine(line);
                        nss.add(ne);
                    }
                }
            }
            finally
            {
                if(br!=null)
                {
                    br.close();
                }
            }
        }
        return nss;
    }

    /**
     * 将一行记录解析成为一个NetSpeed对象
     * 
     * @param line
     * @return
     * @throws Exception
     */
    public static NetSpeed parseLine(String line) throws Exception
    {
        String[] items = line.split(",");
        String startTime = items[0];
        String endTime = items[1];
        String connectTime = items[2];
        String downloadTime = items[3];
        String fileSize = items[5];
        String isSuccessful = items[6].equals("0")?"0":"1";
        String netType = items[7].replaceAll(":", "");
        NetSpeed ns = new NetSpeed();
        ns.setStartTime(getAccuracyDate(startTime));
        ns.setEndTime(getAccuracyDate(endTime));
        ns.setConnectTime(Long.valueOf(connectTime));
        ns.setDownloadTime(Long.valueOf(downloadTime));
        ns.setFileSize(Long.valueOf(fileSize));
        ns.setSuccess(Integer.valueOf(isSuccessful));
        ns.setNetType(netType);
        return ns;
    }
    
    /**
     * 获取精度到分钟的时间
     * @param str
     * @return
     * @throws Exception
     */
    public static Date getAccuracyDate(String str) throws Exception
    {
        Calendar cl=Calendar.getInstance();
        cl.setTimeInMillis(Long.valueOf(str));
        cl.clear(Calendar.SECOND);
        return cl.getTime();
    }
    
    /**
     * 由Map<第三方,List<NetSpeed>>得到Map<网络,Map<第三方,List<NetSpeed>>>
     * 
     * @param thirdPartyMap
     * @return
     * @throws Exception
     */
    public static Map<String, Map<String, List<NetSpeed>>> getNetThirdPartyNetSpeedList(Map<String, List<NetSpeed>> thirdPartyMap) throws Exception
    {
        Map<String, Map<String, List<NetSpeed>>> netThirdPartyMap = new HashMap<String, Map<String, List<NetSpeed>>>();
        Map<String, List<NetSpeed>> tpMap = null;
        List<NetSpeed> list = null;
        for(Entry<String, List<NetSpeed>> entry : thirdPartyMap.entrySet())
        {
            for(NetSpeed ns : entry.getValue())
            {
                String type = ns.getNetType();
                if(netThirdPartyMap.containsKey(type))
                {
                    tpMap = netThirdPartyMap.get(type);
                    if(tpMap.containsKey(entry.getKey()))
                    {
                        list = tpMap.get(entry.getKey());
                        list.add(ns);
                    }
                    else
                    {
                        list = new ArrayList<NetSpeed>();
                        list.add(ns);
                        tpMap.put(entry.getKey(), list);
                    }
                }
                else
                {
                    tpMap = new HashMap<String, List<NetSpeed>>();
                    list = new ArrayList<NetSpeed>();
                    list.add(ns);
                    tpMap.put(entry.getKey(), list);
                    netThirdPartyMap.put(type, tpMap);
                }
            }
        }
        return netThirdPartyMap;
    }
    
//    public static void generateXmlFile(Map<String, Map<String, List<NetSpeed>>> map) throws Exception
//    {
//        for(Entry<String, Map<String, List<NetSpeed>>> entry:map.entrySet())
//        {
//            String net=entry.getKey();
//            Map<String,List<NetSpeed>> tpMap=entry.getValue();
//            Element chart=DocumentHelper.createElement("chart");
//            chart.addAttribute("caption", "成功与否监控数据");
//            chart.addAttribute("xAxisName", "时间（单位：分钟）");
//            chart.addAttribute("yAxisName", "是否成功（0：成功，1：失败）");
//            chart.addAttribute("compactDataMode", "1");
//            chart.addAttribute("dataSeparator", "|");
//            chart.addAttribute("paletteThemeColor", "5D57A5");
//            chart.addAttribute("divLineColor", "5D57A5");
//            chart.addAttribute("divLineAlpha", "40");
//            chart.addAttribute("vDivLineAlpha", "40");
//            chart.addAttribute("dynamicAxis", "1");
//            Element categories=chart.addElement("categories");
//            StringBuilder sbd=new StringBuilder();
//            List<NetSpeed> nss=tpMap.get("sina");
//            for(int i=0;i<nss.size();i++)
//            {
//                NetSpeed ns=nss.get(i);
//                DateFormat df=new SimpleDateFormat("yy-MM-dd HH:mm");
//                sbd.append(df.format(ns.getStartTime()));
//                if(i!=nss.size()-1)
//                {
//                    sbd.append("|");
//                }
//            }
//            categories.setText(sbd.toString());
//            for(Entry<String,List<NetSpeed>> entry2:tpMap.entrySet())
//            {
//                Element dataset=chart.addElement("dataset");
//                dataset.addAttribute("seriesName", entry2.getKey());
//                List<NetSpeed> nss2=entry2.getValue();
//                StringBuilder sbd2=new StringBuilder();
//                for(int i=0;i<nss2.size();i++)
//                {
//                    NetSpeed ns=nss2.get(i);
//                    sbd2.append(ns.getSuccess());
//                    if(i!=nss2.size()-1)
//                    {
//                        sbd2.append("|");
//                    }
//                }
//                dataset.setText(sbd2.toString());
//            }
//            File file=new File("e:/charts/"+net);
//            file.mkdirs();
//            StringReader sr=new StringReader(chart.asXML());
//            OutputStream os=new FileOutputStream(new File("e:/charts/"+net+"/成功与否监控数据.xml"));
//            OutputStreamWriter osw=new OutputStreamWriter(os,"utf-8");
//            int i=0;
//            while((i=sr.read())!=-1)
//            {
//                osw.write(i);
//            }
//            osw.flush();
//            sr.close();
//            os.close();
//            System.out.println("ok");
//        }
//    }
    
    public static void saveXml(String xmlStr,String netType,int captionType) throws Exception
    {
        File file=new File("e:/charts/"+netType);
        file.mkdirs();
        StringReader sr=new StringReader(xmlStr);
        OutputStream os=new FileOutputStream(new File("e:/charts/"+netType+"/"+captionType+".xml"));
        OutputStreamWriter osw=new OutputStreamWriter(os,"utf-8");
        int i=0;
        while((i=sr.read())!=-1)
        {
            osw.write(i);
        }
        osw.flush();
        sr.close();
        os.close();
        System.out.println("ok");
    }
    
    public static void generateChartTitle(Element chart, int captionType) throws Exception
    {
        switch(captionType)
        {
            case 1:
            {
                chart.addAttribute("caption", "连接时间监控数据");
                chart.addAttribute("xAxisName", "时间（单位：分钟）");
                chart.addAttribute("yAxisName", "连接时间（单位：毫秒）");
                break;
            }
            case 2:
            {
                chart.addAttribute("caption", "下载速率监控数据");
                chart.addAttribute("xAxisName", "时间（单位：分钟）");
                chart.addAttribute("yAxisName", "下载速率（单位：KB/s）");
                break;
            }
            case 3:
            {
                chart.addAttribute("caption", "是否失败监控数据");
                chart.addAttribute("xAxisName", "时间（单位：分钟）");
                chart.addAttribute("yAxisName", "失败与否（0：成功，1：失败）");
                break;
            }
        }
    }
    
    public static String getData(NetSpeed ns, int captionType) throws Exception
    {
        String data="";
        switch(captionType)
        {
            case 1:
            {
                data=ns.getConnectTime().toString();
                break;
            }
            case 2:
            {
                data=ns.getDownloadRate().toString();
                break;
            }
            case 3:
            {
                data=ns.getSuccess().toString();
                break;
            }
        }
        return data;
    }
    
    public static String generateXml(Map<String,List<NetSpeed>> tpMap, int captionType) throws Exception
    {
        Element chart=DocumentHelper.createElement("chart");
        generateChartTitle(chart,captionType);
        chart.addAttribute("compactDataMode", "1");
        chart.addAttribute("dataSeparator", "|");
        chart.addAttribute("paletteThemeColor", "5D57A5");
        chart.addAttribute("divLineColor", "5D57A5");
        chart.addAttribute("divLineAlpha", "40");
        chart.addAttribute("vDivLineAlpha", "40");
        chart.addAttribute("dynamicAxis", "1");
        Element categories=chart.addElement("categories");
        List<NetSpeed> nss=tpMap.get("sina");
        StringBuilder sbd=new StringBuilder();
        for(int i=0;i<nss.size();i++)
        {
            NetSpeed ns=nss.get(i);
            DateFormat df=new SimpleDateFormat("yy-MM-dd HH:mm");
            sbd.append(df.format(ns.getStartTime()));
            if(i!=nss.size()-1)
            {
                sbd.append("|");
            }
        }
        categories.setText(sbd.toString());
        for(Entry<String,List<NetSpeed>> entry2:tpMap.entrySet())
        {
            Element dataset=chart.addElement("dataset");
            dataset.addAttribute("seriesName", entry2.getKey());
            List<NetSpeed> nss2=entry2.getValue();
            StringBuilder sbd2=new StringBuilder();
            for(int i=0;i<nss2.size();i++)
            {
                NetSpeed ns=nss2.get(i);
                sbd2.append(getData(ns,captionType));
                if(i!=nss2.size()-1)
                {
                    sbd2.append("|");
                }
            }
            dataset.setText(sbd2.toString());
        }
        return chart.asXML();
    }
    
    public static void handleNet() throws Exception
    {
        Map<String, Map<String, List<NetSpeed>>> netTpMap=getNetThirdPartyNetSpeedList(getNetSpeedList("e:/com.sohu.test"));
        for(Entry<String, Map<String, List<NetSpeed>>> entry:netTpMap.entrySet())
        {
            Map<String,List<NetSpeed>> tpMap=entry.getValue();
            String netType=entry.getKey();
            for(int i=1;i<4;i++)
            {
                String xmlStr=generateXml(tpMap,i);
                saveXml(xmlStr,netType,i);
            }
        }
    }
    
    public static String getXmlStr(String netType,int captionType ,String dirPath) throws Exception
    {
        Map<String, Map<String, List<NetSpeed>>> netTpMap=getNetThirdPartyNetSpeedList(getNetSpeedList(dirPath));
        Map<String, List<NetSpeed>> tpMap=netTpMap.get(netType);
        for(List<NetSpeed> nss:tpMap.values())
        {
            Collections.sort(nss);
        }
        return generateXml(tpMap,captionType);
    }
    
    public static Set<String> getNetList(String dirPath) throws Exception
    {
        Map<String, Map<String, List<NetSpeed>>> netTpMap=getNetThirdPartyNetSpeedList(getNetSpeedList(dirPath));
        Set<String> set=netTpMap.keySet();
        return set;
    }
    
    public static void main(String[] args)
    {
        try
        {
            handleNet();
        }
        catch(Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
package com.sohu.fusioncharts;

import java.util.Date;

public class NetSpeed implements Comparable<NetSpeed> {

    private Date    startTime;
    private Date    endTime;
    private Long    connectTime;
    private Long    DownloadTime;
    private Long    fileSize;
    private Integer success;
    private String  netType;
    private Double  downloadRate;
    private Long    avgConnectTime;
    private Double  avgDownloadRate;
    private Double  avgSuccessRate;

    public static void main(String[] args)
    {
        int x = 1;
        System.out.println(String.format("%.2f", (double) x / 3));
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Long getConnectTime()
    {
        return connectTime;
    }

    public void setConnectTime(Long connectTime)
    {
        this.connectTime = connectTime;
    }

    public Long getDownloadTime()
    {
        return DownloadTime;
    }

    public void setDownloadTime(Long downloadTime)
    {
        DownloadTime = downloadTime;
    }

    public Long getFileSize()
    {
        return fileSize;
    }

    public void setFileSize(Long fileSize)
    {
        this.fileSize = fileSize;
    }

    public String getNetType()
    {
        return netType;
    }

    public void setNetType(String netType)
    {
        this.netType = netType;
    }

    public Double getDownloadRate()
    {
        double fileSize = this.fileSize;
        double downloadTime = this.DownloadTime;
        if(downloadTime != 0)
        {
            this.downloadRate = fileSize / downloadTime;
            this.downloadRate = Double.parseDouble(String.format("%.2f", this.downloadRate));
        }
        else
        {
            this.downloadRate = 0D;
        }
        return downloadRate;
    }

    public void setDownloadRate(Double downloadRate)
    {
        this.downloadRate = downloadRate;
    }

    public Integer getSuccess()
    {
        return success;
    }

    public void setSuccess(Integer success)
    {
        this.success = success;
    }

    public Long getAvgConnectTime()
    {
        return avgConnectTime;
    }

    public void setAvgConnectTime(Long avgConnectTime)
    {
        this.avgConnectTime = avgConnectTime;
    }

    public Double getAvgDownloadRate()
    {
        return avgDownloadRate;
    }

    public void setAvgDownloadRate(Double avgDownloadRate)
    {
        this.avgDownloadRate = avgDownloadRate;
    }

    public Double getAvgSuccessRate()
    {
        return avgSuccessRate;
    }

    public void setAvgSuccessRate(Double avgSuccessRate)
    {
        this.avgSuccessRate = avgSuccessRate;
    }

    @Override
    public int compareTo(NetSpeed o)
    {
        Long outerTime = o.getStartTime().getTime();
        Long innerTime = this.getStartTime().getTime();
        if(outerTime > innerTime)
        {
            return -1;
        }
        else if(outerTime < innerTime)
        {
            return 1;
        }
        return 0;
    }
}
